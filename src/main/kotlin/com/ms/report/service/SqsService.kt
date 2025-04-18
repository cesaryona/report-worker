package com.ms.report.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ms.report.api.config.properties.AwsProperties
import com.ms.report.api.dto.ReportRequestDto
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.Message
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest

@Service
class SqsService(
    private val sqsClient: SqsClient,
    private val properties: AwsProperties,
    private val reportService: ReportService,
    private val objectMapper: ObjectMapper
) {

    private val queueUrl: String = properties.queue.reportRequest

    fun consumeMessages() {
        try {
            while (true) {
                val receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(5)
                    .waitTimeSeconds(10)
                    .build()

                val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

                for (message in messages) {
                    processMessage(message)
                    deleteMessage(message)
                }
            }
        } catch (e: Exception) {
            println("error ${e.message}")
        }

    }

    private fun processMessage(message: Message) {
        println("Mensagem recebida: ${message.body()}")
        try {
            val reportRequestDto: ReportRequestDto = objectMapper.readValue(message.body())
            println("Mensagem deserializada: $reportRequestDto")

            reportService.generateReport(reportRequestDto)
        } catch (e: Exception) {
            println("Erro ao deserializar a mensagem: ${e.message}")
        }
    }

    private fun deleteMessage(message: Message) {
        try {
            val deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build()

            sqsClient.deleteMessage(deleteMessageRequest)
            println("Mensagem deletada: ${message.body()}")
        } catch (e: Exception) {
            println("Erro ao deletar a mensagem: ${e.message}")
        }
    }
}
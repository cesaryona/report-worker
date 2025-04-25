package com.ms.report.service.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ms.report.config.properties.AwsProperties
import com.ms.report.dto.ReportRequestDto
import com.ms.report.service.ReportProcessingService
import com.report.utils.service.SqsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.model.Message

@Service
class SqsConsumer(
    private val sqsService: SqsService,
    private val properties: AwsProperties,
    private val reportService: ReportProcessingService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(SqsConsumer::class.java)

    private val queueUrl: String = properties.queue.reportRequest

    fun consumeMessages() {
        try {
            while (true) {
                sqsService.receivedMessage(queueUrl, 10, 10)
                    .forEach {
                        processMessage(it)
                        deleteMessage(it)
                    }
            }
        } catch (e: Exception) {
            logger.info("Error while consuming request queue: {}", e.message)
        }
    }

    private fun processMessage(message: Message) {
        logger.info("Message received: {}", message.body())
        try {
            val reportRequestDto: ReportRequestDto = objectMapper.readValue(message.body())

            reportService.process(reportRequestDto)
        } catch (e: Exception) {
            logger.info("Error while deserializing message: {}", e.message)
        }
    }

    private fun deleteMessage(message: Message) {
        sqsService.deleteMessage(queueUrl, message)
        logger.info("Message deleted: {}", message.body())
    }

}
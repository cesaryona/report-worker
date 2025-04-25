package com.ms.report.service.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ms.report.config.properties.AwsProperties
import com.ms.report.dto.ReportStatusRequestDto
import com.ms.report.service.ReportHistoryService
import com.report.utils.service.SqsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.model.Message

@Service
class ReportStatusSqsConsumer(
    private val sqsService: SqsService,
    private val properties: AwsProperties,
    private val reportHistoryService: ReportHistoryService,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(ReportStatusSqsConsumer::class.java)

    private val queueUrl: String = properties.queue.reportStatus

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
            logger.info("Error while consuming status queue: {}", e.message)
        }
    }

    private fun processMessage(message: Message) {
        logger.info("Message received: {}", message.body())
        try {
            val reportStatusRequestDto: ReportStatusRequestDto = objectMapper.readValue(message.body())

            reportHistoryService.save(reportStatusRequestDto)
        } catch (e: Exception) {
            logger.info("Error while deserializing message: {}", e.message)
        }
    }

    private fun deleteMessage(message: Message) {
        sqsService.deleteMessage(queueUrl, message)
        logger.info("Message deleted: {}", message.body())
    }

}

package com.ms.report.service.message

import com.ms.report.config.properties.AwsProperties
import com.ms.report.dto.ReportStatusRequestDto
import com.report.utils.service.SqsService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportstatusSqsProducer(
    private val sqsService: SqsService,
    private val properties: AwsProperties,
) {

    private val logger = LoggerFactory.getLogger(ReportstatusSqsProducer::class.java)

    private val queueUrl: String = properties.queue.reportStatus

    fun send(reportStatusRequestDto: ReportStatusRequestDto) {
        logger.info("Sending message to {}, payload {}", queueUrl, reportStatusRequestDto)
        sqsService.sendMessage(queueUrl, reportStatusRequestDto)
    }
}

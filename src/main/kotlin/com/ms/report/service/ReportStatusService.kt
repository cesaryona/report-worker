package com.ms.report.service

import com.ms.report.api.repository.entity.enums.ReportStatus
import com.ms.report.dto.ReportStatusRequestDto
import com.ms.report.service.message.ReportstatusSqsProducer
import com.report.utils.factory.*
import com.report.utils.factory.dto.ReportStatusMessage
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReportStatusService(
    private val sqsProducer: ReportstatusSqsProducer
) {

    fun sendReportStatus(reportId: UUID, status: ReportStatus) {
        sendMessage(ReportStatusRequestDto(reportId, getReportStatusMessage(status)))
    }

    fun sendReportStatus(message: String, reportId: UUID, status: ReportStatus) {
        sendMessage(ReportStatusRequestDto(reportId, createGenericMessage(message, status)))
    }

    private fun sendMessage(dto: ReportStatusRequestDto) {
        sqsProducer.send(dto)
    }
}
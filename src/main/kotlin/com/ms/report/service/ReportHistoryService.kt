package com.ms.report.service

import com.ms.report.dto.ReportStatusRequestDto
import com.ms.report.repository.ReportHistoryRepository
import com.ms.report.repository.entity.ReportEntity
import com.ms.report.repository.entity.ReportHistoryEntity
import com.report.utils.factory.dto.ReportStatusMessage
import org.springframework.stereotype.Service

@Service
class ReportHistoryService(
    private val reportHistoryRepository: ReportHistoryRepository,
    private val reportService: ReportProcessingService
) {

    fun save(reportStatusRequestDto: ReportStatusRequestDto) {
        val reportEntity = reportService.getById(reportStatusRequestDto.reportId)

        reportHistoryRepository.save(toEntity(reportEntity, reportStatusRequestDto.reportstatus))
    }

    fun toEntity(entity: ReportEntity, reportStatusMessage: ReportStatusMessage): ReportHistoryEntity {
        return ReportHistoryEntity(
            report = entity,
            message = reportStatusMessage.message,
            status = reportStatusMessage.status
        )
    }

}
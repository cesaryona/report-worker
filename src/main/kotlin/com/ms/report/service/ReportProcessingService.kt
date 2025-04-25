package com.ms.report.service

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.api.repository.entity.enums.ReportStatus
import com.ms.report.config.properties.AwsProperties
import com.ms.report.dto.ReportRequestDto
import com.ms.report.repository.ReportRepository
import com.ms.report.repository.entity.ReportEntity
import com.ms.report.service.report.ReportGenerator
import com.report.utils.service.S3Service
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ReportProcessingService(
    private val generators: List<ReportGenerator>,
    private val reportRepository: ReportRepository,
    private val s3Service: S3Service,
    private val properties: AwsProperties,
    private val reportStatusService: ReportStatusService
) {

    private val bucket: String = properties.bucket

    fun getById(reportId: UUID): ReportEntity {
        return reportRepository.findById(reportId)
            .orElseThrow { RuntimeException("Report not found: $reportId") }
    }

    fun process(reportRequest: ReportRequestDto) {
        val reportId = reportRequest.reportId

        reportStatusService.sendReportStatus(reportId, ReportStatus.PROCESSING)

        try {
            val s3Path = generateReport(reportRequest)

            updateReportPathInDatabase(reportId, s3Path)

            reportStatusService.sendReportStatus(reportId, ReportStatus.COMPLETED)
        } catch (ex: Exception) {
            reportStatusService.sendReportStatus(ex.message ?: "Unknown error", reportId, ReportStatus.FAILED)
        }
    }

    private fun generateReport(reportRequest: ReportRequestDto): String {
        val generator = generators.firstOrNull { it.getCategory() == reportRequest.reportCategory }
            ?: throw IllegalArgumentException("No generator found for report type: ${reportRequest.reportType}")

        val (reportBytes, contentType) = generator.generate(reportRequest.reportCategory, reportRequest.reportType)

        val filePath = generateFilePath(reportRequest.reportCategory)

        s3Service.uploadFile(bucket, filePath, reportBytes, contentType)

        return filePath
    }

    private fun updateReportPathInDatabase(reportId: UUID, s3Path: String) {
        val reportEntity = reportRepository.findById(reportId)
            .orElseThrow { IllegalStateException("Report not found with ID: $reportId") }

        val updatedEntity = reportEntity.copy(s3Location = s3Path)
        reportRepository.save(updatedEntity)
    }

    private fun generateFilePath(category: ReportCategory): String {
        val date = LocalDate.now()
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-dd-MM"))
        val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))

        return "$category/$formattedDate/tax_report_$formattedTime.csv"
    }
}

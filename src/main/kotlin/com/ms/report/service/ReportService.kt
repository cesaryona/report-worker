package com.ms.report.service

import com.ms.report.api.dto.ReportRequestDto
import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.service.report.ReportGenerator
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReportService(
    private val generators: List<ReportGenerator>,
    private val s3Service: S3Service
) {

    fun generateReport(reportRequest: ReportRequestDto) {
        val generator = generators.firstOrNull { it.getCategory().equals(reportRequest.reportCategory) }
            ?: throw IllegalArgumentException("No generator found for report type: ${reportRequest.reportType}")

        val (reportBytes, contentType) = generator.generate(reportRequest.reportCategory, reportRequest.reportType)
        s3Service.uploadFile(generateFileKey(reportRequest.reportCategory), reportBytes, contentType)
    }

    private fun generateFileKey(category: ReportCategory): String {
        val date = LocalDate.now()
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-dd-MM"))
        val formattedTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))

        return "$category/$formattedDate/tax_report_$formattedTime.csv"
    }

}
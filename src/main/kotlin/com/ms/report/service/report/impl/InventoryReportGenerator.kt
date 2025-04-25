package com.ms.report.service.report.impl

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.api.repository.entity.enums.ReportType
import com.ms.report.service.report.ReportGenerator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InventoryReportGenerator : ReportGenerator {

    private val logger = LoggerFactory.getLogger(InventoryReportGenerator::class.java)

    override fun getCategory(): ReportCategory {
        return ReportCategory.INVENTORY
    }

    override fun generate(category: ReportCategory, type: ReportType): Pair<ByteArray, String> {
        logger.info("Generating report {} in category {}", type, category)
        return Pair(ByteArray(0), "text/csv")
    }
}
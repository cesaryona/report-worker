package com.ms.report.service.report.impl

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.repository.enums.ReportType
import com.ms.report.service.report.ReportGenerator
import org.springframework.stereotype.Component

@Component
class TaxReportGenerator : ReportGenerator {

    override fun getCategory(): ReportCategory {
        return ReportCategory.TAX
    }

    override fun generate(category: ReportCategory, type: ReportType): Pair<ByteArray, String> {
        println("Gerando relatório $type na categoria ${getCategory()}")
        return Pair(ByteArray(0), "text/csv")
    }

}
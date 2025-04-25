package com.ms.report.service.report

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.api.repository.entity.enums.ReportType

interface ReportGenerator {

    fun getCategory(): ReportCategory

    fun generate(category: ReportCategory, type: ReportType): Pair<ByteArray, String>
}

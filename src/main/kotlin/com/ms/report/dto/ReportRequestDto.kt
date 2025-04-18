package com.ms.report.api.dto

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.repository.enums.ReportType
import java.util.*

data class ReportRequestDto(

    val userId: UUID,
    val reportType: ReportType,
    val reportCategory: ReportCategory
)

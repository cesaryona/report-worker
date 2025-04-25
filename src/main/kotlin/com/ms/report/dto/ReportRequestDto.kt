package com.ms.report.dto

import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.api.repository.entity.enums.ReportType
import java.util.*

data class ReportRequestDto(

    val userId: UUID,
    val reportId: UUID,
    val reportType: ReportType,
    val reportCategory: ReportCategory

)

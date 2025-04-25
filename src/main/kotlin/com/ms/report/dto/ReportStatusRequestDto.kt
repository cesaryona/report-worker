package com.ms.report.dto

import com.report.utils.factory.dto.ReportStatusMessage
import java.util.*

data class ReportStatusRequestDto(

    val reportId: UUID,
    val reportstatus: ReportStatusMessage

)

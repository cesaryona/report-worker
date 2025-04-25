package com.ms.report.repository

import com.ms.report.repository.entity.ReportEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReportRepository : JpaRepository<ReportEntity, UUID> {

}
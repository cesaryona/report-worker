package com.ms.report.repository.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.ms.report.api.repository.entity.enums.ReportStatus
import com.ms.report.repository.entity.ReportEntity
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tb_report_history")
data class ReportHistoryEntity(

    @Id
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    val report: ReportEntity,

    @Column(name = "message")
    val message: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    val status: ReportStatus,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

)

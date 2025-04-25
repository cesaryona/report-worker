package com.ms.report.repository.entity


import com.ms.report.api.repository.entity.enums.ReportCategory
import com.ms.report.api.repository.entity.enums.ReportType
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "tb_report")
data class ReportEntity(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(name = "user_id")
    val userId: UUID,

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL], orphanRemoval = true)
    val reportHistory: List<ReportHistoryEntity> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: ReportType,

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: ReportCategory,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "s3_location")
    val s3Location: String? = null
)
package com.ms.report.controller

import com.ms.report.api.dto.ReportRequestDto
import com.ms.report.service.ReportService
import com.ms.report.service.S3Service
import org.springframework.web.bind.annotation.*
import software.amazon.awssdk.services.s3.S3Client

@RestController
@RequestMapping("/test")
class Control(
    private val s3Service: S3Service,
    private val service: ReportService
) {

    // Upload de arquivo
    @PostMapping("/upload")
    fun uploadFile(@RequestBody dto: ReportRequestDto): String {
        service.generateReport(dto)
        return "File uploaded successfully"
    }

    // Deletar arquivo
    @DeleteMapping("/delete")
    fun deleteFile(@RequestParam("key") key: String): String {
        s3Service.deleteFile(key)
        return "File deleted successfully"
    }

    // Listar arquivos no bucket
    @GetMapping("/list")
    fun listFiles(): List<String> {
        return s3Service.listFilesInBucket()
    }
}
package com.ms.report.service

import com.itextpdf.io.exceptions.IOException
import com.ms.report.api.config.properties.AwsProperties
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*

@Service
class S3Service(
    private val s3Client: S3Client,
    private val properties: AwsProperties
) {

    fun uploadFile(key: String, content: ByteArray, contentType: String) {
        createBucket()

        val request = PutObjectRequest.builder()
            .bucket(getBucket())
            .key(key)
            .contentType(contentType)
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(content))
    }

    fun downloadFile(key: String): ByteArray {
        val request = GetObjectRequest.builder()
            .bucket(getBucket())
            .key(key)
            .build()

        try {
            return s3Client.getObject(request).readAllBytes()
        } catch (e: IOException) {
            throw RuntimeException("Erro ao ler arquivo do S3", e)

        }
    }

    fun deleteFile(key: String) {
        val request = DeleteObjectRequest.builder()
            .bucket(getBucket())
            .key(key)
            .build()

        s3Client.deleteObject(request)
    }

    fun listFilesInBucket(): List<String> {
        val listObjectsRequest = ListObjectsV2Request.builder()
            .bucket(getBucket())
            .build()

        val listObjectsResponse: ListObjectsV2Response = s3Client.listObjectsV2(listObjectsRequest)

        val fileNames = listObjectsResponse.contents().map { it.key() }

        return fileNames
    }

    private fun createBucket() {
        val bucket = getBucket()
        if (!bucketExists(bucket)) {
            s3Client.createBucket { it.bucket(bucket) }
        }
    }

    private fun bucketExists(bucket: String): Boolean {
        return try {
            s3Client.headBucket { it.bucket(bucket) }
            true
        } catch (e: S3Exception) {
            e.statusCode() != 404
        }
    }

    private fun getBucket(): String {
        return properties.bucket
    }
}
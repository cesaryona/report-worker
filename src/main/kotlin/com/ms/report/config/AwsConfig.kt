package com.ms.report.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.ms.report.config.properties.AwsProperties
import com.report.utils.config.AwsClientFactory
import com.report.utils.service.S3Service
import com.report.utils.service.SqsService
import com.report.utils.service.impl.S3ServiceImpl
import com.report.utils.service.impl.SqsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
class AwsConfig(private val properties: AwsProperties) {

    @Bean
    fun awsClientFactory(): AwsClientFactory {
        return AwsClientFactory(
            properties.credentials.accessKey,
            properties.credentials.secretKey,
            properties.region,
            properties.endpoint.url
        )
    }

    @Bean
    fun sqsClient(factory: AwsClientFactory): SqsClient {
        return factory.createSqsClient();
    }

    @Bean
    fun sqsService(sqsClient: SqsClient, objectMapper: ObjectMapper): SqsService {
        return SqsServiceImpl(objectMapper, sqsClient);
    }

    @Bean
    fun s3Client(factory: AwsClientFactory): S3Client {
        return factory.createS3Client();
    }

    @Bean
    fun s3Service(s3Client: S3Client): S3Service {
        return S3ServiceImpl(s3Client)
    }

}
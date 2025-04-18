package com.ms.report.api.config

import com.ms.report.api.config.properties.AwsProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

@Configuration
class AwsConfig(private val properties: AwsProperties) {

    private val credentialsProvider = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(properties.credentials.accessKey, properties.credentials.secretKey)
    )

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.builder()
            .endpointOverride(getURI())
            .credentialsProvider(credentialsProvider)
            .region(Region.of(properties.region))
            .build()
    }

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .endpointOverride(getURI())
            .credentialsProvider(credentialsProvider)
            .region(Region.of(properties.region))
            .serviceConfiguration(
                S3Configuration.builder().pathStyleAccessEnabled(true).build()
            )
            .build()
    }

    private fun getURI(): URI {
        return URI.create(properties.endpoint.url)
    }

}
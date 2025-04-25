package com.ms.report.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "aws")
data class AwsProperties(
    var endpoint: Endpoint = Endpoint(),
    var region: String = "",
    var credentials: Credentials = Credentials(),
    var queue: Queue = Queue(),
    var bucket: String = "",
) {

    data class Endpoint(var url: String = "")
    data class Credentials(var accessKey: String = "", var secretKey: String = "")
    data class Queue(var reportRequest: String = "", var reportStatus: String = "")
}
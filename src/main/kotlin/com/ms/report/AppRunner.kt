package com.ms.report

import com.ms.report.service.message.ReportStatusSqsConsumer
import com.ms.report.service.message.SqsConsumer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import kotlin.concurrent.thread

@Component
class AppRunner(
    private val sqsService: SqsConsumer,
    private val reportStatusSqsConsumer: ReportStatusSqsConsumer
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        thread(start = true) {
            sqsService.consumeMessages()
        }

        thread(start = true) {
            reportStatusSqsConsumer.consumeMessages()
        }
    }
}
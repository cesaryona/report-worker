package com.ms.report

import com.ms.report.service.SqsService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class AppRunner(
    private val sqsService: SqsService
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        sqsService.consumeMessages()
    }
}
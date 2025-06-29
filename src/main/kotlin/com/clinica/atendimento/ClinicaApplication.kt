package com.clinica.atendimento

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClinicaApplication

fun main(args: Array<String>) {
    runApplication<ClinicaApplication>(*args)
}
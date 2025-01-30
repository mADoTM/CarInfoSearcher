package ru.dolzhenkoms.carinfosearcher

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@ConfigurationPropertiesScan("ru.dolzhenkoms.carinfosearcher.configuration.properties")
@EnableScheduling
@SpringBootApplication
class CarinfosearcherApplication

fun main(args: Array<String>) {
	runApplication<CarinfosearcherApplication>(*args)
}

package com.github.chr1stian.masterbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MasterBackendApplication

fun main(args: Array<String>) {
	runApplication<MasterBackendApplication>(*args)
}

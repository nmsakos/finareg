package com.therakid.finareg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FinaregApplication

fun main(args: Array<String>) {
	runApplication<FinaregApplication>(*args)
}

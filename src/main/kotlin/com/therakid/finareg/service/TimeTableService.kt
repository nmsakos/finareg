package com.therakid.finareg.service

import com.therakid.finareg.data.TimeTableRepository
import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.TimeTable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TimeTableService(
    val timeTableRepository: TimeTableRepository
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun getAll(): List<TimeTable> =
        timeTableRepository.findAll()

    fun getByClient(client: Client): List<TimeTable> =
        timeTableRepository.getByClient(client)

    fun getById(id: Long): TimeTable =
        timeTableRepository.getById(id)

    fun save(timeTable: TimeTable): TimeTable =
        timeTableRepository.save(timeTable)

    fun delete(id: Long): Boolean {
        try {
            timeTableRepository.deleteById(id)
            return true
        } catch (e: Exception) {
            logger.error("Error happened while deleting TimeTableSlot {}", id, e)
            return false
        }
    }
}
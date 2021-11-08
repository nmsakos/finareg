package com.therakid.finareg.service

import com.therakid.finareg.data.TimeTableRepository
import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.TimeTable
import org.springframework.stereotype.Service

@Service
class TimeTableService(
    val timeTableRepository: TimeTableRepository
) {
    fun getAll(): List<TimeTable> =
        timeTableRepository.findAll()

    fun getByClient(client: Client): List<TimeTable> =
        timeTableRepository.getByClient(client)

    fun getById(id: Long): TimeTable =
        timeTableRepository.getById(id)

    fun save(timeTable: TimeTable): TimeTable =
        timeTableRepository.save(timeTable)
}
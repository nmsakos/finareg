package com.therakid.finareg.service

import com.therakid.finareg.data.TherapyEventRepository
import com.therakid.finareg.data.TherapyPassRepository
import com.therakid.finareg.domain.*
import com.therakid.finareg.service.WeekService.Companion.dowField
import com.therakid.finareg.service.WeekService.Companion.woyField
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class TherapyPassService(
    private val therapyPassRepository: TherapyPassRepository,
    private val therapyEventRepository: TherapyEventRepository,
    private val familyService: FamilyService,
    private val clientService: ClientService,
    private val weekService: WeekService,
    private val timeTableService: TimeTableService
) {

    fun getOpenPasses() =
        therapyPassRepository.findAll().filter { it.eventCount > it.eventsTaken }

    fun getAllPasses() =
        therapyPassRepository.findAll()

    fun getPass(passId: Long) =
        therapyPassRepository.getById(passId)

    fun getPassesOfClient(clientId: Long) =
        therapyPassRepository.findByClient_Id(clientId)

    fun savePass(id: Long, therapyType: TherapyType, eventCount: Int, eventDuration: TherapyEventDuration, client: Client?, family: Family?, eventsTaken: Int) =
        therapyPassRepository.save(TherapyPass(id, therapyType, eventCount, eventDuration, client, family, eventsTaken))

    fun createPass(
        therapyType: TherapyType, eventCount: Int, eventDuration: TherapyEventDuration, clientId: Long?,
        familyId: Long, firstEventDate: OffsetDateTime, firstTimeTable: TimeTable
    ): TherapyPass {
        val client = if (clientId != null) {
            clientService.getById(clientId)
        } else {
            null
        }
        val family = familyService.getById(familyId)
        val pass = therapyPassRepository.save(TherapyPass(0, therapyType, eventCount, eventDuration, client, family, 0))

        val firstEvent = createEvents(pass, therapyType, firstTimeTable, firstEventDate, eventCount, client, family)
        pass.firstEvent = firstEvent
        return therapyPassRepository.save(pass)
    }

    private fun createEvents(
        pass: TherapyPass,
        therapyType: TherapyType,
        firstTimeTable: TimeTable,
        firstEventDate: OffsetDateTime,
        eventCount: Int,
        client: Client?,
        family: Family
    ): TherapyEvent {
        val clients = mutableListOf<Client>()
        if (client != null) {
            clients.add(client)
        } else {
            clients.addAll(clientService.getByFamily(family))
        }

        var actualDate = firstEventDate
        val timeTablesFromService = timeTableService.getByClientAndTherapyType(clients, therapyType)
        val timeTables = mutableListOf<FlatTimeTable>()
        for (tt in timeTablesFromService) {
            timeTables.addAll(tt.clients!!
                .map { FlatTimeTable(it, tt.dayOfWeek, tt.id) }
                .filter { it -> clients.map { it.id }.contains(it.client.id) })
        }

        val events = mutableListOf<TherapyEvent>()
        var index = timeTables.indexOfFirst { it.id == firstTimeTable.id }
        while (events.size < eventCount) {
            val current = timeTables[index]
            val week = getWeek(actualDate)
            val actualDow = actualDate.get(dowField)
            assert(actualDow == current.dow)

            val event = therapyEventRepository.save(TherapyEvent(0, current.client, actualDate, week, current.dow, pass, TherapyEventState.tesPlanned))
            events.add(event)

            index = getNextIndex(timeTables, index)
            val next = timeTables[index]
            val daysIncrease = if (next.id == current.id) {
                7
            } else {
                daysDiff(current.dow, next.dow)
            }
            actualDate = actualDate.plusDays(daysIncrease)
        }
        return events[0]
    }

    private fun daysDiff(dow1: Int, dow2: Int): Long {
        return (if (dow2 >= dow1) {
            dow2 - dow1
        } else {
            7 - dow1 + dow2
        }).toLong()
    }

    private fun getNextIndex(list: List<Any>, index: Int): Int {
        return if (index + 1 >= list.size) {
            0
        } else {
            index + 1
        }
    }

    private fun getWeek(firstEventDate: OffsetDateTime): Week {
        val weekNumber = firstEventDate.get(woyField)
        return weekService.getByYearAndNumber(firstEventDate.year, weekNumber)
    }

    companion object {
        private class FlatTimeTable(
            val client: Client,
            val dow: Int,
            val id: Long
        )
    }
}


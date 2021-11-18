package com.therakid.finareg.resolvers

import com.therakid.finareg.data.TherapyEventRepository
import com.therakid.finareg.domain.TherapyEvent
import com.therakid.finareg.domain.TherapyPass
import com.therakid.finareg.service.*
import graphql.kickstart.tools.GraphQLMutationResolver
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
class TherapyMutationResolver(
    private val therapyPassService: TherapyPassService,
    private val therapyEventService: TherapyEventService,
    private val therapyTypeService: TherapyTypeService,
    private val therapyDurationService: TherapyDurationService,
    private val timeTableService: TimeTableService,
    private val familyService: FamilyService,
    private val clientService: ClientService,
    private val weekService: WeekService,
    private val therapistService: TherapistService,
    private val roomService: RoomService,
    private val therapyEventStateService: TherapyEventStateService
) : GraphQLMutationResolver {
    val logger = LoggerFactory.getLogger(javaClass)

    fun createPass(therapyType: Long, eventCount: Int, eventDuration: Long, clientId: Long, familyId: Long, firstEventDate: OffsetDateTime, firstTimeTable: Long): TherapyPass {
        return therapyPassService.createPass(
            therapyTypeService.getById(therapyType), eventCount,
            therapyDurationService.getById(eventDuration), clientId, familyId,
            firstEventDate,
            timeTableService.getById(firstTimeTable)
        )
    }

    fun updateEvent(
        id: Long, clientId: Long?, date: OffsetDateTime,
        weekId: Long?, dayOfWeek: Int?, passId: Long, invoice: Long?,
        therapistId: Long?, roomId: Long?, stateId: Long
    ): TherapyEvent {
        logger.debug(
            "updateEvent: id: {}, clientId: {}, date: {}, weekId: {}, dayOfWeek: {}, passId: {}, invoice: {}, therapistId: {}, roomId: {}, stateId: {}",
            id, clientId, date, weekId, dayOfWeek, passId, invoice, therapistId, roomId, stateId
        )
        val client = if (clientId == null) {
            null
        } else {
            clientService.getById(clientId)
        }
        val therapist = if (therapistId != null) {
            therapistService.getById(therapistId)
        } else {
            null
        }
        val room = if (roomId != null) {
            roomService.getById(roomId)
        } else {
            null
        }
        val week = if (weekId != null) {
            weekService.getById(weekId)
        } else {
            weekService.getByDate(date)
        }

        return therapyEventService.saveEvent(
            id, client, date, week, dayOfWeek ?: date.dayOfWeek.value, therapyPassService.getPass(passId),
            invoice, therapist, room, therapyEventStateService.getById(stateId)
        )
    }

    fun createEvent(clientId: Long?, date: OffsetDateTime, weekId: Long?, dayOfWeek: Int?, passId: Long, invoice: Long, therapistId: Long?, roomId: Long?, stateId: Long) =
        updateEvent(0, clientId, date, weekId, dayOfWeek, passId, invoice, therapistId, roomId, stateId)

    fun savePass(
        id: Long,
        clientId: Long?,
        familyId: Long,
        therapyTypeId: Long,
        eventCount: Int,
        eventDurationId: Long,
        firstEventId: Long,
        eventsTaken: Int,
        invoice: Long?
    ): TherapyPass {
        val client = if (clientId == null) {
            null
        } else {
            clientService.getById(clientId)
        }
        return therapyPassService.savePass(
            id, therapyTypeService.getById(therapyTypeId),
            eventCount, therapyDurationService.getById(eventDurationId),
            client, familyService.getById(familyId), eventsTaken
        )
    }
}
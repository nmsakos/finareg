package com.therakid.finareg.resolvers

import com.therakid.finareg.domain.Room
import com.therakid.finareg.domain.TherapyType
import com.therakid.finareg.domain.TimeTable
import com.therakid.finareg.service.ClientService
import com.therakid.finareg.service.RoomService
import com.therakid.finareg.service.TherapistService
import com.therakid.finareg.service.TimeTableService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component
import java.time.OffsetTime

@Component
class TimeTableMutationResolver(
    val timeTableService: TimeTableService,
    val clientService: ClientService,
    val therapistService: TherapistService
) : GraphQLMutationResolver {

    fun assignTimeSlotByIdToClient(timeSlot: Long, clientId: Long): TimeTable? {
        val timeTable = timeTableService.getById(timeSlot)
        val client = clientService.getById(clientId)
        timeTable.clients = listOf(client)
        return timeTableService.save(timeTable)

    }

    fun assignTimeSlotByDayAndTimeToClient(dayOfWeek: Int, fromTime: OffsetTime, toTime: OffsetTime, therapist: Long, room: Long, client: Long): TimeTable? {
        return null
    }

    fun createTimeTableSlot(
        dayOfWeek: Int,
        fromTime: OffsetTime, toTime: OffsetTime,
        therapyType: Long, room: Long, therapist: Long,
        clients: List<Long>
    ): TimeTable {
        val timeTable = TimeTable(0)
        timeTable.fromTime = fromTime
        timeTable.toTime = toTime
        timeTable.dayOfWeek = dayOfWeek
        timeTable.room = Room.byId(room)
        timeTable.therapyType = TherapyType.byId(therapyType)
        timeTable.therapist = therapistService.getById(therapist)
        timeTable.clients = clients.map { clientService.getById(it) }

        return timeTableService.save(timeTable)
    }

    fun updateTimeTableSlot(
        timeSlot: Long, dayOfWeek: Int,
        fromTime: OffsetTime, toTime: OffsetTime,
        therapyType: Long, room: Long, therapist: Long,
        clients: List<Long>
    ): TimeTable {
        val timeTable = timeTableService.getById(timeSlot)
        timeTable.fromTime = fromTime
        timeTable.toTime = toTime
        timeTable.dayOfWeek = dayOfWeek
        timeTable.room = Room.byId(room)
        timeTable.therapyType = TherapyType.byId(therapyType)
        timeTable.therapist = therapistService.getById(therapist)
        timeTable.clients = clients.map { clientService.getById(it) }

        return timeTableService.save(timeTable)
    }

    fun deleteTimeTableSlot(timeSlot: Long): Boolean {
        return timeTableService.delete(timeSlot)
    }


}
package com.therakid.finareg.resolvers

import com.therakid.finareg.data.*
import com.therakid.finareg.domain.*
import com.therakid.finareg.service.ClientService
import com.therakid.finareg.service.RoomService
import com.therakid.finareg.service.TherapistService
import com.therakid.finareg.service.TimeTableService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TimeTableQueryResolvers(
    val roomService: RoomService,
    val timeTableService: TimeTableService,
    val therapistService: TherapistService,
    val clientService: ClientService
) : GraphQLQueryResolver {

    fun rooms(): List<Room> =
        roomService.getAll();

    fun timeTables(): List<TimeTable> =
        timeTableService.getAll()

    fun timeTablesFiltered(room: Long, therapist: Long) =
        timeTables().filter {
            (room < 1 || it.room!!.id == room) &&
                    (therapist < 1 || it.therapist?.id == therapist)
        }

    fun therapists() =
        therapistService.getAll()

    fun timeTablesByClient(client: Long) =
        timeTableService.getByClient(clientService.getById(client))
}

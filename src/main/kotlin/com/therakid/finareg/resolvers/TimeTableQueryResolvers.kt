package com.therakid.finareg.resolvers

import com.therakid.finareg.domain.Room
import com.therakid.finareg.domain.TherapyType
import com.therakid.finareg.domain.TimeTable
import com.therakid.finareg.service.*
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TimeTableQueryResolvers(
    private val roomService: RoomService,
    private val timeTableService: TimeTableService,
    private val therapistService: TherapistService,
    private val clientService: ClientService,
    private val therapyTypeService: TherapyTypeService
) : GraphQLQueryResolver {

    fun rooms(): List<Room> =
        roomService.getAll()

    fun timeTables(): List<TimeTable> =
        timeTableService.getAll()

    fun timeTablesFiltered(room: Long, therapist: Long) =
        timeTables().filter {
            (room < 1 || it.room!!.id == room) &&
                    (therapist < 1 || it.therapist?.id == therapist)
        }

    fun therapists() =
        therapistService.getAll()

    fun timeTablesByClients(clients: List<Long>) =
        timeTableService.getByClient(clients.map { clientService.getById(it) })

    fun timeTablesByClientsAndTherapyType(clients: List<Long>, therapyType: Long) =
        timeTableService.getByClientAndTherapyType(clients.map { clientService.getById(it) }, therapyTypeService.getById(therapyType))
}

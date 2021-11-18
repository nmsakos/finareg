package com.therakid.finareg.service

import com.therakid.finareg.data.TherapyEventRepository
import com.therakid.finareg.domain.*
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class TherapyEventService(
    private val therapyEventRepository: TherapyEventRepository
) {
    fun getEventsOfPass(passId: Long) =
        therapyEventRepository.findByTherapyPass_Id(passId)

    fun getAllEvents() =
        therapyEventRepository.findAll()

    fun getById(id: Long) =
        therapyEventRepository.getById(id)

    fun saveEvent(
        id: Long,
        client: Client?,
        date: OffsetDateTime,
        week: Week,
        dayOfWeek: Int,
        pass: TherapyPass,
        invoice: Long?,
        therapist: Therapist?,
        room: Room?,
        state: TherapyEventState
    ): TherapyEvent {
        return therapyEventRepository.save(TherapyEvent(id, client, date, week, dayOfWeek, pass, therapist, room, state))
    }

}
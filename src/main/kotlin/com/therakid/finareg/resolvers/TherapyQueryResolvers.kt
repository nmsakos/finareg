package com.therakid.finareg.resolvers

import com.therakid.finareg.data.*
import com.therakid.finareg.domain.TherapyEvent
import com.therakid.finareg.domain.TherapyEventState
import com.therakid.finareg.domain.TherapyPass
import com.therakid.finareg.service.*
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TherapyQueryResolver(
    private val therapyPassService: TherapyPassService,
    private val therapyEventService: TherapyEventService,
    private val therapyTypeService: TherapyTypeService,
    private val therapyDurationService: TherapyDurationService,
    private val therapyEventStateService: TherapyEventStateService
) : GraphQLQueryResolver {

    fun therapyTypes() =
        therapyTypeService.getAll()

    fun therapyEventDurations() =
        therapyDurationService.getAll()

    fun therapyEventStates() =
        therapyEventStateService.getAll()

    fun therapyPasses(onlyOpen: Boolean) =
        if (onlyOpen) {
            therapyPassService.getOpenPasses()
        } else {
            therapyPassService.getAllPasses()
        }

    fun therapyPass(passId: Long) =
        therapyPassService.getPass(passId)

    fun therapyEvents() =
        therapyEventService.getAllEvents()

    fun passesOfClient(clientId: Long, onlyOpen: Boolean) =
        therapyPassService.getPassesOfClient(clientId).filter { !onlyOpen || it.eventCount > it.eventsTaken }

    fun eventsOfPass(passId: Long, noCancelled: Boolean) =
        therapyEventService.getEventsOfPass(passId).filter { !noCancelled || it.state.id != TherapyEventState.tesCancelled.id }
}

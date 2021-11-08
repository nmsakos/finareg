package com.therakid.finareg.resolvers

import com.therakid.finareg.data.*
import com.therakid.finareg.domain.TherapyEvent
import com.therakid.finareg.domain.TherapyPass
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component

@Component
class TherapyQueryResolver(
    val therapyPassRepository: TherapyPassRepository,
    val therapyEventRepository: TherapyEventRepository,
    val therapyTypeRepository: TherapyTypeRepository,
    val therapyEventDurationRepository: TherapyEventDurationRepository
) : GraphQLQueryResolver {

    fun therapyTypes() =
        therapyTypeRepository.findAll()

    fun therapyEventDurations() =
        therapyEventDurationRepository.findAll()

    fun therapyPasses(onlyOpen: Boolean) =
        therapyPassRepository.findAll().filter { !onlyOpen || it.eventCount > it.eventsTaken }

    fun therapyEvents() =
        therapyEventRepository.findAll()

    fun passesOfClient(clientId: Long, onlyOpen: Boolean) =
        therapyPassRepository.getOneToManyEntities(clientId).filter { !onlyOpen || it.eventCount > it.eventsTaken }

    fun eventsOfPass(passId: Long) =
        therapyEventRepository.getOneToManyEntities(passId)
}

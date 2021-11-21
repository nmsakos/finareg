package com.therakid.finareg.service

import com.therakid.finareg.data.TherapyEventDurationRepository
import com.therakid.finareg.data.TherapyEventStateRepository
import com.therakid.finareg.data.TherapyTypeRepository
import com.therakid.finareg.domain.TherapyEventDuration
import com.therakid.finareg.domain.TherapyEventState
import com.therakid.finareg.domain.TherapyType
import org.springframework.stereotype.Service

@Service
class TherapyTypeService(
    private val therapyTypeRepository: TherapyTypeRepository
) {
    fun getById(id: Long) =
        therapyTypeRepository.getById(id)

    fun getAll() =
        therapyTypeRepository.findAll()

    fun save(id: Long, description: String) =
        therapyTypeRepository.save(TherapyType(id, description))
}

@Service
class TherapyDurationService(
    private val therapyEventDurationRepository: TherapyEventDurationRepository
) {

    fun getAll() =
        therapyEventDurationRepository.findAll()

    fun getById(id: Long) =
        therapyEventDurationRepository.getById(id)

    fun save(id: Long, minutes: Int, description: String) =
        therapyEventDurationRepository.save(TherapyEventDuration(id, minutes, description))
}

@Service
class TherapyEventStateService(
    private val therapyEventStateRepository: TherapyEventStateRepository
) {

    fun getAll() =
        therapyEventStateRepository.findAll()

    fun getById(id: Long) =
        therapyEventStateRepository.getById(id)

    fun save(id: Long, description: String) =
        therapyEventStateRepository.save(TherapyEventState(id, description))
}
package com.therakid.finareg.service

import com.therakid.finareg.data.TherapyEventDurationRepository
import com.therakid.finareg.data.TherapyEventStateRepository
import com.therakid.finareg.data.TherapyTypeRepository
import org.springframework.stereotype.Service

@Service
class TherapyTypeService(
    private val therapyTypeRepository: TherapyTypeRepository
) {
    fun getById(id: Long) =
        therapyTypeRepository.getById(id)

    fun getAll() =
        therapyTypeRepository.findAll()
}

@Service
class TherapyDurationService(
    private val therapyEventDurationRepository: TherapyEventDurationRepository
) {

    fun getAll() =
        therapyEventDurationRepository.findAll()

    fun getById(id: Long) =
        therapyEventDurationRepository.getById(id)
}

@Service
class TherapyEventStateService(
    private val therapyEventStateRepository: TherapyEventStateRepository
) {

    fun getAll() =
        therapyEventStateRepository.findAll()

    fun getById(id: Long) =
        therapyEventStateRepository.getById(id)

}
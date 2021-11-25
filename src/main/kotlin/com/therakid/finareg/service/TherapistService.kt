package com.therakid.finareg.service

import com.therakid.finareg.data.TherapistRepository
import com.therakid.finareg.domain.Therapist
import org.springframework.stereotype.Service

@Service
class TherapistService(
    val therapistRepository: TherapistRepository
) {
    fun getAll(): List<Therapist> =
        therapistRepository.findAll()

    fun getById(id: Long) =
        therapistRepository.getById(id)

    fun save(id: Long, name: String, phone: String, email: String) =
        therapistRepository.save(Therapist(id, name, phone, email))
}
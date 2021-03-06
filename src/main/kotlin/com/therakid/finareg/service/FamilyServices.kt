package com.therakid.finareg.service

import com.therakid.finareg.data.ClientRepository
import com.therakid.finareg.data.FamilyRepository
import com.therakid.finareg.data.ParentRepository
import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.Family
import com.therakid.finareg.domain.Parent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClientService(
    private val clientRepository: ClientRepository
) {
    fun getAll(): List<Client> =
        clientRepository.findAll()

    fun getById(id: Long): Client =
        clientRepository.getById(id)

    fun getByFamily(familyId: Long): List<Client> =
        clientRepository.findByFamilyId(familyId)

    fun getByFamily(family: Family): List<Client> =
        clientRepository.findByFamilyId(family.id)
}

@Service
class ParentService(
    private val parentRepository: ParentRepository
) {
    fun getAll(): List<Parent> =
        parentRepository.findAll()

    fun getById(id: Long): Parent =
        parentRepository.getById(id)

    fun getByFamily(familyId: Long): List<Parent> =
        parentRepository.findByFamilyId(familyId)

    fun getByFamily(family: Family): List<Parent> =
        parentRepository.findByFamilyId(family.id)
}

@Service
class FamilyService(
    private val familyRepository: FamilyRepository,
    private val clientService: ClientService,
    private val parentService: ParentService
) {

    fun getAll(): List<Family> =
        familyRepository.findAll()

    fun getById(id: Long): Family =
        familyRepository.getById(id)

    fun getFamilyJoins(family: Family) : Family {
        family.clients = clientService.getByFamily(family)
        family.parents = parentService.getByFamily(family)
        return family
    }
}

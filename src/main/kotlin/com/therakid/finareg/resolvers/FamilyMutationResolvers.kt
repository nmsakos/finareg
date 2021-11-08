package com.therakid.finareg.resolvers

import com.therakid.finareg.data.ClientRepository
import com.therakid.finareg.data.FamilyRepository
import com.therakid.finareg.data.ParentRepository
import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.Family
import com.therakid.finareg.domain.Parent
import com.therakid.finareg.service.FamilyService
import graphql.kickstart.tools.GraphQLMutationResolver
import org.springframework.stereotype.Component

@Component
class FamilyMutationResolver(val familyRepository: FamilyRepository,
                             val familyService: FamilyService) : GraphQLMutationResolver {

    fun newFamily(name: String) =
        familyRepository.save(Family(0, name))

    fun updateFamily(id: Long, name: String) : Family {
        val family = Family(id, name)
        familyService.getFamilyJoins(family)
        return familyRepository.save(family)
    }

    fun deleteFamily(id: Long) : Family {
        throw NotImplementedError()
    }
}

@Component
class ParentMutationResolver(val parentRepository: ParentRepository) : GraphQLMutationResolver {

    fun newParent(name: String, phone: String, email: String, familyId: Long) : Parent =
        parentRepository.save(Parent(0, name, phone, email, familyId))

    fun updateParent(id: Long, name: String, phone: String?, email: String?, familyId: Long) : Parent =
        parentRepository.save(Parent(id, name, phone?:"", email?:"", familyId))

    fun removeParent(id: Long) : Boolean {
        val parent = parentRepository.findById(id)
        parentRepository.delete(parent.get())
        return true
    }
}

@Component
class ClientMutationResolver(val clientRepository: ClientRepository) : GraphQLMutationResolver {

    fun newClient(name: String, phone: String, email: String, familyId: Long) : Client =
        clientRepository.save(Client(0, name, phone, email, familyId))

    fun newClientFull(newClient: Client) =
        newClient(newClient.name, newClient.phone ?: "", newClient.email ?: "", newClient.familyId)

    fun updateClient(id: Long, name: String, phone: String?, email: String?, familyId: Long) : Client =
        clientRepository.save(Client(id, name, phone?:"", email?:"", familyId))

    fun removeClient(id: Long) : Boolean {
        val parent = clientRepository.findById(id)
        clientRepository.delete(parent.get())
        return true
    }
}
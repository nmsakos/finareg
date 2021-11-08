package com.therakid.finareg.resolvers

import com.therakid.finareg.data.ClientRepository
import com.therakid.finareg.data.FamilyRepository
import com.therakid.finareg.data.ParentRepository
import com.therakid.finareg.domain.Family
import com.therakid.finareg.service.ClientService
import com.therakid.finareg.service.FamilyService
import com.therakid.finareg.service.ParentService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.stereotype.Component
import java.util.*

@Component
class FamilyQueryResolver(
    val familyService: FamilyService
) : GraphQLQueryResolver {

    fun families(): List<Family> {
        val findAll = familyService.getAll()
        for (family in findAll) {
            familyService.getFamilyJoins(family)
        }
        return findAll
    }

    fun family(id: Long): Optional<Family> {
        val family = Optional.ofNullable(familyService.getById(id))
        family.ifPresent { familyService.getFamilyJoins(it) }
        return family
    }

}

@Component
class ClientQueryResolver(val clientService: ClientService) : GraphQLQueryResolver {

    fun clients() = clientService.getAll()

    fun client(id: Long) = clientService.getById(id)

    fun clientsOfFamily(familyId: Long) = clientService.getByFamily(familyId)

}

@Component
class ParentQueryResolver(val parentService: ParentService) : GraphQLQueryResolver {

    fun parents() = parentService.getAll()

    fun parent(id: Long) = parentService.getById(id)

    fun parentsOfFamily(familyId: Long) = parentService.getByFamily(familyId)

}


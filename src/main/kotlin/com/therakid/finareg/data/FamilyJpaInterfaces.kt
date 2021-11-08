package com.therakid.finareg.data

import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.Family
import com.therakid.finareg.domain.Parent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface FamilyRepository: JpaRepository<Family, Long>

interface ClientRepository: JpaRepository<Client, Long>, CustomOneToManyEntityRepository<Client>

interface ParentRepository: JpaRepository<Parent, Long>, CustomOneToManyEntityRepository<Parent>



@Repository
class ClientRepositoryImpl: AbstractOneToManyEntityRepository<Client>() {

    override val entityClass: Class<Client>
        get() = Client::class.java
    override val mappedBy: String
        get() = "familyId"


}

@Repository
class ParentRepositoryImpl: AbstractOneToManyEntityRepository<Parent>() {

    override val entityClass: Class<Parent>
        get() = Parent::class.java
    override val mappedBy: String
        get() = "familyId"

}

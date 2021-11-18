package com.therakid.finareg.data

import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.Family
import com.therakid.finareg.domain.Parent
import org.springframework.data.jpa.repository.JpaRepository

interface FamilyRepository: JpaRepository<Family, Long>

interface ClientRepository: JpaRepository<Client, Long> {
    fun findByFamilyId(familyId: Long): List<Client>
}

interface ParentRepository: JpaRepository<Parent, Long>{
    fun findByFamilyId(familyId: Long): List<Parent>
}

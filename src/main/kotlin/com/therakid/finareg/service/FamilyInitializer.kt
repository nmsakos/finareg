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
class FamilyInitializer {

    @Autowired
    lateinit var familyRepository: FamilyRepository

    @Autowired
    lateinit var clientRepository: ClientRepository

    @Autowired
    lateinit var parentRepository: ParentRepository

    fun initData() {
        createNemesCsalad()
        createHervaiCsalad()
    }

    private fun createNemesCsalad() {
        val csalad = familyRepository.save(Family(0, "Nemes"))
        parentRepository.save(Parent(0, "Kata", "+36202459782", "fhrkata@gmail.com", csalad.id))
        parentRepository.save(Parent(0, "Akos", "+36204996113", "nmsakos@gmail.com", csalad.id))
        clientRepository.save(Client(0, "Nemes Gyerek", "", "", csalad.id))
    }

    private fun createHervaiCsalad() {
        val csalad = familyRepository.save(Family(0, "Hervai"))
        parentRepository.save(Parent(0, "Hervai Anita", "+36208888888", "hervai.anita@gmail.com", csalad.id))
        clientRepository.save(Client(0, "Hervai Gergő", "", "", csalad.id))
        clientRepository.save(Client(0, "Hervai Bence", "", "", csalad.id))
    }

    fun removeAll() {
        familyRepository.deleteAll()
        clientRepository.deleteAll()
        parentRepository.deleteAll()
    }

}
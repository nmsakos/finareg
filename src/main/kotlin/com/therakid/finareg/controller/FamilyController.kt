package com.therakid.finareg.controller

import com.therakid.finareg.data.FamilyRepository
import com.therakid.finareg.domain.Client
import com.therakid.finareg.domain.Family
import com.therakid.finareg.domain.Parent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/families")
class FamilyController {

    @Autowired
    lateinit var familyRepository: FamilyRepository

    @RequestMapping("")
    fun getFamilies() : List<Family> {
        val findAll = familyRepository.findAll()
        return findAll
    }

    @RequestMapping("/{id}/details")
    fun getFamily(@PathVariable id: Long) : ModelAndView =
        ModelAndView("families/details", "family", familyRepository.findById(id).orElse(Family(0,"")))

    @RequestMapping("/{id}/edit")
    fun editFamily(@PathVariable id: Long) : ModelAndView =
        ModelAndView("families/edit", "family", familyRepository.findById(id).orElse(Family(0,"")))

    @RequestMapping("/add")
    fun addFamily(): ModelAndView {
        val family = Family(0, "")
        family.parents = listOf(Parent(0,"","","", 0), Parent(0,"","","", 0))
        family.clients = listOf(Client(0,"","","", 0))
        return ModelAndView("families/add","family", family)
    }

    @RequestMapping(value =arrayOf("save"), method = arrayOf(RequestMethod.POST))
    fun saveFamily(family: Family) : String {
        familyRepository.save(family)
        return "redirect:/families/"
    }


}
package com.therakid.finareg.controller

import com.therakid.finareg.service.FamilyInitializer
import com.therakid.finareg.service.TherapyInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    @Autowired
    lateinit var familyInitializer: FamilyInitializer

    @Autowired
    lateinit var therapyInitializer: TherapyInitializer

    @RequestMapping("")
    fun homePage(): ModelAndView {
        return ModelAndView("index")
    }

    @RequestMapping("bootstrap")
    fun bootstrap(): ModelAndView {
        therapyInitializer.removeAll()
        familyInitializer.removeAll()

        familyInitializer.initData()
        therapyInitializer.initData()

        return ModelAndView("/")
    }

}
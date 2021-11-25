package com.therakid.finareg.controller

import com.therakid.finareg.service.FamilyInitializer
import com.therakid.finareg.service.TherapyInitializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@CrossOrigin
@RestController
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

    @RequestMapping("clearAllData", produces = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun clearAllData(): Response {
        therapyInitializer.removeAll()
        familyInitializer.removeAll()

        return Response("success")
    }

}

class Response(
    val response: String
)
package com.therakid.finareg.service

import com.therakid.finareg.data.*
import com.therakid.finareg.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.*
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class TherapyInitializer {

    private val offset: ZoneOffset
        get() = OffsetTime.now().offset

    private val currentYear: Int = 2021

    @Autowired
    lateinit var therapyTypeRepository: TherapyTypeRepository

    @Autowired
    lateinit var therapyPassRepository: TherapyPassRepository

    @Autowired
    lateinit var therapyEventRepository: TherapyEventRepository

    @Autowired
    lateinit var therapyEventStateRepository: TherapyEventStateRepository

    @Autowired
    lateinit var therapyEventDurationRepository: TherapyEventDurationRepository

    @Autowired
    lateinit var familyService: FamilyService

    @Autowired
    lateinit var weekService: WeekService

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var timeTableRepository: TimeTableRepository

    @Autowired
    lateinit var therapistRepository: TherapistRepository

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun initData() {

        val kata = therapistRepository.save(Therapist(1, "Fehér Kata", "+36202459782", "fhrkata@gmail.com"))
        val anita = therapistRepository.save(Therapist(2, "Turcsik Anita", "+36209999999", "anita@gmail.com"))

        roomRepository.save(Room.room1)
        roomRepository.save(Room.room2)
        roomRepository.save(Room.room3)

        therapyTypeRepository.save(TherapyType.ttMozgas)
        therapyTypeRepository.save(TherapyType.ttBeszed)

        therapyEventStateRepository.save(TherapyEventState.tesPlanned)
        therapyEventStateRepository.save(TherapyEventState.tesComplete)
        therapyEventStateRepository.save(TherapyEventState.tesCancelled)

        val ted30perc = therapyEventDurationRepository.save(TherapyEventDuration(1, 30, "30 perc"))
        val ted45perc = therapyEventDurationRepository.save(TherapyEventDuration(2, 45, "45 perc"))

        val week35 = weekService.getByYearAndNumber(currentYear, 35)
        val week36 = weekService.getByYearAndNumber(currentYear, 36)
        val week37 = weekService.getByYearAndNumber(currentYear, 37)
        val week38 = weekService.getByYearAndNumber(currentYear, 38)
        val week39 = weekService.getByYearAndNumber(currentYear, 39)

        val nemesFamily = getFamily("Nemes")
        familyService.getFamilyJoins(nemesFamily)
        val nemesGyerek = nemesFamily.clients?.get(0) ?: getClients(nemesFamily)!![0]

        val hervaiFamily = getFamily("Hervai")
        val hervaiGyerekek = getClients(hervaiFamily)!!

        val nemesClientPass = therapyPassRepository.save(TherapyPass(0, TherapyType.ttBeszed, 4, ted30perc, nemesGyerek, null, 4))
        val familyPass = therapyPassRepository.save(TherapyPass(0, TherapyType.ttMozgas, 8, ted45perc, null, hervaiFamily, 5))

        nemesClientPass.firstEvent = therapyEventRepository.save(
            TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 3), week35, 5, nemesClientPass, kata, Room.room2, TherapyEventState.tesPlanned)
        )
        therapyPassRepository.save(nemesClientPass)

        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 10), week36, 5, nemesClientPass, kata, Room.room2, TherapyEventState.tesPlanned))
        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 17), week37, 5, nemesClientPass, kata, Room.room2, TherapyEventState.tesPlanned))
        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 24), week38, 5, nemesClientPass, kata, Room.room2, TherapyEventState.tesPlanned))

        familyPass.firstEvent = therapyEventRepository.save(
            TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 3), week35, 5, familyPass, kata, Room.room2, TherapyEventState.tesPlanned)
        )
        therapyPassRepository.save(familyPass)

        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[0], getDate(Month.SEPTEMBER, 10), week36, 5, familyPass, kata, Room.room2, TherapyEventState.tesPlanned))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[0], getDate(Month.SEPTEMBER, 17), week37, 5, familyPass, kata, Room.room2, TherapyEventState.tesPlanned))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[1], getDate(Month.SEPTEMBER, 24), week38, 5, familyPass, kata, Room.room2, TherapyEventState.tesPlanned))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[1], getDate(Month.OCTOBER, 1), week39, 5, familyPass, kata, Room.room2, TherapyEventState.tesPlanned))

        timeTableRepository.save(TimeTable(0, 1, getTime(9, 10), getTime(9, 55),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(nemesGyerek)))

        timeTableRepository.save(TimeTable(0, 1, getTime(10, 5), getTime(10, 50),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(hervaiGyerekek[0])))

        timeTableRepository.save(TimeTable(0, 3, getTime(10, 5), getTime(10, 50),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(hervaiGyerekek[0])))

        timeTableRepository.save(TimeTable(0, 3, getTime(10, 30), getTime(11, 15),
            TherapyType.ttBeszed, Room.room3, anita, mutableListOf(hervaiGyerekek[0])))

        timeTableRepository.save(TimeTable(0, 3, getTime(14, 50), getTime(15, 35),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(hervaiGyerekek[0])))

        timeTableRepository.save(TimeTable(0, 2, getTime(13, 0), getTime(13, 45),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(hervaiGyerekek[1])))

        timeTableRepository.save(TimeTable(0, 4, getTime(13, 0), getTime(13, 45),
            TherapyType.ttBeszed, Room.room2, kata, mutableListOf(hervaiGyerekek[1], hervaiGyerekek[0])))

        timeTableRepository.save(TimeTable(0, 2, getTime(8, 15), getTime(9, 0),
            TherapyType.ttMozgas, Room.room3, anita,  mutableListOf(nemesGyerek, hervaiGyerekek[0], hervaiGyerekek[1])))

        timeTableRepository.save(TimeTable(0, 4, getTime(9, 10), getTime(9, 55),
            TherapyType.ttMozgas, Room.room3, anita, mutableListOf(nemesGyerek, hervaiGyerekek[0], hervaiGyerekek[1])))
    }

    private fun getDate(month: Month, day: Int) =
        OffsetDateTime.of(LocalDateTime.of(currentYear, month, day, 0, 0), offset)

    private fun getTime(hour: Int, minute: Int) =
        OffsetTime.of(hour, minute, 0, 0, offset)

    private fun getFamily(name: String): Family {
        val builder = entityManager.criteriaBuilder
        val entityClass = Family::class.java
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(builder.equal(entity.get<Long>("name"), name))

        return entityManager.createQuery(query).singleResult
    }

    private fun getClients(family: Family): List<Client>? {
        val builder = entityManager.criteriaBuilder
        val entityClass = Client::class.java
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(builder.equal(entity.get<Long>("familyId"), family.id))

        return entityManager.createQuery(query).resultList
    }

    fun removeAll() {
        therapyPassRepository.findAll().forEach {
            it.firstEvent = null
            therapyPassRepository.save(it)
        }
        therapyEventRepository.deleteAll()
        timeTableRepository.deleteAll()
        therapyPassRepository.deleteAll()
        weekService.deleteAll()
        therapyTypeRepository.deleteAll()
        therapyEventDurationRepository.deleteAll()
        roomRepository.deleteAll()
        therapistRepository.deleteAll()
        therapyEventStateRepository.deleteAll()
    }

    fun getTimeTable(dayOfWeek: Int, fromTime: OffsetTime, toTime: OffsetTime, room: Room, therapist: Therapist): TimeTable {
        val builder = entityManager.criteriaBuilder
        val entityClass = TimeTable::class.java
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(
            builder.and(
                builder.equal(entity.get<Long>("dayOfWeek"), dayOfWeek),
                builder.equal(entity.get<Long>("fromTime"), fromTime),
                builder.equal(entity.get<Long>("toTime"), toTime),
                builder.equal(entity.get<Long>("therapist"), therapist),
                builder.equal(entity.get<Long>("room"), room)
            )
        )

        return entityManager.createQuery(query).singleResult
    }

    fun getWeek(week: Int): Week {

        val builder = entityManager.criteriaBuilder
        val entityClass = Week::class.java
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(
            builder.and(
                builder.equal(entity.get<Long>("year"), currentYear),
                builder.equal(entity.get<Long>("number"), week)
            )
        )

        return entityManager.createQuery(query).singleResult
    }
}
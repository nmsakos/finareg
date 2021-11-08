package com.therakid.finareg.service

import com.therakid.finareg.data.*
import com.therakid.finareg.domain.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.*
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
    lateinit var therapyEventDurationRepository: TherapyEventDurationRepository

    @Autowired
    lateinit var familyService: FamilyService

    @Autowired
    lateinit var weekRepository: WeekRepository

    @Autowired
    lateinit var roomRepository: RoomRepository

    @Autowired
    lateinit var timeTableRepository: TimeTableRepository

    @Autowired
    lateinit var therapistRepository: TherapistRepository

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun initData() {

        val kata = therapistRepository.save(Therapist(0, "Fehér Kata", "+36202459782", "fhrkata@gmail.com"))
        val anita = therapistRepository.save(Therapist(0, "Turcsik Anita", "+36209999999", "anita@gmail.com"))

        roomRepository.save(Room.room1)
        roomRepository.save(Room.room2)
        roomRepository.save(Room.room3)

        therapyTypeRepository.save(TherapyType.ttMozgas)
        therapyTypeRepository.save(TherapyType.ttBeszed)

        val ted30perc = therapyEventDurationRepository.save(TherapyEventDuration(1, 30, "30 perc"))
        val ted45perc = therapyEventDurationRepository.save(TherapyEventDuration(2, 45, "45 perc"))

        createWeeks()
        val week35 = getWeek(35)
        val week36 = getWeek(36)
        val week37 = getWeek(37)
        val week38 = getWeek(38)
        val week39 = getWeek(39)

        val nemesFamily = getFamily("Nemes")
        familyService.getFamilyJoins(nemesFamily)
        val nemesGyerek = nemesFamily.clients?.get(0) ?: getClients(nemesFamily)!![0]

        val hervaiFamily = getFamily("Hervai")
        val hervaiGyerekek = getClients(hervaiFamily)!!

        val nemesClientPass = therapyPassRepository.save(TherapyPass(0, TherapyType.ttBeszed, 4, ted30perc, nemesGyerek, null, 4))
        val familyPass = therapyPassRepository.save(TherapyPass(0, TherapyType.ttMozgas, 8, ted45perc, null, hervaiFamily, 5))

        nemesClientPass.firstEvent = therapyEventRepository.save(
            TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 3), week35, 5, nemesClientPass, kata, Room.room2)
        )
        therapyPassRepository.save(nemesClientPass)

        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 10), week36, 5, nemesClientPass, kata, Room.room2))
        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 17), week37, 5, nemesClientPass, kata, Room.room2))
        therapyEventRepository.save(TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 24), week38, 5, nemesClientPass, kata, Room.room2))

        familyPass.firstEvent = therapyEventRepository.save(
            TherapyEvent(0, nemesGyerek, getDate(Month.SEPTEMBER, 3), week35, 5, familyPass, kata, Room.room2)
        )
        therapyPassRepository.save(familyPass)

        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[0], getDate(Month.SEPTEMBER, 10), week36, 5, familyPass, kata, Room.room2))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[0], getDate(Month.SEPTEMBER, 17), week37, 5, familyPass, kata, Room.room2))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[1], getDate(Month.SEPTEMBER, 24), week38, 5, familyPass, kata, Room.room2))
        therapyEventRepository.save(TherapyEvent(0, hervaiGyerekek[1], getDate(Month.OCTOBER, 1), week39, 5, familyPass, kata, Room.room2))

        createTimeTable(1, TherapyType.ttBeszed, Room.room2, kata)
        createTimeTable(2, TherapyType.ttBeszed, Room.room2, kata)
        createTimeTable(3, TherapyType.ttBeszed, Room.room2, kata)
        createTimeTable(4, TherapyType.ttBeszed, Room.room2, kata)
        createTimeTable(5, TherapyType.ttBeszed, Room.room2, kata)

        val tt = getTimeTable(1, getTime(9, 10), getTime(9, 55), Room.room2, kata)
        tt.clients = mutableListOf(nemesGyerek)
        timeTableRepository.save(tt)

        val tt2 = getTimeTable(1, getTime(10, 5), getTime(10, 50), Room.room2, kata)
        tt2.clients = mutableListOf(hervaiGyerekek[0])
        timeTableRepository.save(tt2)

        val tt21 = getTimeTable(3, getTime(14, 50), getTime(15, 35), Room.room2, kata)
        tt21.clients = mutableListOf(hervaiGyerekek[0])
        timeTableRepository.save(tt21)

        val tt3 = getTimeTable(2, getTime(13, 0), getTime(13, 45), Room.room2, kata)
        tt3.clients = mutableListOf(hervaiGyerekek[1])
        timeTableRepository.save(tt3)

        val tt4 = getTimeTable(4, getTime(13, 0), getTime(13, 45), Room.room2, kata)
        tt4.clients = mutableListOf(hervaiGyerekek[1], hervaiGyerekek[0])
        timeTableRepository.save(tt4)

        timeTableRepository.save(TimeTable(0, 2, getTime(8, 15), getTime(9, 0), TherapyType.ttMozgas, Room.room3, anita))
        timeTableRepository.save(TimeTable(0, 4, getTime(9, 10), getTime(9, 55), TherapyType.ttMozgas, Room.room3, anita))
    }

    private fun createWeeks() {
        IntArray(52) { it + 1 }
            .forEach { weekRepository.save(Week(0, currentYear, it)) }
    }

    private fun getDate(month: Month, day: Int) =
        OffsetDateTime.of(LocalDateTime.of(currentYear, month, day, 0, 0), offset)

    private fun createTimeTable(dayOfWeek: Int, therapyType: TherapyType, room: Room, therapist: Therapist) {
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(8, 15), getTime(9, 0), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(9, 10), getTime(9, 55), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(10, 5), getTime(10, 50), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(11, 0), getTime(11, 45), therapyType, room, therapist))

        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(13, 0), getTime(13, 45), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(13, 55), getTime(14, 40), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(14, 50), getTime(15, 35), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(15, 45), getTime(16, 30), therapyType, room, therapist))
        timeTableRepository.save(TimeTable(0, dayOfWeek, getTime(16, 40), getTime(17, 25), therapyType, room, therapist))
    }

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
        weekRepository.deleteAll()
        therapyTypeRepository.deleteAll()
        therapyEventDurationRepository.deleteAll()
        roomRepository.deleteAll()
        therapistRepository.deleteAll()
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
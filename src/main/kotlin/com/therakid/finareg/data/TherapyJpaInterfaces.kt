package com.therakid.finareg.data

import com.therakid.finareg.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface WeekRepository : JpaRepository<Week, Long>
interface RoomRepository : JpaRepository<Room, Long>
interface TherapistRepository : JpaRepository<Therapist, Long>
interface TherapyTypeRepository : JpaRepository<TherapyType, Long>
interface TherapyEventDurationRepository : JpaRepository<TherapyEventDuration, Long>

interface TimeTableRepository : JpaRepository<TimeTable, Long>, CustomOneToManyEntityRepository<TimeTable>, CustomTimeTableRepository

interface CustomTimeTableRepository {
    fun getByClient(client: Client): List<TimeTable>
}

@Repository
class TimeTableRepositoryImpl : AbstractOneToManyEntityRepository<TimeTable>(), CustomTimeTableRepository {
    override val entityClass: Class<TimeTable>
        get() = TimeTable::class.java
    override val mappedBy: String
        get() = "therapist"

    override fun getByClient(client: Client): List<TimeTable> {
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(builder.isMember(client, entity.get<List<Client>>("clients")))

        return entityManager.createQuery(query).resultList
    }
}

interface TherapyPassRepository : JpaRepository<TherapyPass, Long>, CustomOneToManyEntityRepository<TherapyPass>

@Repository
class TherapyPassRepositoryImpl : AbstractOneToManyEntityRepository<TherapyPass>() {

    override val entityClass: Class<TherapyPass>
        get() = TherapyPass::class.java
    override val mappedBy: String
        get() = "client"

}

interface TherapyEventRepository : JpaRepository<TherapyEvent, Long>, CustomOneToManyEntityRepository<TherapyEvent>

@Repository
class TherapyEventRepositoryImpl : AbstractOneToManyEntityRepository<TherapyEvent>() {

    override val entityClass: Class<TherapyEvent>
        get() = TherapyEvent::class.java
    override val mappedBy: String
        get() = "therapyPass"

}


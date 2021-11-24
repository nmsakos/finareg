package com.therakid.finareg.data

import com.therakid.finareg.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WeekRepository : JpaRepository<Week, Long> {
    fun findByYearAndNumber(year: Int, number: Int): Week?
}

interface RoomRepository : JpaRepository<Room, Long>
interface TherapistRepository : JpaRepository<Therapist, Long>
interface TherapyTypeRepository : JpaRepository<TherapyType, Long>
interface TherapyEventDurationRepository : JpaRepository<TherapyEventDuration, Long>

interface TimeTableRepository : JpaRepository<TimeTable, Long> {
    fun findByClientsInOrderByDayOfWeekAscFromTimeAsc(clients: List<Client>): List<TimeTable>
    fun findByClientsInAndTherapyTypeOrderByDayOfWeekAscFromTimeAsc(clients: List<Client>, therapyType: TherapyType): List<TimeTable>
}

interface TherapyPassRepository : JpaRepository<TherapyPass, Long> {
    fun findByClient_Id(id: Long): List<TherapyPass>
}

interface TherapyEventStateRepository : JpaRepository<TherapyEventState, Long>
interface TherapyEventRepository : JpaRepository<TherapyEvent, Long> {
    fun findByTherapyPass_IdOrderByDateAsc(id: Long): List<TherapyEvent>
}


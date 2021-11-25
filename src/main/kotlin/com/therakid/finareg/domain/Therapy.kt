package com.therakid.finareg.domain

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.time.OffsetTime
import javax.persistence.*

@Entity
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("year", "number"))))
data class Week(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val year: Int,
    val number: Int,
    val monday: OffsetDateTime
)

@Entity
data class Therapist(
    @Id
    val id: Long,
    val name: String,
    val phone: String,
    val email: String
)

@Entity
data class TherapyEventState(
    @Id
    val id: Long,
    val description: String
) {
    companion object {
        val tesPlanned = TherapyEventState(1, "Tervezett")
        val tesComplete = TherapyEventState(2, "Megtartott")
        val tesCancelled = TherapyEventState(3, "Elmaradt")

        private val entities = listOf(tesPlanned, tesComplete, tesCancelled)
        fun byId(id: Long) =
            entities.find { it.id == id }
    }
}

@Entity
data class TherapyEvent(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val date: OffsetDateTime,
    @ManyToOne
    val week: Week,
    val dayOfWeek: Int
) {
    @ManyToOne
    var client: Client? = null
    @ManyToOne
    var therapyPass: TherapyPass? = null

    @ManyToOne
    var therapist: Therapist? = null

    @ManyToOne
    var room: Room? = null

    @ManyToOne
    var invoice: Invoice? = null

    @ManyToOne
    var state: TherapyEventState = TherapyEventState.tesPlanned

    constructor(
        id: Long, client: Client?, date: OffsetDateTime, week: Week, dayOfWeek: Int,
        therapyPass: TherapyPass, therapist: Therapist?, room: Room?, state: TherapyEventState
    ) : this(
        id,
        date,
        week,
        dayOfWeek
    ) {
        this.client = client
        this.therapyPass = therapyPass
        this.therapist = therapist
        this.room = room
        this.state = state
    }

    constructor(
        id: Long, client: Client?, date: OffsetDateTime, week: Week, dayOfWeek: Int,
        therapyPass: TherapyPass, state: TherapyEventState
    ) : this(
        id,
        date,
        week,
        dayOfWeek
    ) {
        this.client = client
        this.therapyPass = therapyPass
        this.state = state
    }
}

@Entity
data class TherapyPass(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    @ManyToOne
    val therapyType: TherapyType,
    val eventCount: Int,
    @ManyToOne
    val eventDuration: TherapyEventDuration,
) {
    @ManyToOne
    var client: Client? = null

    @ManyToOne
    var family: Family? = null

    @ManyToOne
    var firstEvent: TherapyEvent? = null
    var eventsTaken: Int = 0

    @ManyToOne
    var invoice: Invoice? = null

    constructor(
        id: Long, therapyType: TherapyType, eventCount: Int, eventDuration: TherapyEventDuration,
        client: Client?, family: Family?, eventsTaken: Int
    ) : this(id, therapyType, eventCount, eventDuration) {
        this.client = client
        this.family = family
        this.eventsTaken = eventsTaken
    }
}

@Entity
data class TherapyEventDuration(
    @Id
    val id: Long,
    val minutes: Int,
    val description: String
)

@Entity
data class TherapyType(
    @Id
    val id: Long,
    val description: String
) {
    companion object {
        val ttBeszed = TherapyType(1, "Beszédfejlesztés")
        val ttMozgas = TherapyType(2, "Mozgásfejlesztés")

        private val entities = listOf(ttBeszed, ttMozgas)

        fun byId(id: Long) =
            entities.find { it.id == id }
    }

}

@Entity
data class Invoice(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val number: String,
    @ManyToOne
    val client: Client,
    val amount: BigDecimal,
    val createdDate: OffsetDateTime
) {
    var payData: OffsetDateTime? = null
}

@Entity
data class Room(
    @Id
    val id: Long,
    val description: String
) {
    companion object {
        val room1 = Room(1, "Logo szoba")
        val room2 = Room(2, "Vizsgáló")
        val room3 = Room(3, "Tornaszoba")

        private val entities = listOf(room1, room2, room3)
        fun byId(id: Long) =
            entities.find { it.id == id }
    }
}

@Entity
data class TimeTable(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
) {
    var dayOfWeek: Int = 0
    var fromTime: OffsetTime = OffsetTime.now()
    var toTime: OffsetTime = OffsetTime.now()

    @ManyToOne
    var therapyType: TherapyType? = null

    @ManyToOne
    var room: Room? = null

    @ManyToMany(fetch = FetchType.EAGER)
    var clients: List<Client>? = mutableListOf()

    @ManyToOne
    var therapist: Therapist? = null

    constructor(id: Long, dayOfWeek: Int, fromTime: OffsetTime, toTime: OffsetTime, therapyType: TherapyType, room: Room, therapist: Therapist) : this(
        id
    ) {
        this.therapist = therapist
        this.dayOfWeek = dayOfWeek
        this.fromTime = fromTime
        this.toTime = toTime
        this.therapyType = therapyType
        this.room = room
    }

    constructor(id: Long, dayOfWeek: Int, fromTime: OffsetTime, toTime: OffsetTime, therapyType: TherapyType, room: Room, therapist: Therapist, clients: List<Client>) : this(
        id, dayOfWeek, fromTime, toTime, therapyType, room, therapist
    ) {
        this.clients = clients
    }
}
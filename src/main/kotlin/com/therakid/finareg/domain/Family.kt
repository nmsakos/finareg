package com.therakid.finareg.domain

import org.hibernate.annotations.LazyGroup
import org.hibernate.annotations.LazyToOne
import org.hibernate.annotations.LazyToOneOption
import javax.persistence.*

@Entity
data class Family(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val name: String){

    @OneToMany(mappedBy = "familyId")
    var parents: List<Parent>? = null

    @OneToMany(mappedBy = "familyId")
    var clients: List<Client>? = null

    val clientNames : String
    get() = clients?.joinToString { it.name } ?: ""

    val parentNames : String
    get() = parents?.joinToString { it.name } ?: ""

}

@Entity
data class Client(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String?,
    val familyId: Long)
{
    @OneToMany(mappedBy = "client")
    var passes: List<TherapyPass>? = null
}

@Entity
data class Parent(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long,
    val name: String,
    val phone: String,
    val email: String,
    val familyId: Long)

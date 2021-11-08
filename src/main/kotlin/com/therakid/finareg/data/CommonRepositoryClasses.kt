package com.therakid.finareg.data

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

interface CustomOneToManyEntityRepository<T> {

    fun getOneToManyEntities(id: Long): List<T>
    fun getOneToManyEntities(id: List<Long>, mappedBy: List<String>): List<T>

}

abstract class AbstractOneToManyEntityRepository<T> : CustomOneToManyEntityRepository<T> {

    @PersistenceContext
    lateinit var entityManager: EntityManager

    abstract val entityClass: Class<T>
    abstract val mappedBy: String

    override fun getOneToManyEntities(id: Long) =
        getOneToManyEntities(listOf(id), listOf(mappedBy))

    override fun getOneToManyEntities(id: List<Long>, mappedBy: List<String>): List<T> {
        val builder = entityManager.criteriaBuilder
        val query = builder.createQuery(entityClass)
        val entity = query.from(entityClass)
        query.where(builder.and(*ands(builder, entity, id, mappedBy)))

        return entityManager.createQuery(query).resultList
    }

    private fun ands(builder: CriteriaBuilder, entity: Root<T>, ids: List<Long>, mappedBys: List<String>): Array<Predicate> {
        val result = ArrayList<Predicate>()
        for (i in ids.indices) {
            result.add(builder.equal(entity.get<Long>(mappedBys[i]), ids[i]))
        }
        return result.toTypedArray()
    }

}
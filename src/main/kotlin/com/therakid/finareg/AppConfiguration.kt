package com.therakid.finareg

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfiguration {

    @Bean
    fun dateTimeType(): GraphQLScalarType =
        ExtendedScalars.DateTime

    @Bean
    fun TimeType(): GraphQLScalarType =
        ExtendedScalars.Time

}
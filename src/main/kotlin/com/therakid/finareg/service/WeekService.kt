package com.therakid.finareg.service

import com.therakid.finareg.data.WeekRepository
import com.therakid.finareg.domain.Week
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.temporal.TemporalField
import java.time.temporal.WeekFields
import java.util.*

@Service
class WeekService(
    private val weekRepository: WeekRepository
) {
    fun getMondayOfWeek(year: Int, number: Int): OffsetDateTime {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.WEEK_OF_YEAR, number)
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)

        cal.set(Calendar.HOUR, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.toInstant().atOffset(OffsetTime.now().offset)
    }

    fun getByYearAndNumber(year: Int, number: Int) =
        weekRepository.findByYearAndNumber(year, number) ?: weekRepository.save(Week(0, year, number, getMondayOfWeek(year, number)))

    fun getByDate(date: OffsetDateTime) =
        getByYearAndNumber(date.year, date.get(woyField))

    fun deleteAll() =
        weekRepository.deleteAll()

    fun getById(id: Long) =
        weekRepository.getById(id)

    companion object {
        val woyField: TemporalField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()
        val dowField: TemporalField = WeekFields.of(Locale.getDefault()).dayOfWeek()
    }
}
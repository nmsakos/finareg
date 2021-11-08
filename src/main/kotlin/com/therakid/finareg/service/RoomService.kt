package com.therakid.finareg.service

import com.therakid.finareg.data.RoomRepository
import com.therakid.finareg.domain.Room
import org.springframework.stereotype.Service

@Service
class RoomService (
    val roomRepository: RoomRepository)
{
    fun getAll(): List<Room> =
        roomRepository.findAll()

    fun getById(roomId: Long) =
        roomRepository.getById(roomId)
}
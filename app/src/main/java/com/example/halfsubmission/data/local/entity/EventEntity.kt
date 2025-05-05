package com.example.halfsubmission.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DatabaseEventsLocal")
data class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "summary")
    val summary: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String? = null,

    @ColumnInfo(name = "mediaCover")
    val mediaCover: String? = null,

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "ownerName")
    val ownerName: String? = null,

    @ColumnInfo(name = "cityName")
    val cityName: String? = null,

    @ColumnInfo(name = "quota")
    val quota: Int = 0,

    @ColumnInfo(name = "registrants")
    val registrants: Int = 0,

    @ColumnInfo(name = "beginTime")
    val beginTime: String? = null,

    @ColumnInfo(name = "endTime")
    val endTime: String? = null,

    @ColumnInfo(name = "link")
    val link: String? = null,

    @ColumnInfo(name = "isBookmarked")
    var isBookmarked: Boolean? = false,

    @ColumnInfo(name = "isActive")
    val isUpcomming: Boolean
)
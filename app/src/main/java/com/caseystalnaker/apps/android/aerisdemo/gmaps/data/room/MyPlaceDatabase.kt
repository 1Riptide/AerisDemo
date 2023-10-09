package com.caseystalnaker.apps.android.aerisdemo.gmaps.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MyPlace::class], version = 1, exportSchema = false)
abstract class MyPlaceDatabase : RoomDatabase() {
    abstract fun myPlaceDao(): MyPlaceDao
}
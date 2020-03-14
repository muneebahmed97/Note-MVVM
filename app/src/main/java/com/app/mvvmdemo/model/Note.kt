package com.app.mvvmdemo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
open class Note(
    var title: String,
    var description: String,
    var priority: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
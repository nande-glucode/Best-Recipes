package com.example.bestrecipes.Data

import androidx.room.TypeConverter
import com.example.bestrecipes.Data.Responses.Instructions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromInstructionsList(value: List<Instructions>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Instructions>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toInstructionsList(value: String?): List<Instructions>? {
        val gson = Gson()
        val type = object : TypeToken<List<Instructions>>() {}.type
        return gson.fromJson(value, type)
    }
}
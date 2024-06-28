package com.example.bestrecipes.data

import androidx.room.TypeConverter
import com.example.bestrecipes.data.responses.Instructions
import com.example.bestrecipes.data.responses.Step
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromInstructionsList(value: List<Instructions>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Instructions>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    @JvmStatic
    fun toInstructionsList(value: String): List<Instructions> {
        val listType = object : TypeToken<List<Instructions>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromStepList(steps: List<Step>): String {
        return Gson().toJson(steps)
    }

    @TypeConverter
    @JvmStatic
    fun toStepList(json: String): List<Step> {
        val type = object : TypeToken<List<Step>>() {}.type
        return Gson().fromJson(json, type)
    }
}
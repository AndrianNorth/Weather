package ru.geekbrains.weather.repository

import ru.geekbrains.weather.model.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}
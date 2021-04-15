package ru.geekbrains.weather.utils

import ru.geekbrains.weather.model.FactDTO
import ru.geekbrains.weather.model.Weather
import ru.geekbrains.weather.model.WeatherDTO
import ru.geekbrains.weather.model.getDefaultCity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact!!
    return listOf(Weather(getDefaultCity(), fact.temp!!, fact.feels_like!!, fact.condition!!))
}
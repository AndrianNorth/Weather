package ru.geekbrains.weather.repository

import ru.geekbrains.weather.model.Weather
import ru.geekbrains.weather.model.getRussianCities
import ru.geekbrains.weather.model.getWorldCities

class MainRepositoryImpl : MainRepository {

    override fun getWeatherFromServer() = Weather()

    override fun getWeatherFromLocalStorageRus() = getRussianCities()

    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}
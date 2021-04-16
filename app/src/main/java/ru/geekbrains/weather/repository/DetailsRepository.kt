package ru.geekbrains.weather.repository

import ru.geekbrains.weather.model.WeatherDTO

interface DetailsRepository {

    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>)
}
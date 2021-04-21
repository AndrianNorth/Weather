package ru.geekbrains.weather.repository

import ru.geekbrains.weather.model.Weather
import ru.geekbrains.weather.room.HistoryDao
import ru.geekbrains.weather.utils.convertHistoryEntityToWeather
import ru.geekbrains.weather.utils.convertWeatherToEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDao): LocalRepository {

    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}
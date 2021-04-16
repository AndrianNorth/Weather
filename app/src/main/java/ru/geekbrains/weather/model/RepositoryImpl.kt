package ru.geekbrains.weather.model

class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()


    override fun getWeatherFromLocalStorageRus() = getRussianCities()


    override fun getWeatherFromLocalStorageWorld() = getWorldCities()



}
package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.repository.MainRepository
import ru.geekbrains.weather.repository.MainRepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val mainRepositoryImpl: MainRepository = MainRepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)

    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(
                AppState.Success(
                    if (isRussian)
                        mainRepositoryImpl.getWeatherFromLocalStorageRus()
                    else
                        mainRepositoryImpl.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}
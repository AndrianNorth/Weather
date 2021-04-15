package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.geekbrains.weather.model.WeatherDTO
import ru.geekbrains.weather.repository.DetailsRepositoryImpl
import ru.geekbrains.weather.repository.RemoteDataSource
import ru.geekbrains.weather.utils.convertDtoToModel
import java.io.IOException


private const val SERVER_ERROR = "ОШИБКА СЕРВЕРА"
private const val REQUEST_ERROR = "ОШИБКА ЗАПРОСА НА СЕРВЕР"
private const val CORRUPTED_DATA = "НЕПОЛНЫЕ ДАННЫЕ"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepositoryImpl =
        DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callback)
    }

    private val callback = object : Callback {

        @Throws(IOException::class)
        override fun onResponse(call: Call?, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call, e: IOException) {
            detailsLiveData.postValue(AppState.Error(Throwable(e?.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: String): AppState {
            val WeatherDTO: WeatherDTO =
                Gson().fromJson(serverResponse, WeatherDTO::class.java)
            val fact = WeatherDTO.fact
            return if (fact == null || fact.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(
                    convertDtoToModel(
                        WeatherDTO
                    )
                )
            }
        }
    }
}


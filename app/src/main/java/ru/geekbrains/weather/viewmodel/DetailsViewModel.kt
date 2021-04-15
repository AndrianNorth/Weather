package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.weather.model.WeatherDTO
import ru.geekbrains.weather.repository.DetailsRepository
import ru.geekbrains.weather.repository.DetailsRepositoryImpl
import ru.geekbrains.weather.repository.RemoteDataSource
import ru.geekbrains.weather.utils.convertDtoToModel
import java.io.IOException

private const val SERVER_ERROR = "ОШИБКА СЕРВЕРА"
private const val REQUEST_ERROR = "ОШИБКА ЗАПРОСА НА СЕРВЕР"
private const val CORRUPTED_DATA = "НЕПОЛНЫЕ ДАННЫЕ"

class DetailsViewModel(
    val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    private val callBack = object : Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(serverResponse: WeatherDTO): AppState {
            val fact = serverResponse.fact
            return if (fact?.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.Success(convertDtoToModel(serverResponse))
            }
        }
    }
}


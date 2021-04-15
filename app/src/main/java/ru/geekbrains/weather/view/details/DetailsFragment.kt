package ru.geekbrains.weather.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.viewmodel.DetailsViewModel
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.model.Weather
import ru.geekbrains.weather.utils.showSnackBar
import ru.geekbrains.weather.viewmodel.AppState

private const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA) ?: Weather()
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        viewModel.getWeatherFromRemoteSource(
            MAIN_LINK +
                    "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}"
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(appState.weatherData[0])
            }
            is AppState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            MAIN_LINK +
                                    "lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}"
                        )
                    }
                )
            }
        }
    }

    private fun setWeather(Weather: Weather) {
        val city = weatherBundle.city
        binding.cityName.text = city.city
        binding.cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            city.lon.toString(),
            city.lat.toString()
        )
        binding.temperatureValue.text = Weather.temperature.toString()
        binding.feelsLikeValue.text = Weather.feelsLike.toString()
        binding.weatherCondition.text = Weather.condition
    }
}


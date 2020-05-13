package zfani.assaf.world_weather.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import zfani.assaf.world_weather.R
import zfani.assaf.world_weather.model.ApiClient
import zfani.assaf.world_weather.model.Weather


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var weatherList: MutableLiveData<List<Weather>>? = null
    val app = application

    init {
        weatherList = MutableLiveData()
    }

    fun getWeatherList(isCelsius: Boolean): MutableLiveData<List<Weather>>? {
        var cityCodes = ""
        app.resources.getStringArray(R.array.cities_codes).forEach {
            cityCodes += "$it,"
        }
        val call: Call<JsonObject> = ApiClient.getClient.getAllCityWeathers(
            apiKey = app.getString(R.string.api_key),
            groupIds = cityCodes,
            unit = if (isCelsius) "metric" else "imperial"
        )
        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>?, response: Response<JsonObject>?) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
            }
        })
        return weatherList
    }

    private fun handleResponse(response: Response<JsonObject>?) {
        val jsonObject = response!!.body()!!
        val weathers = ArrayList<Weather>()
        if (jsonObject.has("list")) {
            jsonObject.getAsJsonArray("list").forEach { it ->
                val weather = Weather()
                val item = it.asJsonObject
                weather.cityId = item.get("id").asString
                weather.cityName = item.get("name").asString
                item.getAsJsonArray("weather").forEach {
                    val weatherObj = it.asJsonObject
                    weather.icon = weatherObj.get("icon").asString
                    weather.weatherDesc = weatherObj.get("description").asString
                }
                val main = item.getAsJsonObject("main")
                weather.minTemp = main.get("temp_min").asString
                weather.maxTemp = main.get("temp_max").asString
                weathers.add(weather)
            }
        }
        weatherList!!.value = weathers
    }
}

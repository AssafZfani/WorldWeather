package zfani.assaf.world_weather.ui.forecast

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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ForecastViewModel(application: Application) : AndroidViewModel(application) {

    val app = application
    private var weatherList: MutableLiveData<List<Weather>>? = null

    init {
        weatherList = MutableLiveData()
    }

    fun getWeatherList(cityId: String, isCelsius: Boolean): MutableLiveData<List<Weather>>? {
        val call: Call<JsonObject> = ApiClient.getClient.getCityForecast(
            apiKey = app.getString(R.string.api_key),
            cityId = cityId,
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
                weather.day = item.get("dt").asLong * 1000
                weather.dayText = SimpleDateFormat("EEEE", Locale.ENGLISH).format(Date(weather.day))
                item.getAsJsonArray("weather").forEach {
                    val weatherObj = it.asJsonObject
                    weather.icon = weatherObj.get("icon").asString
                    weather.weatherDesc = weatherObj.get("description").asString
                }
                val temp = item.getAsJsonObject("temp")
                weather.minTemp = temp.get("min").asString
                weather.maxTemp = temp.get("max").asString
                weathers.add(weather)
            }
        }
        weathers.sort()
        weatherList!!.value = weathers
    }
}
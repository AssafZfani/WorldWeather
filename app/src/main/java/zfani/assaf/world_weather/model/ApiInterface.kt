package zfani.assaf.world_weather.model

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("group")
    fun getAllCityWeathers(
        @Query("id") groupIds: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ): Call<JsonObject>

    @GET("forecast/daily")
    fun getCityForecast(
        @Query("id") cityId: String,
        @Query("appid") apiKey: String,
        @Query("units") unit: String
    ): Call<JsonObject>
}
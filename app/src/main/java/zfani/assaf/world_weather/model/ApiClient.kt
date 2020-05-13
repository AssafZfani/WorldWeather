package zfani.assaf.world_weather.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private var BASE_URL: String = "https://api.openweathermap.org/data/2.5/"
    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

            return retrofit.create(ApiInterface::class.java)
        }
}
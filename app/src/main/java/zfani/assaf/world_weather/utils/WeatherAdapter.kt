package zfani.assaf.world_weather.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_item_city_weather.view.*
import zfani.assaf.world_weather.App
import zfani.assaf.world_weather.R
import zfani.assaf.world_weather.model.Weather
import zfani.assaf.world_weather.ui.forecast.ForecastFragment
import zfani.assaf.world_weather.ui.main.MainFragment
import java.util.*

class WeatherAdapter(private var isForecast: Boolean) :

    ListAdapter<Weather, WeatherAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<Weather>() {
            override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean {
                return oldItem.cityId == newItem.cityId && oldItem.day == newItem.day
            }

            override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean {
                return oldItem.cityName == newItem.cityName && oldItem.maxTemp == newItem.maxTemp && oldItem.minTemp == newItem.minTemp
            }
        }) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(weather: Weather, isForecast: Boolean) {
            Glide.with(itemView.context)
                .load("https://openweathermap.org/img/wn/" + weather.icon + "@2x.png")
                .into(itemView.ivIcon)
            itemView.tvDayOrCityName.text =
                if (isForecast) weather.dayText.toUpperCase(Locale.ENGLISH) else weather.cityName.toUpperCase(
                    Locale.ENGLISH
                )
            itemView.tvWeatherDescription.text = weather.weatherDesc
            itemView.tvMaxMinTemperature.text =
                StringBuilder(weather.minTemp + " °" + (if (App.isCelsius.value!!) "C" else "F") + " - " + weather.maxTemp + " °" + (if (App.isCelsius.value!!) "C" else "F"))
            if (!isForecast) {
                itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString(ForecastFragment.KEY_CITY_ID, weather.cityId)
                    val forecastFragment = ForecastFragment.newInstance()
                    forecastFragment.arguments = bundle
                    (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                        .replace(R.id.container, forecastFragment)
                        .addToBackStack(MainFragment.TAG).commit()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_city_weather, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(getItem(position), isForecast)
    }
}
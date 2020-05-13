package zfani.assaf.world_weather.ui.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.forest_fragment.*
import zfani.assaf.world_weather.App
import zfani.assaf.world_weather.R
import zfani.assaf.world_weather.utils.WeatherAdapter
import java.util.*

class ForecastFragment : Fragment() {

    companion object {
        const val KEY_CITY_ID: String = ""
        fun newInstance() = ForecastFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forest_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)
        val weatherAdapter = WeatherAdapter(true)
        rvForecastList.adapter = weatherAdapter
        rvForecastList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        App.isCelsius.observe(viewLifecycleOwner, Observer {
            viewModel.getWeatherList(arguments!!.getString(KEY_CITY_ID, ""), it)
                ?.observe(viewLifecycleOwner, Observer { list ->
                    App.text.observe(viewLifecycleOwner, Observer { text ->
                        if (text.isEmpty()) {
                            weatherAdapter.submitList(list)
                        } else {
                            weatherAdapter.submitList(list.filter { weather ->
                                weather.dayText.toLowerCase(Locale.getDefault())
                                    .contains(text) || weather.weatherDesc.toLowerCase(
                                    Locale.getDefault()
                                ).contains(text)
                            })
                        }
                    })
                })
        })
    }
}
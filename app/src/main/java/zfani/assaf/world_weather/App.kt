package zfani.assaf.world_weather

import android.app.Application
import androidx.lifecycle.MutableLiveData

class App : Application() {

    companion object {
        lateinit var isCelsius: MutableLiveData<Boolean>
        lateinit var text: MutableLiveData<String>
    }

    override fun onCreate() {
        super.onCreate()
        isCelsius = MutableLiveData(true)
        text = MutableLiveData("")
    }
}
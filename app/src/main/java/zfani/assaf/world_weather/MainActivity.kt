package zfani.assaf.world_weather

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.main_activity.*
import zfani.assaf.world_weather.ui.main.MainFragment
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        designStatusBarColor()
        btnChangeTemp.setOnClickListener {
            App.isCelsius.value = App.isCelsius.value!!.not()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                App.text.value = newText?.toLowerCase(Locale.getDefault()) ?: ""
                return true
            }
        })
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
                .commit()
        }
    }

    private fun designStatusBarColor() {
        var statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val hsv = FloatArray(3)
        Color.colorToHSV(statusBarColor, hsv)
        hsv[2] *= 0.8f
        statusBarColor = Color.HSVToColor(hsv)
        window.statusBarColor = statusBarColor
    }
}

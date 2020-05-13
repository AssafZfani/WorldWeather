package zfani.assaf.world_weather.model

data class Weather(
    var cityId: String = "",
    var icon: String = "",
    var cityName: String = "",
    var weatherDesc: String = "",
    var minTemp: String = "",
    var maxTemp: String = "",
    var dayText: String = "",
    var day: Long = 0
) : Comparable<Weather> {
    override fun compareTo(other: Weather): Int {
        return (this.day - other.day).toInt()
    }
}

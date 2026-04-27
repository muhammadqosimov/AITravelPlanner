package com.example.aitravelplanner.data.model

data class Flight(
    val id: String,
    val airline: String,
    val flightNumber: String,
    val from: String,
    val to: String,
    val date: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: String,
    val priceUsd: Int,
    val stops: Int = 0,
    val logoEmoji: String = "✈️"
)

object MockFlightData {

    private val airlines = listOf(
        Triple("Uzbekistan Airways", "HY", "🇺🇿"),
        Triple("Turkish Airlines", "TK", "🇹🇷"),
        Triple("Emirates", "EK", "🇦🇪"),
        Triple("Air Astana", "KC", "🇰🇿"),
        Triple("FlyDubai", "FZ", "🇦🇪"),
        Triple("Aeroflot", "SU", "🇷🇺"),
        Triple("Lufthansa", "LH", "🇩🇪"),
        Triple("British Airways", "BA", "🇬🇧"),
        Triple("Qatar Airways", "QR", "🇶🇦"),
        Triple("Air France", "AF", "🇫🇷")
    )

    private val departureTimes = listOf(
        "06:30", "08:15", "10:00", "11:45", "13:20",
        "15:00", "16:30", "18:15", "20:00", "22:30"
    )

    private val durations = listOf(
        "2h 30m", "3h 15m", "4h 00m", "5h 45m", "6h 20m",
        "7h 10m", "8h 00m", "9h 30m", "11h 45m", "13h 20m"
    )

    fun generateFlights(from: String, to: String, date: String): List<Flight> {
        val shuffledAirlines = airlines.shuffled().take(6)
        return shuffledAirlines.mapIndexed { index, (airlineName, code, emoji) ->
            val departureTime = departureTimes[index % departureTimes.size]
            val duration = durations[index % durations.size]
            val stops = if (index < 2) 0 else if (index < 5) 1 else 2
            val basePrice = when (stops) {
                0 -> (300..1200).random()
                1 -> (180..700).random()
                else -> (100..400).random()
            }
            Flight(
                id = "FL${code}${index + 1}${System.currentTimeMillis() % 10000}",
                airline = airlineName,
                flightNumber = "$code${(100..999).random()}",
                from = from.trim().ifEmpty { "TAS" },
                to = to.trim().ifEmpty { "IST" },
                date = date,
                departureTime = departureTime,
                arrivalTime = calculateArrival(departureTime, duration),
                duration = duration,
                priceUsd = basePrice,
                stops = stops,
                logoEmoji = emoji
            )
        }.sortedBy { it.priceUsd }
    }

    private fun calculateArrival(departure: String, duration: String): String {
        return try {
            val parts = departure.split(":")
            var hours = parts[0].toInt()
            var minutes = parts[1].toInt()
            val dParts = duration.replace("h", "").replace("m", "").trim().split(" ")
            hours += dParts[0].trim().toInt()
            minutes += dParts[1].trim().toInt()
            hours += minutes / 60
            minutes %= 60
            hours %= 24
            "%02d:%02d".format(hours, minutes)
        } catch (e: Exception) {
            "N/A"
        }
    }
}

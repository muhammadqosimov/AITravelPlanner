package com.example.aitravelplanner.data.remote

import com.example.aitravelplanner.BuildConfig
import com.example.aitravelplanner.data.model.DestinationRequest
import com.example.aitravelplanner.data.model.TravelPlanRequest
import com.example.aitravelplanner.util.Constants
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor() {

    private val model: GenerativeModel by lazy {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "your_gemini_api_key_here") {
            throw IllegalStateException(
                "GEMINI_API_KEY is not set. Please add it to local.properties."
            )
        }
        GenerativeModel(
            modelName = Constants.GEMINI_MODEL,
            apiKey = apiKey,
            generationConfig = generationConfig {
                temperature = 0.8f
                maxOutputTokens = 8192
            }
        )
    }

    suspend fun generateTravelItinerary(request: TravelPlanRequest): String {
        val prompt = buildTravelPlanPrompt(request)
        val response = model.generateContent(prompt)
        return response.text ?: "Could not generate itinerary. Please try again."
    }

    suspend fun findDestinations(request: DestinationRequest): String {
        val prompt = buildDestinationFinderPrompt(request)
        val response = model.generateContent(prompt)
        return response.text ?: "Could not find destinations. Please try again."
    }

    private fun buildTravelPlanPrompt(request: TravelPlanRequest): String = """
        You are an expert travel planner. Create a detailed and exciting travel itinerary for the following trip:

        - Departure City: ${request.departureCity}
        - Destination: ${request.destination}
        - Total Budget: $${request.budgetUsd} USD
        - Trip Duration: ${request.days} days
        - Travel Style: ${request.travelStyle}

        Please provide a comprehensive plan with these sections:

        ## 🌍 Destination Overview
        Brief introduction to the destination, culture, and what makes it special.

        ## 📅 Day-by-Day Itinerary
        For each day, include:
        - Morning activities with times
        - Afternoon activities with times
        - Evening activities with times
        - Recommended restaurants for each meal

        ## 🏛️ Top Attractions
        List the must-see attractions with brief descriptions.

        ## 🍽️ Food & Dining Recommendations
        Local dishes to try and recommended restaurants or food markets.

        ## 🎒 Packing Tips
        Essential items to pack for this specific trip and destination.

        ## 💡 Travel Tips & Advice
        Local customs, transportation tips, safety advice, best times to visit attractions, and money-saving tips.

        ## 💰 Budget Breakdown
        Rough estimate of costs per category (accommodation, food, activities, transport).

        Make the response friendly, detailed, and tailored to the ${request.travelStyle} travel style.
    """.trimIndent()

    private fun buildDestinationFinderPrompt(request: DestinationRequest): String {
        val surpriseText = if (request.surpriseMode) "Pick completely random and surprising destinations!" else ""
        val weatherText = if (request.preferredWeather.isNotBlank()) "- Preferred Weather/Climate: ${request.preferredWeather}" else ""
        val durationText = if (request.durationDays > 0) "- Trip Duration: ${request.durationDays} days" else ""
        val styleText = if (request.travelStyle.isNotBlank()) "- Travel Style: ${request.travelStyle}" else ""

        return """
            You are an expert travel advisor. Suggest 4 travel destinations for someone with these preferences:

            - Total Budget: $${request.budgetUsd} USD
            $weatherText
            $durationText
            $styleText
            $surpriseText

            For EACH destination, provide:

            ### 🌏 [Destination Name], [Country]

            **Estimated Cost:** ${'$'}X - ${'$'}Y USD (for the full trip)

            **Why It Fits Your Budget:** Explain how this destination is achievable within the budget.

            **Top 3 Attractions:**
            1. Attraction name - brief description
            2. Attraction name - brief description
            3. Attraction name - brief description

            **Best Time to Visit:** Month(s)

            **Why You'll Love It:** 2-3 sentences about why this destination is special.

            ---

            Make the suggestions diverse, exciting, and genuinely achievable within the $${request.budgetUsd} budget.
            Include a mix of popular and hidden gem destinations.
        """.trimIndent()
    }
}

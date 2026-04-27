package com.example.aitravelplanner.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aitravelplanner.R
import com.example.aitravelplanner.data.local.entity.TripEntity
import com.example.aitravelplanner.ui.components.FeatureCard
import com.example.aitravelplanner.ui.components.SectionHeader
import com.example.aitravelplanner.ui.navigation.Screen
import com.example.aitravelplanner.ui.theme.Blue50
import com.example.aitravelplanner.ui.theme.Blue900
import com.example.aitravelplanner.ui.theme.Green50
import com.example.aitravelplanner.ui.theme.Green600
import com.example.aitravelplanner.ui.theme.Orange50
import com.example.aitravelplanner.ui.theme.Orange500
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateTo: (Screen) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recentTrips by viewModel.recentTrips.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigateTo(Screen.Settings) }) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.home_settings))
                    }
                    IconButton(onClick = { onNavigateTo(Screen.About) }) {
                        Icon(Icons.Default.Info, contentDescription = stringResource(R.string.home_about))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item { HeroBanner() }

            item {
                SectionHeader(
                    title = stringResource(R.string.home_quick_actions),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                FeatureCard(
                    title = stringResource(R.string.home_plan_trip),
                    description = stringResource(R.string.home_plan_trip_desc),
                    icon = Icons.Default.Map,
                    containerColor = Blue50,
                    contentColor = Blue900,
                    onClick = { onNavigateTo(Screen.TravelPlanner) }
                )
            }
            item {
                FeatureCard(
                    title = stringResource(R.string.home_find_destination),
                    description = stringResource(R.string.home_find_destination_desc),
                    icon = Icons.Default.Place,
                    containerColor = Orange50,
                    contentColor = Orange500,
                    onClick = { onNavigateTo(Screen.DestinationFinder) }
                )
            }
            item {
                FeatureCard(
                    title = stringResource(R.string.home_book_flight),
                    description = stringResource(R.string.home_book_flight_desc),
                    icon = Icons.Default.Flight,
                    containerColor = Green50,
                    contentColor = Green600,
                    onClick = { onNavigateTo(Screen.FlightBooking) }
                )
            }
            item {
                FeatureCard(
                    title = stringResource(R.string.home_saved_trips),
                    description = "View your saved itineraries and destinations",
                    icon = Icons.Default.Bookmark,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = { onNavigateTo(Screen.SavedTrips) }
                )
            }

            if (recentTrips.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Recent Trips",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recentTrips.take(5)) { trip ->
                            RecentTripCard(trip = trip)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "✈️ 🌍 🗺️", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.home_greeting),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun RecentTripCard(trip: TripEntity) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = trip.destination,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                maxLines = 1
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${trip.days} days • ${trip.travelStyle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
            Text(
                text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(Date(trip.savedAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
            )
        }
    }
}

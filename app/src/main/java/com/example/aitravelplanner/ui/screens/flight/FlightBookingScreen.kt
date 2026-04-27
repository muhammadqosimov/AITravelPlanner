package com.example.aitravelplanner.ui.screens.flight

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aitravelplanner.R
import com.example.aitravelplanner.data.model.Flight
import com.example.aitravelplanner.ui.components.ErrorCard
import com.example.aitravelplanner.ui.components.LoadingCard
import com.example.aitravelplanner.ui.components.TravelTopBar
import com.example.aitravelplanner.util.UiState

@Composable
fun FlightBookingScreen(
    onNavigateBack: () -> Unit,
    viewModel: FlightBookingViewModel = hiltViewModel()
) {
    val flightsState by viewModel.flightsState.collectAsState()
    val bookedFlight by viewModel.bookedFlight.collectAsState()

    var from by rememberSaveable { mutableStateOf("") }
    var to by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }

    // Booking confirmation dialog
    if (bookedFlight != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissBookingConfirmation() },
            icon = { Text("🎉", style = MaterialTheme.typography.displaySmall) },
            title = {
                Text(
                    text = stringResource(R.string.flight_booked_title),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(stringResource(R.string.flight_booked_message))
                    Spacer(modifier = Modifier.height(8.dp))
                    bookedFlight?.let { flight ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "${flight.logoEmoji} ${flight.airline}",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    "${flight.from} → ${flight.to}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    "${flight.departureTime} → ${flight.arrivalTime}  •  \$${flight.priceUsd}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.dismissBookingConfirmation() }) {
                    Text(stringResource(R.string.flight_booked_ok))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TravelTopBar(
                title = stringResource(R.string.flight_title),
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.flight_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = from,
                        onValueChange = { from = it },
                        label = { Text(stringResource(R.string.flight_from)) },
                        leadingIcon = { Icon(Icons.Default.FlightTakeoff, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("e.g. Tashkent") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = to,
                        onValueChange = { to = it },
                        label = { Text(stringResource(R.string.flight_to)) },
                        leadingIcon = { Icon(Icons.Default.FlightLand, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("e.g. Istanbul") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text(stringResource(R.string.flight_date)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        placeholder = { Text("e.g. 2025-12-25") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.searchFlights(
                                from = from.trim().ifEmpty { "TAS" },
                                to = to.trim().ifEmpty { "IST" },
                                date = date.trim().ifEmpty { "2025-12-25" }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Search, null)
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            stringResource(R.string.flight_search),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            when (val state = flightsState) {
                is UiState.Loading -> item {
                    LoadingCard(message = stringResource(R.string.flight_searching))
                }
                is UiState.Error -> item { ErrorCard(message = state.message) }
                is UiState.Success -> {
                    item {
                        Text(
                            text = stringResource(R.string.flight_results_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    items(state.data) { flight ->
                        FlightCard(
                            flight = flight,
                            onBook = { viewModel.bookFlight(flight) }
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun FlightCard(flight: Flight, onBook: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = flight.logoEmoji, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = flight.airline,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = flight.flightNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Badge(
                    containerColor = if (flight.stops == 0)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = when (flight.stops) {
                            0 -> "Direct"
                            1 -> "1 Stop"
                            else -> "${flight.stops} Stops"
                        },
                        modifier = Modifier.padding(horizontal = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = flight.departureTime,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = flight.from,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Flight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = flight.duration,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = flight.arrivalTime,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = flight.to,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "\$${flight.priceUsd}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "per person",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = onBook,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        stringResource(R.string.flight_book),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

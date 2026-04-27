package com.example.aitravelplanner.ui.screens.planner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aitravelplanner.R
import com.example.aitravelplanner.data.model.TravelPlanRequest
import com.example.aitravelplanner.ui.components.ErrorCard
import com.example.aitravelplanner.ui.components.LoadingCard
import com.example.aitravelplanner.ui.components.TravelTopBar
import com.example.aitravelplanner.util.Constants
import com.example.aitravelplanner.util.MarkdownText
import com.example.aitravelplanner.util.UiState

@Composable
fun TravelPlannerScreen(
    onNavigateBack: () -> Unit,
    viewModel: TravelPlannerViewModel = hiltViewModel()
) {
    val itineraryState by viewModel.itineraryState.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var departure by rememberSaveable { mutableStateOf("") }
    var destination by rememberSaveable { mutableStateOf("") }
    var budget by rememberSaveable { mutableStateOf("") }
    var days by rememberSaveable { mutableStateOf("") }
    var selectedStyle by rememberSaveable { mutableStateOf(Constants.TRAVEL_STYLES[0]) }
    var showErrors by rememberSaveable { mutableStateOf(false) }

    val savedMessage = stringResource(R.string.planner_saved_success)

    LaunchedEffect(saveState) {
        if (saveState) {
            snackbarHostState.showSnackbar(savedMessage)
            viewModel.resetSaveState()
        }
    }

    Scaffold(
        topBar = {
            TravelTopBar(
                title = stringResource(R.string.planner_title),
                onNavigateBack = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { InputSection(
                departure = departure,
                destination = destination,
                budget = budget,
                days = days,
                selectedStyle = selectedStyle,
                showErrors = showErrors,
                onDepartureChange = { departure = it },
                onDestinationChange = { destination = it },
                onBudgetChange = { budget = it },
                onDaysChange = { days = it },
                onStyleChange = { selectedStyle = it }
            )}

            item {
                Button(
                    onClick = {
                        showErrors = true
                        if (departure.isNotBlank() && destination.isNotBlank() &&
                            budget.isNotBlank() && days.isNotBlank()) {
                            viewModel.generateItinerary(
                                TravelPlanRequest(
                                    departureCity = departure.trim(),
                                    destination = destination.trim(),
                                    budgetUsd = budget.toIntOrNull() ?: 1000,
                                    days = days.toIntOrNull() ?: 7,
                                    travelStyle = selectedStyle
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.TravelExplore, contentDescription = null)
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        stringResource(R.string.planner_generate),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                when (val state = itineraryState) {
                    is UiState.Loading -> LoadingCard(
                        message = stringResource(R.string.planner_generating)
                    )
                    is UiState.Error -> ErrorCard(message = state.message)
                    is UiState.Success -> ItineraryResultCard(
                        itinerary = state.data,
                        onSave = {
                            viewModel.saveTrip(
                                TravelPlanRequest(
                                    departureCity = departure,
                                    destination = destination,
                                    budgetUsd = budget.toIntOrNull() ?: 0,
                                    days = days.toIntOrNull() ?: 0,
                                    travelStyle = selectedStyle
                                ),
                                state.data
                            )
                        }
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun InputSection(
    departure: String,
    destination: String,
    budget: String,
    days: String,
    selectedStyle: String,
    showErrors: Boolean,
    onDepartureChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onBudgetChange: (String) -> Unit,
    onDaysChange: (String) -> Unit,
    onStyleChange: (String) -> Unit
) {
    val required = stringResource(R.string.field_required)

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.planner_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = departure,
            onValueChange = onDepartureChange,
            label = { Text(stringResource(R.string.planner_departure)) },
            leadingIcon = { Icon(Icons.Default.FlightTakeoff, null) },
            isError = showErrors && departure.isBlank(),
            supportingText = { if (showErrors && departure.isBlank()) Text(required) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = onDestinationChange,
            label = { Text(stringResource(R.string.planner_destination)) },
            leadingIcon = { Icon(Icons.Default.FlightLand, null) },
            isError = showErrors && destination.isBlank(),
            supportingText = { if (showErrors && destination.isBlank()) Text(required) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = budget,
                onValueChange = onBudgetChange,
                label = { Text(stringResource(R.string.planner_budget)) },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                isError = showErrors && budget.isBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
            OutlinedTextField(
                value = days,
                onValueChange = onDaysChange,
                label = { Text(stringResource(R.string.planner_days)) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                isError = showErrors && days.isBlank(),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.planner_travel_style),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Constants.TRAVEL_STYLES.forEach { style ->
                FilterChip(
                    selected = selectedStyle == style,
                    onClick = { onStyleChange(style) },
                    label = { Text(style, style = MaterialTheme.typography.labelMedium) }
                )
            }
        }
    }
}

@Composable
private fun ItineraryResultCard(itinerary: String, onSave: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.planner_result_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = onSave,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Text(stringResource(R.string.planner_save))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            MarkdownText(
                text = itinerary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

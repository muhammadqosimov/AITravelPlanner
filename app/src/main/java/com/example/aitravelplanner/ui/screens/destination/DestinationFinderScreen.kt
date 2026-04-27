package com.example.aitravelplanner.ui.screens.destination

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
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import com.example.aitravelplanner.data.model.DestinationRequest
import com.example.aitravelplanner.ui.components.ErrorCard
import com.example.aitravelplanner.ui.components.LoadingCard
import com.example.aitravelplanner.ui.components.TravelTopBar
import com.example.aitravelplanner.util.Constants
import com.example.aitravelplanner.util.MarkdownText
import com.example.aitravelplanner.util.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationFinderScreen(
    onNavigateBack: () -> Unit,
    viewModel: DestinationFinderViewModel = hiltViewModel()
) {
    val destinationsState by viewModel.destinationsState.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var budget by rememberSaveable { mutableStateOf("") }
    var weather by rememberSaveable { mutableStateOf("") }
    var duration by rememberSaveable { mutableStateOf("") }
    var selectedStyle by rememberSaveable { mutableStateOf("") }
    var weatherExpanded by remember { mutableStateOf(false) }

    val savedMsg = stringResource(R.string.destination_saved_success)

    LaunchedEffect(saveState) {
        if (saveState) {
            snackbarHostState.showSnackbar(savedMsg)
            viewModel.resetSaveState()
        }
    }

    Scaffold(
        topBar = {
            TravelTopBar(
                title = stringResource(R.string.destination_title),
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
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.destination_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = { Text(stringResource(R.string.destination_budget)) },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = weatherExpanded,
                        onExpandedChange = { weatherExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = weather,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.destination_weather)) },
                            leadingIcon = { Icon(Icons.Default.WbSunny, null) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(weatherExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = weatherExpanded,
                            onDismissRequest = { weatherExpanded = false }
                        ) {
                            Constants.WEATHER_OPTIONS.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        weather = option
                                        weatherExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text(stringResource(R.string.destination_duration)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = selectedStyle,
                            onValueChange = { selectedStyle = it },
                            label = { Text(stringResource(R.string.destination_style)) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            viewModel.findDestinations(
                                DestinationRequest(
                                    budgetUsd = budget.toIntOrNull() ?: 1000,
                                    preferredWeather = weather,
                                    durationDays = duration.toIntOrNull() ?: 0,
                                    travelStyle = selectedStyle
                                )
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
                            stringResource(R.string.destination_find),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            viewModel.surpriseMe(budget.toIntOrNull() ?: 1000)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(Icons.Default.Shuffle, null)
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            stringResource(R.string.destination_surprise),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item {
                when (val state = destinationsState) {
                    is UiState.Loading -> LoadingCard(
                        message = stringResource(R.string.destination_finding)
                    )
                    is UiState.Error -> ErrorCard(message = state.message)
                    is UiState.Success -> DestinationResultCard(
                        content = state.data,
                        onSave = {
                            viewModel.saveDestination("AI Suggestions", state.data)
                        }
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
private fun DestinationResultCard(
    content: String,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.destination_results_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = onSave,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.BookmarkAdd, null)
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                Text(stringResource(R.string.destination_save))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            MarkdownText(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

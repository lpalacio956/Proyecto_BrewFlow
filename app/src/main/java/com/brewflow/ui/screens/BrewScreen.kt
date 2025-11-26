package com.brewflow.ui.screens

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brewflow.data.model.BrewingMethodDto
import com.brewflow.data.model.BrewStepDto
import com.brewflow.presentation.brew.BrewViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.brewflow.R
import com.brewflow.ui.theme.BrewFlowTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BrewScreen(
    method: BrewingMethodDto,
    cups: Int,
    brewViewModel: BrewViewModel = hiltViewModel(),
    context: Context = androidx.compose.ui.platform.LocalContext.current,
    onFinish: () -> Unit
) {
    val steps = method.steps.sortedBy { it.order }
    val index by brewViewModel.currentStepIndex.collectAsState()
    val secondsLeft by brewViewModel.timerSecondsLeft.collectAsState()
    val step = steps[index]

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        AnimatedContent(targetState = step, transitionSpec = {
            slideInHorizontally { it } + fadeIn() with slideOutHorizontally { -it } + fadeOut()
        }) { currentStep ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "${method.name} — Paso ${index + 1}/${steps.size}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { (index + 1f) / steps.size },
                    color = ProgressIndicatorDefaults.linearColor,
                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
                Spacer(Modifier.height(12.dp))
                AsyncImage(
                    model = currentStep.imageUrl ?: method.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
                Spacer(Modifier.height(12.dp))
                Text(currentStep.title, style = MaterialTheme.typography.headlineSmall)
                Text(currentStep.description ?: "", style = MaterialTheme.typography.bodyLarge)
                currentStep.waterMl?.let {
                    Text("~${it * cups} ml de agua")
                }

                Spacer(Modifier.height(16.dp))

                if (currentStep.timeSeconds > 0) {
                    val remaining = secondsLeft.takeIf { it > 0 } ?: currentStep.timeSeconds.toLong()
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { 1f - remaining / currentStep.timeSeconds.toFloat() },
                                modifier = Modifier.size(100.dp),
                                color = ProgressIndicatorDefaults.circularColor,
                                strokeWidth = 8.dp,
                                trackColor = ProgressIndicatorDefaults.circularDeterminateTrackColor,
                                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                            )
                            Text("$remaining s")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            brewViewModel.startTimer(currentStep.timeSeconds) {
                                val uri =
                                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                                MediaPlayer.create(context, uri)?.start()
                            }
                        }) { Text("Iniciar") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { brewViewModel.cancelTimer() }) { Text("Detener") }
                    }
                }

                Spacer(Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { brewViewModel.prevStep() },
                        enabled = index > 0
                    ) { Text("Atrás") }

                    Button(
                        onClick = {
                            if (index < steps.lastIndex) brewViewModel.nextStep(steps.size)
                            else onFinish()
                        }
                    ) {
                        Text(if (index < steps.lastIndex) "Siguiente" else "Finalizar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrewScreenPreview() {
    BrewFlowTheme {
        val sampleMethod = BrewingMethodDto(
            id = "1",
            name = "Aeropress",
            imageUrl = "https://images.unsplash.com/photo-1593079631846-3435b8d05333",
            description = "A versatile and quick brewing method.",
            // Corrected to BrewingStepDto and added missing parameters with default values
            steps = listOf(
                BrewStepDto(
                    order = 1,
                    title = "Prepare the Aeropress",
                    description = "Insert a filter into the Aeropress cap and twist it on. Place the Aeropress on your mug.",
                    timeSeconds = 30
                ),
                BrewStepDto(
                    order = 2,
                    title = "Add Coffee and Water",
                    description = "Add 17g of finely ground coffee. Pour hot water (around 90°C) up to the number 2 mark.",
                    timeSeconds = 60,
                    waterMl = 100
                ),
                BrewStepDto(
                    order = 3,
                    title = "Plunge",
                    description = "Gently press the plunger downwards for about 20-30 seconds until you hear a hissing sound.",
                    timeSeconds = 30
                )
            ),
            maxCups = 4,
            coffeeGramsPerCup = 15,
            waterMlPerCup = 200,
            grindSize = "Fine"
        )

        // Since we can't use a real ViewModel in a @Preview,
        // we pass a default instance and manage state simply for the preview.
        BrewScreen(
            method = sampleMethod,
            cups = 1,
            onFinish = {}
        )
    }
}

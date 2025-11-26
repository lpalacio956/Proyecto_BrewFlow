package com.brewflow.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brewflow.R
import com.brewflow.data.model.BrewingMethodDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodDetailScreen(
    method: BrewingMethodDto,
    onStartBrew: (String, Int) -> Unit,
    onBack: () -> Unit
) {
    var cups by remember { mutableStateOf(1) }
    val max = method.maxCups
    val scrollState = rememberScrollState()
    val imageOffset by remember { derivedStateOf { scrollState.value * 0.5f } }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(method.name) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(Modifier.padding(padding)) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    ) {
                        AsyncImage(
                            model = method.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                                    )
                                )
                        )
                    }

                    Column(Modifier.padding(16.dp)) {
                        Text(method.description ?: "", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(16.dp))
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text("Tazas:", Modifier.weight(1f))
                            IconButton(onClick = { cups = (cups - 1).coerceAtLeast(1) }) {
                                Icon(Icons.Default.Remove, contentDescription = null)
                            }
                            Text("$cups")
                            IconButton(onClick = { cups = (cups + 1).coerceAtMost(max) }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                        val totalCoffee = method.coffeeGramsPerCup * cups
                        val totalWater = method.waterMlPerCup * cups
                        Text("Café: $totalCoffee g")
                        Text("Agua: $totalWater ml")
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { onStartBrew(method.id, cups) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Iniciar preparación")
                        }
                    }
                }
            }
        }
    }
}

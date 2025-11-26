package com.brewflow.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brewflow.R
import com.brewflow.data.model.BrewStepDto
import com.brewflow.data.model.BrewingMethodDto
import com.brewflow.presentation.home.HomeViewModel
import com.brewflow.ui.theme.LightBrown
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onOpenMethod: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()
    val methods by viewModel.methods.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val tabs = listOf("Descubrir", "Métodos")

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("BrewFlow ☕", style = MaterialTheme.typography.titleLarge) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(title) }
                        )
                    }
                }

                HorizontalPager(state = pagerState) { page ->
                    when (page) {
                        0 -> DiscoverTab(methods, suggestMethodForFlavor = viewModel::suggestMethodForFlavor, onOpenMethod)
                        1 -> MethodsTab(loading, methods, onOpenMethod)
                    }
                }
            }
        }
    }
}

@Composable
fun DiscoverTab(
    methods: List<BrewingMethodDto>,
    suggestMethodForFlavor: (String) -> BrewingMethodDto?,
    onOpenMethod: (String) -> Unit
) {

    val flavors = methods.flatMap { it.flavorProfiles }.distinct()
    var selectedFlavor by remember { mutableStateOf<String?>(null) }
    val suggestion = selectedFlavor?.let(suggestMethodForFlavor)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Elige el sabor que buscas:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp) // Adds vertical space between rows
        ) {
            flavors.chunked(2).forEach { rowFlavors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds space between chips
                ) {
                    rowFlavors.forEach { flavor ->
                        FilterChip(
                            modifier = Modifier
                                .weight(1f) // Each chip takes equal space
                                .padding(horizontal = 4.dp),
                            selected = selectedFlavor == flavor,
                            onClick = { selectedFlavor = if (selectedFlavor == flavor) null else flavor },
                            label = { Text(flavor) }
                        )
                    }
                    // If the number of flavors is odd, this adds a spacer to keep the layout consistent
                    if (rowFlavors.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        AnimatedVisibility(visible = suggestion != null) {
            suggestion?.let { method ->
                Spacer(Modifier.height(20.dp))
                Card(
                    onClick = { onOpenMethod(method.id) },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBrown)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = method.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(method.name, style = MaterialTheme.typography.titleLarge)
                        Text(method.description ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(8.dp))
                        Text("Toca para ver detalles →", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun MethodsTab(
    loading: Boolean,
    methods: List<BrewingMethodDto>,
    onOpenMethod: (String) -> Unit
) {
    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(methods) { m ->
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(if (pressed) 0.97f else 1f)

                Card(
                    onClick = { onOpenMethod(m.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    pressed = true
                                    tryAwaitRelease()
                                    pressed = false
                                }
                            )
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = LightBrown)
                ) {
                    Column {
                        AsyncImage(
                            model = m.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )
                        Column(Modifier.padding(16.dp)) {
                            Text(m.name, style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(6.dp))
                            Text(m.description ?: "", style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Tazas máx.: ${m.maxCups}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

private val sampleMethods = listOf(
    BrewingMethodDto(
        id = "v60",
        name = "V60",
        description = "Pour-over method, bright and clean cup.",
        maxCups = 4,
        coffeeGramsPerCup = 16,
        waterMlPerCup = 256,
        grindSize = "Medium",
        imageUrl = "https://www.haymancoffee.com/cdn/shop/articles/Hario_v60_coffee_v60_coffee_pour_over_coffee_filter_coffee.jpg?v=1603289229",
        steps = listOf(
            BrewStepDto(1, "Heat water", "Heat to 88°C."),
            BrewStepDto(2, "Purge filter", "Rinse paper filter with hot water and discard rinse water."),
            BrewStepDto(3, "Add coffee", "Add freshly ground coffee to the filter."),
            BrewStepDto(4, "Preinfuse", "Preinfuse for 30 seconds with ~70 ml water.")
        ),
        flavorProfiles = listOf("Bright", "Clean", "Fruity")
    ),
    BrewingMethodDto(
        id = "aeropress",
        name = "Aeropress",
        description = "Immersion + pressure for a concentrated cup.",
        maxCups = 1,
        coffeeGramsPerCup = 16,
        waterMlPerCup = 200,
        grindSize = "Fine to Medium",
        imageUrl = "https://cdn.coffeecircle.com/6d3ea3ba-e22b-4350-aef8-15074f08b163/-/resize/1200x/-/quality/lighter/-/progressive/yes/-/format/auto/HeaderAeropRess.jpg",
        steps = emptyList(),
        flavorProfiles = listOf("Strong", "Clean", "Concentrated")
    ),
    BrewingMethodDto(
        id = "french_press",
        name = "French Press",
        description = "Immersion brewing for full body.",
        maxCups = 6,
        coffeeGramsPerCup = 12,
        waterMlPerCup = 200,
        grindSize = "Coarse",
        imageUrl = "https://www.taylorlane.com/cdn/shop/articles/pros-cons-electric-french-press_512x.jpg?v=1598986444",
        steps = emptyList(),
        flavorProfiles = listOf("Full", "Bold", "Heavy")
    )
)


@Preview
@Composable
fun DiscoverTabPreview() {

    DiscoverTab(methods = sampleMethods, suggestMethodForFlavor = {null},  onOpenMethod = {})
}

// --- Methods Tab Preview ---
@Preview
@Composable
fun MethodsTabPreview() {
    MethodsTab(
        loading = false,
        methods = sampleMethods,
        onOpenMethod = {}
    )
}


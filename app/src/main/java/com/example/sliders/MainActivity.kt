package com.example.sliders

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.sliders.ui.theme.Pink80
import com.example.sliders.ui.theme.Purple80
import com.example.sliders.ui.theme.SlidersTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val sliderViewModel by viewModels<SliderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SlidersTheme {
                val data = sliderViewModel.data.collectAsState(initial = null)
                val firstCir = sliderViewModel.firstCir.collectAsState()
                val secCir = sliderViewModel.secCir.collectAsState()
                val progress = sliderViewModel.progress.collectAsState()
                val showDialog = sliderViewModel.isDialogVisible.collectAsState()
                val currentPosition = sliderViewModel.currentProgressPosition.collectAsState()

                val colorChange1 by remember { derivedStateOf { currentPosition.value >= firstCir.value } }
                val colorChange2 by remember { derivedStateOf { currentPosition.value >= secCir.value } }
                val isToastEnabled by remember { derivedStateOf { progress.value >= 1f } }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xffA9BA9D))
                        .padding(top = 200.dp, start = 20.dp, end = 20.dp)
                ) {
                    val size by animateFloatAsState(
                        targetValue = progress.value,
                        tween(
                            durationMillis = 1000, delayMillis = 200,
                            easing = LinearOutSlowInEasing
                        ), label = ""
                    )
                    data.value?.apply {
                        ConstraintLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color(0xffA9BA9D))
                        ) {
                            val (startPrice, column1, endPrice, column2) = createRefs()
                            Text(text = min.toString(), modifier = Modifier
                                .padding(end = 2.dp)
                                .constrainAs(startPrice) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    end.linkTo(column1.start)
                                })

                            Column(modifier = Modifier.constrainAs(column1) {
                                start.linkTo(startPrice.end)
                                top.linkTo(parent.top)
                                end.linkTo(endPrice.start)
                            }) {
                                Box(
                                    modifier = Modifier
                                        .width(270.dp)
                                        .height(17.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Green)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(size)
                                            .fillMaxHeight()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.Black.copy(alpha = 0.5f))
                                            .animateContentSize()
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(17.dp)
                                            .offset(x = firstCir.value.dp)
                                            .border(
                                                width = 2.dp,
                                                color = Color.White,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Canvas(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.Center)
                                        ) {
                                            drawCircle(
                                                color = if (colorChange1) Purple80 else Pink80
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(17.dp)
                                            .offset(x = secCir.value.dp)
                                            .border(
                                                width = 2.dp,
                                                color = Color.White,
                                                shape = CircleShape
                                            )
                                    ) {
                                        Canvas(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.Center)
                                        ) {
                                            drawCircle(
                                                color = if (colorChange2) Purple80 else Pink80
                                            )
                                        }
                                    }
                                }
                                Row {
                                    Text(
                                        text = points.first().value.toString(),
                                        modifier = Modifier.offset((firstCir.value - 10).dp)
                                    )
                                    Text(
                                        text = points[1].value.toString(),
                                        modifier = Modifier.offset((secCir.value - firstCir.value + 10).dp)
                                    )
                                }
                            }

                            Text(text = max.toString(), modifier = Modifier
                                .padding(start = 2.dp)
                                .constrainAs(endPrice) {
                                    top.linkTo(parent.top)
                                    end.linkTo(parent.end)
                                })

                            Column(modifier = Modifier
                                .constrainAs(column2) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(column1.bottom)
                                }
                                .padding(top = 32.dp)) {
                                LazyColumn {
                                    items(items) {
                                        IncreaseOrDecrease(
                                            sliderViewModel = sliderViewModel,
                                            item = it,
                                            showDialog.value
                                        )
                                    }
                                }
                            }
                        }
                        if (isToastEnabled) {
                            Toast.makeText(
                                this@MainActivity,
                                "Max Limit Reached",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncreaseOrDecrease(sliderViewModel: SliderViewModel, item: Item, showDialog: Boolean) {
    Row(modifier = Modifier.padding(top = 16.dp)) {
        SliderButtons(
            onClickMinus = {
                sliderViewModel.totalCost(
                    false, when (item.multiple) {
                        1 -> Quantity.ONE
                        2 -> Quantity.TWO
                        else -> Quantity.THREE
                    }
                )
            },
            onClickPlus = { /* Handle plus button click */ },
            text = "-",
            isPlus = false
        )
        Text(
            text = item.multiple.toString(),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterVertically)
                .clickable {
                    sliderViewModel.showDialog(true)
                    sliderViewModel.saveMultiplier = item.multiple
                    sliderViewModel.saveQtyValue = item.eachQtyValue
                },
            textAlign = TextAlign.Center,
            fontSize = 40.sp
        )
        SliderButtons(
            onClickMinus = { /* Handle minus button click */ },
            onClickPlus = {
                sliderViewModel.totalCost(
                    true, when (item.multiple) {
                        1 -> Quantity.ONE
                        2 -> Quantity.TWO
                        else -> Quantity.THREE
                    }
                )
            },
            text = "+",
            isPlus = true
        )
    }
    if (showDialog) {
        DialogBox(
            onDismiss = {
                sliderViewModel.apply {
                    showDialog(false)
                    updateProgress(it)
                }
            }, multiplier = when (sliderViewModel.saveMultiplier) {
                1 -> 1
                2 -> 2
                else -> 3
            }, eachQuantity = sliderViewModel.saveQtyValue
        )
    }
}

@Composable
fun SliderButtons(
    modifier: Modifier = Modifier,
    onClickMinus: () -> Unit,
    onClickPlus: () -> Unit,
    text: String,
    isPlus: Boolean
) {
    Column(
        modifier = modifier
            .size(60.dp)
            .border(2.dp, Color(0xff006400), RoundedCornerShape(12.dp))
            .clickable { if (isPlus) onClickPlus.invoke() else onClickMinus.invoke() }
            .padding(4.dp),
    ) {
        Text(
            text = text,
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DialogBox(
    onDismiss: (Int) -> Unit,
    multiplier: Int,
    eachQuantity: Int
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.background(color = Color.Cyan)
            ) {
                items(30) {
                    Text(
                        text = "${it.plus(1).times(multiplier).times(eachQuantity)}",
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = Color.Gray, shape = RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                            .clickable {
                                onDismiss.invoke(
                                    multiplier
                                        .times(it.plus(1))
                                        .times(eachQuantity)
                                )
                            },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
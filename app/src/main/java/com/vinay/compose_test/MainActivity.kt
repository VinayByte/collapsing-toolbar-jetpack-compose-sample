package com.vinay.compose_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.vinay.compose_test.ui.theme.Black900
import com.vinay.compose_test.ui.theme.ComposetestTheme

/**
 * Created by Vinay
 * @Author: Vinay
 * @Date: 27-05-2023
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposetestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.surface
                ) {
                    CollapsingToolbarScreen()
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposetestTheme {
        Greeting("Android")
    }
}

private val headerHeight = 250.dp
private val toolbarHeight = 56.dp

private val paddingMedium = 16.dp

private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f

@Preview
@Composable
fun CollapsingToolbarScreen(modifier: Modifier = Modifier) {
    val scroll: ScrollState = rememberScrollState(0)

    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

    Box(modifier = modifier) {
        Header(
            scroll = scroll,
            headerHeightPx = headerHeightPx,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        )
        Body(
            scroll = scroll,
            modifier = Modifier.fillMaxSize()
        )
        Toolbar(
            scroll = scroll,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
        Title(scroll = scroll)
    }
}

@Composable
private fun Header(
    scroll: ScrollState,
    headerHeightPx: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = -scroll.value.toFloat() / 2f // Parallax effect
                alpha = (-1f / headerHeightPx) * scroll.value + 1
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.header_image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Black900),
                        startY = 3 * headerHeightPx / 4
                    )
                )
        )
    }
}

@Composable
private fun Body(scroll: ScrollState, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(scroll)
    ) {
        Spacer(Modifier.height(headerHeight))
        repeat(5) {
            Text(
                text = stringResource(R.string.lorem_ipsum),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun Toolbar(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float,
    modifier: Modifier = Modifier
) {
    val toolbarBottom by remember {
        mutableStateOf(headerHeightPx - toolbarHeightPx)
    }

    val showToolbar by remember {
        derivedStateOf {
            scroll.value >= toolbarBottom
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            title = {},
            backgroundColor = Color.Blue,
            elevation = 0.dp
        )
    }
}

@Composable
private fun Title(
    scroll: ScrollState,
    modifier: Modifier = Modifier
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }

    Text(
        text = stringResource(id = R.string.app_name),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .graphicsLayer {
                val collapseRange: Float = (headerHeight.toPx() - toolbarHeight.toPx())
                val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                val scaleXY = lerp(
                    titleFontScaleStart.dp,
                    titleFontScaleEnd.dp,
                    collapseFraction
                )

                val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                val titleYFirstInterpolatedPoint = lerp(
                    headerHeight - titleHeightPx.toDp() - paddingMedium,
                    headerHeight / 2,
                    collapseFraction
                )

                val titleXFirstInterpolatedPoint = lerp(
                    titlePaddingStart,
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    collapseFraction
                )

                val titleYSecondInterpolatedPoint = lerp(
                    headerHeight / 2,
                    toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                    collapseFraction
                )

                val titleXSecondInterpolatedPoint = lerp(
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    titlePaddingEnd - titleExtraStartPadding,
                    collapseFraction
                )

                val titleY = lerp(
                    titleYFirstInterpolatedPoint,
                    titleYSecondInterpolatedPoint,
                    collapseFraction
                )

                val titleX = lerp(
                    titleXFirstInterpolatedPoint,
                    titleXSecondInterpolatedPoint,
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                titleHeightPx = it.size.height.toFloat()
                titleWidthPx = it.size.width.toFloat()
            }
    )
}
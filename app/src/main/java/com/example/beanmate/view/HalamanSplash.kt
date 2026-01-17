package com.example.beanmate2.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.view.viewmodel.SplashViewModel
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@Composable
fun HalamanSplash(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        viewModel.mulaiSplash()
    }

    LaunchedEffect(viewModel.selesai) {
        if (viewModel.selesai) {
            navigateToHome()
        }
    }
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "Splash Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFF1A1A1A).copy(alpha = 0.7f),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 12.dp,
                modifier = Modifier.scale(scale)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 40.dp, vertical = 32.dp)
                        .widthIn(max = 320.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BeanMate",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 38.sp,
                            letterSpacing = 1.5.sp
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFF5E6D3)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(3.dp)
                            .background(
                                Color(0xFFD4A574),
                                RoundedCornerShape(2.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Crafted Beans, Calm Moments",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        ),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE8D4B8)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Brewing your vibe...",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            letterSpacing = 0.8.sp
                        ),
                        fontWeight = FontWeight.Light,
                        color = Color.White.copy(alpha = alpha),
                        modifier = Modifier.alpha(alpha)
                    )
                }
            }
        }
    }
}
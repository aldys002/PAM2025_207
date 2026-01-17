package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beanmate2.R
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.DetailProdukViewModel
import com.example.beanmate2.view.viewmodel.StatusUiDetailProduk
import com.example.beanmate2.view.viewmodel.UiEventCart
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetailProduk(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    detailViewModel: DetailProdukViewModel = viewModel(factory = PenyediaViewModel.Factory),
    cartViewModel: CartViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(cartViewModel.uiEvent) {
        when (val ev = cartViewModel.uiEvent) {
            is UiEventCart.Message -> {
                snackbarHostState.showSnackbar(ev.text)
                cartViewModel.clearEvent()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.product_detail_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF6B4E3D),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFBF5),
                            Color(0xFFFFF8E7)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            when (val state = detailViewModel.statusUiDetailProduk) {

                is StatusUiDetailProduk.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFD4A574),
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = "Loading product details...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }

                is StatusUiDetailProduk.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.error),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                is StatusUiDetailProduk.Success -> {
                    val produk = state.produk
                    val imageUrl = baseUploads + produk.image

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp)
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = produk.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.3f)
                                            )
                                        )
                                    )
                            )
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = (-24).dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                                ),
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = produk.name,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontSize = 28.sp,
                                        letterSpacing = 0.3.sp
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C2C2C)
                                )
                                Surface(
                                    color = Color(0xFFFFF8E7),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = "Price",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = Color(0xFF666666),
                                                fontWeight = FontWeight.Medium
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Rp ${produk.price}",
                                                style = MaterialTheme.typography.headlineSmall.copy(
                                                    fontSize = 26.sp
                                                ),
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Color(0xFFD4A574)
                                            )
                                        }
                                    }
                                }
                                Divider(
                                    color = Color(0xFFEEEEEE),
                                    thickness = 1.dp
                                )
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 18.sp
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2C2C2C)
                                    )
                                    Text(
                                        text = produk.description,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            lineHeight = 24.sp
                                        ),
                                        color = Color(0xFF666666)
                                    )
                                }
                                Surface(
                                    color = Color(0xFFF5F5F5),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "Image: ${produk.image}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp
                                        ),
                                        color = Color(0xFF999999),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        cartViewModel.addToCart(produk.id)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF6B4E3D)
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 6.dp,
                                        pressedElevation = 2.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = stringResource(R.string.product_add_to_cart),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
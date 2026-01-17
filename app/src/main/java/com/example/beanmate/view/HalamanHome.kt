package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beanmate2.R
import com.example.beanmate2.data.DataBeanMate
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.HomeViewModel
import com.example.beanmate2.view.viewmodel.StatusUiProduk
import com.example.beanmate2.view.viewmodel.StatusUiCart
import com.example.beanmate2.view.viewmodel.UiEventCart
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    navigateToLoginAdmin: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    navigateToCart: () -> Unit,
    refreshTrigger: Boolean,
    onRefreshConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory),
    cartViewModel: CartViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadProduk()
            cartViewModel.loadCart()
            onRefreshConsumed()
        }
    }

    LaunchedEffect(cartViewModel.uiEvent) {
        if (cartViewModel.uiEvent is UiEventCart.Message) {
            cartViewModel.loadCart()
            cartViewModel.clearEvent()
        }
    }
    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
    }

    val cartStatus by remember { derivedStateOf { cartViewModel.statusUiCart } }
    val cartCount =
        if (cartStatus is StatusUiCart.Success)
            (cartStatus as StatusUiCart.Success).cart.size
        else 0

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.home_title),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = navigateToLoginAdmin,
                    containerColor = Color(0xFF6B4E3D),
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin"
                        )
                        Text(
                            text = "Admin",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
                if (cartCount > 0) {
                    FloatingActionButton(
                        onClick = navigateToCart,
                        containerColor = Color(0xFFD4A574),
                        contentColor = Color.White,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart"
                                )
                                if (cartCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .size(18.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(x = 8.dp, y = (-8).dp)
                                            .background(Color.Red, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = cartCount.toString(),
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Cart",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        HomeBody(
            statusUiProduk = viewModel.listProduk,
            retryAction = viewModel::loadProduk,
            navigateToDetail = navigateToDetail,
            cartViewModel = cartViewModel,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    statusUiProduk: StatusUiProduk,
    retryAction: () -> Unit,
    navigateToDetail: (Int) -> Unit,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (statusUiProduk) {
            is StatusUiProduk.Loading -> LoadingScreen()
            is StatusUiProduk.Success -> DaftarProduk(
                produk = statusUiProduk.produk,
                onProdukClick = navigateToDetail,
                cartViewModel = cartViewModel
            )
            is StatusUiProduk.Error -> ErrorScreen(retryAction)
        }
    }
}

@Composable
fun DaftarProduk(
    produk: List<DataBeanMate>,
    onProdukClick: (Int) -> Unit,
    cartViewModel: CartViewModel
) {
    LazyColumn(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(produk, key = { it.id }) { item ->
            ItemProduk(
                produk = item,
                onAddToCart = { cartViewModel.addToCart(item.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProdukClick(item.id) }
            )
        }
    }
}

@Composable
fun ItemProduk(
    produk: DataBeanMate,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"
    val imageUrl = if (produk.image.isNotBlank()) baseUploads + produk.image else ""

    Card(
        modifier = modifier.shadow(
            elevation = 6.dp,
            shape = MaterialTheme.shapes.large
        ),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFD4A574),
                                Color(0xFF8B6F47)
                            )
                        )
                    )
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = produk.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = produk.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        letterSpacing = 0.3.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFFFF8E7),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "Rp ${produk.price}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD4A574),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = produk.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            FilledIconButton(
                onClick = onAddToCart,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color(0xFFD4A574),
                    contentColor = Color.White
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Add to Cart",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator(
            color = Color(0xFFD4A574),
            strokeWidth = 4.dp
        )
        Text(
            text = "Loading products...",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF666666)
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.error),
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF2C2C2C),
            fontWeight = FontWeight.SemiBold
        )
        Button(
            onClick = retryAction,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFD4A574)
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.retry),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
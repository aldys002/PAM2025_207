package com.example.beanmate2.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beanmate2.R
import com.example.beanmate2.data.DataBeanMate
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.HomeViewModel
import com.example.beanmate2.view.viewmodel.StatusUiProduk
import com.example.beanmate2.view.viewmodel.StatusUiCart
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

    // Logika Sinkronisasi: Berjalan saat refreshTrigger bernilai true
    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadProduk() // Refresh daftar produk (setelah hapus/edit)
            cartViewModel.loadCart() // Refresh data keranjang (setelah checkout)
            onRefreshConsumed()
        }
    }

    // Mengamati status keranjang untuk menghitung jumlah item
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
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                FloatingActionButton(onClick = navigateToLoginAdmin) {
                    Text(text = "Admin")
                }

                // Tombol Cart hanya muncul jika ada item (cartCount > 0)
                if (cartCount > 0) {
                    FloatingActionButton(onClick = navigateToCart) {
                        Text(text = "Cart ($cartCount)")
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
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp,
            bottom = 90.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
    // Alamat IP 10.0.2.2 digunakan untuk mengakses localhost dari Emulator Android
    val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"
    val imageUrl = if (produk.image.isNotBlank()) baseUploads + produk.image else ""

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = produk.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = produk.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rp ${produk.price}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = produk.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onAddToCart) {
                Icon(
                    imageVector = Icons.Default.AddShoppingCart,
                    contentDescription = "Tambah ke Keranjang"
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen(retryAction: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = stringResource(R.string.error))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = retryAction) {
            Text(text = stringResource(R.string.retry))
        }
    }
}
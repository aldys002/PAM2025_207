package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = detailViewModel.statusUiDetailProduk) {

                is StatusUiDetailProduk.Loading -> {
                    CircularProgressIndicator()
                }

                is StatusUiDetailProduk.Error -> {
                    Text(text = stringResource(R.string.error))
                }

                is StatusUiDetailProduk.Success -> {
                    val produk = state.produk
                    val imageUrl = baseUploads + produk.image // contoh: .../arabika.jpg

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = produk.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = produk.name,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Rp ${produk.price}",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = produk.description,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Image: ${produk.image}",
                            style = MaterialTheme.typography.labelSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                cartViewModel.addToCart(produk.id)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.product_add_to_cart))
                        }
                    }
                }
            }
        }
    }
}

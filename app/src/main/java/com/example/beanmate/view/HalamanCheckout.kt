package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.beanmate2.R
import com.example.beanmate2.view.viewmodel.CheckoutViewModel
import com.example.beanmate2.view.viewmodel.StatusUiCheckout
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCheckout(
    navigateBack: () -> Unit,
    navigateToOrders: () -> Unit,
    navigateToOrderDetail: (Int) -> Unit,
    onCheckoutSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckoutViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var nama by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var telepon by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.checkout_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            when (val state = viewModel.statusUiCheckout) {

                is StatusUiCheckout.Idle -> {
                    Text(
                        text = "Ringkasan Pesanan",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Card {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Total Harga")
                            Text(
                                text = "Rp ${viewModel.totalHarga}",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Text(
                        text = "Data Pemesan",
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text(stringResource(R.string.checkout_name)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text(stringResource(R.string.checkout_address)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = telepon,
                        onValueChange = { telepon = it },
                        label = { Text(stringResource(R.string.checkout_phone)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            viewModel.checkout(
                                nama = nama,
                                alamat = alamat,
                                telepon = telepon
                            )
                        },
                        enabled = nama.isNotBlank() &&
                                alamat.isNotBlank() &&
                                telepon.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Checkout Sekarang")
                    }
                }

                is StatusUiCheckout.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is StatusUiCheckout.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Checkout gagal")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.reset() }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                is StatusUiCheckout.Success -> {
                    val orderId = state.data.data?.order_id ?: 0 // Ambil order_id dari response

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Checkout Berhasil 🎉",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Total: Rp ${state.data.data?.total_price}")

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                viewModel.reset()
                                if (orderId > 0) {
                                    navigateToOrderDetail(orderId)
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Lihat Pesanan")
                        }
                    }
                }

            }
        }
    }
}


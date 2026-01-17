package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFBF5),
                            Color(0xFFFFF8E7)
                        )
                    )
                )
        ) {
            when (val state = viewModel.statusUiCheckout) {

                is StatusUiCheckout.Idle -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Ringkasan Pesanan",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C2C2C)
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 6.dp,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Harga",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color(0xFF666666),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Rp ${viewModel.totalHarga}",
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontSize = 28.sp
                                            ),
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFD4A574)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFD4A574).copy(alpha = 0.15f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Receipt,
                                            contentDescription = null,
                                            tint = Color(0xFF6B4E3D),
                                            modifier = Modifier
                                                .size(60.dp)
                                                .padding(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(
                                text = "Data Pemesan",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 22.sp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C2C2C)
                            )
                            OutlinedTextField(
                                value = nama,
                                onValueChange = { nama = it },
                                label = { Text(stringResource(R.string.checkout_name)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color(0xFFD4A574)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFD4A574),
                                    focusedLabelColor = Color(0xFFD4A574),
                                    cursorColor = Color(0xFFD4A574)
                                ),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = alamat,
                                onValueChange = { alamat = it },
                                label = { Text(stringResource(R.string.checkout_address)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Home,
                                        contentDescription = null,
                                        tint = Color(0xFFD4A574)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFD4A574),
                                    focusedLabelColor = Color(0xFFD4A574),
                                    cursorColor = Color(0xFFD4A574)
                                ),
                                maxLines = 3
                            )
                            OutlinedTextField(
                                value = telepon,
                                onValueChange = { telepon = it },
                                label = { Text(stringResource(R.string.checkout_phone)) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = Color(0xFFD4A574)
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFD4A574),
                                    focusedLabelColor = Color(0xFFD4A574),
                                    cursorColor = Color(0xFFD4A574)
                                ),
                                singleLine = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6B4E3D),
                                disabledContainerColor = Color(0xFF6B4E3D).copy(alpha = 0.5f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            Text(
                                text = "Checkout Sekarang",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                is StatusUiCheckout.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFD4A574),
                            strokeWidth = 4.dp,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Processing your order...",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                    }
                }

                is StatusUiCheckout.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Checkout gagal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Terjadi kesalahan, silakan coba lagi",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.reset() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFD4A574)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Coba Lagi",
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                is StatusUiCheckout.Success -> {
                    val orderId = state.data.data?.order_id ?: 0

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF4CAF50).copy(alpha = 0.15f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Checkout Berhasil 🎉",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontSize = 26.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF8E7)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Total Pembayaran",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Color(0xFF666666)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Rp ${state.data.data?.total_price}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontSize = 24.sp
                                    ),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFD4A574)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = {
                                viewModel.reset()
                                if (orderId > 0) {
                                    navigateToOrderDetail(orderId)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6B4E3D)
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp
                            )
                        ) {
                            Text(
                                text = "Lihat Pesanan",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.data.DataOrderItem
import com.example.beanmate2.view.viewmodel.OrderDetailViewModel
import com.example.beanmate2.view.viewmodel.StatusUiOrderDetail
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanOrderDetail(
    orderId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrderDetailViewModel = viewModel(
        factory = PenyediaViewModel.Factory,
        key = "OrderDetailViewModel_$orderId"
    )
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadDetail()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.order_detail_title),
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
                ),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewModel.statusUiOrderDetail) {
                is StatusUiOrderDetail.Loading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFD4A574),
                            strokeWidth = 4.dp
                        )
                        Text(
                            text = "Loading order details...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666)
                        )
                    }
                }

                is StatusUiOrderDetail.Error -> {
                    Text(
                        text = stringResource(R.string.error),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                is StatusUiOrderDetail.Success -> {
                    val data = state.detail.data ?: run {
                        Text(
                            text = stringResource(R.string.error),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                        return@Box
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Surface(
                                color = Color(0xFFD4A574).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF6B4E3D)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Receipt,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .padding(12.dp)
                                        )
                                    }
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Order #${data.order.id}",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                fontSize = 22.sp
                                            ),
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2C2C2C)
                                        )
                                        Text(
                                            text = data.order.createdAt,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 12.sp
                                            ),
                                            color = Color(0xFF666666)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Data Pemesan",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 17.sp
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2C2C2C)
                                    )

                                    Divider(color = Color(0xFFEEEEEE))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = Color(0xFFD4A574),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = data.order.nama,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF2C2C2C)
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = null,
                                            tint = Color(0xFFD4A574),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = data.order.alamat,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF2C2C2C)
                                        )
                                    }

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = Color(0xFFD4A574),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = data.order.telepon,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF2C2C2C)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 4.dp,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = "Ringkasan Pesanan",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 17.sp
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2C2C2C)
                                    )

                                    Divider(color = Color(0xFFEEEEEE))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Status",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color(0xFF666666)
                                        )
                                        Surface(
                                            color = when (data.order.status.lowercase()) {
                                                "pending" -> Color(0xFFFFF3E0)
                                                "completed" -> Color(0xFFE8F5E9)
                                                "cancelled" -> Color(0xFFFFEBEE)
                                                else -> Color(0xFFE0E0E0)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = data.order.status,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = when (data.order.status.lowercase()) {
                                                    "pending" -> Color(0xFFF57C00)
                                                    "completed" -> Color(0xFF2E7D32)
                                                    "cancelled" -> Color(0xFFC62828)
                                                    else -> Color(0xFF424242)
                                                },
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                            )
                                        }
                                    }

                                    Surface(
                                        color = Color(0xFFFFF8E7),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Total Harga",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = Color(0xFF666666)
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = "Rp ${data.order.totalPrice}",
                                                    style = MaterialTheme.typography.headlineSmall.copy(
                                                        fontSize = 24.sp
                                                    ),
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = Color(0xFFD4A574)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color(0xFF6B4E3D),
                                    modifier = Modifier.size(22.dp)
                                )
                                Text(
                                    text = "Daftar Item",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontSize = 18.sp
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C2C2C)
                                )
                            }
                        }
                        items(
                            items = data.items,
                            key = { it.productId }
                        ) { item -> ItemOrderDetail(item = item) }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemOrderDetail(
    item: DataOrderItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp
                ),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C2C2C)
            )

            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Qty: ${item.qty}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "@ Rp ${item.priceEach}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Subtotal",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF999999)
                    )
                    Text(
                        text = "Rp ${item.subtotal}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4A574)
                    )
                }
            }
        }
    }
}
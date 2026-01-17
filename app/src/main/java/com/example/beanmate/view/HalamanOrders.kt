package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ShoppingBag
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
import com.example.beanmate2.R
import com.example.beanmate2.data.DataOrder
import com.example.beanmate2.view.viewmodel.OrdersViewModel
import com.example.beanmate2.view.viewmodel.StatusUiOrders
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanOrders(
    navigateBack: () -> Unit,
    navigateToOrderDetail: (Int) -> Unit,
    refreshTrigger: Boolean,
    onRefreshConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OrdersViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    androidx.compose.runtime.LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadOrders()
            onRefreshConsumed()
        }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.orders_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        OrdersBody(
            statusUiOrders = viewModel.statusUiOrders,
            retryAction = viewModel::loadOrders,
            onOrderClick = navigateToOrderDetail,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}



@Composable
fun OrdersBody(
    statusUiOrders: StatusUiOrders,
    retryAction: () -> Unit,
    onOrderClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFFFFBF5),
                    Color(0xFFFFF8E7)
                )
            )
        ),
        contentAlignment = Alignment.Center
    ) {
        when (statusUiOrders) {
            is StatusUiOrders.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFD4A574),
                        strokeWidth = 4.dp
                    )
                    Text(
                        text = "Loading orders...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )
                }
            }

            is StatusUiOrders.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.error),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        onClick = retryAction,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD4A574)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.retry),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            is StatusUiOrders.Success -> {
                if (statusUiOrders.orders.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFD4A574).copy(alpha = 0.15f),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                tint = Color(0xFF6B4E3D),
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp)
                            )
                        }
                        Text(
                            text = "No Orders Yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Your order history will appear here",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(statusUiOrders.orders, key = { it.id }) { order ->
                            ItemOrder(
                                order = order,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOrderClick(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemOrder(
    order: DataOrder,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(
            elevation = 4.dp,
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD4A574).copy(alpha = 0.15f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = Color(0xFF6B4E3D),
                        modifier = Modifier
                            .size(50.dp)
                            .padding(12.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 16.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2C2C)
                    )

                    Text(
                        text = "Rp ${order.totalPrice}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFD4A574)
                    )
                    Surface(
                        color = when (order.status.lowercase()) {
                            "pending" -> Color(0xFFFFF3E0)
                            "completed" -> Color(0xFFE8F5E9)
                            "cancelled" -> Color(0xFFFFEBEE)
                            else -> Color(0xFFE0E0E0)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = order.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = when (order.status.lowercase()) {
                                "pending" -> Color(0xFFF57C00)
                                "completed" -> Color(0xFF2E7D32)
                                "cancelled" -> Color(0xFFC62828)
                                else -> Color(0xFF424242)
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = order.createdAt,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp
                        ),
                        color = Color(0xFF999999)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View details",
                tint = Color(0xFFD4A574),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
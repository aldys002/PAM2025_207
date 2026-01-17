package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.RemoveShoppingCart
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
import com.example.beanmate2.data.DataCartItem
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.StatusUiCart
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanCart(
    navigateBack: () -> Unit,
    navigateToCheckout: () -> Unit,
    refreshTrigger: Boolean,
    onRefreshConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    androidx.compose.runtime.LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadCart()
            onRefreshConsumed()
        }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.cart_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        CartBody(
            statusUiCart = viewModel.statusUiCart,
            onRemove = viewModel::removeCart,
            retryAction = viewModel::loadCart,
            onCheckout = navigateToCheckout,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}


@Composable
fun CartBody(
    statusUiCart: StatusUiCart,
    onRemove: (Int) -> Unit,
    retryAction: () -> Unit,
    onCheckout: () -> Unit,
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
        when (statusUiCart) {
            is StatusUiCart.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFD4A574),
                        strokeWidth = 4.dp
                    )
                    Text(
                        text = "Loading cart...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )
                }
            }

            is StatusUiCart.Error -> {
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

            is StatusUiCart.Success -> {
                if (statusUiCart.cart.isEmpty()) {
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
                                imageVector = Icons.Default.RemoveShoppingCart,
                                contentDescription = null,
                                tint = Color(0xFF6B4E3D),
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp)
                            )
                        }
                        Text(
                            text = stringResource(R.string.cart_empty),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Add some products to your cart!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DaftarCart(
                            cart = statusUiCart.cart,
                            onRemove = onRemove,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            shadowElevation = 12.dp,
                            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                val totalAmount = statusUiCart.cart.sumOf { it.subtotal }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Amount",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = Color(0xFF666666),
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Rp $totalAmount",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontSize = 24.sp
                                            ),
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFD4A574)
                                        )
                                    }

                                    Text(
                                        text = "${statusUiCart.cart.size} item(s)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFF999999)
                                    )
                                }
                                Button(
                                    onClick = onCheckout,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF6B4E3D)
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 4.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ShoppingCart,
                                        contentDescription = null,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = stringResource(R.string.cart_checkout),
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
    }
}

@Composable
fun DaftarCart(
    cart: List<DataCartItem>,
    onRemove: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(items = cart, key = { it.cartId }) { item ->
            ItemCart(
                item = item,
                onRemove = { onRemove(item.cartId) }
            )
        }
    }
}

@Composable
fun ItemCart(
    item: DataCartItem,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
                text = item.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 17.sp
                ),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C2C2C)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFFFFF8E7),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Qty: ${item.qty}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6B4E3D),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
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

            Divider(
                color = Color(0xFFEEEEEE),
                thickness = 1.dp
            )

            // Remove Button
            OutlinedButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 1.5.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.cart_remove_item),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
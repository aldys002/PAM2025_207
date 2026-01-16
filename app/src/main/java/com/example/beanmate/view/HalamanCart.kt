package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.data.DataCartItem
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.StatusUiCart
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver


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
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (statusUiCart) {
            is StatusUiCart.Loading -> {
                CircularProgressIndicator()
            }

            is StatusUiCart.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.error))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is StatusUiCart.Success -> {
                if (statusUiCart.cart.isEmpty()) {
                    Text(text = stringResource(R.string.cart_empty))
                } else {
                    Column(modifier = Modifier.fillMaxSize()) {

                        DaftarCart(
                            cart = statusUiCart.cart,
                            onRemove = onRemove,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(text = stringResource(R.string.cart_checkout))
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
        modifier = modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Qty: ${item.qty}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Subtotal: Rp ${item.subtotal}")

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.cart_remove_item))
            }
        }
    }
}

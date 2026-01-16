package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beanmate2.R
import com.example.beanmate2.data.DataBeanMate
import com.example.beanmate2.view.route.DestinasiKelolaProduk
import com.example.beanmate2.view.viewmodel.KelolaProdukViewModel
import com.example.beanmate2.view.viewmodel.StatusUiProduk
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanKelolaProduk(
    navigateBack: () -> Unit,
    navigateToEntry: () -> Unit,
    navigateToEdit: (Int) -> Unit,
    onProdukChanged: () -> Unit,
    refreshTrigger: Boolean,
    onRefreshConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: KelolaProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var showConfirmDialog by remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }

    LaunchedEffect(Unit) {
        viewModel.loadProduk()
    }
    LaunchedEffect(refreshTrigger) {
        if (refreshTrigger) {
            viewModel.loadProduk()
            onRefreshConsumed()
        }
    }

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(DestinasiKelolaProduk.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToEntry,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_m))
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->

        KelolaProdukBody(
            statusUiProduk = viewModel.statusUiProduk,
            onEditClick = navigateToEdit,
            onDeleteClick = { id ->
                showConfirmDialog = true to id
            },
            retryAction = viewModel::loadProduk,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
        if (showConfirmDialog.first) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false to null },
                title = { Text("Konfirmasi Hapus") },
                text = { Text("Anda yakin ingin menghapus produk ini?") },
                confirmButton = {
                    Button(onClick = {
                        showConfirmDialog.second?.let { id ->
                            viewModel.hapusProduk(id)
                        }
                        showConfirmDialog = false to null
                    }) {
                        Text("Ya")
                    }
                },
                dismissButton = {
                    Button(onClick = { showConfirmDialog = false to null }) {
                        Text("Tidak")
                    }
                }
            )
        }
    }
}

@Composable
fun KelolaProdukBody(
    statusUiProduk: StatusUiProduk,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (statusUiProduk) {
            is StatusUiProduk.Loading -> {
                CircularProgressIndicator()
            }

            is StatusUiProduk.Error -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = stringResource(R.string.error))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is StatusUiProduk.Success -> {
                if (statusUiProduk.produk.isEmpty()) {
                    Text("-")
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        statusUiProduk.produk.forEach { item ->
                            ItemKelolaProduk(
                                produk = item,
                                onEdit = { onEditClick(item.id) },
                                onDelete = { onDeleteClick(item.id) }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemKelolaProduk(
    produk: DataBeanMate,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"
    val imageUrl = baseUploads + produk.image

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = produk.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = produk.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Rp ${produk.price}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = produk.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update")
                    }

                    Button(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Hapus")
                    }
                }
            }
        }
    }
}
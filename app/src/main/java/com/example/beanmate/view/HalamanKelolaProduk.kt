package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                containerColor = Color(0xFFD4A574),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_m))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    modifier = Modifier.size(28.dp)
                )
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
                title = {
                    Text(
                        text = "Konfirmasi Hapus",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2C2C)
                    )
                },
                text = {
                    Text(
                        text = "Anda yakin ingin menghapus produk ini?",
                        color = Color(0xFF666666)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog.second?.let { id ->
                                viewModel.hapusProduk(id)
                            }
                            showConfirmDialog = false to null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Ya",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showConfirmDialog = false to null },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Tidak",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
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
        when (statusUiProduk) {
            is StatusUiProduk.Loading -> {
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

            is StatusUiProduk.Error -> {
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

            is StatusUiProduk.Success -> {
                if (statusUiProduk.produk.isEmpty()) {
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
                                imageVector = Icons.Default.Inventory,
                                contentDescription = null,
                                tint = Color(0xFF6B4E3D),
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(20.dp)
                            )
                        }
                        Text(
                            text = "Belum ada produk",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tekan tombol + untuk menambah produk",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        statusUiProduk.produk.forEach { item ->
                            ItemKelolaProduk(
                                produk = item,
                                onEdit = { onEditClick(item.id) },
                                onDelete = { onDeleteClick(item.id) }
                            )
                        }
                        Spacer(modifier = Modifier.height(60.dp))
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
        modifier = modifier
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
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
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
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = produk.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 17.sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Surface(
                    color = Color(0xFFFFF8E7),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Rp ${produk.price}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD4A574),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
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

                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onEdit,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6B4E3D)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Update",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Button(
                        onClick = onDelete,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Hapus",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
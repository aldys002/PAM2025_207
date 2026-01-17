package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.beanmate2.R
import com.example.beanmate2.data.DetailBeanMate
import com.example.beanmate2.data.UIStateBeanMate
import com.example.beanmate2.view.route.DestinasiEntryProduk
import com.example.beanmate2.view.viewmodel.EntryProdukViewModel
import com.example.beanmate2.view.viewmodel.StatusUiUploads
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntryProduk(
    navigateBack: () -> Unit,
    onProdukChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(Unit) {
        viewModel.loadUploads()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(DestinasiEntryProduk.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        EntryProdukBody(
            uiStateProduk = viewModel.uiStateProduk,
            statusUiUploads = viewModel.statusUiUploads,
            onReloadUploads = viewModel::loadUploads,
            onPilihGambar = viewModel::pilihGambar,
            onProdukValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    val sukses = viewModel.addProduk()
                    if (sukses) navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryProdukBody(
    uiStateProduk: UIStateBeanMate,
    statusUiUploads: StatusUiUploads,
    onReloadUploads: () -> Unit,
    onPilihGambar: (String) -> Unit,
    onProdukValueChange: (DetailBeanMate) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .padding(20.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFBF5),
                        Color(0xFFFFF8E7)
                    )
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {

        FormTambahProduk(
            detail = uiStateProduk.detailBeanMate,
            onValueChange = onProdukValueChange,
            modifier = Modifier.fillMaxWidth(),
            statusUiUploads = statusUiUploads,
            onPilihGambar = onPilihGambar,
            onReloadUploads = onReloadUploads
        )

        Button(
            onClick = onSaveClick,
            enabled = uiStateProduk.isEntryValid,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6B4E3D),
                disabledContainerColor = Color(0xFF6B4E3D).copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp
            )
        ) {
            Text(
                text = stringResource(R.string.save),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun FormTambahProduk(
    detail: DetailBeanMate,
    modifier: Modifier = Modifier,
    onValueChange: (DetailBeanMate) -> Unit = {},
    enabled: Boolean = true,
    statusUiUploads: StatusUiUploads,
    onPilihGambar: (String) -> Unit,
    onReloadUploads: () -> Unit
) {
    var dialogPilihGambar by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = detail.name,
            onValueChange = { onValueChange(detail.copy(name = it)) },
            label = { Text(stringResource(R.string.product_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DriveFileRenameOutline,
                    contentDescription = null,
                    tint = Color(0xFFD4A574)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD4A574),
                focusedLabelColor = Color(0xFFD4A574),
                cursorColor = Color(0xFFD4A574)
            )
        )
        OutlinedTextField(
            value = if (detail.price == 0) "" else detail.price.toString(),
            onValueChange = {
                val angka = it.toIntOrNull() ?: 0
                onValueChange(detail.copy(price = angka))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.product_price)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = null,
                    tint = Color(0xFFD4A574)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD4A574),
                focusedLabelColor = Color(0xFFD4A574),
                cursorColor = Color(0xFFD4A574)
            )
        )
        OutlinedTextField(
            value = detail.description,
            onValueChange = { onValueChange(detail.copy(description = it)) },
            label = { Text(stringResource(R.string.product_description)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFFD4A574)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            enabled = enabled,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFD4A574),
                focusedLabelColor = Color(0xFFD4A574),
                cursorColor = Color(0xFFD4A574)
            ),
            maxLines = 4
        )
        Surface(
            color = Color(0xFFFFF8E7),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (detail.image.isNotBlank()) {
                    Text(
                        text = "Selected Image:",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF666666),
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = detail.image,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2C2C2C),
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "No image selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF999999)
                    )
                }

                OutlinedButton(
                    onClick = { dialogPilihGambar = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = enabled,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6B4E3D)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.form_choose_image),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (enabled) {
            Surface(
                color = Color(0xFFFFF3E0).copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.msg_field_required),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8B6F47),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    if (dialogPilihGambar) {
        AlertDialog(
            onDismissRequest = { dialogPilihGambar = false },
            title = {
                Text(
                    text = stringResource(R.string.form_choose_image),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
            },
            text = {
                when (statusUiUploads) {
                    is StatusUiUploads.Idle -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "No images loaded yet",
                                color = Color(0xFF666666)
                            )
                            Button(
                                onClick = onReloadUploads,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD4A574)
                                )
                            ) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }

                    is StatusUiUploads.Loading -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = Color(0xFFD4A574)
                                )
                                Text(
                                    text = "Loading images...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF666666)
                                )
                            }
                        }
                    }

                    is StatusUiUploads.Error -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = statusUiUploads.msg,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = onReloadUploads,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD4A574)
                                )
                            ) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }

                    is StatusUiUploads.Success -> {
                        val files = statusUiUploads.files
                        val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.height(360.dp)
                        ) {
                            items(files, key = { it }) { filename ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onPilihGambar(filename)
                                            dialogPilihGambar = false
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        AsyncImage(
                                            model = baseUploads + filename,
                                            contentDescription = filename,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFF5F5F5)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Text(
                                            text = filename,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp
                                            ),
                                            maxLines = 2,
                                            textAlign = TextAlign.Center,
                                            color = Color(0xFF666666)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { dialogPilihGambar = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF6B4E3D)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
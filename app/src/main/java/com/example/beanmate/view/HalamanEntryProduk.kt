package com.example.beanmate2.view

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_l)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_m))
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
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save))
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
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_m))
    ) {
        OutlinedTextField(
            value = detail.name,
            onValueChange = { onValueChange(detail.copy(name = it)) },
            label = { Text(stringResource(R.string.product_name)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = if (detail.price == 0) "" else detail.price.toString(),
            onValueChange = {
                val angka = it.toIntOrNull() ?: 0
                onValueChange(detail.copy(price = angka))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text(stringResource(R.string.product_price)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = detail.description,
            onValueChange = { onValueChange(detail.copy(description = it)) },
            label = { Text(stringResource(R.string.product_description)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
        OutlinedTextField(
            value = detail.image,
            onValueChange = {},
            label = { Text(stringResource(R.string.form_choose_image)) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled) { dialogPilihGambar = true },
            enabled = false,
            readOnly = true,
            singleLine = true
        )

        Button(
            onClick = { dialogPilihGambar = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text(text = stringResource(R.string.form_choose_image))
        }

        if (enabled) {
            Text(
                text = stringResource(R.string.msg_field_required),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_m))
            )
        }

        Divider(
            thickness = dimensionResource(id = R.dimen.padding_s),
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.padding_m))
        )
    }

    if (dialogPilihGambar) {
        AlertDialog(
            onDismissRequest = { dialogPilihGambar = false },
            title = { Text(text = stringResource(R.string.form_choose_image)) },
            text = {
                when (statusUiUploads) {
                    is StatusUiUploads.Idle -> {
                        // kalau idle, biasanya belum load — kasih tombol
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "-")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onReloadUploads) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }

                    is StatusUiUploads.Loading -> {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is StatusUiUploads.Error -> {
                        Column {
                            Text(text = statusUiUploads.msg)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = onReloadUploads) {
                                Text(text = stringResource(R.string.retry))
                            }
                        }
                    }

                    is StatusUiUploads.Success -> {
                        val files = statusUiUploads.files

                        val baseUploads = "http://10.0.2.2/beanmate/api/uploads/"

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.height(320.dp)
                        ) {
                            items(files, key = { it }) { filename ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onPilihGambar(filename)
                                            dialogPilihGambar = false
                                        },
                                    elevation = CardDefaults.cardElevation(2.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AsyncImage(
                                            model = baseUploads + filename,
                                            contentDescription = filename,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(MaterialTheme.shapes.small),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            text = filename,
                                            style = MaterialTheme.typography.labelSmall,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogPilihGambar = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

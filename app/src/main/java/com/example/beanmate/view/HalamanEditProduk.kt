package com.example.beanmate2.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.data.DetailBeanMate
import com.example.beanmate2.data.UIStateBeanMate
import com.example.beanmate2.view.route.DestinasiEditProduk
import com.example.beanmate2.view.viewmodel.EditProdukViewModel
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEditProduk(
    navigateBack: () -> Unit,
    onProdukChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProdukViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(DestinasiEditProduk.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        EditProdukBody(
            uiStateProduk = viewModel.uiStateProduk,
            onProdukValueChange = viewModel::updateUiState,
            onUpdateClick = {
                coroutineScope.launch {
                    val sukses = viewModel.editProduk()
                    if (sukses) {
                        onProdukChanged()
                        navigateBack()
                    }
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
fun EditProdukBody(
    uiStateProduk: UIStateBeanMate,
    onProdukValueChange: (DetailBeanMate) -> Unit,
    onUpdateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_l)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_m))
    ) {
        FormEditProduk(
            detail = uiStateProduk.detailBeanMate,
            onValueChange = onProdukValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onUpdateClick,
            enabled = uiStateProduk.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.update))
        }
    }
}

@Composable
fun FormEditProduk(
    detail: DetailBeanMate,
    modifier: Modifier = Modifier,
    onValueChange: (DetailBeanMate) -> Unit = {},
    enabled: Boolean = true
) {
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
            onValueChange = { onValueChange(detail.copy(image = it)) },
            label = { Text(stringResource(R.string.form_choose_image)) },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

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
}

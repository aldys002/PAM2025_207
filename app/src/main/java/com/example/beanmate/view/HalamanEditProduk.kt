package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        FormEditProduk(
            detail = uiStateProduk.detailBeanMate,
            onValueChange = onProdukValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onUpdateClick,
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
                text = stringResource(R.string.update),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
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
        OutlinedTextField(
            value = detail.image,
            onValueChange = { onValueChange(detail.copy(image = it)) },
            label = { Text(stringResource(R.string.form_choose_image)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Image,
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
}
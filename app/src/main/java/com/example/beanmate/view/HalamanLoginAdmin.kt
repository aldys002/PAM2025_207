package com.example.beanmate2.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beanmate2.R
import com.example.beanmate2.view.viewmodel.LoginAdminViewModel
import com.example.beanmate2.view.viewmodel.provider.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanLoginAdmin(
    navigateBack: () -> Unit,
    navigateToKelolaProduk: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginAdminViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiStateAdmin

    Scaffold(
        topBar = {
            BeanMateTopAppBar(
                title = stringResource(R.string.admin_login_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = uiState.detailAdmin.username,
                onValueChange = {
                    viewModel.updateUiState(
                        uiState.detailAdmin.copy(username = it)
                    )
                },
                label = { Text(stringResource(R.string.admin_username)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.detailAdmin.password,
                onValueChange = {
                    viewModel.updateUiState(
                        uiState.detailAdmin.copy(password = it)
                    )
                },
                label = { Text(stringResource(R.string.admin_password)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    scope.launch {
                        val sukses = viewModel.loginAdmin()
                        if (sukses) {
                            navigateToKelolaProduk()
                        }
                    }
                },
                enabled = uiState.isEntryValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.admin_login_button))
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.loginMessage.isNotBlank()) {
                Text(
                    text = viewModel.loginMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

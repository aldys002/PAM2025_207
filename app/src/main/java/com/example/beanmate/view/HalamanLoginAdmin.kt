package com.example.beanmate2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF8E7),
                            Color(0xFFFFF0D6)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFD4A574).copy(alpha = 0.15f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin",
                            tint = Color(0xFF6B4E3D),
                            modifier = Modifier
                                .size(72.dp)
                                .padding(16.dp)
                        )
                    }

                    Text(
                        text = "Admin Login",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            letterSpacing = 0.5.sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2C2C)
                    )

                    Text(
                        text = "Enter your credentials to continue",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.detailAdmin.username,
                        onValueChange = {
                            viewModel.updateUiState(
                                uiState.detailAdmin.copy(username = it)
                            )
                        },
                        label = { Text(stringResource(R.string.admin_username)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username",
                                tint = Color(0xFFD4A574)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD4A574),
                            focusedLabelColor = Color(0xFFD4A574),
                            cursorColor = Color(0xFFD4A574)
                        ),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.detailAdmin.password,
                        onValueChange = {
                            viewModel.updateUiState(
                                uiState.detailAdmin.copy(password = it)
                            )
                        },
                        label = { Text(stringResource(R.string.admin_password)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password",
                                tint = Color(0xFFD4A574)
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD4A574),
                            focusedLabelColor = Color(0xFFD4A574),
                            cursorColor = Color(0xFFD4A574)
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6B4E3D),
                            disabledContainerColor = Color(0xFF6B4E3D).copy(alpha = 0.5f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 2.dp
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.admin_login_button),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                    if (viewModel.loginMessage.isNotBlank()) {
                        Surface(
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = viewModel.loginMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(12.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
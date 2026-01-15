package com.example.beanmate2.view.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.beanmate2.data.DetailAdmin
import com.example.beanmate2.data.UIStateAdmin
import com.example.beanmate2.repositori.RepositoryBeanMate

class LoginAdminViewModel(
    private val repositoryBeanMate: RepositoryBeanMate
) : ViewModel() {

    var uiStateAdmin by mutableStateOf(UIStateAdmin())
        private set

    var loginMessage by mutableStateOf("")
        private set

    private fun validasiInput(
        detailAdmin: DetailAdmin = uiStateAdmin.detailAdmin
    ): Boolean {
        return detailAdmin.username.isNotBlank() &&
                detailAdmin.password.isNotBlank()
    }

    fun updateUiState(detailAdmin: DetailAdmin) {
        uiStateAdmin = UIStateAdmin(
            detailAdmin = detailAdmin,
            isEntryValid = validasiInput(detailAdmin)
        )
    }

    suspend fun loginAdmin(): Boolean {
        if (!validasiInput()) {
            loginMessage = "Username dan password wajib diisi"
            return false
        }

        return try {
            val response = repositoryBeanMate.loginAdmin(
                uiStateAdmin.detailAdmin.username,
                uiStateAdmin.detailAdmin.password
            )
            if (response.status == "success") {
                loginMessage = "Login berhasil"
                true
            } else {
                loginMessage = response.message
                false
            }
        } catch (e: Exception) {
            loginMessage = "Gagal terhubung ke server"
            false
        }
    }
}

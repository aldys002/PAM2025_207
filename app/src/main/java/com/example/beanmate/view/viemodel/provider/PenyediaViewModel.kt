package com.example.beanmate2.view.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beanmate2.repositori.AplikasiBeanMate
import com.example.beanmate2.view.viewmodel.CartViewModel
import com.example.beanmate2.view.viewmodel.CheckoutViewModel
import com.example.beanmate2.view.viewmodel.DetailProdukViewModel
import com.example.beanmate2.view.viewmodel.EditProdukViewModel
import com.example.beanmate2.view.viewmodel.EntryProdukViewModel
import com.example.beanmate2.view.viewmodel.HomeViewModel
import com.example.beanmate2.view.viewmodel.KelolaProdukViewModel
import com.example.beanmate2.view.viewmodel.LoginAdminViewModel
import com.example.beanmate2.view.viewmodel.OrderDetailViewModel
import com.example.beanmate2.view.viewmodel.OrdersViewModel
import com.example.beanmate2.view.viewmodel.SplashViewModel

fun CreationExtras.aplikasiBeanMate(): AplikasiBeanMate =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiBeanMate)

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer { SplashViewModel() }
        initializer { HomeViewModel(aplikasiBeanMate().container.repositoryBeanMate) }
        initializer { LoginAdminViewModel(aplikasiBeanMate().container.repositoryBeanMate) }
        initializer { KelolaProdukViewModel(aplikasiBeanMate().container.repositoryBeanMate) }
        initializer {
            CartViewModel(aplikasiBeanMate().container.repositoryBeanMate)
        }
        initializer { CheckoutViewModel(aplikasiBeanMate().container.repositoryBeanMate) }

        initializer { OrdersViewModel(aplikasiBeanMate().container.repositoryBeanMate) }
        initializer {
            OrderDetailViewModel(
                this.createSavedStateHandle(),
                aplikasiBeanMate().container.repositoryBeanMate
            )
        }
        initializer { EntryProdukViewModel(aplikasiBeanMate().container.repositoryBeanMate) }

        initializer {
            EditProdukViewModel(
                this.createSavedStateHandle(),
                aplikasiBeanMate().container.repositoryBeanMate
            )
        }

        initializer {
            DetailProdukViewModel(
                this.createSavedStateHandle(),
                aplikasiBeanMate().container.repositoryBeanMate
            )
        }


    }
}


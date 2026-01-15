package com.example.beanmate2.view.uicontroller

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.beanmate2.view.*
import com.example.beanmate2.view.route.*

@Composable
fun BeanMateApp(
    navController: NavHostController = rememberNavController(),
    modifier: androidx.compose.ui.Modifier
) {
    HostNavigasi(navController = navController, modifier = modifier)
}

@Composable
fun HostNavigasi(
    navController: NavHostController,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiSplash.route,
        modifier = modifier
    ) {

        composable(DestinasiSplash.route) {
            HalamanSplash(
                navigateToHome = {
                    navController.navigate(DestinasiHome.route) {
                        popUpTo(DestinasiSplash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(DestinasiHome.route) {
            val entry = navController.currentBackStackEntry!!
            val refreshHome by entry.savedStateHandle
                .getStateFlow("refresh_home", false)
                .collectAsState()

            HalamanHome(
                navigateToLoginAdmin = { navController.navigate(DestinasiLoginAdmin.route) },
                navigateToDetail = { id ->
                    navController.navigate("${DestinasiDetailProduk.route}/$id")
                },
                navigateToCart = { navController.navigate(DestinasiCart.route) }, // ✅ tambahkan ini
                refreshTrigger = refreshHome,
                onRefreshConsumed = { entry.savedStateHandle["refresh_home"] = false }
            )
        }

        composable(
            route = DestinasiDetailProduk.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailProduk.itemIdArg) { type = NavType.IntType })
        ) {
            HalamanDetailProduk(navigateBack = { navController.navigateUp() })
        }

        composable(DestinasiLoginAdmin.route) {
            HalamanLoginAdmin(
                navigateBack = { navController.navigateUp() },
                navigateToKelolaProduk = {
                    navController.navigate(DestinasiKelolaProduk.route) {
                        popUpTo(DestinasiLoginAdmin.route) { inclusive = true }
                    }
                }
            )
        }

        composable(DestinasiKelolaProduk.route) {
            val entry = navController.currentBackStackEntry!!
            val refreshProduk by entry.savedStateHandle
                .getStateFlow("refresh_produk", false)
                .collectAsState()

            HalamanKelolaProduk(
                navigateBack = { navController.navigateUp() },
                navigateToEntry = { navController.navigate(DestinasiEntryProduk.route) },
                navigateToEdit = { id -> navController.navigate("${DestinasiEditProduk.route}/$id") },
                onProdukChanged = {
                    navController.getBackStackEntry(DestinasiHome.route)
                        .savedStateHandle["refresh_home"] = true
                },
                refreshTrigger = refreshProduk,
                onRefreshConsumed = { entry.savedStateHandle["refresh_produk"] = false }
            )
        }

        composable(DestinasiEntryProduk.route) {
            HalamanEntryProduk(
                navigateBack = { navController.navigateUp() },
                onProdukChanged = {
                    // refresh home
                    navController.getBackStackEntry(DestinasiHome.route)
                        .savedStateHandle["refresh_home"] = true

                    // refresh kelola
                    runCatching {
                        navController.getBackStackEntry(DestinasiKelolaProduk.route)
                            .savedStateHandle["refresh_produk"] = true
                    }
                }
            )
        }

        composable(
            route = DestinasiEditProduk.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditProduk.itemIdArg) { type = NavType.IntType })
        ) {
            HalamanEditProduk(
                navigateBack = { navController.navigateUp() },
                onProdukChanged = {
                    // refresh home
                    navController.getBackStackEntry(DestinasiHome.route)
                        .savedStateHandle["refresh_home"] = true

                    // refresh kelola
                    runCatching {
                        navController.getBackStackEntry(DestinasiKelolaProduk.route)
                            .savedStateHandle["refresh_produk"] = true
                    }
                }
            )
        }

        composable(DestinasiCart.route) {
            val entry = navController.currentBackStackEntry!!
            val refreshCart by entry.savedStateHandle
                .getStateFlow("refresh_cart", false)
                .collectAsState()

            HalamanCart(
                navigateBack = { navController.navigateUp() },
                navigateToCheckout = { navController.navigate(DestinasiCheckout.route) },
                refreshTrigger = refreshCart,
                onRefreshConsumed = { entry.savedStateHandle["refresh_cart"] = false }
            )
        }

        composable(DestinasiCheckout.route) {
            HalamanCheckout(
                navigateBack = { navController.navigateUp() },
                navigateToOrders = { navController.navigate(DestinasiOrders.route) },
                navigateToOrderDetail = { orderId ->
                    navController.navigate("${DestinasiOrderDetail.route}/$orderId")
                },
                onCheckoutSuccess = {
                    runCatching {
                        navController.getBackStackEntry(DestinasiOrders.route)
                            .savedStateHandle["refresh_orders"] = true
                    }

                    runCatching {
                        navController.getBackStackEntry(DestinasiCart.route)
                            .savedStateHandle["refresh_cart"] = true
                    }
                }
            )
        }

        composable(DestinasiOrders.route) {
            val entry = navController.currentBackStackEntry!!
            val refreshOrders by entry.savedStateHandle
                .getStateFlow("refresh_orders", false)
                .collectAsState()

            HalamanOrders(
                navigateBack = { navController.navigateUp() },
                navigateToOrderDetail = { id -> navController.navigate("${DestinasiOrderDetail.route}/$id") },
                refreshTrigger = refreshOrders,
                onRefreshConsumed = { entry.savedStateHandle["refresh_orders"] = false }
            )
        }

        composable(
            route = DestinasiOrderDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiOrderDetail.itemIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt(DestinasiOrderDetail.itemIdArg) ?: 0
            HalamanOrderDetail(
                navigateBack = { navController.navigateUp() },
                orderId = orderId
            )
        }

    }
}

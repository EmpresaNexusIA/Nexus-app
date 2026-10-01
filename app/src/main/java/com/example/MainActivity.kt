package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.AssistantScreen
import com.example.ui.screens.DemoScreen
import com.example.ui.screens.FaqScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.PricingScreen
import com.example.ui.theme.NexoraCyanNeon
import com.example.ui.theme.NexoraNavyBackground
import com.example.ui.theme.NexoraOnSurfaceVariant
import com.example.ui.theme.NexoraOutline
import com.example.ui.theme.NexoraTheme

enum class AppTab(val title: String, val icon: ImageVector) {
    INICIO("Inicio", Icons.Default.Storefront),
    ASISTENTE("Asistente", Icons.Default.AutoAwesome),
    DEMO("Demo", Icons.Default.ShoppingBag),
    PRECIOS("Precios", Icons.Default.Payments),
    FAQ("FAQ", Icons.Default.Help)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexoraTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    var currentTab by remember { mutableStateOf(AppTab.INICIO) }

    // Android back handler to pop back to INICIO screen when on other tabs
    BackHandler(enabled = currentTab != AppTab.INICIO) {
        currentTab = AppTab.INICIO
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NexoraNavyBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NexoraBottomNav(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        }
    ) { innerPadding ->
        when (currentTab) {
            AppTab.INICIO -> LandingScreen(
                innerPadding = innerPadding,
                onNavigateToDemo = { currentTab = AppTab.DEMO },
                onNavigateToAssistant = { currentTab = AppTab.ASISTENTE }
            )
            AppTab.ASISTENTE -> AssistantScreen(innerPadding = innerPadding)
            AppTab.DEMO -> DemoScreen(innerPadding = innerPadding)
            AppTab.PRECIOS -> PricingScreen(innerPadding = innerPadding)
            AppTab.FAQ -> FaqScreen(innerPadding = innerPadding)
        }
    }
}

@Composable
fun NexoraBottomNav(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF151B2B).copy(alpha = 0.96f))
            .border(
                width = 1.dp,
                color = Color(0x3000F0FF),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppTab.values().forEach { tab ->
                val isSelected = tab == currentTab
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onTabSelected(tab) }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("tab_${tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        tint = if (isSelected) NexoraCyanNeon else NexoraOutline,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = tab.title,
                        color = if (isSelected) NexoraCyanNeon else NexoraOnSurfaceVariant,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

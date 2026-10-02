package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.NexoraCyanNeon
import com.example.ui.theme.NexoraGoldSecondary
import com.example.ui.theme.NexoraOnSurface
import com.example.ui.theme.NexoraOnSurfaceVariant
import com.example.ui.theme.NexoraOutline
import com.example.ui.theme.WhatsAppGreen
import com.example.util.WhatsAppHelper

data class CatalogItem(
    val id: String,
    val name: String,
    val category: String,
    val price: Int,
    val formattedPrice: String,
    val imageRes: Int,
    val description: String = ""
)

val SampleCatalogItems = listOf(
    CatalogItem(
        id = "pan",
        name = "Pan casero",
        category = "Panadería",
        price = 1200,
        formattedPrice = "$1.200",
        imageRes = R.drawable.img_pan_frances,
        description = "Horneado del día con masa madre"
    ),
    CatalogItem(
        id = "queso",
        name = "Queso cremoso",
        category = "Lácteos",
        price = 1800,
        formattedPrice = "$1.800",
        imageRes = R.drawable.img_queso,
        description = "El kilo, ideal para pizzas y tartas"
    ),
    CatalogItem(
        id = "medialunas",
        name = "Medialunas ×6",
        category = "Panadería",
        price = 1400,
        formattedPrice = "$1.400",
        imageRes = R.drawable.img_medialunas,
        description = "Dulces de manteca con almíbar"
    ),
    CatalogItem(
        id = "cafe",
        name = "Café tostado 1/2 kg",
        category = "Cafetería",
        price = 2900,
        formattedPrice = "$2.900",
        imageRes = R.drawable.img_cafe,
        description = "Grano entero o molido a elección"
    ),
    CatalogItem(
        id = "mermelada",
        name = "Mermelada artesanal",
        category = "Almacén",
        price = 1500,
        formattedPrice = "$1.500",
        imageRes = R.drawable.img_mermelada,
        description = "Frutos rojos de estación sin conservantes"
    ),
    CatalogItem(
        id = "yerba",
        name = "Yerba orgánica 1kg",
        category = "Almacén",
        price = 2200,
        formattedPrice = "$2.200",
        imageRes = R.drawable.img_yerba,
        description = "Estacionamiento natural bajo en polvo"
    )
)

@Composable
fun ProductGrid(
    items: List<CatalogItem> = SampleCatalogItems,
    modifier: Modifier = Modifier,
    minColumnWidth: Dp = 156.dp,
    gridHeight: Dp? = 480.dp,
    onItemOrdered: (CatalogItem) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember(items) {
        listOf("Todos") + items.map { it.category }.distinct()
    }

    val filteredItems = remember(items, selectedCategory, searchQuery) {
        items.filter { item ->
            val matchCategory = selectedCategory == "Todos" || item.category == selectedCategory
            val matchSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
            matchCategory && matchSearch
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Search & Filter header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar en el catálogo...",
                        color = NexoraOutline,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = NexoraCyanNeon,
                        modifier = Modifier.size(16.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NexoraCyanNeon,
                    unfocusedBorderColor = Color(0x3000F0FF),
                    focusedContainerColor = Color(0xFF151B2B),
                    unfocusedContainerColor = Color(0xFF151B2B),
                    focusedTextColor = NexoraOnSurface,
                    unfocusedTextColor = NexoraOnSurface
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("catalog_search_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF151B2B))
                    .border(1.dp, Color(0x3000F0FF), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 13.dp)
            ) {
                Text(
                    text = "${filteredItems.size} ítems",
                    color = NexoraGoldSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = cat == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(
                            if (isSelected) NexoraCyanNeon.copy(alpha = 0.2f) else Color(0xFF151B2B)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) NexoraCyanNeon else Color(0x2500F0FF),
                            shape = RoundedCornerShape(100.dp)
                        )
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .testTag("category_chip_${cat.lowercase()}")
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) NexoraCyanNeon else NexoraOnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        // Responsive LazyVerticalGrid
        val gridModifier = if (gridHeight != null) {
            Modifier
                .fillMaxWidth()
                .height(gridHeight)
        } else {
            Modifier.fillMaxWidth()
        }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = minColumnWidth),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = gridModifier.testTag("responsive_product_grid")
        ) {
            items(filteredItems, key = { it.id }) { item ->
                ProductGridCard(
                    item = item,
                    onOrderClick = {
                        onItemOrdered(item)
                        val orderMsg = "¡Hola! Quiero pedir: ${item.name} (${item.formattedPrice})"
                        WhatsAppHelper.openWhatsApp(context, orderMsg)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductGridCard(
    item: CatalogItem,
    onOrderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberGlassCard(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${item.id}"),
        borderColor = Color(0x3500F0FF),
        backgroundColor = Color(0xFF191F2F),
        cornerRadius = 10.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Product Image Container with aspect ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.25f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF242A3A)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imageRes,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category tag badge overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF0D1322).copy(alpha = 0.85f))
                        .border(1.dp, Color(0x30FFFFFF), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        color = NexoraCyanNeon,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = item.name,
                color = NexoraOnSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Price in Monospace Gold
            Text(
                text = item.formattedPrice,
                color = NexoraGoldSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.2.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )

            if (item.description.isNotBlank()) {
                Text(
                    text = item.description,
                    color = NexoraOnSurfaceVariant,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // "Order via WhatsApp" Action Button (Green)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(WhatsAppGreen)
                    .clickable { onOrderClick() }
                    .padding(horizontal = 8.dp)
                    .testTag("order_whatsapp_btn_${item.id}"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Chat,
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF050811),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PEDIR POR WHATSAPP",
                        color = Color(0xFF050811),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        letterSpacing = 0.4.sp
                    )
                }
            }
        }
    }
}

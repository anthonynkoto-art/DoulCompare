package com.doulcompare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.doulcompare.app.model.ProduitUi
import com.doulcompare.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

private val FR = Locale.FRANCE
private fun fmtFcfa(n: Int): String = NumberFormat.getIntegerInstance(FR).format(n) + " FCFA"

val CATEGORY_ICON = mapOf(
    "Bebe" to "🍼", "Boissons" to "🥤", "Boulangerie Patisserie" to "🥖",
    "Entretien Maison" to "🧴", "Epicerie Salee" to "🍚", "Hygiene et Beaute" to "🧼",
    "Papeterie et Divers" to "📎", "Produits Frais" to "🥦", "Produits Laitiers" to "🧀",
    "Surgeles" to "🧊", "Viandes et Poissons" to "🍗"
)
val CATEGORY_LABEL = mapOf(
    "Bebe" to "Bébé", "Boissons" to "Boissons", "Boulangerie Patisserie" to "Boulangerie",
    "Entretien Maison" to "Entretien", "Epicerie Salee" to "Épicerie salée",
    "Hygiene et Beaute" to "Hygiène & beauté", "Papeterie et Divers" to "Papeterie",
    "Produits Frais" to "Produits frais", "Produits Laitiers" to "Laitiers",
    "Surgeles" to "Surgelés", "Viandes et Poissons" to "Viandes & poissons"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoulCompareApp(catalogue: List<ProduitUi>) {
    var query by remember { mutableStateOf("") }
    var activeCategory by remember { mutableStateOf("Toutes") }
    var selectedProduit by remember { mutableStateOf<ProduitUi?>(null) }

    val categories = remember(catalogue) {
        listOf("Toutes") + catalogue.map { it.categorie }.distinct().sorted()
    }

    val filtered = remember(catalogue, query, activeCategory) {
        catalogue.filter { p ->
            (activeCategory == "Toutes" || p.categorie == activeCategory) &&
                (query.isBlank() || p.nom.contains(query, ignoreCase = true))
        }
    }

    Scaffold(
        containerColor = Cream,
        bottomBar = { DoulBottomBar() }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            HeaderSection(query, onQueryChange = { query = it })

            LazyRow(
                Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(categories) { cat ->
                    CategoryChip(
                        label = if (cat == "Toutes") "🗂️ Toutes" else "${CATEGORY_ICON[cat] ?: "🛒"} ${CATEGORY_LABEL[cat] ?: cat}",
                        active = cat == activeCategory,
                        onClick = { activeCategory = cat }
                    )
                }
            }

            if (filtered.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun produit trouvé.\nEssaie une autre recherche.", color = Muted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    item {
                        val label = if (activeCategory == "Toutes") "Tous les produits" else (CATEGORY_LABEL[activeCategory] ?: activeCategory)
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Marine)
                            Text("${filtered.size} article${if (filtered.size > 1) "s" else ""}", fontSize = 11.5.sp, color = Muted)
                        }
                    }
                    items(filtered, key = { it.id }) { p ->
                        ProductCard(p, onClick = { selectedProduit = p })
                        Spacer(Modifier.height(10.dp))
                    }
                    item { Spacer(Modifier.height(70.dp)) }
                }
            }
        }
    }

    selectedProduit?.let { p ->
        ComparisonSheet(produit = p, onDismiss = { selectedProduit = null })
    }
}

@Composable
private fun HeaderSection(query: String, onQueryChange: (String) -> Unit) {
    Column(
        Modifier.fillMaxWidth().background(Marine).padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(9.dp).clip(CircleShape).background(Rouge))
                Spacer(Modifier.width(8.dp))
                Text("DoulCompare", color = Color.White, fontWeight = FontWeight.Black, fontSize = 19.sp)
            }
            Row(
                Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.09f))
                    .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Rouge, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text("Douala", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = Muted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Box(Modifier.weight(1f)) {
                if (query.isEmpty()) {
                    Text("Chercher un produit (ex: Coca-Cola, riz…)", color = Color(0xFFB4AFA6), fontSize = 14.sp)
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(color = Ink, fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, active: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (active) Marron else Color.White)
            .border(1.dp, if (active) Marron else LineColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 13.dp, vertical = 8.dp)
    ) {
        Text(label, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold, color = if (active) Color.White else Marine)
    }
}

@Composable
private fun ProductCard(p: ProduitUi, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .border(1.dp, LineColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(13.dp)
    ) {
        Box(
            Modifier.size(54.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFE8DD)),
            contentAlignment = Alignment.Center
        ) {
            Text(CATEGORY_ICON[p.categorie] ?: "🛒", fontSize = 24.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    Text(p.nom, fontWeight = FontWeight.SemiBold, fontSize = 13.5.sp, color = Ink, maxLines = 2)
                    Spacer(Modifier.height(2.dp))
                    Text("${p.sousCategorie} · ${p.unite}", fontSize = 11.sp, color = Muted)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(fmtFcfa(p.prixMin), fontWeight = FontWeight.Black, fontSize = 14.5.sp, color = Rouge)
                    Text("à partir de", fontSize = 9.5.sp, color = Muted)
                }
            }
            Spacer(Modifier.height(9.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(Good, Color(0xFFE0B23A), Rouge)))
            )
            Spacer(Modifier.height(5.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${p.offres.size} enseignes comparées", fontSize = 10.sp, color = Muted)
                val ecart = p.prixMax - p.prixMin
                Text("écart ${if (ecart > 0) fmtFcfa(ecart) else "—"}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = Marine)
            }
            if (p.aPromo) {
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier.clip(RoundedCornerShape(8.dp)).background(Color(0xFFFCEBE0)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text("🏷️ Promo disponible", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Marron)
                }
            }
        }
    }
}

@Composable
private fun DoulBottomBar() {
    NavigationBar(containerColor = Color.White, contentColor = Muted) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Text("🏠") }, label = { Text("Accueil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Rouge, selectedTextColor = Rouge, unselectedIconColor = Muted, unselectedTextColor = Muted, indicatorColor = Color.Transparent))
        NavigationBarItem(selected = false, onClick = {}, icon = { Text("🔎") }, label = { Text("Chercher", fontSize = 10.sp) })
        NavigationBarItem(selected = false, onClick = {}, icon = { Text("🏷️") }, label = { Text("Promos", fontSize = 10.sp) })
        NavigationBarItem(selected = false, onClick = {}, icon = { Text("👤") }, label = { Text("Compte", fontSize = 10.sp) })
    }
}

internal fun formatFcfa(n: Int) = fmtFcfa(n)

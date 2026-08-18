package com.doulcompare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.doulcompare.app.model.OffreUi
import com.doulcompare.app.model.ProduitUi
import com.doulcompare.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

private fun fmt(n: Int): String = NumberFormat.getIntegerInstance(Locale.FRANCE).format(n) + " FCFA"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonSheet(produit: ProduitUi, onDismiss: () -> Unit) {
    val offres = produit.offres.sortedBy { it.prix }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Cream
    ) {
        Column(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(horizontal = 20.dp).padding(bottom = 14.dp)) {
                Text(
                    (CATEGORY_LABEL[produit.categorie] ?: produit.categorie).uppercase(),
                    fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Marron
                )
                Spacer(Modifier.height(4.dp))
                Text(produit.nom, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Marine)
                Spacer(Modifier.height(2.dp))
                Text(produit.unite, fontSize = 12.sp, color = Muted)
            }
            HorizontalDivider(color = LineColor)

            LazyColumn(
                Modifier.padding(horizontal = 16.dp).padding(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                itemsIndexed(offres) { index, offre ->
                    OfferRow(index + 1, offre, isBest = index == 0)
                }
            }
        }
    }
}

@Composable
private fun OfferRow(rank: Int, offre: OffreUi, isBest: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (isBest) Color(0xFFF1F8F4) else Color.White)
            .border(1.dp, if (isBest) Good else LineColor, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(26.dp).clip(CircleShape).background(if (isBest) Good else Marine),
            contentAlignment = Alignment.Center
        ) {
            Text("$rank", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(offre.enseigne, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Ink)
            Text("${offre.quartier}, Douala", fontSize = 11.sp, color = Muted)
            Row(Modifier.padding(top = 5.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (offre.promo) Tag("Promo", Color(0xFFFCEBE0), Marron)
                if (!offre.disponible) Tag("Rupture", Color(0xFFF5E4E4), Rouge)
                if (isBest) Tag("Moins cher", Color(0xFFDDF0E4), Good)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(fmt(offre.prix), fontWeight = FontWeight.Black, fontSize = 15.sp, color = if (isBest) Good else Marine)
            offre.prixNormal?.let {
                Text(fmt(it), fontSize = 10.5.sp, color = Muted, textDecoration = TextDecoration.LineThrough)
            }
        }
    }
}

@Composable
private fun Tag(label: String, bg: Color, fg: Color) {
    Box(Modifier.clip(RoundedCornerShape(6.dp)).background(bg).padding(horizontal = 7.dp, vertical = 2.dp)) {
        Text(label, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = fg)
    }
}

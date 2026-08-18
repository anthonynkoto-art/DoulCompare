package com.doulcompare.app.model

/** Une offre de prix pour un produit dans une enseigne donnée. */
data class OffreUi(
    val enseigne: String,
    val quartier: String,
    val prix: Int,
    val prixNormal: Int?,
    val promo: Boolean,
    val disponible: Boolean
)

/** Un produit du catalogue, avec toutes ses offres comparées entre enseignes. */
data class ProduitUi(
    val id: String,
    val nom: String,
    val categorie: String,
    val sousCategorie: String,
    val unite: String,
    val offres: List<OffreUi>
) {
    val prixMin: Int get() = offres.minOfOrNull { it.prix } ?: 0
    val prixMax: Int get() = offres.maxOfOrNull { it.prix } ?: 0
    val aPromo: Boolean get() = offres.any { it.promo }
}

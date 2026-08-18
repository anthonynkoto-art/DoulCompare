package com.doulcompare.app.data

import android.content.Context
import com.doulcompare.app.model.OffreUi
import com.doulcompare.app.model.ProduitUi
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Charge le catalogue produit, les enseignes et la table de prix comparateur
 * depuis les fichiers JSON embarqués dans /assets (générés à partir de la
 * base réelle du supermarché + prix simulés multi-enseignes).
 */
object DataRepository {

    fun load(context: Context): List<ProduitUi> {
        val produitsJson = readAsset(context, "produits.json")
        val enseignesJson = readAsset(context, "enseignes.json")
        val prixJson = readAsset(context, "prix_comparateur.json")

        // id_enseigne -> Pair(nom, quartier)
        val enseignes = HashMap<String, Pair<String, String>>()
        val ensArr = JSONArray(enseignesJson)
        for (i in 0 until ensArr.length()) {
            val o = ensArr.getJSONObject(i)
            enseignes[o.getString("id_enseigne")] = Pair(o.getString("nom"), o.getString("quartier"))
        }

        // id_produit -> liste d'offres
        val offresParProduit = HashMap<String, MutableList<OffreUi>>()
        val prixArr = JSONArray(prixJson)
        for (i in 0 until prixArr.length()) {
            val o = prixArr.getJSONObject(i)
            val idProduit = o.getString("id_produit")
            val idEnseigne = o.getString("id_enseigne")
            val ens = enseignes[idEnseigne] ?: continue
            val offre = OffreUi(
                enseigne = ens.first,
                quartier = ens.second,
                prix = o.getInt("prix_fcfa"),
                prixNormal = if (o.isNull("prix_normal_fcfa")) null else o.getInt("prix_normal_fcfa"),
                promo = o.getBoolean("en_promotion"),
                disponible = o.getBoolean("disponible")
            )
            offresParProduit.getOrPut(idProduit) { mutableListOf() }.add(offre)
        }

        // Construction finale du catalogue
        val produits = mutableListOf<ProduitUi>()
        val prodArr = JSONArray(produitsJson)
        for (i in 0 until prodArr.length()) {
            val o = prodArr.getJSONObject(i)
            val id = o.getString("id_produit")
            val offres = offresParProduit[id] ?: continue
            if (offres.isEmpty()) continue
            produits.add(
                ProduitUi(
                    id = id,
                    nom = o.getString("nom"),
                    categorie = o.getString("categorie"),
                    sousCategorie = o.getString("sous_categorie"),
                    unite = o.getString("unite"),
                    offres = offres.sortedBy { it.prix }
                )
            )
        }
        return produits
    }

    private fun readAsset(context: Context, name: String): String {
        context.assets.open(name).use { input ->
            BufferedReader(InputStreamReader(input, Charsets.UTF_8)).use { reader ->
                return reader.readText()
            }
        }
    }
}

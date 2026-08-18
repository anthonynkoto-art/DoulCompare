# DoulCompare — Projet Android

Comparateur de prix multi-enseignes pour Douala. Application Kotlin / Jetpack
Compose, catalogue de 2313 produits, 5 enseignes, ~8800 offres de prix
embarquées (fichiers JSON dans `app/src/main/assets/`).

## Obtenir le fichier .apk (aucune installation requise)

1. Crée un dépôt sur GitHub (public ou privé) et pousse ce dossier tel quel :
   ```bash
   git init
   git add .
   git commit -m "DoulCompare - projet initial"
   git branch -M main
   git remote add origin https://github.com/TON-COMPTE/doulcompare.git
   git push -u origin main
   ```
2. Va dans l'onglet **Actions** de ton dépôt GitHub. Le workflow
   **"Build DoulCompare APK"** se déclenche automatiquement à chaque push
   (comptez 5 à 10 minutes).
3. Une fois le workflow terminé (coche verte), clique dessus puis descends
   jusqu'à **Artifacts** → télécharge **DoulCompare-debug-apk**. C'est un
   `.zip` contenant `app-debug.apk`.
4. Transfère ce `.apk` sur un téléphone Android (via un lien de téléchargement,
   WhatsApp, USB…), ouvre-le et autorise "Installer des applications inconnues"
   si demandé. L'app s'installe et se lance.

Aucune installation d'Android Studio n'est nécessaire pour cette méthode.

## Alternative : builder en local avec Android Studio

1. Installe [Android Studio](https://developer.android.com/studio).
2. Ouvre ce dossier comme projet existant.
3. Laisse Gradle synchroniser (ça télécharge les dépendances).
4. Menu **Build → Build Bundle(s)/APK(s) → Build APK(s)**.
5. L'APK apparaît dans `app/build/outputs/apk/debug/`.

## Important — build "debug"

L'APK généré est un **build debug**, installable directement sur un
téléphone mais pas destiné au Google Play Store. Pour publier sur le Play
Store, il faudrait signer un build "release" avec un certificat — étape
à faire quand tu seras prêt à publier officiellement, je peux t'accompagner
à ce moment-là.

## Où sont les données

- `app/src/main/assets/produits.json` — catalogue (2313 articles)
- `app/src/main/assets/enseignes.json` — les 5 points de vente comparés
- `app/src/main/assets/prix_comparateur.json` — le prix de chaque produit
  dans chaque enseigne (table centrale du comparateur)

Ces prix par enseigne sont actuellement **simulés** (générés à partir du prix
de référence de ton fichier source, qui ne contenait qu'un seul supermarché).
Pour passer à de vrais prix, il suffit de remplacer ce fichier JSON par de
vraies données au même format — aucune autre modification n'est nécessaire.

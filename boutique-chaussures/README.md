# ShoeBoutique — E-commerce de chaussures (Java / JSP / Servlets / MySQL)

Projet Java EE classique : Servlets + JSP/JSTL/EL, sans framework, pensé pour
JDK 20.0.2, Apache Tomcat 8.5.92 et MySQL 8.1 avec mysql-connector-j-8.0.32.

## 1. Arborescence

```
boutique-chaussures/
├── pom.xml
├── sql/schema.sql                      -> script de création de la base + données de démo
└── src/main/
    ├── java/com/boutique/
    │   ├── modele/     -> POJOs (Chaussure, Taille, Utilisateur, Commande, ...)
    │   ├── dao/        -> accès JDBC (PreparedStatement, transactions)
    │   ├── util/       -> ConnexionBD, PasswordUtil, CarteCreditValidator, CreerAdmin
    │   ├── filtre/      -> AuthFiltre (panier/commande), AdminFiltre (/admin/*)
    │   └── servlet/     -> pages publiques + servlet/admin -> back-office
    └── webapp/
        ├── WEB-INF/web.xml, WEB-INF/views/*.jsp (+ admin/, commun/)
        └── css/style.css
```

## 2. Base de données

1. Démarrer MySQL 8.1.
2. Exécuter le script :
   ```
   mysql -u root -p < sql/schema.sql
   ```
   Il crée la base `boutique_chaussures`, toutes les tables, et insère des
   données de démonstration (marques, 4 modèles de chaussures avec leurs
   tailles US/FR, 2 partenaires, 2 codes de carte cadeau Google de test).
3. Adapter si besoin les identifiants dans
   `src/main/java/com/boutique/util/ConnexionBD.java` (URL, utilisateur, mot de passe).

## 3. Créer le compte administrateur

Le mot de passe doit être haché (SHA-256 salé) avant d'être stocké : on ne peut
donc pas insérer l'admin directement en SQL. Utilise la classe utilitaire
`CreerAdmin` une fois le projet compilé :

```
javac -cp "target/classes:chemin/vers/mysql-connector-j-8.0.32.jar" -d target/classes $(find src/main/java -name "*.java")
java -cp "target/classes:chemin/vers/mysql-connector-j-8.0.32.jar" com.boutique.util.CreerAdmin admin@boutique.com MotDePasse1!
```

(remplace `:` par `;` sous Windows). Le compte est créé avec le rôle `ADMIN`.

## 4. Compiler et déployer

Avec Maven (le `pom.xml` déclare javax.servlet-api, javax.servlet.jsp-api,
jstl 1.2 et mysql-connector-j 8.0.32) :

```
mvn clean package
```

Cela génère `target/boutique-chaussures.war`. Copie ce fichier dans
`$CATALINA_HOME/webapps/` de ton Tomcat 8.5.92, puis démarre Tomcat. Le site
est ensuite accessible sur `http://localhost:8080/boutique-chaussures/`.

> Sans Maven : compile manuellement les `.java` avec javac en pointant vers
> les jars servlet-api, jsp-api, jstl-1.2.jar et mysql-connector-j-8.0.32.jar,
> place les `.class` dans `WEB-INF/classes`, copie ces jars (sauf
> servlet/jsp-api qui sont fournis par Tomcat) dans `WEB-INF/lib`, et
> assemble le tout dans un `.war`.

## 5. Fonctionnalités livrées

- **Rubriques publiques** : Accueil, Femmes, Hommes, Partenaires, Contact.
- **Chaussures** : modèle, marque, date de sortie, prix, tailles US **et**
  FR par modèle avec stock, collection initialisée sur `Aucune` par défaut.
- **Compte client** : inscription, connexion (session HttpSession),
  déconnexion. Mot de passe haché (SHA-256 + sel aléatoire), jamais stocké
  en clair.
- **Indicateur de force du mot de passe** (`PasswordUtil.evaluerForce`),
  affiché en direct en JS sur le formulaire d'inscription, basé sur 3
  critères : présence d'un chiffre, d'une majuscule, d'un caractère spécial
  (en plus du minimum de 8 caractères obligatoire).
- **Consentement cookies** : case à cocher lors de l'inscription/connexion,
  et bandeau persistant (`CookieConsentServlet`) si aucun choix n'a encore
  été fait.
- **Panier** : protégé par `AuthFiltre` (connexion obligatoire), ajout,
  suppression d'articles.
- **Commande** : transformation du panier en commande (transaction SQL avec
  décrémentation du stock), annulation (restitue le stock), historique
  client.
- **Paiement**, 3 moyens :
  - **Carte bancaire** : `CarteCreditValidator` vérifie l'algorithme de Luhn
    **et** le préfixe/la longueur pour confirmer qu'il s'agit bien d'une
    carte **Visa** ou **Mastercard**, plus la date d'expiration et le CVV.
  - **TWINT** : simulation via un numéro de téléphone suisse.
  - **Carte cadeau Google** : vérification du code et du solde en base
    (table `carte_cadeau_google`).
- **Back-office administrateur** (`/admin/*`, protégé par `AdminFiltre`) :
  tableau de bord, CRUD des modèles de chaussures, gestion des tailles/stock
  par modèle, ajout de partenaires, consultation de toutes les commandes,
  lecture des messages du formulaire de contact.

## 6. Numéros de carte de test (algorithme de Luhn valide)

- Visa : `4111 1111 1111 1111`
- Mastercard : `5500 0000 0000 0004`

Ces numéros sont des numéros de test standards, ils ne débitent rien de
réel (aucune passerelle de paiement externe n'est appelée dans ce projet,
tout est simulé côté serveur).

## 7. Sécurité — pistes d'amélioration pour une mise en production réelle

Ce projet est pédagogique. Avant une mise en production, il faudrait a
minima :
- remplacer SHA-256+sel par bcrypt/Argon2 (ex. librairie jBCrypt),
- passer les identifiants MySQL en variables d'environnement / JNDI plutôt
  qu'en dur dans `ConnexionBD.java`,
- ajouter une protection CSRF sur les formulaires,
- brancher de vraies passerelles de paiement (Stripe/Saferpay pour les
  cartes, l'API officielle TWINT, l'API Google Play/Wallet pour les cartes
  cadeaux) plutôt que la simulation actuelle,
- forcer HTTPS et le flag `Secure` sur les cookies de session.

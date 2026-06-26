# Outil de Génération de Mots de Passe

**Auteur : Kouassi Ahou Ketura Acsa**

---

## Description

Outil en ligne de commande (CLI) permettant de générer des mots de passe robustes et d'évaluer leur force via deux méthodes :

- Une évaluation **locale** basée sur des critères de sécurité (longueur, types de caractères)
- Une validation **externe** via **Zxcvbn** tournant dans un conteneur **Docker**

---

## Fonctionnalités

- Configuration de la longueur du mot de passe (minimum 6 caractères)
- Choix des types de caractères : majuscules, minuscules, chiffres, symboles
- Mode rafale : génération de plusieurs mots de passe en une seule exécution
- Validation des saisies : redemande si la réponse est invalide (o/n uniquement)
- Indicateur de force : Très faible / Faible / Moyen / Fort / Très fort
- Double évaluation : locale (Java) + externe (Docker + Zxcvbn)

---

## Prérequis

- Java 21
- Apache Maven 3.9+
- Docker Desktop (démarré)

---

## Structure du Projet

```
outil-mot-de-passe/
├── src/
│   ├── main/java/pigier/securite/
│   │   ├── App.java          # Point d'entrée, interface CLI et communication Docker
│   │   ├── Generateur.java   # Génération sécurisée des mots de passe (SecureRandom)
│   │   └── Evaluateur.java   # Évaluation locale de la force du mot de passe
│   └── test/java/pigier/securite/
│       └── AppTest.java      # Tests unitaires
├── Dockerfile                # Image Docker Node.js + Zxcvbn
├── zxcvbn-server.js          # Serveur HTTP d'évaluation des mots de passe
├── pom.xml                   # Configuration Maven (Java 21)
└── README.md                 # Documentation du projet
```

---

## Guide d'Installation

### Étape 1 — Cloner le projet

```bash
git clone https://github.com/TON_USERNAME/outil-mot-de-passe.git
cd outil-mot-de-passe
```

### Étape 2 — Construire l'image Docker

```bash
docker build -t zxcvbn-serveur .
```

### Étape 3 — Lancer le conteneur Docker

```bash
docker run -d -p 3000:3000 --name zxcvbn zxcvbn-serveur
```

### Étape 4 — Compiler le projet Java

```bash
mvn compile
```

### Étape 5 — Exécuter l'application

```bash
mvn exec:java -Dexec.mainClass="pigier.securite.App"
```

---

## Exemple d'utilisation

```
===========================================
   OUTIL DE GÉNÉRATION DE MOTS DE PASSE
===========================================

Longueur du mot de passe (min 6) : 12
Inclure des MAJUSCULES ? (o/n) : o
Inclure des MINUSCULES ? (o/n) : o
Inclure des CHIFFRES ? (o/n) : o
Inclure des SYMBOLES ? (o/n) : o
Combien de mots de passe à générer ? : 3

--- MOTS DE PASSE GÉNÉRÉS ---
1. +U.APrOkTb7L
   → Force locale  : Fort        [******]
   → Force Docker  : Très fort   [******]

2. Q]QPBLdq1a8,
   → Force locale  : Fort        [******]
   → Force Docker  : Très fort   [******]

3. :%{kOAs4.v3M
   → Force locale  : Fort        [******]
   → Force Docker  : Très fort   [******]

===========================================
```

---

## Architecture DevOps

```
Utilisateur
    ↓ saisie paramètres
App.java (Java 21)
    ↓ génère mot de passe → Generateur.java
    ↓ évalue localement  → Evaluateur.java
    ↓ HTTP POST JSON
Conteneur Docker (port 3000)
    ↓ analyse avec Zxcvbn
    ↓ retourne score (0-4) + niveau
App.java
    ↓ affiche résultats
Utilisateur
```

L'application Java envoie chaque mot de passe généré au serveur Node.js tournant dans Docker via une requête HTTP POST sur le port 3000. Le serveur utilise la bibliothèque **Zxcvbn** pour analyser la robustesse et retourner un score de 0 à 4.

---

## Niveaux de force

| Score | Niveau      | Signification                 |
| ----- | ----------- | ----------------------------- |
| 0     | Très faible | Cracké en moins d'une seconde |
| 1     | Faible      | Cracké en quelques secondes   |
| 2     | Moyen       | Résistance modérée            |
| 3     | Fort        | Bonne résistance              |
| 4     | Très fort   | Résistance excellente         |

---

## Arrêter le conteneur Docker

```bash
docker stop zxcvbn
docker rm zxcvbn
```

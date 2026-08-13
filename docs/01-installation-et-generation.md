# Guide de Prise en Main et Génération gRPC

Bienvenue sur le projet gRPC **Distributeur automatique**.

## 1. Prérequis
- **Python 3.9+**
- **Git**

## 2. Installation de l'environnement
Dans votre terminal à la racine du projet :

```bash
# 1. Créer l'environnement virtuel
python -m venv .venv

# 2. Activer l'environnement virtuel
# Sur Windows (PowerShell) :
.\.venv\Scripts\Activate.ps1
# Sur Linux / macOS :
source .venv/bin/activate

# 3. Installer les dépendances gRPC
pip install -r requirements.txt
```

## 3. Génération du code gRPC (Protobuf)
Les stubs de compilation gRPC ne sont pas versionnés sur Git. Après avoir récupéré le projet (`git pull`), régénérez le code localement :

* **Sur Windows (PowerShell)** :
  ```powershell
  .\scripts\gen.ps1
  ```

* **Sur Linux / macOS (Bash)** :
  ```bash
  chmod +x ./scripts/gen.sh
  ./scripts/gen.sh
  ```

Le code généré sera automatiquement placé dans `sandbox/adebayo` et `app/common`.

## 4. Règles de Contribution
- **Brouillons et tests personnels** : Développez librement dans votre dossier `sandbox/<votre-prenom>/`.
- **Application commune (`app/`)** : Créez une branche `feature/<description>` puis ouvrez une Pull Request (PR) pour fusionner sur `main`.

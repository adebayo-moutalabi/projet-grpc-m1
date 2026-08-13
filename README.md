# Projet gRPC Distributeur Automatique (`projet-grpc-m1`)

Ce dépôt contient le projet collaboratif de démonstration gRPC pour le système de **Distributeur Automatique de Boissons**.

## 📁 Architecture du Dépôt

```text
projet-grpc-m1/
├── proto/                  # Contrat unique Protobuf (.proto)
│   └── distributeur.proto
├── scripts/                # Scripts de génération automatique gRPC
│   ├── gen.ps1             # Pour Windows (PowerShell)
│   └── gen.sh              # Pour Linux / macOS (Bash)
├── app/                    # Code de l'application partagée
│   ├── server/             # Serveur gRPC (Backend)
│   ├── client_cli/         # Client Utilisateur (CLI / IHM)
│   ├── client_admin/       # Client de Maintenance
│   └── common/             # Stubs et utilitaires communs
├── sandbox/                # Espaces personnels de test (Zéro conflit)
│   └── adebayo/
├── docs/                   # Documentation et guides
│   └── 01-installation-et-generation.md
├── examples/               # Exemples et prototypes validés
└── slides/                 # Supports de présentation
```

## 🚀 Démarrage Rapide

1. **Cloner le projet** :
   ```bash
   git clone https://github.com/adebayo-moutalabi/projet-grpc-m1.git
   cd projet-grpc-m1
   ```

2. **Installer l'environnement et générer le code** :
   Consultez le guide détaillé : [docs/01-installation-et-generation.md](docs/01-installation-et-generation.md)

   * **Windows** : `.\scripts\gen.ps1`
   * **Linux / Mac** : `./scripts/gen.sh`

## 👥 Équipe et Répartition

| Membre | Rôle / Module attribué | Espace Sandbox |
| :--- | :--- | :--- |
| **Adebayo** | Contrat gRPC & Sandbox initial | `sandbox/adebayo/` |
| *Membre 2* | À définir | `sandbox/membre2/` |
| *Membre 3* | À définir | `sandbox/membre3/` |
| *Membre 4* | À définir | `sandbox/membre4/` |

## 🛡️ Règles de Contribution
- **Sandbox** : Libres commits et push direct sur `main` dans votre dossier `sandbox/<votre-nom>/`.
- **Application (`app/`) & Contrat (`proto/`)** : Découpage par branche `feature/<nom>` + Pull Request (PR) validée par au moins 1 pair.

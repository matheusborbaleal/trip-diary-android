# 🌍 Diário de Viagens (Trip Diary)

Aplicativo Android nativo desenvolvido em Kotlin para o gerenciamento de registros de viagens, com suporte a persistência local e sincronização em tempo real na nuvem.

## 🚀 Funcionalidades

- **Autenticação Segura:** Sistema de login e cadastro integrado ao Firebase Authentication.
- **Gerenciamento de Viagens (CRUD):** Criação, leitura, atualização e exclusão de registros de viagens.
- **Persistência Local (Offline-First):** Uso da biblioteca **Room Database** para garantir que os dados estejam disponíveis mesmo sem internet.
- **Sincronização em Nuvem:** Integração com **Firebase Realtime Database** para backup e sincronização de dados entre dispositivos.
- **Segurança de Dados:** Regras de segurança no Firebase que garantem o isolamento dos dados por usuário (Multi-tenant).
- **Interface Moderna (Material Design 3):** - Uso de `MaterialCardView` para listagem.
    - `FloatingActionButton` para ações rápidas.
    - Feedback visual com `ProgressBar` (Loading) e `Empty States`.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** [Kotlin](https://kotlinlang.org/)
- **Banco de Dados Local:** [Room](https://developer.android.com/training/data-storage/room)
- **Backend/Cloud:** [Firebase](https://firebase.google.com/) (Auth & Realtime Database)
- **Interface:** Material Design 3, RecyclerView, ConstraintLayout.
- **Gerenciamento de Dependências:** Versão moderna com `libs.versions.toml` (Version Catalog).

## 🏗️ Arquitetura

O projeto segue os princípios de separação de responsabilidades:
- **Model:** Entidade `Trip` representando os dados.
- **DAO:** Interface de acesso a dados para o Room.
- **Sync:** Lógica de espelhamento entre a base local e o Firebase sob o nó seguro `users/{uid}/trips`.

---
Desenvolvido por **Matheus** como projeto final de Pós-Graduação.
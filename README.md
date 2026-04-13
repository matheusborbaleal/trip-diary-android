# ✈️ Diário de Viagens - Android

Aplicativo avançado para registro de experiências de viagem, desenvolvido com foco em arquitetura modular, persistência híbrida e integração com serviços cloud do Firebase.

## 📋 Demonstração de Rubricas (Checklist Acadêmico)

### 1. Interfaces Dinâmicas e Componentização
- **Componentização de UI:** A tela de Login foi desenvolvida utilizando fragmentos reutilizáveis (`GenericInputFragment` e `GenericButtonFragment`), permitindo injeção dinâmica de campos de E-mail, Senha e botões de ação.
- **Fragments de Navegação:** Uso de `TripListFragment` para listagem e `ProfileFragment` para gestão de perfil e consumo de API.

### 2. Persistência Remota e Consumo de Dados
- **Firebase Realtime Database:** Utilizado para persistir as tarefas (viagens) do usuário e o link da foto de capa do perfil.
- **Firebase Storage:** Armazenamento físico das fotos das viagens e da imagem de capa do usuário.
- **API Restful:** Integração com a API **ViaCEP** via **Retrofit** para busca automática de endereços a partir do CEP.

### 3. Notificações, Permissões e Background
- **Permissions Runtime:** Implementação de solicitação de permissões em tempo de execução para GPS e Notificações (compatível com Android 13/API 33).
- **Notificações:** Sistema de notificações locais (Background Notification) ao salvar registros.
- **Firebase Cloud Messaging:** Configurado para suporte a Push Notifications.
- **Analytics:** Monitoramento de eventos de interação e salvamento de dados.

### 4. Localização, Anúncios e Estabilidade
- **Internacionalização:** Suporte completo aos idiomas Português (Brasil) e Inglês (USA).
- **AdMob:** Integração de anúncios em banner na HomeActivity.
- **Crashlytics:** Implementado para monitoramento de estabilidade e relatórios de falhas em tempo real.
- **App Distribution:** Configurado no console do Firebase para distribuição de versões de teste.

## 🛠️ Stack Tecnológica
- **Linguagem:** Kotlin + Coroutines (Tarefas em segundo plano)
- **Banco Local:** Room Database (Offline First)
- **Injeção de Interface:** FragmentManager & FragmentContainerView
- **Rede:** Retrofit + Gson
- **Imagens:** Glide (Cache e Rendering)

## 🏗️ Arquitetura de Fragmentos Reutilizáveis
O projeto utiliza um padrão de "Legos" na LoginActivity:
1. `EmailInputFragment`: Gerencia a entrada de e-mail com validação.
2. `PasswordInputFragment`: Gerencia a entrada de senha com `InputType` protegido.
3. `ButtonFragment`: Encapsula a lógica de clique e callback para a Activity mãe.



## 🚀 Como Executar
1. Clone o repositório.
2. Certifique-se de que o arquivo `google-services.json` está presente na pasta `app/`.
3. O SDK do AdMob utiliza IDs de teste padrão do Google para fins de avaliação acadêmica.
4. Execute o projeto em um emulador ou dispositivo real (Recomendado API 33+).

---
**Desenvolvido por:** Matheus  
**Disciplina:** Desenvolvimento Mobile
# ✈️ Diário de Viagens - Android

Um aplicativo completo para registro de memórias de viagens, desenvolvido como projeto acadêmico de alta performance. O app combina persistência local e na nuvem, integração com hardware e consumo de serviços externos.

## 🚀 Funcionalidades Principal
- **Autenticação:** Login, Cadastro e Recuperação de Senha via Firebase Auth.
- **Diário de Fotos:** Captura de fotos da galeria com armazenamento no Firebase Storage.
- **Geolocalização:** Registro automático de coordenadas (Latitude e Longitude) via GPS.
- **Sincronização Híbrida:** Funcionamento Offline (Room Database) com sincronização automática para Nuvem (Firebase Realtime Database).
- **Consulta de Endereços:** Integração com a API ViaCEP para preenchimento de endereço através do CEP.
- **Internacionalização:** Suporte total aos idiomas Português (Brasil) e Inglês (USA).
- **Notificações:** Avisos de confirmação ao salvar registros, compatível com Android 13+.

## 🛠️ Tecnologias e Arquitetura
O projeto segue as melhores práticas de desenvolvimento Android moderno:

- **Linguagem:** Kotlin
- **Arquitetura:** Interface dinâmica baseada em **Fragments** com navegação por **BottomNavigationView**.
- **Concorrência:** Kotlin Coroutines para operações assíncronas em banco de dados e rede.
- **Banco de Dados Local:** Room Database com Migrations.
- **Rede:** Retrofit + Gson para consumo de API Restful.
- **Image Loading:** Glide para carregamento e cache de imagens.
- **Firebase Suite:** - Authentication
  - Realtime Database
  - Cloud Storage
  - Analytics (Monitoramento de eventos)
  - Crashlytics (Relatórios de estabilidade)



## 📋 Pré-requisitos de Entrega (Checklist)
| Requisito | Status | Tecnologia |
| :--- | :---: | :--- |
| Interface com Fragments | ✅ | FragmentManager & Navigation |
| Consumo de API Restful | ✅ | Retrofit (ViaCEP) |
| Banco de Dados Local | ✅ | Room SQLite |
| Integração Nuvem | ✅ | Firebase Database |
| Hardware (GPS/Câmera) | ✅ | Location Services & Storage |
| Internacionalização | ✅ | Strings.xml (pt/en) |
| Notificações Push/Local | ✅ | NotificationManager (API 33+) |
| Analytics & Crashlytics | ✅ | Firebase Analytics |

## 🔧 Como Rodar o Projeto
1. Clone o repositório.
2. Certifique-se de ter o arquivo `google-services.json` na pasta `/app`.
3. Sincronize o Gradle.
4. Rode em um emulador ou dispositivo físico com Android 8.0 ou superior.

---
Desenvolvido por **Matheus** como parte do projeto de Desenvolvimento Mobile.
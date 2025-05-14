# 🏙️ Ativo e Operante!

**API web que permite ao cidadão denunciar problemas em sua comunidade de forma rápida e eficiente.**

Este sistema oferece uma plataforma onde o cidadão pode registrar denúncias de situações como buracos na rua, abandono de animais, poda de árvores, problemas no trânsito, entre outros.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot (API REST)**
- **Spring Security + JWT**
- **Spring Data JPA**
- **Banco de Dados Relacional (MySQL/PostgreSQL)**
- **JSON para comunicação entre frontend e backend**

## 🔐 Funcionalidades

### Para Cidadãos
- Cadastro e login de usuários
- Registro de denúncias com:
  - Título e descrição
  - Nível de urgência (1 a 5)
  - Órgão competente
  - Tipo de problema
  - Imagem (upload)
- Visualização das próprias denúncias e feedbacks recebidos

### Para Administradores
- Login com usuário pré-definido
- Gerenciamento de:
  - Tipos de problemas
  - Órgãos competentes
- Visualização de todas as denúncias
- Feedback único por denúncia
- Exclusão de denúncias

## 🔐 Acesso

### Administrador
- **Login:** `admin@pm.br`
- **Senha:** `123321`

## 🛠️ Estrutura

- **/api/cidadao/** → Endpoints públicos e de cidadão autenticado
- **/api/admin/** → Endpoints restritos a administradores
- **/uploads/** → Armazenamento de imagens de denúncias

## 📦 Como executar

1. Clone o repositório
2. Configure seu banco de dados em `application.properties`
3. Execute o projeto via IDE ou com o comando:

```bash
./mvnw spring-boot:run

# ☕ Cafeteria

Sistema web desenvolvido para uma cafeteria, com o objetivo de oferecer uma experiência moderna, intuitiva e agradável para apresentação dos produtos e realização de pedidos.

O projeto foi desenvolvido pensando em **usabilidade, organização, responsividade e facilidade de manutenção**, permitindo que o cliente navegue pelo cardápio, escolha seus produtos e acompanhe o resumo do pedido.

---

## 📌 Sobre o Projeto

A **Cafeteria** é uma aplicação web desenvolvida para representar digitalmente uma cafeteria e seus principais serviços.

O sistema permite apresentar produtos como:

* ☕ Cafés
* 🥐 Salgados
* 🍰 Doces
* 🥤 Bebidas
* 🍪 Acompanhamentos
* 🧁 Sobremesas

A interface foi desenvolvida com foco em um visual moderno e agradável, proporcionando uma navegação simples tanto em computadores quanto em dispositivos móveis.

---

## 🎯 Objetivos

O projeto tem como principais objetivos:

* Criar uma interface moderna para uma cafeteria;
* Facilitar a visualização do cardápio;
* Permitir a seleção de produtos;
* Exibir um resumo do pedido;
* Melhorar a experiência do usuário;
* Desenvolver uma aplicação responsiva;
* Aplicar boas práticas de desenvolvimento web;
* Integrar o Front-End com o Back-End quando necessário.

---

## 🖥️ Funcionalidades

### 🏠 Página Inicial

Apresentação da cafeteria, seus produtos e principais informações.

### 📋 Cardápio

Exibição organizada dos produtos disponíveis, contendo informações como:

* Nome;
* Descrição;
* Preço;
* Imagem;
* Categoria.

### 🛒 Pedidos

O usuário pode selecionar os produtos desejados e adicioná-los ao pedido.

O sistema apresenta um resumo contendo:

* Produtos selecionados;
* Quantidade;
* Preço individual;
* Subtotal;
* Valor total.

### 📱 Responsividade

A interface foi desenvolvida para funcionar em diferentes tamanhos de tela:

* 💻 Computadores;
* 💻 Notebooks;
* 📱 Smartphones;
* 📲 Tablets.

---

## 🛠️ Tecnologias Utilizadas

### Front-End

* HTML5
* CSS3
* JavaScript
* Bootstrap

### Back-End

Caso integrado ao servidor:

* Java
* Spring Boot

### Banco de Dados

* MySQL

### Ferramentas

* Visual Studio Code
* IntelliJ IDEA
* Git
* GitHub

---

## 📂 Estrutura do Projeto

```text
cafeteria/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ...
│   │   │
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   ├── img/
│   │       │   └── pages/
│   │       │
│   │       └── templates/
│   │
│   └── test/
│
├── pom.xml
├── README.md
└── .gitignore
```

---

## 🎨 Interface

A interface foi projetada para proporcionar uma experiência visual agradável, utilizando elementos como:

* Cards de produtos;
* Botões de ação;
* Menu de navegação;
* Formulários;
* Resumo do pedido;
* Animações e efeitos de interação;
* Layout responsivo.

---

## 🔄 Fluxo do Pedido

O funcionamento básico do sistema segue o seguinte fluxo:

```text
Cliente
   │
   ▼
Acessa a Cafeteria
   │
   ▼
Visualiza o Cardápio
   │
   ▼
Seleciona os Produtos
   │
   ▼
Adiciona ao Pedido
   │
   ▼
Confere o Resumo
   │
   ▼
Confirma o Pedido
   │
   ▼
Sistema processa a solicitação
```

---

## 🚀 Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
```

### 2. Acesse a pasta

```bash
cd cafeteria
```

### 3. Execute o projeto

Se o projeto utilizar Spring Boot:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

Depois, acesse:

```text
http://localhost:8080
```

---

## 🗄️ Banco de Dados

Caso o projeto utilize MySQL, configure o banco de dados no arquivo:

```text
application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cafeteria
spring.datasource.username=root
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Substitua as informações de conexão de acordo com o seu ambiente.

---

## 📊 Possíveis Entidades

O sistema pode trabalhar com entidades como:

```text
Cliente
   │
   └── Pedido
          │
          ├── Produto
          │
          └── ItemPedido
```

### Cliente

* id
* nome
* email
* telefone

### Produto

* id
* nome
* descrição
* preço
* categoria
* imagem

### Pedido

* id
* data
* valorTotal
* status

### ItemPedido

* id
* quantidade
* preço
* produto

---

## 🔐 Boas Práticas

O projeto busca aplicar boas práticas de desenvolvimento, como:

* Organização dos arquivos;
* Separação entre Front-End e Back-End;
* Código sem duplicações desnecessárias;
* Nomenclatura clara;
* Responsividade;
* Validação de formulários;
* Organização de componentes;
* Versionamento utilizando Git;
* Documentação do projeto.

---

## 📱 Responsividade

O sistema foi planejado seguindo uma abordagem responsiva, permitindo que os componentes se adaptem de acordo com o tamanho da tela.

Exemplo:

```text
Desktop
┌───────────────────────────────────┐
│             CAFETERIA             │
├───────────────────────────────────┤
│ Produto │ Produto │ Produto       │
│ Produto │ Produto │ Produto       │
└───────────────────────────────────┘

Mobile
┌──────────────────┐
│    CAFETERIA     │
├──────────────────┤
│     Produto      │
│     Produto      │
│     Produto      │
│     Produto      │
└──────────────────┘
```

---

## 🔮 Melhorias Futuras

Entre as funcionalidades que podem ser adicionadas futuramente estão:

* [ ] Sistema de login;
* [ ] Cadastro de clientes;
* [ ] Histórico de pedidos;
* [ ] Status do pedido em tempo real;
* [ ] Integração com formas de pagamento;
* [ ] Painel administrativo;
* [ ] Gerenciamento de produtos;
* [ ] Controle de estoque;
* [ ] Sistema de avaliações;
* [ ] Integração com WhatsApp;
* [ ] Banco de dados completo;
* [ ] API REST;
* [ ] Autenticação e autorização.

---

## 👨‍💻 Desenvolvimento

Projeto desenvolvido com foco em aprendizado e aplicação prática de conceitos de:

**Desenvolvimento Web • Java • Spring Boot • Banco de Dados • UX/UI • Git e GitHub**

---

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e de desenvolvimento de habilidades em programação.

---

## ⭐ Contribuição

Sugestões e melhorias são bem-vindas.

Para contribuir:

```bash
git clone https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
```

Crie uma nova branch:

```bash
git checkout -b minha-melhoria
```

Faça suas alterações e depois envie um Pull Request.

---

# ☕ Cafeteria

**Um projeto criado para transformar uma experiência simples de cafeteria em uma experiência digital moderna.**

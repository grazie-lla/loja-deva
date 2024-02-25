# Loja Deva - Projeto Final do módulo de Programação Web.

### Índice

- [Descrição do projeto](#descrição-do-projeto)

- [Pré-requisitos](#pré-requisitos)

- [Tecnologias Utilizadas](#tecnologias-utilizadas)

- [Estrutura do Projeto](#estrutura-do-projeto)

- [Como Executar a Aplicação](#como-executar-a-aplicação)

- [Swagger](#swagger)

- [Documentação da API](#documentação-da-api)

- [Gerenciamento de clientes](#gerenciamento-de-clientes)

- [Gerenciamento de produtos](#gerenciamento-de-procutos)

- [Gerenciamento de carrinhos de compras](#gerenciamento-de-carrinhos-de-compras)

- [Gerenciamento dos itens dos carrinhos de compras](#gerenciamento-dos-itens-dos-carrinhos-de-compras)

- [Desenvolvedoras](#desenvolvedoras)


## Descrição do Projeto
Este projeto é o resultado final do módulo de Programação Web do Programa Deva da B3 em parceria com a Ada Tech. O objetivo principal foi desenvolver uma API de e-commerce em Java utilizando tecnologias como Maven, Spring Boot, JPA, Banco de Dados H2 e Spring Security. O projeto abrange diversas funcionalidades essenciais para um e-commerce, como gestão de clientes, produtos, carrinho de compras, pedidos e métodos de pagamento.

## Pré-requisitos
- [Java](https://www.java.com/pt-BR/download/)
- [Maven](https://maven.apache.org/download.cgi)

## Tecnologias Utilizadas
- Java
- Maven
- Spring Boot
- Spring Data JPA
- H2 Database
- Spring Security
- Swagger

## Estrutura do Projeto
O projeto está organizado nas seguintes pastas e classes:

### Pasta `config`
- `RestControllerAdvice`: Classe que fornece tratamento global de exceções para controladores REST.
- `SecurityConfig`: Configurações de segurança utilizando o Spring Security.
- `SwaggerConfig`: Configurações para a documentação da API utilizando o Swagger.

### Pasta `controller`
- `BasketItemController`: Controlador responsável por operações relacionadas aos itens do carrinho.
- `ClientController`: Controlador para gerenciamento de clientes.
- `OrderController`: Controlador responsável por operações relacionadas a pedidos.
- `OrderItemController`: Controlador para operações relacionadas aos itens de pedidos.
- `ProductController`: Controlador para gerenciamento de produtos.
- `ShoppingBasketController`: Controlador para operações do carrinho de compras.

### Pasta `domain`
- Classes de domínio representando entidades essenciais para o e-commerce, como `BasketItem`, `Client`, `Order`, `OrderItem`, `PaymentMethod`, `Product`, `ShoppingBasket` e `Status`.

### Pasta `dto`
- Classes de Transferência de Dados (DTO) utilizadas para comunicação entre o frontend e o backend, incluindo classes como `BasketItemRequest`, `ClientRequest`, `ClientResponse`, `OrderRequest`, `ProductRequest`, `ShoppingBasketRequest`, `UpdateItemQuantityRequest`, `UpdateOrderRequest` e `UpdateProductDetailsRequest`.

### Pasta `repository`
- Interfaces de repositório que estendem JpaRepository para acesso ao banco de dados, incluindo `BasketItemRepository`, `ClientRepository`, `OrderItemRepository`, `OrderRepository`, `ProductRepository` e `ShoppingBasketRepository`.

### Pasta `service`
- Classes de serviço que encapsulam a lógica de negócios, incluindo subpacote `exceptions` com a classe `ResourceNotFoundException`.
- `BasketItemService` é responsável por fornecer lógica de negócios relacionada aos itens do carrinho de compras. Ele gerencia a adição, remoção e atualização de itens no carrinho.
- `ClientService` lida com operações relacionadas à gestão de clientes, incluindo cadastro, atualização e busca de informações do cliente.
- `OrderItemService` é encarregado de manipular os itens de pedidos. Ele gerencia a criação e atualização de itens associados aos pedidos feitos pelos clientes.
- `OrderService` gerencia as operações relacionadas à criação e processamento de pedidos. Ele interage com `OrderItemService` para garantir a integridade dos pedidos.
- `ProductService` trata de operações relacionadas à gestão de produtos. Ele lida com a adição, remoção e atualização de produtos disponíveis para compra.
- `ShoppingBasketService` é responsável pela lógica de negócios relacionada ao carrinho de compras como um todo. Ele interage com `BasketItemService` para garantir que o carrinho seja manipulado de acordo com as necessidades do cliente.

Esses serviços são essenciais para o funcionamento adequado do sistema, garantindo a integridade e a consistência das operações no e-commerce.

### Classe Principal `LojaDevaApplication`
- Ponto de entrada da aplicação Spring Boot.

## Como Executar a Aplicação
1. Certifique-se de ter o Java e uma IDE instalados na sua máquina.
2. Clone o repositório: `git clone https://github.com/seu-usuario/loja-deva.git`
3. Navegue até o diretório do projeto: `cd loja-deva`
4. Execute a aplicação: `mvn spring-boot:run`
5. Utilize o Swagger ou Postman para testar endpoints da API.

A aplicação estará disponível em `http://localhost:8080`.

## Swagger
A documentação da API pode ser acessada através do Swagger. Após iniciar a aplicação, acesse `http://localhost:8080/swagger-ui.html` no seu navegador.

## Documentação da API
## Gerenciamento de clientes

#### Cadastro do cliente
Rota não autenticada

```http
  POST localhost:8080/clients
```
Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório** |
| `email` | `string` | **Obrigatório**. |
| `cpf` | `string` | **Obrigatório**. Chave única |
| `address` | `string` | **Obrigatório** |
| `postalCode` | `string` | **Obrigatório** |
| `phoneNumber` | `string` | **Obrigatório** |
| `password` | `string` | **Obrigatório** |

- Ao registrar o cliente um carrinho de compras associado ao client_id é criado automaticamente.

#### Buscar cliente pelo seu id
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  GET localhost:8080/clients/:id
```
 Passar como parâmetro na URL da requisição o ID do client que deseja detalhar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.

#### Atualização de todos os dados do cliente
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  PUT localhost:8080/clients/:id
```
 Passar como parâmetro na URL da requisição o ID do client que deseja atualizar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.


Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório** |
| `email` | `string` | **Obrigatório**. |
| `cpf` | `string` | **Obrigatório**. Chave única |
| `address` | `string` | **Obrigatório** |
| `postalCode` | `string` | **Obrigatório** |
| `phoneNumber` | `string` | **Obrigatório** |
| `password` | `string` | **Obrigatório** |

#### Atualização parcial dos dados do cliente
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  PATCH localhost:8080/clients/:id
```
 Passar como parâmetro na URL da requisição o ID do client que deseja atualizar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.


Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Opcional** |
| `email` | `string` | **Opcional**. |
| `address` | `string` | **Opcional** |
| `postalCode` | `string` | **Opcional** |
| `phoneNumber` | `string` | **Opcional** |
| `password` | `string` | **Opcional** |

#### Remover cliente
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.
```http
  DELETE localhost:8080/clients/:id
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do cliente que deseja deletar |

## Gerenciamento de produtos

#### Cadastro do produto

Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth

```http
  POST localhost:8080/product
```
Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório**. | 
| `description` | `string` | **Obrigatório** |
| `price` | `BigDecimal` | **Obrigatório**. |
| `inventoryQuantity` | `Integer` | **Obrigatório**. |
| `category` | `string` | **Obrigatório**. |

### Retorna todos os produtos
Rota não autenticada.

```http
  GET localhost:8080/product
```

#### Buscar produtos pelo seu ID
Rota não autenticada.

```http
  GET localhost:8080/product/:id
```
Passar como parâmetro na URL da requisição o código do produto que deseja detalhar.

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do produto que você quer buscar. | 

#### Atualização de todos os dados do produto
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  PUT localhost:8080/product/:id
```
 Passar como parâmetro na URL da requisição o ID do produto que deseja atualizar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.|

Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `name` | `string` | **Obrigatório**. | 
| `description` | `string` | **Obrigatório** |
| `price` | `BigDecimal` | **Obrigatório**. |
| `inventoryQuantity` | `Integer` | **Obrigatório**. |
| `category` | `string` | **Obrigatório**. |

#### Atualização parcial os dados do produto
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  PATCH localhost:8080/product/:id
```
 Passar como parâmetro na URL da requisição o ID do produto que deseja atualizar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.|

Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `description` | `string` | **Opcional** |
| `price` | `BigDecimal` | **Opcional**. |
| `inventoryQuantity` | `Integer` | **Opcional**. |

#### Remover produto
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.
```http
  DELETE localhost:8080/product/:id
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do produto que deseja deletar |

## Gerenciamento de carrinhos de compras
#### Buscar carrinho de compras pelo seu ID
Rota não autenticada.

```http
  GET localhost:8080/shopping-basket/:id
```
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do produto que deseja detalhar |
- Retorna o cliente associado ao carrinho e a lista de itens do carrinho com a quantidade e valor total da compra.

## Gerenciamento dos itens dos carrinhos de compras
#### Adição do produto ao carrinho de compras

Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth

```http
  POST localhost:8080/basket-item
```
Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `shoppingBasketId` | `string` | **Obrigatório**. | 
| `productId` | `string` | **Obrigatório** |
| `quantity` | `Integer` | **Obrigatório**. |

#### Buscar item do carrinho de compras pelo seu ID
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  GET localhost:8080/basket-item/:id
```
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do item que deseja detalhar |

### Retornar todos os itens adicionados em todos os carrinhos
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth

```http
  GET localhost:8080/basket-item/items
```
- Retorna todos os itens adicionados em todos os carrinhos com a quantidade total de itens.

### Retornar todos os itens adicionados um carrinho pelo ID do carrinho
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth

```http
  GET localhost:8080/basket-item/items
```
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `shoppingBasketId`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do shoppingBasket que deseja detalhar |

- Retorna todos os itens adicionados em um carrinho específico com a quantidade total de itens.

#### Atualização da quantidade de um item
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.

```http
  PATCH localhost:8080/basket-item/:id
```
 Passar como parâmetro na URL da requisição o ID do item que deseja atualizar.
| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**.|

Passar parâmetros no body da requisição em formato JSON:

| Parâmetro   | Tipo       | Descrição                           |
| :---------- | :--------- | :---------------------------------- |
| `quantity` | `Integer` | **Opcional**. |

#### Remover item do carrinho
Rota autenticada - necessário configurar sua ferramenta de teste da aplicação de preferência com Authorization - tipo Basic Auth.
```http
  DELETE localhost:8080/basket-item/:id
```

| Parâmetro   | Descrição                                   |
| :---------- | :------------------------------------------ |
| `id`      | **Obrigatório**. Passar como parâmetro na URL da requisição o ID do item que deseja deletar |



## Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## Desenvolvedoras
Para qualquer dúvida ou sugestão, entre em contato com a equipe de desenvolvimento:
| [<img src="https://media.licdn.com/dms/image/D4D03AQE5y3lBKkJqMQ/profile-displayphoto-shrink_400_400/0/1702420057924?e=1714608000&v=beta&t=DhvcI0lbfslMew0eiLlgmUKvGIOtK1mHqmARbHQ3dmU" width=115><br><sub>Adilane Pereira</sub>](https://www.linkedin.com/in/adilane-borges/?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=ios_app) |  [<img src="https://media.licdn.com/dms/image/C4D03AQHZZ7hoeBMwxQ/profile-displayphoto-shrink_400_400/0/1611092925723?e=1714608000&v=beta&t=52PnMjX9L6DLh_L_jaaNIUzJrjexhQigGbTYqUZisTM" width=115><br><sub>Aline Werner</sub>](https://www.linkedin.com/in/alinewer/) |  [<img src="" width=115><br><sub>Ana Luiza Akiyama</sub>]() | [<img src="https://media.licdn.com/dms/image/C4D03AQF2yNZ3qZ3ZjQ/profile-displayphoto-shrink_400_400/0/1629249879532?e=1714608000&v=beta&t=nXzvMwaLOGbsMATomIbybI0IaHl3Gpxb8LoywvtaYkA" width=115><br><sub>Graziella Guedes</sub>](https://www.linkedin.com/in/graziellacosta/) | [<img src="https://media.licdn.com/dms/image/D4D35AQGgZcEVZlV8Iw/profile-framedphoto-shrink_800_800/0/1660592108061?e=1709438400&v=beta&t=Sf1mxxIOx7doSTSeDPgtgtYrbl_qYY5D5s8m7IrYAu8" width=115><br><sub>Nathalya Lucena</sub>](https://www.linkedin.com/in/nathalya-lucena-466773244/)
| :---: | :---: | :---: | :---: | :---: 


Atenciosamente,

Equipe de Desenvolvimento Loja Deva.

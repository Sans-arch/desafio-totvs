### Desafio Backend
Neste desafio você deverá implementar uma API REST para um sistema simples de
contas a pagar. O sistema permitirá realizar o **CRUD de uma conta a pagar**, alterar a
situação dela quando for efetuado pagamento, obter informações sobre as contas
cadastradas no banco de dados, e importar um lote de contas de um arquivo CSV, conforme
descrito abaixo.

#### Requisitos Gerais
- [x] Utilizar a linguagem de programação Java, versão 17 ou superior.
- [x] Utilizar Spring Boot.
- [x] Utilizar o banco de dados PostgreSQL. 
- [x] A aplicação deve ser executada em um container Docker.
- [x] Tanto a aplicação, banco de dados, quanto outros serviços necessários para
   executar a aplicação, devem ser orquestrados utilizando Docker Compose.
- [x] O código do projeto deve ser hospedado no GitHub ou GitLab.
- [x] Utilizar mecanismo de autenticação.
- [x] Organizar o projeto com Domain Driven Design.
- [x] Utilizar o Flyway para criar a estrutura de banco de dados.
- [x] Utilizar JPA.
- [x] Todas as APIs de consulta devem ser paginadas.

#### Requisitos Específicos
- [x] Cadastrar a tabela no banco de dados para armazenar as contas a pagar. Deve
   incluir no mínimo os seguintes campos: (Faça a tipagem conforme achar adequado)
   - a. id
   - b. data_vencimento
   - c. data_pagamento
   - d. valor
   - e. descricao
   - f. situacao
- [x] Implementar a entidade “Conta” na aplicação, de acordo com a tabela criada
   anteriormente.

##### Implementar as seguintes APIs:
- [x] Cadastrar conta;
- [x] Atualizar conta;
- [x] Alterar a situação da conta;
- [x] Obter a lista de contas a pagar, com filtro de data de vencimento e descrição;
- [x] Obter conta filtrando o id;
- [x] Obter valor total pago por período.
- [x] Implementar mecanismo para importação de contas a pagar via arquivo csv.
   - O arquivo será consumido via API. 
- [x] Implementar testes unitários.

# Payables API

Uma API RESTful para gerenciamento de contas a pagar, construída com Spring Boot e PostgreSQL.
## Início Rápido

### Pré-requisitos
- Docker e Docker Compose
- Java 21+
- Make (opcional, mas recomendado)

### Documentação da API
- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI Docs: `http://localhost:8080/api-docs`

### Comandos Make Disponíveis

```bash
make start      # Configuração inicial (build + docker-build + docker-up)
make run        # Iniciar containers sem reconstruir
make stop       # Parar containers
make restart    # Reset completo (clean + build + docker-build + docker-up)
make logs       # Visualizar logs dos containers
make test      # Executar testes
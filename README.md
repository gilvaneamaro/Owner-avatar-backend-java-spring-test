# Teste Prático para Desenvolvedor Pleno Back-End em Java e Spring Boot

Este repositório contém o teste prático para a vaga de Desenvolvedor Pleno Back-End em Java e Spring Boot.

## Descrição do Projeto

Desenvolver uma aplicação de gerenciamento de tarefas (todo list) com as seguintes funcionalidades:

1. **Cadastro de Usuários**:
   - Criar, editar, excluir e listar usuários.
   - Cada usuário deve ter um nome e nível (admin, user).
2. **Gerenciamento de Tarefas**:
   - Criar, editar, excluir e listar tarefas.
   - Cada tarefa deve ter um título, descrição, data de criação, data de vencimento, status (pendente, em andamento, concluída) e um usuário associado.
3. **Filtros e Ordenação**:
   - Permitir que as tarefas sejam filtradas por status.
   - Permitir que as tarefas sejam ordenadas por data de vencimento.
4. **Associação de Tarefas a Usuários**
   - Permitir que tarefas sejam atribuídas a usuários específicos.
   - Permitir que as tarefas de um usuário específico sejam listadas.
   
### Aplicação de Testes unitários
   - O metodos devem ser testados com JUnit

## Requisitos Técnicos

- Java 11 ou superior
- Spring Boot
- Banco de Dados Relacional (H2 para facilitar os teste)
- JPA/Hibernate
- Maven
- Spring Security
- Spring Data JPA
- JUnit e Mockito para testes

## Instruções para executar

### Requisitos para testes:
- [Java SDK 22](https://www.oracle.com/br/java/technologies/downloads/);
- [Maven](https://dicasdeprogramacao.com.br/como-instalar-o-maven-no-windows/);
- [Postman](https://www.postman.com/downloads/) (ou alguma ferramenta para testar endpoints).

### Após iniciar o projeto, documentação dos endpoints pelo Swagger estará disponível pelo [link](http://localhost:8080/swagger-ui/index.html):

### 1. Clone o repositório e acesse o diretório:
   ```
   git clone https://github.com/gilvaneamaro/backend-java-spring-test.git
   cd backend-java-spring-boot-test
   ```

### 2. Execute o comando para compilar e executar os testes unitários:
Compilando:
```
mvn clean install
```
Iniciando projeto:
   ```
   mvn spring-boot:run
   ```

### 3. Reproduza as requisições através do Postman:
### Gerenciamento de usuários
   #### Para criar um novo usuário, realize uma requisição do tipo POST na seguinte URL:
   ```
   http://localhost:8080/api/users
   ```
   #### O body da requisição deve seguir o seguinte modelo:
   ```
   {
     "username":"gilvaneamaro",
     "password":"123456",
     "role":"ADMIN"
   }
   ```
#### Para realizar o login, basta enviar uma requisição do tipo POST para a seguinte URL:
   ```
   http://localhost:8080/api/users/login
   ```
Body da requisição:
   ```
   {
     "username":"gilvaneamaro",
     "password":"123456",
   }
   ```
   Observação: será retornado um  **Bearer token** de autenticação, que será necessário para as demais requisições.
   
#### Para listar os usuários cadastrados, basta enviar requisição do tipo GET
 ```
 http://localhost:8080/api/users
 ```
#### Para atualizar o cadastro de um usuário, envie requisição do tipo PUT:
```
http://localhost:8080/api/users
```
Body da requisição:
```
{
  "username":"gilvaneamaro",
  "password":"123456",
  "role":"USER"
}
 ```
É possível atualizar o nome, senha e a role do usuário.

#### Para deletar um usuário, envie requisição do tipo DELETE com o id do usuário na URL:
```
http://localhost:8080/api/users/1
```
### Gerenciamento de tarefas
#### Para criar uma nova tarefa, envie requisição do tipo POST:
```
http://localhost:8080/api/tasks
```
Body da requisição:
```
{
  "title": "Preparação para o primeiro dia na Selaz",
  "description": "Tomar água, respirar fundo e fazer diferença!",
  "createAt": "2024-09-04T10:30:00",
  "dueDate": "2024-09-20T230:59:59",
  "status": "EM_ANDAMENTO",
  "userID":"1"
}
```
#### Para listar todas as tarefas de um usuário autenticado requisição do tipo GET:
```
http://localhost:8080/api/tasks
```
Será retornado todos as tasks relacionadas ao usuário do token utilizado.

#### Para atualizar uma tarefa existente, envie uma requisição do tipo PUT com o id:
```
http://localhost:8080/api/tasks/{id}
```
Body da requisição:
```
{
  "title": "Preparação para o primeiro dia na Selaz",
  "description": "Tomar água, respirar fundo e fazer diferença!",
  "dueDate": "2024-09-20T230:59:59",
  "status": "CONCLUIDA"
}
```
É possível atualizar apenas um item, se desejado.

#### Para deletar uma nova tarefa, envie requisição do tipo DELETE com o id:
```
http://localhost:8080/api/tasks/{id}
```

#### Para filtrar tarefas por status, passe o parâmetro pela URL e uma requisição do tipo GET:
```
http://localhost:8080/api/tasks?status=CONCLUIDA
```

#### Para ordenar as tarefas por data de vencimento, passe o parâmetro pela URL e uma requisição do tipo GET::
```
http://localhost:8080/api/tasks?sort=duedate
```
#### Para listar todas as tarefas de um usuário específico, passe o ID pela URL em uma requisição do tipo GET::
```
http://localhost:8080/api/users/{userID}/tasks
```






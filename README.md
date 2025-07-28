# 📚 Sistema de Biblioteca 📚
Projeto dedicado a exploração de banco de dados com a API JDBC padrão do Java para realizar conexão com um banco de dados de uma biblioteca apresentando operações de CRUD e utilizando boas práticas de código, padrões de projetos, organização do código e documentação do projeto.

## 🌱 Sobre o projeto

- Foi realizado com o intuito de colocar em prática os meus conhecimentos de manipulação e criação de entidades, criando interfaces para separar a **lógica de negócio** e o acesso ao banco de dados utilizando padrão de projeto ``Dao``.
- O projeto consiste na utilização da interface ***CLI*** do programa para realizar **emprestimos e devoluções** de livros da biblioteca, fazer operações de **CRUD** com as entidades (`Book`, `User`, `Author` e `LibraryLoan`) e realização de **testes unitários** com ***JUnit*** para garantir a qualidade da aplicação. 

## 🛠️ Tecnologias utilizadas 

- **Java 17**
- **Maven** (gestão de dependências)
- **Lombok** (para redução de código boilerplate)
- **Log4j2** (biblioteca de logs para registrar informações e erros)
- **JUnit** (para testes unitários)
- **MySQL** (para persistência de dados)
- **Docker** (docker-compose para utilizar um container do mySQL)

## ✔️ Funcionalidades implementadas 

☑️Operações `CRUD` - cada Dao com suas respectivas operações de CREATE, READ, UPDATE, DELETE.

☑️Geração de relatórios - com filtragem e Busca (avançadas) explorando conceitos como `GROUP BY` `HAVING` e `JOINS`

☑️Regras de negócio:
- [x]  Um livro só pode ser emprestado se estiver disponível.
- [x]  Um livro só pode ser devolvido se estiver com o status indisponível, emprestado ou atrasado.  
- [x]  Um livro deve ser devolvido em `Y` dias.
- [x]  Cobrança de multas por empréstimo(s) atrasado(s).
      
☑️Design Patterns (padrões de projeto) - `Builder`, `Factory` e `Dao`.

☑️Teste Unitários - Testes unitários realizados na camada `service` onde fica centralizada as regras de negócios, validações e lançamento de exceções. 
 - Os testes unitários cobrem as classes Service com foco nos principais métodos garantindo que as validações e lançamento de exceções estão corretas conforme o esperado. 
 - Instanciei classes internas no método `setUp` para representarem as implementações dos `Daos`, para que os testes sejam realizados sem interferir na camada de persistência.

☑️Transações - utilizando o princípio ***ACID*** para garantir que empréstimos e devoluções ocorram garantindo a integridade dos dados. Todas as operações devem ser concluídas com sucesso `commit` caso ao contrário nenhuma é executada `rollback`.

☑️Tratamento de exceções personalizado - criei exceções **uncheckeds** personalizadas para serem lançadas por meio de erros de validação (id inválido por exemplo), argumentos inválidos (por exemplo deixar de digitar um campo numérico) e cenários específicos da biblioteca (livro não disponível para empréstimo, usuário não encontrado etc). Além de relançar uma exceção ou exibir um `log de erro` quando uma exceção SQL é capturada.    

## <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/World%20Map.png" alt="World Map" width="25" height="25" /> Arquitetura do projeto

```sh
📂 src
 │  ├──📂main/java/br/sabrinaweb/appbiblioteca
 │  │   ├──📂 conn        # Classe para o pool de conexões
 │  │   ├── ConnectionFactory.java
 │  │   │
 │  │   ├── 📂 model    # Modelo para manipulação dados, criação de entidades e exceções
 │  │   │   ├── 📂 dao
 │  │   │   │   ├── 📂impl # Classes de Implementação dos Daos responsáveis pela persistência dos dados
 │  │   │    │   │    ├── AuthorDaoJdbc.java
 │  │   │    │   │    ├── BookDaoJdbc.java
 │  │   │    │   │    ├── LibraryLoanDaoJdbc.java
 │  │   │    │   │    ├── UserDaoJdbc.java
 │  │   │    │   │                      # Interfaces Dao e fábrica de Dao para instanciar a implementação concreta
 │  │   │    │   ├── AuthorDao.java 
 │  │   │    │   ├── BookDao.java
 │  │   │    │   ├── DaoFactory.java
 │  │   │    │   ├── LibraryLoanDao.java
 │  │   │    │   ├── UserDao.java
 │  │   │    │
 │  │   │    ├── 📂 entities # Modelo de entidades que representam as tabelas no banco de dados
 │  │   │    │   ├── Author.java
 │  │   │    │   ├── Book.java
 │  │   │    │   ├── LibraryLoan.java
 │  │   │    │   ├── User.java
 │  │   │    │
 │  │   │    ├──📂 exceptions # Exceções personalizadas
 │  │   │    │   ├── BookNotAvailableForLoanException.java
 │  │   │    │   ├── DbException.java
 │  │   │    │   ├── InvalidIdException.java
 │  │   │    │   ├── InvalidLoanException.java
 │  │   │    │   ├── UserNotFoundException.java
 │  │   │    │      
 │  │   │    ├──📂 service         # Serviços dos Daos (aqui fica a lógica de negócio)
 │  │   │       ├──AuthorService.java
 │  │   │       ├──BookService.java
 │  │   │       ├──LibraryLoanService.java
 │  │   │       ├──UserService.java
 │  │   │  
 │  │   ├── 📂 view 
 │  │        ├── LibraryProgram # Classe para rodar a aplicação
 │  │        ├── MenuMain    # Classe com menu de interação via CLI
 │  │
 │  ├──📂test/java/br/sabrinaweb/appbiblioteca/model/service  # Classes de teste para testar a camada de negócios
         ├──AuthorService.java
         ├──BookService.java
         ├──LibraryLoanService.java
         ├──UserService.jav
```
## ⚙️ Como Executar o Projeto

### 📌 Instalações necessárias (pré-requisitos)
- ***JDK 17***
- ***Maven***
- ***Uma IDE ou editor de texto***
- ***Docker Desktop (após instalado deixar rodando)***
  
### <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Hourglass%20Done.png" alt="Hourglass Done" width="25" height="25" /> Passo a passo de como executar a aplicação

1. **Clone meu repositório**
   
```
git clone https://github.com/SasaGomess/biblioteca-JDBC-docker.git 
```
  
No terminal digite:

2. **Build de dependências e compilar o projeto**
   
 ```
 mvn clean install
```
3. **Subir o conteiner MySQL com docker**  ``estará rodando na porta 3306 garanta que nenhum banco de dados está utilizando ela``
   
```
docker-compose up
```
### Utilizando a aplicação pelo CLI

### Menu Principal🔛
<img width="308" height="221" alt="Captura de tela 2025-07-26 145757" src="https://github.com/user-attachments/assets/94ec3990-0f2f-4c75-857a-5681721106a4" />

### Menu do Empréstimo <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Objects/Credit%20Card.png" alt="Credit Card" width="25" height="25" />
<img width="526" height="153" alt="Captura de tela 2025-07-26 145845" src="https://github.com/user-attachments/assets/6db47666-7d55-4fe2-8671-885cb5552fb4" />

### Menu do Usuário <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/People%20with%20professions/Man%20Medium-Light%20Skin%20Tone%2C%20Curly%20Hair.png" alt="Man Medium-Light Skin Tone, Curly Hair" width="35" height="35" />
<img width="255" height="150" alt="Captura de tela 2025-07-26 145915" src="https://github.com/user-attachments/assets/d829d058-c71c-478c-95ca-d299a0fe0f48" />

### Menu do Livro 📚
<img width="251" height="196" alt="Captura de tela 2025-07-26 145933" src="https://github.com/user-attachments/assets/91e8011f-8825-466b-9e0d-afa9398a8b4c" />

### Menu do Relatório 📋
<img width="309" height="150" alt="Captura de tela 2025-07-26 145950" src="https://github.com/user-attachments/assets/b4bbedf9-7e31-4282-8038-b77540fb9466" />

<h3>Menu do Autor <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/People%20with%20professions/Woman%20Teacher%20Medium-Light%20Skin%20Tone.png" alt="Woman Teacher Medium-Light Skin Tone" width="30" height="30" /> </h3> 

<img width="239" height="169" alt="image" src="https://github.com/user-attachments/assets/98e95b80-6237-46f3-bdc2-558b115ff6af" />




## Contribuições <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Animals/Butterfly.png" alt="Butterfly" width="25" height="25" /> <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Animals/Cat%20Face.png" alt="Cat Face" width="35" height="35" />

### Contribuições são sempre bem vindas fique a vontade para contribuir com o projeto!

1. Faça um fork deste repositório
2. Crie uma nova branch (git checkout -b minha-feature)
3. Faça as alterações e commite (git commit -m 'feat: minha nova feature')
4. Faça o push para a sua branch (git push origin minha-feature)
5. Abra um pull request.


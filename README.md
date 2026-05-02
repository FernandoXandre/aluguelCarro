# Sistema de Gestão de Aluguel de Carros

Sistema desenvolvido para a disciplina de **POO2** do curso de **Análise e Desenvolvimento de Sistemas – SENAC**, 4º semestre, 2º bimestre.

Aplicação web REST para gerenciamento de aluguel de veículos, cobrindo cadastro de clientes, frota de carros, registro de aluguéis e controle de usuários.

---

## Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.6 |
| Spring Data JPA | (incluso no Boot) |
| Spring Web MVC | (incluso no Boot) |
| MySQL | 8+ |
| Lombok | latest |
| Maven | 3.9+ |

---

## Como Executar o Projeto

### Pré-requisitos

- Java 21 instalado
- MySQL instalado e rodando
- Maven instalado (ou usar o `./mvnw` incluso no projeto)

### 1. Configurar o banco de dados

Abra o arquivo `src/main/resources/application.properties` e ajuste as credenciais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/aluguel_carro?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
```

O banco `aluguel_carro` será criado automaticamente na primeira execução.

### 2. Compilar e executar

```bash
# Usando o Maven Wrapper (não precisa ter Maven instalado)
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

A aplicação sobe na porta **8080** por padrão.

---

## Arquitetura em Camadas

O sistema segue o padrão de **Arquitetura em Camadas** (Layered Architecture), dividido em:

```
┌──────────────────────────────────┐
│     Camada de Apresentação       │  controller/
│  (Controllers REST - @RestController) │
├──────────────────────────────────┤
│     Camada de Serviço            │  service/
│  (Regras de negócio - @Service)  │
├──────────────────────────────────┤
│     Camada de Domínio            │  domain/
│  (Entidades JPA - @Entity)       │
├──────────────────────────────────┤
│     Camada de Persistência       │  repository/
│  (Repositórios - JpaRepository)  │
├──────────────────────────────────┤
│         Banco de Dados           │
│            (MySQL)               │
└──────────────────────────────────┘
```

### Responsabilidade de cada camada

| Camada | Pacote | Responsabilidade |
|---|---|---|
| Apresentação | `controller/` | Recebe requisições HTTP, retorna respostas JSON |
| Serviço | `service/` | Contém as regras de negócio e orquestra as operações |
| Domínio | `domain/` | Define as entidades e seus relacionamentos |
| Persistência | `repository/` | Acesso ao banco de dados via Spring Data JPA |

---

## Design Patterns Utilizados

### 1. Repository (DAO)

**O que é:** Abstrai o acesso ao banco de dados por trás de uma interface, separando a lógica de negócio da lógica de persistência.

**Onde foi aplicado:** Em todas as interfaces do pacote `repository/`, que estendem `JpaRepository`.

```java
// Exemplo: CarroRepository.java
public interface CarroRepository extends JpaRepository<Carro, Long> {
    List<Carro> findByDisponivelTrue();
    Optional<Carro> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
```

**Por que foi escolhido:** Permite trocar o banco de dados sem alterar a lógica de negócio. O Spring Data JPA gera automaticamente a implementação das queries a partir do nome dos métodos.

**Benefício:** Baixo acoplamento entre as camadas de serviço e persistência.

---

### 2. DTO (Data Transfer Object)

**O que é:** Objetos simples usados para transportar dados entre camadas, sem expor diretamente as entidades do banco.

**Onde foi aplicado:** Em todos os arquivos do pacote `dto/`, implementados como `record` do Java 21.

```java
// Exemplo: CarroDTO.java
public record CarroDTO(
    Long id,
    String marca,
    String modelo,
    String placa,
    Integer ano,
    BigDecimal valorDiaria,
    Boolean disponivel,
    String categoria
) {}
```

**Por que foi escolhido:** Evita expor dados sensíveis (como senha do usuário) e desacopla o contrato da API da estrutura interna do banco.

**Benefício:** A API pode evoluir sem impactar as entidades JPA, e vice-versa.

---

## Estrutura de Pacotes

```
src/main/java/com/projeto_aluguelCarro/aluguelCarro/
│
├── AluguelCarroApplication.java        ← Classe principal (ponto de entrada)
│
├── controller/                         ← Camada de Apresentação
│   ├── CarroController.java
│   ├── ClienteController.java
│   ├── AluguelController.java
│   └── UsuarioController.java
│
├── service/                            ← Camada de Serviço
│   ├── CarroService.java
│   ├── ClienteService.java
│   ├── AluguelService.java
│   └── UsuarioService.java
│
├── domain/                             ← Camada de Domínio
│   ├── Carro.java
│   ├── Cliente.java
│   ├── Aluguel.java
│   ├── Usuario.java
│   └── enums/
│       ├── StatusAluguel.java
│       └── PerfilUsuario.java
│
├── dto/                                ← Padrão DTO
│   ├── CarroDTO.java
│   ├── ClienteDTO.java
│   ├── AluguelDTO.java
│   └── UsuarioDTO.java
│
└── repository/                         ← Camada de Persistência (DAO)
    ├── CarroRepository.java
    ├── ClienteRepository.java
    ├── AluguelRepository.java
    └── UsuarioRepository.java
```

---

## Entidades do Sistema

### Carro

Representa os veículos disponíveis para aluguel.

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único (gerado automaticamente) |
| marca | String | Marca do veículo (ex: Toyota) |
| modelo | String | Modelo do veículo (ex: Corolla) |
| placa | String | Placa única do veículo |
| ano | Integer | Ano de fabricação |
| valorDiaria | BigDecimal | Valor cobrado por dia de aluguel |
| disponivel | Boolean | Indica se o carro está disponível |
| categoria | String | Categoria do veículo (ex: Econômico, SUV) |

### Cliente

Representa os clientes cadastrados no sistema.

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único |
| nome | String | Nome completo |
| cpf | String | CPF único do cliente |
| email | String | Endereço de e-mail |
| telefone | String | Número de telefone |
| endereco | String | Endereço residencial |
| cnh | String | Número da carteira de motorista |

### Aluguel

Representa o registro de um aluguel de veículo.

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único |
| dataInicio | LocalDate | Data de início do aluguel |
| dataFim | LocalDate | Data de devolução prevista |
| valorTotal | BigDecimal | Calculado automaticamente (dias × valorDiária) |
| status | StatusAluguel | PENDENTE / ATIVO / CONCLUIDO / CANCELADO |
| cliente | Cliente | Cliente que realizou o aluguel |
| carro | Carro | Veículo alugado |
| usuario | Usuario | Atendente responsável pelo registro |

### Usuario

Representa os usuários do sistema (atendentes e administradores).

| Campo | Tipo | Descrição |
|---|---|---|
| id | Long | Identificador único |
| username | String | Nome de usuário único |
| senha | String | Senha do usuário |
| perfil | PerfilUsuario | ADMIN ou ATENDENTE |

---

## Regras de Negócio

### Carro
- A placa deve ser única no sistema
- Ao criar um aluguel, o carro é marcado como **indisponível** automaticamente
- Ao concluir ou cancelar o aluguel, o carro volta a ficar **disponível**

### Cliente
- O CPF deve ser único no sistema
- Um cliente **não pode ser removido** se possuir aluguéis registrados

### Aluguel
- O **valor total é calculado automaticamente**: `(dataFim - dataInicio) × valorDiária`
- A data fim deve ser posterior à data de início
- Só é possível alugar um carro que esteja disponível

---

## Endpoints da API

### Carros — `/carros`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/carros` | Lista todos os carros |
| GET | `/carros/disponiveis` | Lista apenas carros disponíveis |
| GET | `/carros/{id}` | Busca carro por ID |
| POST | `/carros` | Cadastra novo carro |
| PUT | `/carros/{id}` | Atualiza dados do carro |
| DELETE | `/carros/{id}` | Remove o carro |

### Clientes — `/clientes`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Busca cliente por ID |
| POST | `/clientes` | Cadastra novo cliente |
| PUT | `/clientes/{id}` | Atualiza dados do cliente |
| DELETE | `/clientes/{id}` | Remove cliente (se não tiver aluguéis) |

### Aluguéis — `/alugueis`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/alugueis` | Lista todos os aluguéis |
| GET | `/alugueis/{id}` | Busca aluguel por ID |
| GET | `/alugueis/cliente/{clienteId}` | Lista aluguéis de um cliente |
| GET | `/alugueis/periodo?inicio=&fim=` | Lista aluguéis por período |
| POST | `/alugueis` | Registra novo aluguel |
| PATCH | `/alugueis/{id}/concluir` | Conclui o aluguel e libera o carro |
| PATCH | `/alugueis/{id}/cancelar` | Cancela o aluguel e libera o carro |

### Usuários — `/usuarios`

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/usuarios` | Lista todos os usuários |
| GET | `/usuarios/{id}` | Busca usuário por ID |
| POST | `/usuarios` | Cadastra novo usuário |
| DELETE | `/usuarios/{id}` | Remove usuário |

---

## Exemplos de Requisição

### Cadastrar um carro

```json
POST /carros
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "placa": "ABC-1234",
  "ano": 2022,
  "valorDiaria": 150.00,
  "disponivel": true,
  "categoria": "Sedan"
}
```

### Cadastrar um cliente

```json
POST /clientes
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "email": "joao@email.com",
  "telefone": "(11) 99999-0000",
  "endereco": "Rua das Flores, 123",
  "cnh": "12345678901"
}
```

### Registrar um aluguel

```json
POST /alugueis
{
  "dataInicio": "2026-05-01",
  "dataFim": "2026-05-05",
  "clienteId": 1,
  "carroId": 1,
  "usuarioId": 1
}
```

### Cadastrar um usuário

```json
POST /usuarios
{
  "username": "atendente01",
  "senha": "senha123",
  "perfil": "ATENDENTE"
}
```

---

## Banco de Dados

As tabelas são criadas automaticamente pelo Hibernate com base nas entidades JPA (`ddl-auto=update`).

### Tabelas geradas

| Tabela | Entidade |
|---|---|
| `carros` | Carro |
| `clientes` | Cliente |
| `alugueis` | Aluguel |
| `usuarios` | Usuario |

### Diagrama de relacionamentos

```
usuarios
  └── (1:N) alugueis

clientes
  └── (1:N) alugueis

carros
  └── (1:N) alugueis
```

---

## Dependências (pom.xml)

| Dependência | Finalidade |
|---|---|
| `spring-boot-starter-data-jpa` | Integração com banco via JPA/Hibernate |
| `spring-boot-starter-webmvc` | Criação de endpoints REST |
| `mysql-connector-j` | Driver de conexão com MySQL |
| `lombok` | Geração automática de getters, setters, construtores e builder |

---

## Repositório

GitHub: https://github.com/FernandoXandre/aluguelCarro

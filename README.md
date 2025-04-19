# Sistema de Atendimento Clínico

Sistema de gerenciamento clínico com funcionalidades de transcrição e resumo de áudio para sessões de atendimento.

## Descrição

O Sistema de Atendimento Clínico é uma aplicação web completa para gerenciamento de clínicas, que permite o cadastro e acompanhamento de pacientes, responsáveis, profissionais e agendamentos. O sistema possui uma funcionalidade diferenciada de gravação, transcrição e resumo de áudios de sessões clínicas, utilizando processamento assíncrono e inteligência artificial.

## Tecnologias Utilizadas

### Backend
- **Java 17** com **Spring Boot**
- **Spring Data JPA** para persistência de dados
- **PostgreSQL** como banco de dados relacional
- **Apache Kafka** para processamento assíncrono de mensagens
- **WebSockets** para comunicação em tempo real
- **DeepSeek API** para processamento de linguagem natural e transcrição

### Frontend
- **Vue.js** para interface de usuário
- **WebSockets** para comunicação em tempo real com o backend
- **HTML/CSS** para estilização

## Estrutura do Projeto

```
├── src/
│   ├── main/
│   │   ├── java/com/clinica/atendimento/
│   │   │   ├── config/           # Configurações (Kafka, WebSocket, etc.)
│   │   │   ├── controller/       # Controladores WebSocket
│   │   │   ├── deepseek/         # Integração com DeepSeek API
│   │   │   ├── dto/              # Objetos de transferência de dados
│   │   │   ├── handler/          # Handlers WebSocket
│   │   │   ├── kafka/            # Produtores e consumidores Kafka
│   │   │   ├── model/            # Entidades JPA
│   │   │   ├── repository/       # Repositórios Spring Data
│   │   │   └── service/          # Serviços de negócio
│   │   ├── resources/
│   │   │   ├── static/           # Arquivos estáticos
│   │   │   └── application.properties # Configurações da aplicação
│   │   └── webapp/               # Código fonte do frontend Vue.js
└── Dockerfile                    # Configuração para containerização
```

## Funcionalidades Principais

- Cadastro e gerenciamento de pacientes
- Cadastro e gerenciamento de responsáveis
- Cadastro e gerenciamento de profissionais e profissões
- Agendamento de sessões
- Gravação de áudio durante sessões
- Transcrição automática de áudio
- Resumo e interpretação de sessões usando IA

## Configuração e Instalação

### Pré-requisitos
- Java 17 ou superior
- Maven
- PostgreSQL
- Apache Kafka
- Node.js e npm (para desenvolvimento frontend)

### Configuração do Banco de Dados
1. Crie um banco de dados PostgreSQL chamado `clinica`
2. Atualize as configurações de conexão em `application.properties`

### Configuração do Kafka
1. Instale e configure o Apache Kafka
2. Atualize o endereço do servidor Kafka em `application.properties`

### Configuração da API DeepSeek
1. Configure o endpoint da API DeepSeek em `application.properties`

### Executando a Aplicação
1. Clone o repositório
2. Compile o projeto: `mvn clean install`
3. Execute a aplicação: `mvn spring-boot:run`
4. Acesse a aplicação em: `http://localhost:8080`

### Usando Docker
Alternativamente, você pode usar Docker para executar a aplicação:
```
docker build -t clinica-atendimento .
docker run -p 8080:8080 clinica-atendimento
```

## Desenvolvimento Frontend

Para desenvolvimento do frontend:
1. Navegue até a pasta `src/main/webapp`
2. Instale as dependências: `npm install`
3. Execute o servidor de desenvolvimento: `npm run dev`
4. Execute o script de build para atualizar os arquivos estáticos: `./build.sh`

## Fluxo de Processamento de Áudio

1. O áudio é capturado pela interface web
2. Os dados são enviados via WebSocket para o backend
3. O backend envia o áudio para o tópico Kafka "transcrever"
4. O consumidor processa o áudio e gera a transcrição
5. A transcrição é enviada para o tópico Kafka "resumir"
6. O resumo e interpretação são gerados usando a API DeepSeek
7. Os resultados são enviados de volta ao cliente via WebSocket

## Licença

Este projeto é licenciado sob a licença [MIT](LICENSE).
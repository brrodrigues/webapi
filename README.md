# Projeto WebAPI

Este projeto foi criado com o intuito de disponibilizar um projeto core um projeto utilizando as seguinte tecnologias:

- CDI 1.0
- Jersey
- Hibernate


#### Startup:

```
mvn clean package tomcat7:run
```

#### URL's

GET http://localhost:8080/webapi/resources/contatos

GET http://localhost:8080/webapi/resources/contatos/{id}

POST http://localhost:8080/webapi/resources/contatos

PUT http://localhost:8080/webapi/resources/contatos/{id}

#### Authentication

Foi utilizado uma segurança da API(Basic Authentication) para acessar.
Credencial padrao: vempra:meta








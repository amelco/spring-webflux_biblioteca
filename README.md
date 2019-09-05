# Spring Boot - Reactive web application

*Versão 1*  
Cria passo-a-passo detalhando a elaboração de um projeto reativo básico utilizando o framework Spring Boot.

## Descrição do projeto
Vamos criar um aplicativo **[reativo](#reativo)** e **[assíncrono](#assíncrono)** que controle a adição e remoção de livros de uma biblioteca. Livros podem ser adicionados e removidos do acervo da bibliopteca. É útil também saber quais livros fazem parte do acervo.

O banco de dados escolhido foi o MongoDB por suportar acesso assíncrono.

> Assumimos que o leitor está usando a IDE [Spring Tool Suite](https://spring.io/tools) em uma plataforma Linux.

#### Reativo
De maneira bem simples, a programação reativa pode ser definida como sendo a possibilidade de se desenvolver uma aplicação baseado em um fluxo assíncrono de dados. Em linhas gerais, a aplicação deve responder a eventos e estímulos assim que estes ocorrerem. Estes estímulos podem ser desde um simples clique em um botão, a requisições HTTP ou alterações em arquivo em disco.  


#### Assíncrono
Também de maneira simples, uma aplicação assíncrona é aquela que não espera a resposta da requisição antes de se efetuar uma nova requisição. Ou seja, várias requisições podem ser feitas sem que seja necessário o término de uma para se iniciar a outra.

Uma boa explicação para esses termos é dada por [Steven Haines](https://www.javaworld.com/author/Steven-Haines/), em inglês, [neste guia](https://www.javaworld.com/article/3288219/mastering-spring-framework-5-part-2-spring-webflux.html) que é a principal fonte deste passo-a-passo.

## *Blueprint* da Implementação
Para construirmos a aplicação, necessitaremos das seguintes *classes* e *interfaces*:

- **Livro**: Uma *classe* que representará o livro em nosso serviço.
- **LivroRepository**: Uma *interface* entre o Spring e o MongoDB para enviar e receber dados do banco.
- **LivroService**: Uma *interface* que definirá quais as "regras de negócio" que deverão ser implementadas (o [*CRUD*](https://www.codecademy.com/articles/what-is-crud), basicamente).
- **LivroServiceImpl**: Uma *classe* que implementa a interface **LivroService**. A *interface* de serviço (no nosso caso **LivroService**) não é necessária mas é recomendado se criar uma camada entre o controlador (**Controller**) e o banco de dados. Assim, pode-se mudar para um banco de dados relacional, por exemplo, sem causar impacto nos controllers.
- **LivroController**: Uma classe que irá receber as *requisições* e retornar as *respostas* reativas ([Monos]() e [Fluxes]()).

---

## 1 Criando o projeto
- Crie um "Spring Start project" no menu *File*.
- Insira o nome, grupo, descrição e clique *Next* para configurar as dependências.

![iniciando novo projeto](/images/start.png)

### 1.1 Dependências
- ***Spring Reactive Web*** por ser uma aplicação *reativa*.
- ***Spring Data MongoDB*** por utilizarmos o banco de dados MongoDB.
- ***Lombok*** para utilizar métodos que facilitam na criação de *getters* e *setters* e construtores (**não é essencial**).

> As dependências podem ser inseridas digitando seu nome na caixa de pesquisa e selecionando-as.
- Finalize clicando em *Finish*.

![dependências](/images/dep.png)

# 2 Conhecendo a estrutura básica do projeto
A estrutura inicial do seu projeto deverá ser bem semelhante a mostrada na figura abaixo:

![estrutura de diretórios](/images/tree.png)

O código fonte da aplicação ficará no diretório `/src/main/java`, onde se encontra o código padrão para inicialização da aplicação em `BibliotecaApplication.java`. Esse arquivo é a classe principal da aplicação, onde está o método `main()`.

```java
package com.webflux.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
		System.out.println("Olá, Mundo!");
		String currentDir = System.getProperty("user.dir");
        System.out.println("Diretório raiz do projeto: " +currentDir);
	}

}
```
A anotação `@SpringBootApplication` é necessária e diz, basicamente, que essa classe é uma aplicação Spring Boot.

---
### Hello World
Vamos fazer uma pequena pausa para executar o projeto.  
Vamos testar a aplicação e ver ela funcionando com o código padrão inserido pela IDE.  
Para não fugir do costume, vamos imprimir o famoso `Olá, Mundo!`

Digite o código abaixo em `BibliotecaApplication.java`
```java
public static void main(String[] args) {
    SpringApplication.run(BibliotecaApplication.class, args);
    System.out.println("Olá, Mundo!\n");

    String currentDir = System.getProperty("user.dir");
    System.out.println("Diretório raiz do projeto: " +currentDir);

}
```
e rode o programa clicando no pequeno símbolo de "play" verde, ou apertando `Ctrl+F11`, escolha a opção `Spring Boot App` e fique de olho na saída do console.

![rodando a aplicação](/images/run.png)

> Alguns erros referentes à conexão com o MongoDB deverão aparecer. Ignore-os, por enquanto, vamos tratar dele [mais à frente](#Instalando-o-Mongo-DB).



A saída deverá ser semelhante a da figura abaixo. Além de imprimir `Olá, mundo!`, a aplicação deverá imprimir no console o diretório raiz do projeto. Iremos utilizar essa informação na configuração do MongoDB.

![resultado](/images/run-res.png)

---

Continuando com a estrutura de diretórios do projeto: existem dois arquivos importantes: o *pom.xml*, localizado na raiz `/` e o *application.properties*, localizado em `/src/main/resources`.

## 2.1 pom.xml
Esse arquivo indica que você escolheu criar uma aplicação *Maven* e guarda algumas configurações, dentre elas as dependências da aplicação. Caso você queira adicionar/returar dependências, uma maneira simples é clicar com o botão direito no arquivo e escolher `Spring -> Edit Starters`. A janela de pesquisa de dependências irá aperecer e você pode alterar à vontade.

## 2.2 application.properties
Ainda não sei de todas as capacidades desse arquivo, mas nele se pode realizar algumas configurações como alteração da porta padrão do servidor http do Spring (que é a 8080) e definição do banco de dados.

### Instalando o MongoDB
Como escolhemos utilizar o MongoDB, devemos tê-lo instalado em nosso sistema. Se você está utilizando uma distribuição baseada no Debian (MX Linux, Ubuntu, Kali, Deepin), basta atualizar os repositórios de pacotes
```bash
sudo apt-get update
```
e instalar
```bash
sudo apt-get install mongodb
```
Para mais detalhes, acesse a [documentação oficial](https://docs.mongodb.com/manual/installation/).

### Inicializando o MongoDB

Existe uma maneira fácil de utilizarmos o MongoDB localmente em nossa aplicação. basta entrar no [diretório raíz](#Hello-World) do projeto e criar o diretório `mongoDB`. Depois execute:
```bash
mongodb --dbpath /mongoDB
```
Isso iniciará o MongoDB fisicamente em `mongoDB`. O serviço é iniciado em `127.0.0.1:27017` ou em seu alias `localhost:27017`.  
Agora precisamos configurar a aplicação para acessar o MongoDB nesse diretório.

### Acessando o MongoDB
Adicione a seguinte linha em *application.properties*:
```
spring.data.mongodb.uri=mongodb://localhost/mongoDB
```
Isso irá dizer à aplicação para procurar o MongoDB no endereço especificado.

# 3 Implementando o código

## 3.1 Classe **Livro**
Vamos começar a implementação do código da aplicação, começando criando a classe **Livro**, no pacote `com.webflux.biblioteca.model`.

```java
package com.webflux.biblioteca.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

	private String id;

	private String titulo;

	private String autor;

}
```
### Explicando o código
A classe **Livro** contém apenas 3 atributos: id, titulo e autor. A anotação **@Document** define que é um documento MongoDB.  
As anotações **@Data**, **@NoArgsConstructor** e **@AllArgsConstructor** pertencem à biblioteca *Lombok*.

**@Data**: Gera *getters* and *setters* para todos os campos. Gera construtor e funciona melhor com **@NoArgsConstructor** e **@AllArgsConstructor**.

A biblioteca *Lombok* não é necessária. Os construtores, *getters* e *setters* podem todos ser feitos à mão. Utilizamos o *Lombok* por conveniência.

## 3.2 Interface **LivroRepository**
 Agora criamos a interface **LivroRepository**, no pacote `com.webflux.biblioteca.repository`.
```java
package com.webflux.biblioteca.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.webflux.biblioteca.model.Livro;

public interface LivroRepository 
						extends ReactiveMongoRepository<Livro, String>{ }
```
### Explicando o código
**LivroRepository** é uma interface que herda de **ReactiveMongoRepository** vários métodos reativos como findAll(), findById(), deleteById() e save();

**ReacitveMongoRepository** tem como argumentos a classe que ela irá retornar e o tipo da variável id, que no nosso caso é **Livro** e `String` respectivamente.
Assim, os métodos retornam [**Mono**s]() ou [**Flux**es]().

## 3.3 Interface **BookService**
Criamos um pacote `com.webflux.biblioteca.service` e, dentro dele, a interface **LivroService**.

```java
package com.webflux.biblioteca.service;

import com.webflux.biblioteca.model.Livro;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LivroService {
	
	Flux<Livro> findAll();
	
	Mono<Livro> findById(String id);
	
	Mono<Livro> save(Livro livro);
	
	Mono<Void> deleteById(String id);

}
```
### Explicando o código

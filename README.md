# AT2 - LDDM

## Sobre o Projeto

Este projeto foi desenvolvido para a disciplina de Laboratório de Desenvolvimento para Dispositivos Móveis (LDDM).

A aplicação consiste em um sistema simples de cadastro de personagens inspirado no universo Sylvanian Families, utilizando uma arquitetura Full Stack com:

* Backend desenvolvido em Ktor
* Aplicativo Android desenvolvido com Jetpack Compose
* Comunicação entre cliente e servidor utilizando requisições HTTP
* Troca de dados em formato JSON

Os dados são armazenados em memória utilizando uma MutableList, sem utilização de banco de dados.

---

## Funcionalidades

* Listar personagens cadastrados
* Cadastrar novos personagens
* Atualização automática da lista após cada cadastro
* Comunicação entre aplicativo Android e servidor Ktor

---

## Estrutura dos Dados

Cada personagem possui os seguintes campos:

| Campo     | Tipo   |
| --------- | ------ |
| id        | Int    |
| nome      | String |
| familia   | String |
| descricao | String |

Exemplo:

```json
{
  "id": 1,
  "nome": "Freya Coelho",
  "familia": "Família Coelho da Floresta",
  "descricao": "Mãe gentil que adora jardinagem"
}
```

---

## Rotas da API

### GET /personagens

Retorna a lista de personagens cadastrados.

Exemplo de resposta:

```json
[
  {
    "id": 1,
    "nome": "Freya Coelho",
    "familia": "Família Coelho da Floresta",
    "descricao": "Mãe gentil que adora jardinagem"
  }
]
```

---

### POST /personagens

Realiza o cadastro de um novo personagem.

Exemplo de requisição:

```json
{
  "nome": "Lucas Coelho",
  "familia": "Família Coelho da Floresta",
  "descricao": "Gosta de aventuras na floresta"
}
```

Exemplo de resposta:

```json
{
  "id": 4,
  "nome": "Lucas Coelho",
  "familia": "Família Coelho da Floresta",
  "descricao": "Gosta de aventuras na floresta"
}
```

Isabele Leticia Gonçalves Queiroz
FATEC Registro - Desenvolvimento de Software Multiplataforma

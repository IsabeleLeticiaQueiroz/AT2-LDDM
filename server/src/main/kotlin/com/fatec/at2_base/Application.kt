package com.fatec.at2_base

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Personagem(val id: Int, val nome: String, val familia: String, val descricao: String)

@Serializable
data class NovoPersonagem(val nome: String, val familia: String, val descricao: String)

val personagens = mutableListOf(
    Personagem(1, "Freya Coelho", "Família Coelho da Floresta", "Mãe gentil que adora jardinagem"),
    Personagem(2, "Oliver Urso", "Família Urso Chocolate", "Filhote curioso que coleciona bolotas"),
    Personagem(3, "Bella Esquilo", "Família Esquilo Noz", "Irmã mais velha, ótima em culinária")
)
var proximoId = 4

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/personagens") {
            call.respond(personagens)
        }

        post("/personagens") {
            val novo = call.receive<NovoPersonagem>()
            val personagem = Personagem(
                id = proximoId++,
                nome = novo.nome,
                familia = novo.familia,
                descricao = novo.descricao
            )
            personagens.add(personagem)
            call.respond(HttpStatusCode.Created, personagem)
        }
    }
}

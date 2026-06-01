package com.fatec.at2_base

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.foundation.shape.RoundedCornerShape

@Serializable
data class Personagem(val id: Int, val nome: String, val familia: String, val descricao: String)

@Serializable
data class NovoPersonagem(val nome: String, val familia: String, val descricao: String)

val client = HttpClient {
    install(ContentNegotiation) { json() }
}

const val BASE_URL = "http://10.0.2.2:8080"

val famílias = listOf(
    "Família Coelho da Floresta",
    "Família Urso Chocolate",
    "Família Esquilo Noz",
    "Família Gato Baunilha",
    "Família Cachorro Labrador",
    "Família Raposa",
    "Família Ouriço",
    "Outra"
)
val cardShape = RoundedCornerShape(12.dp)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFE8B4C7),
            secondary = Color(0xFFF5DCE5),
            background = Color(0xFFFFFAFC),
            surface = Color(0xFFFFFFFF),
            onPrimary = Color.White,
        )
    ) {
        var personagens by remember { mutableStateOf<List<Personagem>>(emptyList()) }
        var nome by remember { mutableStateOf("") }
        var familia by remember { mutableStateOf(famílias[0]) }
        var descricao by remember { mutableStateOf("") }
        var mensagem by remember { mutableStateOf("") }
        var mensagemErro by remember { mutableStateOf(false) }
        var expandido by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        fun carregar() {
            scope.launch {
                try {
                    personagens = client.get("$BASE_URL/personagens").body()
                    mensagem = ""
                } catch (e: Exception) {
                    mensagem = "Erro ao carregar personagens"
                    mensagemErro = true
                }
            }
        }

        LaunchedEffect(Unit) { carregar() }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Sylvanian Families",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFFA8CBB7),
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = Color(0xFFFDFBF7)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(Modifier.height(12.dp)) }

                // form
                item {
                    Card(
                        shape = cardShape,
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                "Cadastrar Personagem",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF7B6D6D)
                            )
                            Spacer(Modifier.height(12.dp))

                            OutlinedTextField(
                                value = nome,
                                onValueChange = { nome = it },
                                label = { Text("Nome do personagem") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(Modifier.height(8.dp))


                            ExposedDropdownMenuBox(
                                expanded = expandido,
                                onExpandedChange = { expandido = !expandido }
                            ) {
                                OutlinedTextField(
                                    value = familia,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Família") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                                    modifier = Modifier.fillMaxWidth().menuAnchor()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandido,
                                    onDismissRequest = { expandido = false }
                                ) {
                                    famílias.forEach { f ->
                                        DropdownMenuItem(
                                            text = { Text(f) },
                                            onClick = {
                                                familia = f
                                                expandido = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = descricao,
                                onValueChange = { descricao = it },
                                label = { Text("Descrição") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 2,
                                maxLines = 3
                            )
                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (nome.isBlank()) {
                                        mensagem = "Preencha o nome do personagem."
                                        mensagemErro = true
                                        return@Button
                                    }
                                    scope.launch {
                                        try {
                                            client.post("$BASE_URL/personagens") {
                                                contentType(ContentType.Application.Json)
                                                setBody(NovoPersonagem(nome, familia, descricao))
                                            }
                                            nome = ""
                                            descricao = ""
                                            mensagem = "✓ Personagem cadastrado!"
                                            mensagemErro = false
                                            carregar()
                                        } catch (e: Exception) {
                                            mensagem = "Erro ao cadastrar: ${e.message}"
                                            mensagemErro = true
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8CBB7))
                            ) {
                                Text("Cadastrar")
                            }

                            if (mensagem.isNotBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    mensagem,
                                    color = if (mensagemErro) MaterialTheme.colorScheme.error else Color(0xFF5A7A5A)
                                )
                            }
                        }
                    }
                }

                // lista
                item {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Personagens cadastrados (${personagens.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFD4829A)
                    )
                }

                items(personagens) { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = cardShape,                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text(
                                p.nome,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(p.familia, style = MaterialTheme.typography.labelSmall, color = Color(0xFFD4829A))
                            if (p.descricao.isNotBlank()) {
                                Spacer(Modifier.height(2.dp))
                                Text(p.descricao, style = MaterialTheme.typography.bodySmall, color = Color(0xFF666666))
                            }
                        }
                    }
                }

                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

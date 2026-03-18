package com.example.projectandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class FormularioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFA7EAB0)
                ) {
                    TelaDesignFigma()
                }
            }
        }
    }
}

@Composable
fun TelaDesignFigma() {
    val context = LocalContext.current
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color(0xFFEFFFF1),
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val icons = listOf(Icons.Default.Home, Icons.Default.AddCircle, Icons.Default.LocationOn, Icons.Default.List, Icons.Default.Person)
                    icons.forEachIndexed { index, icon ->
                        NavigationBarItem(
                            icon = {
                                if (selectedItem == index) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF13C69D)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, contentDescription = null, tint = Color.Black)
                                    }
                                } else {
                                    Icon(icon, contentDescription = null, tint = Color.Black)
                                }
                            },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // === PARTE SUPERIOR VERDE ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.White.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "Olá, Bem Vindo",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Denúncie problemas da sua cidade\ne ajude a melhorar o bairro.",
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // A IMAGEM DA CIDADE NO FUNDO VERDE
                Image(
                    painter = painterResource(id = R.drawable.cidade_zelus), // Mude para o nome correto da sua imagem
                    contentDescription = "Ilustração da cidade Zelus",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
            }

            // === PARTE INFERIOR BRANCA COM BOTÕES ===
            Surface(
                shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp, vertical = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BotaoZelus(
                        texto = "Nova Denúncia",
                        icone = Icons.Default.Notifications,
                        onClick = { Toast.makeText(context, "Abrindo Câmera e GPS...", Toast.LENGTH_SHORT).show() }
                    )

                    BotaoZelus(
                        texto = "Ver Denúncias",
                        icone = Icons.Default.Menu,
                        onClick = { Toast.makeText(context, "Ver Denúncias", Toast.LENGTH_SHORT).show() }
                    )

                    BotaoZelus(
                        texto = "Sobre O Aplicativo",
                        icone = Icons.Default.Info,
                        onClick = { Toast.makeText(context, "Sobre o Aplicativo", Toast.LENGTH_SHORT).show() }
                    )
                }
            }
        }
    }
}

@Composable
fun BotaoZelus(texto: String, icone: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFA7EAB0))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 4.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF13C69D)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icone, contentDescription = null, tint = Color.Black)
        }

        Text(
            text = texto,
            modifier = Modifier
                .weight(1f)
                .padding(end = 52.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DesignPreview() {
    TelaDesignFigma()
}
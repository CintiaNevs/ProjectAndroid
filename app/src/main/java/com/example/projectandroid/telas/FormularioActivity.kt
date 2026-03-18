package com.example.projectandroid.telas // <-- Agora ele sabe que está na pasta organizada!

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectandroid.R // <-- Puxa as suas imagens da pasta principal

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

    // VARIÁVEL PARA GUARDAR A FOTO TIRADA
    var imagemCapturada by remember { mutableStateOf<Bitmap?>(null) }

    // 2. O QUE ACONTECE DEPOIS DE TIRAR A FOTO
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imagemCapturada = bitmap // Guarda a foto
            Toast.makeText(context, "Foto capturada com sucesso!", Toast.LENGTH_SHORT).show()
        }
    }

    // 1. O QUE ACONTECE AO PEDIR PERMISSÃO
    val permissaoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Se o usuário deixou, abre a câmera!
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Precisamos da câmera para a denúncia!", Toast.LENGTH_LONG).show()
        }
    }

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
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
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

                Image(
                    painter = painterResource(id = R.drawable.cidade_zelus), // Mantenha o nome da sua imagem aqui
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
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // SE A FOTO FOI TIRADA, ELA VAI APARECER AQUI!
                    imagemCapturada?.let { bmp ->
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Foto da Denúncia",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Text("Foto anexada!", color = Color(0xFF13C69D), fontWeight = FontWeight.Bold)
                    }

                    BotaoZelus(
                        texto = "Nova Denúncia",
                        icone = Icons.Default.Notifications,
                        onClick = {
                            // AQUI A MÁGICA ACONTECE: Pede a permissão e abre a câmera
                            permissaoCameraLauncher.launch(Manifest.permission.CAMERA)
                        }
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
package com.example.projectandroid.telas

// --- IMPORTAÇÕES (Atualizadas para o GPS, Imagens e Bordas) ---
import android.Manifest
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
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
import com.example.projectandroid.R
import com.google.android.gms.location.LocationServices
import java.util.Locale

class FormularioActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFA7EAB0)) {
                    AplicativoZelus()
                }
            }
        }
    }
}

@Composable
fun AplicativoZelus() {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    // NAVEGAÇÃO: 0=Home, 1=Foto Batida, 2=Mapa (GPS)
    var telaAtual by remember { mutableIntStateOf(0) }
    var imagemCapturada by remember { mutableStateOf<Bitmap?>(null) }
    var enderecoDetectado by remember { mutableStateOf("Buscando sua rua...") }

    // VARIÁVEL DE TÍTULO: Guarda o nome da rua para o topo da tela.
    var tituloRua by remember { mutableStateOf("Buscando...") }

    // FUNÇÃO PARA BUSCAR O ENDEREÇO REAL
    fun buscarLocalizacao() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val enderecos = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!enderecos.isNullOrEmpty()) {
                        // Extraímos a rua e o número para o título
                        val rua = enderecos[0].thoroughfare ?: "Rua não identificada"
                        val numero = enderecos[0].subThoroughfare ?: ""
                        tituloRua = if (numero.isNotEmpty()) "$rua, $numero" else rua

                        // Extraímos a cidade para o endereço completo
                        val cidade = enderecos[0].locality ?: "Cidade desconhecida"
                        enderecoDetectado = "$rua - $cidade"
                    }
                } else {
                    enderecoDetectado = "Localização não encontrada"
                    tituloRua = "Nenhuma Rua"
                }
            }
        } catch (e: SecurityException) {
            enderecoDetectado = "Sem permissão de GPS"
            tituloRua = "Sem GPS"
        }
    }

    // PERMISSÕES DE GPS
    val launcherGps = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            buscarLocalizacao()
            telaAtual = 2 // Pula para a tela do mapa
        } else {
            Toast.makeText(context, "GPS necessário para a denúncia!", Toast.LENGTH_SHORT).show()
        }
    }

    // (Lógica da Câmera mantida igual)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            imagemCapturada = bitmap
            telaAtual = 1
        }
    }

    val permissaoCameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) cameraLauncher.launch(null)
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Surface(
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color(0xFFEFFFF1),
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBar(containerColor = Color.Transparent) {
                    val icons = listOf(Icons.Default.Home, Icons.Default.AddCircle, Icons.Default.LocationOn, Icons.AutoMirrored.Filled.List, Icons.Default.Person)
                    icons.forEachIndexed { index, icon ->
                        NavigationBarItem(
                            icon = {
                                if (telaAtual == index) {
                                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF13C69D)), contentAlignment = Alignment.Center)
                                    { Icon(icon, contentDescription = null, tint = Color.Black) }
                                } else { Icon(icon, contentDescription = null, tint = Color.Black) }
                            },
                            selected = telaAtual == index,
                            onClick = { telaAtual = index },
                            colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // 👇 AJUSTE DO FUNDO BRANCO: Tiramos o padding da barra do contêiner principal para o branco descer.
        Box(modifier = Modifier.fillMaxSize()) {
            when (telaAtual) {
                0 -> TelaHome(
                    onNovaDenunciaClick = { permissaoCameraLauncher.launch(Manifest.permission.CAMERA) },
                    paddingBarra = innerPadding // Passamos o espaço para usar nos botões
                )
                1 -> TelaNovaDenuncia(
                    imagem = imagemCapturada,
                    onConfirmarClick = {
                        launcherGps.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    },
                    paddingBarra = innerPadding
                )
                2 -> TelaLocalizacao(endereco = enderecoDetectado, ruaTitle = tituloRua, paddingBarra = innerPadding)
                else -> TelaEmConstrucao()
            }
        }
    }
}

// --- TELA 0: HOME ---
@Composable
fun TelaHome(onNovaDenunciaClick: () -> Unit, paddingBarra: PaddingValues) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.5f)) {
                    Text("Olá, Bem Vindo", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Denuncie problemas da sua cidade\ne ajude a melhorar o bairro.", fontSize = 16.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                Image(painter = painterResource(id = R.drawable.paisagem_cidade), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }
        Surface(shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp), color = Color.White, modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 32.dp, top = 40.dp, end = 32.dp, bottom = paddingBarra.calculateBottomPadding() + 16.dp), // 👇 Respeitamos a barra de baixo aqui
                verticalArrangement = Arrangement.spacedBy(45.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotaoZelus(texto = "Nova Denúncia", icone = Icons.Default.Notifications, onClick = onNovaDenunciaClick)
                BotaoZelus(texto = "Ver Denúncias", icone = Icons.AutoMirrored.Filled.List, onClick = {})
                BotaoZelus(texto = "Sobre O Aplicativo", icone = Icons.Default.Info, onClick = {})
            }
        }
    }
}

// --- TELA 1: NOVA DENÚNCIA (FOTO) ---
@Composable
fun TelaNovaDenuncia(imagem: Bitmap?, onConfirmarClick: () -> Unit, paddingBarra: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(shape = RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp), color = Color(0xFFF3FFF5), modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingBarra.calculateBottomPadding()) // 👇 Respeitamos a barra de baixo aqui
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nova Denúncia", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = Modifier.fillMaxWidth(0.75f).aspectRatio(9f/16f).clip(RoundedCornerShape(24.dp)).background(Color(0xFF3B3253)).border(6.dp, Color(0xFF2D2D2D), RoundedCornerShape(24.dp)), contentAlignment = Alignment.Center) {
                if (imagem != null) Image(bitmap = imagem.asImageBitmap(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                else Text("Câmera Pronta", color = Color.White)
            }
            Spacer(modifier = Modifier.height(35.dp))
            Button(onClick = onConfirmarClick, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA7EAB0)), shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)) {
                Text("Confirmar Localização", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- TELA 2: LOCALIZAÇÃO DETECTADA (TELA 4) ---
@Composable
fun TelaLocalizacao(endereco: String, ruaTitle: String, paddingBarra: PaddingValues) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        // --- CABEÇALHO VERDE (Página 4 do seu PDF) ---
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            modifier = Modifier.padding(horizontal = 24.dp), // Dá um espacinho da borda
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(10.dp))
            // 👇 AJUSTE 3: O título agora mostra apenas o nome da rua, bem grande.
            Text(
                text = ruaTitle,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace, // Fonte do protótipo
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 👇 AJUSTE 1: O fundo branco agora preenche tudo até o final, weight(1f)
        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    // 👇 Respeitamos a barra de baixo aqui para os botões não sumirem
                    .padding(bottom = paddingBarra.calculateBottomPadding() + 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra de Endereço Completo
                Surface(
                    modifier = Modifier.fillMaxWidth().height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF3FFF5),
                    border = BorderStroke(2.dp, Color(0xFF13C69D))
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        // Ícone de "retornar" simulado
                        Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFEFFFF1)), contentAlignment = Alignment.Center)
                        { Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null, tint = Color.Black) }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Mostra o endereço completo detetado
                        Text(endereco, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 👇 AJUSTE 2: Placeholder do Mapa com a IMAGEM REAL DO MAPA 👇
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Ocupa o maior espaço
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mapa_), // 👇 ADICIONE ESTA IMAGEM NO DRAWABLE
                        contentDescription = "Mapa Detectado",
                        contentScale = ContentScale.Crop, // Preenche a moldura sem distorcer
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botão "Prosseguir para Denúncia"
                Button(
                    onClick = { /* Próxima tela: Descrição da Denúncia */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF13C69D)),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Prosseguir para Denúncia", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

// (Funções BotaoZelus e TelaEmConstrucao mantidas abaixo)
@Composable
fun TelaEmConstrucao() {
    Surface(shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp), color = Color.White, modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Em Construção 🚧", color = Color.Gray, textAlign = TextAlign.Center) }
    }
}

@Composable
fun BotaoZelus(texto: String, icone: ImageVector, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(32.dp)).background(Color(0xFFA7EAB0)).clickable { onClick() }, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(start = 4.dp).size(48.dp).clip(CircleShape).background(Color(0xFF13C69D)), contentAlignment = Alignment.Center) { Icon(icone, contentDescription = null, tint = Color.Black) }
        Text(text = texto, modifier = Modifier.weight(1f).padding(end = 52.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = Color.Black)
    }
}
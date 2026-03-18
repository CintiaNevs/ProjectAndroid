package drawable.projectandroid

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
import androidx.compose.ui.layout.ContentScale
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
                    color = Color(0xFFA7EAB0) // Fundo verde principal
                ) {
                    TelaRefinada()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaRefinada() {
    val context = LocalContext.current
    var selectedItem by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.Transparent, // Deixa o verde de fundo aparecer
        bottomBar = {
            // Barra de navegação inferior que ficará SOBRE o branco
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Espaço para não colar na borda do celular
                color = Color.Transparent
            ) {
                // A barra em si com fundo transparente
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
                                            .background(Color(0xFF13C69D)), // Verde água mais escuro
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
                                indicatorColor = Color.Transparent // Remove o indicador padrão
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
            // === PARTE SUPERIOR VERDE E TEXTO ===
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                // Etiqueta "Olá, Bem Vindo"
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

                // Texto descritivo
                Text(
                    text = "Denúncie problemas da sua cidade\ne ajude a melhorar o bairro.",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // === O PAINEL DE CONTEÚDO BRANCO E EXTENDIDO ===
            Surface(
                shape = RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp),
                color = Color.White,
                modifier = Modifier.weight(1f) // Faz o painel branco preencher todo o resto da tela
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    // --- CÓDIGO DA IMAGEM AGORA DENTRO DO PAINEL BRANCO, RENTE AO TOPO ---
                    // Certifique-se de que o nome da sua imagem está correto abaixo: R.drawable.NOME_DA_SUA_IMAGEM
                    Image(
                        painter = painterResource(id = R.drawable.cidade_zelus),
                        contentDescription = "Ilustração da cidade Zelus",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp)) // A imagem segue a curva
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f) // Garante que a área de botões ocupa o resto do branco
                            .padding(horizontal = 32.dp, vertical = 24.dp),
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
}

// COMPONENTE DO BOTÃO (MANTIDO IGUAL)
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
package com.example.projectandroid.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectandroid.R

@Composable
fun ZelusSplashScreen(onAcessarClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7EAB0))
            .systemBarsPadding(), // Isso evita que o nome "Zelus" fique escondido atrás do relógio do celular
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        // Título
        Text(
            text = "Zelus",
            fontSize = 72.sp, // Aumentei o tamanho
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Serif, // Fonte mais elegante
            color = Color.White,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.25f), // Sombra levemente transparente
                    offset = Offset(4f, 8f),
                    blurRadius = 8f
                )
            )
        )

        // AGRUPAMENTO: Coloco a Imagem e os Textos juntos na mesma coluna
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.zelus_app), // Certifique-se de que a imagem continua sendo zelus_app
                contentDescription = "Logo Zelus",
                modifier = Modifier.size(280.dp) // Aumentei um pouquinho a imagem também
            )

            Spacer(modifier = Modifier.height(16.dp)) // Um pequeno espaço entre a imagem e o texto

            Text(
                text = "Denúncia",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Ambiente",
                fontSize = 24.sp,
                color = Color.White
            )
        }

        // Botão "Acessar"
        Button(
            onClick = { onAcessarClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
                .height(56.dp)
        ) {
            Text(
                text = "Acessar",
                color = Color(0xFFA7EAB0),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ZelusPreview() {
    MaterialTheme {
        ZelusSplashScreen(onAcessarClick = {})
    }
}
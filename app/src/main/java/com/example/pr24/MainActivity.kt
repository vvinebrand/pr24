package com.example.pr24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

// Модель для экранов
data class OnboardPage(
    val title: String,
    val description: String,
    val image: Int
)

// Данные для экранов
val onboardPages = listOf(
    OnboardPage(
        title = "Анализы",
        description = "Экспресс сбор и получение проб",
        image = R.drawable.onboard1_image // Замените на свои изображения
    ),
    OnboardPage(
        title = "Уведомления",
        description = "Вы быстро узнаете о результатах",
        image = R.drawable.onboard2_image
    ),
    OnboardPage(
        title = "Мониторинг",
        description = "Наши врачи всегда наблюдают за вашими показателями здоровья",
        image = R.drawable.onboard3_image
    )
)

// Основной экран Onboarding
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Кнопка "Пропустить"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (pagerState.currentPage != onboardPages.size - 1) {
                TextButton(onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(onboardPages.size - 1)
                    }
                }) {
                    Text("Пропустить", fontSize = 14.sp, color = Color.Blue)
                }
            }
        }

        // Pager для экранов
        HorizontalPager(
            count = onboardPages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardPageContent(onboardPages[page])
        }

        // Индикатор страниц
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = Color.Blue,
            inactiveColor = Color.LightGray
        )

        // Кнопка "Завершить" на последнем экране
        if (pagerState.currentPage == onboardPages.size - 1) {
            Button(
                onClick = { onFinish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Завершить")
            }
        }
    }
}

// Содержимое одной страницы
@Composable
fun OnboardPageContent(page: OnboardPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = page.title,
            style = MaterialTheme.typography.h5,
            color = Color(0xFF4CAF50)
        )
        Text(
            text = page.description,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// Главный Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                OnboardingScreen(onFinish = {
                    // Обработка завершения Onboarding (переход на главный экран)
                })
            }
        }
    }
}

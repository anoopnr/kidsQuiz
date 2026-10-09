package com.aadil.quiz

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.aadil.quiz.ui.theme.AadilsQuizTheme
import com.aadil.quiz.ui.theme.BerrySoft
import com.aadil.quiz.ui.theme.CardWhite
import com.aadil.quiz.ui.theme.Cream
import com.aadil.quiz.ui.theme.Grape
import com.aadil.quiz.ui.theme.Ink
import com.aadil.quiz.ui.theme.LeafGreen
import com.aadil.quiz.ui.theme.LeafSoft
import com.aadil.quiz.ui.theme.SkySoft
import com.aadil.quiz.ui.theme.SunshineSoft
import java.util.Locale

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    private var textToSpeech: TextToSpeech? = null
    private var speechReady by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        textToSpeech = TextToSpeech(this, this)
        setContent {
            AadilsQuizTheme {
                QuizApp(speechReady = speechReady, speak = ::speak)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech?.apply {
                val result = setLanguage(Locale.forLanguageTag("en-IN"))
                setSpeechRate(0.68f)
                setPitch(1.03f)
                speechReady = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
            }
        }
    }

    private fun speak(words: String) {
        if (speechReady) {
            textToSpeech?.speak(words, TextToSpeech.QUEUE_FLUSH, null, "kids-fun-quiz")
        }
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}

@Composable
private fun QuizApp(speechReady: Boolean, speak: (String) -> Unit) {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences("aadil_quiz_progress", ComponentActivity.MODE_PRIVATE)
    }
    val bestScores = remember {
        mutableStateMapOf<String, Int>().apply {
            quizSections.forEach { section ->
                val saved = preferences.getInt(section.id, -1)
                if (saved >= 0) put(section.id, saved)
            }
        }
    }
    var selectedSection by remember { mutableStateOf<Int?>(null) }
    var showNextHint by remember {
        mutableStateOf(!preferences.getBoolean("next_hint_seen", false))
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        if (selectedSection == null) {
            HomeScreen(
                scores = bestScores,
                speechReady = speechReady,
                speak = speak,
                onChooseSection = { selectedSection = it },
            )
        } else {
            val section = quizSections[selectedSection!!]
            QuizScreen(
                section = section,
                speechReady = speechReady,
                speak = speak,
                animateNextHint = showNextHint,
                onNextHintSeen = {
                    showNextHint = false
                    preferences.edit().putBoolean("next_hint_seen", true).apply()
                },
                onBack = { selectedSection = null },
                onFinished = { score ->
                    val oldScore = bestScores[section.id] ?: 0
                    if (score > oldScore) {
                        bestScores[section.id] = score
                        preferences.edit().putInt(section.id, score).apply()
                    }
                },
            )
        }
    }
}

@Composable
private fun HomeScreen(
    scores: Map<String, Int>,
    speechReady: Boolean,
    speak: (String) -> Unit,
    onChooseSection: (Int) -> Unit,
) {
    LaunchedEffect(speechReady) {
        if (speechReady) speak("Welcome to Kids Fun Quiz. Choose a fun activity to begin.")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SkySoft, Cream, SunshineSoft.copy(alpha = 0.55f))))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Kids Fun Quiz", style = MaterialTheme.typography.headlineLarge, color = Grape)
                Text("Learn, listen and play!", fontSize = 17.sp, color = Ink.copy(alpha = 0.72f))
            }
            Card(
                onClick = { speak("Choose a fun activity to begin.") },
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(3.dp),
            ) {
                Text("🔊", fontSize = 27.sp, modifier = Modifier.padding(12.dp))
            }
        }

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Grape),
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("🦁", fontSize = 46.sp)
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Hello, little explorer!", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    Text("Pick a card and show what you know.", color = Color.White.copy(alpha = 0.9f), fontSize = 15.sp)
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val columns = if (maxWidth >= 760.dp) 4 else if (maxWidth >= 520.dp) 3 else 2
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                itemsIndexed(quizSections) { index, section ->
                    SectionCard(
                        section = section,
                        index = index,
                        bestScore = scores[section.id],
                        onClick = { onChooseSection(index) },
                    )
                }
            }
        }
    }
}

private val cardColours = listOf(
    SunshineSoft, SkySoft, LeafSoft, BerrySoft,
    Color(0xFFEDE5FF), Color(0xFFFFEBD9), Color(0xFFE0F5F0), Color(0xFFFFF3D8),
)

@Composable
private fun SectionCard(section: QuizSection, index: Int, bestScore: Int?, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColours[index % cardColours.size]),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 7.dp),
        modifier = Modifier.fillMaxWidth().height(154.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(section.icon, fontSize = 43.sp)
            Spacer(Modifier.height(5.dp))
            Text(
                section.title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                lineHeight = 20.sp,
                color = Ink,
            )
            if (bestScore != null) {
                Spacer(Modifier.height(5.dp))
                Text("⭐ $bestScore / ${section.questions.size}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = LeafGreen)
            }
        }
    }
}

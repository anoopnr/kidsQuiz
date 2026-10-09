package com.aadil.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aadil.quiz.ui.theme.Berry
import com.aadil.quiz.ui.theme.BerrySoft
import com.aadil.quiz.ui.theme.CardWhite
import com.aadil.quiz.ui.theme.Cream
import com.aadil.quiz.ui.theme.Grape
import com.aadil.quiz.ui.theme.Ink
import com.aadil.quiz.ui.theme.LeafGreen
import com.aadil.quiz.ui.theme.LeafSoft
import com.aadil.quiz.ui.theme.SkyBlue
import com.aadil.quiz.ui.theme.SkySoft
import com.aadil.quiz.ui.theme.Sunshine
import com.aadil.quiz.ui.theme.SunshineSoft

private enum class Feedback { NONE, TRY_AGAIN, CORRECT, REVEALED }

@Composable
fun QuizScreen(
    section: QuizSection,
    speechReady: Boolean,
    speak: (String) -> Unit,
    animateNextHint: Boolean,
    onNextHintSeen: () -> Unit,
    onBack: () -> Unit,
    onFinished: (Int) -> Unit,
) {
    var questionIndex by remember(section.id) { mutableIntStateOf(0) }
    var score by remember(section.id) { mutableIntStateOf(0) }
    var attempts by remember(section.id) { mutableIntStateOf(0) }
    var feedback by remember(section.id) { mutableStateOf(Feedback.NONE) }
    var wrongIndex by remember(section.id) { mutableIntStateOf(-1) }
    var shakeKey by remember(section.id) { mutableIntStateOf(0) }
    var finished by remember(section.id) { mutableStateOf(false) }

    if (finished) {
        ResultScreen(
            section = section,
            score = score,
            speak = speak,
            onHome = onBack,
            onAgain = {
                questionIndex = 0
                score = 0
                attempts = 0
                feedback = Feedback.NONE
                wrongIndex = -1
                finished = false
            },
        )
        return
    }

    val question = section.questions[questionIndex]
    val correctChoice = question.choices[question.correctIndex]
    val isLocked = feedback == Feedback.CORRECT || feedback == Feedback.REVEALED
    val scrollState = rememberScrollState()

    fun readQuestion() {
        speak("${section.title}. ${question.instruction} ${question.prompt}")
    }

    LaunchedEffect(speechReady, questionIndex) {
        if (speechReady) readQuestion()
    }
    LaunchedEffect(questionIndex) {
        scrollState.scrollTo(0)
    }
    LaunchedEffect(feedback) {
        if (feedback != Feedback.NONE) scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Cream, SkySoft.copy(alpha = 0.55f))))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onBack) {
                Text("‹ Home", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Grape)
            }
            Text(
                "${section.icon}  ${section.title}",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 21.sp,
                color = Ink,
            )
            TextButton(onClick = ::readQuestion) {
                Text("🔊", fontSize = 24.sp)
            }
        }

        LinearProgressIndicator(
            progress = { (questionIndex + 1f) / section.questions.size },
            modifier = Modifier.fillMaxWidth().height(9.dp).clip(CircleShape),
            color = LeafGreen,
            trackColor = LeafSoft,
        )
        Text(
            "Question ${questionIndex + 1} of ${section.questions.size}",
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp, bottom = 9.dp),
            textAlign = TextAlign.End,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Ink.copy(alpha = 0.58f),
        )

        Column(
            modifier = Modifier.weight(1f).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        question.instruction,
                        color = Grape,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    if (question.hero.isNotBlank()) {
                        Text(
                            question.hero,
                            fontSize = if (question.kind == QuestionKind.LETTER) 66.sp else 48.sp,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                    Text(
                        question.prompt,
                        modifier = Modifier.padding(top = 8.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        fontSize = if (question.kind == QuestionKind.LETTER) 38.sp else 25.sp,
                        lineHeight = if (question.kind == QuestionKind.LETTER) 44.sp else 31.sp,
                        letterSpacing = if (question.kind == QuestionKind.LETTER) 4.sp else 0.sp,
                        color = Ink,
                    )
                }
            }

            Spacer(Modifier.height(15.dp))
            ChoiceGrid(
                question = question,
                isLocked = isLocked,
                wrongIndex = wrongIndex,
                feedback = feedback,
                shakeKey = shakeKey,
                onSelect = { index ->
                    if (index == question.correctIndex) {
                        score += 1
                        feedback = Feedback.CORRECT
                        wrongIndex = -1
                        speak("Brilliant! ${correctChoice.text} is correct!")
                    } else {
                        attempts += 1
                        wrongIndex = index
                        shakeKey += 1
                        if (attempts == 1) {
                            feedback = Feedback.TRY_AGAIN
                            speak("Oops, almost! Have one more try.")
                        } else {
                            feedback = Feedback.REVEALED
                            val learningTip = if (question.tip.isBlank()) "" else " ${question.tip}"
                            speak("Let's learn it. The correct answer is ${correctChoice.text}.$learningTip")
                        }
                    }
                },
            )

            AnimatedVisibility(visible = feedback != Feedback.NONE) {
                FeedbackCard(feedback = feedback, answer = correctChoice.text, tip = question.tip)
            }

            Spacer(Modifier.height(18.dp))
        }

        AnimatedVisibility(visible = isLocked) {
            NextActionButton(
                isLastQuestion = questionIndex == section.questions.lastIndex,
                animateHint = animateNextHint,
                onClick = {
                    if (animateNextHint) onNextHintSeen()
                    if (questionIndex == section.questions.lastIndex) {
                        onFinished(score)
                        finished = true
                        speak("Wonderful work! You finished ${section.title} and earned $score stars.")
                    } else {
                        questionIndex += 1
                        attempts = 0
                        feedback = Feedback.NONE
                        wrongIndex = -1
                    }
                },
            )
        }
    }
}

@Composable
private fun ChoiceGrid(
    question: QuizQuestion,
    isLocked: Boolean,
    wrongIndex: Int,
    feedback: Feedback,
    shakeKey: Int,
    onSelect: (Int) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val columns = if (maxWidth >= 760.dp) 4 else 2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            question.choices.indices.chunked(columns).forEach { rowIndices ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    rowIndices.forEach { index ->
                        val showCorrect = isLocked && index == question.correctIndex
                        val showWrong = index == wrongIndex && feedback != Feedback.NONE
                        AnswerCard(
                            choice = question.choices[index],
                            kind = question.kind,
                            showCorrect = showCorrect,
                            showWrong = showWrong,
                            shakeKey = if (showWrong) shakeKey else 0,
                            enabled = !isLocked,
                            onClick = { onSelect(index) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                    repeat(columns - rowIndices.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun NextActionButton(
    isLastQuestion: Boolean,
    animateHint: Boolean,
    onClick: () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "nextHint")
    val arrowOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (animateHint) 15f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(520),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "nextArrow",
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Grape),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 12.dp)
            .height(62.dp),
    ) {
        Text(
            if (isLastQuestion) "See my stars" else "Next question",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.width(12.dp))
        Text(
            if (isLastQuestion) "⭐" else "➜",
            fontSize = 24.sp,
            modifier = Modifier.graphicsLayer { translationX = arrowOffset },
        )
    }
}

@Composable
private fun AnswerCard(
    choice: QuizChoice,
    kind: QuestionKind,
    showCorrect: Boolean,
    showWrong: Boolean,
    shakeKey: Int,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shake = remember { Animatable(0f) }
    val scale by animateFloatAsState(
        targetValue = if (showCorrect) 1.055f else 1f,
        animationSpec = tween(260),
        label = "answerScale",
    )
    LaunchedEffect(shakeKey) {
        if (shakeKey > 0) {
            shake.snapTo(0f)
            listOf(-14f, 14f, -10f, 10f, -5f, 5f, 0f).forEach { offset ->
                shake.animateTo(offset, tween(45))
            }
        }
    }

    val background = when {
        showCorrect -> LeafSoft
        showWrong -> BerrySoft
        else -> CardWhite
    }
    val border = when {
        showCorrect -> BorderStroke(4.dp, LeafGreen)
        showWrong -> BorderStroke(3.dp, Berry)
        else -> BorderStroke(2.dp, SkyBlue.copy(alpha = 0.30f))
    }

    Card(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = background, disabledContainerColor = background),
        border = border,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(if (kind == QuestionKind.LETTER) 1.55f else 1.25f)
            .graphicsLayer {
                translationX = shake.value
                scaleX = scale
                scaleY = scale
            },
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(10.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                if (kind == QuestionKind.PICTURE) {
                    Text(choice.visual, fontSize = 51.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    choice.text,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = if (kind == QuestionKind.LETTER) 34.sp else 17.sp,
                    lineHeight = 20.sp,
                    color = Ink,
                )
            }
            if (showCorrect) {
                Text(
                    "✓",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(LeafGreen)
                        .padding(horizontal = 7.dp, vertical = 3.dp),
                )
            }
        }
    }
}

@Composable
private fun FeedbackCard(feedback: Feedback, answer: String, tip: String) {
    val icon: String
    val title: String
    val detail: String
    val accent: Color
    when (feedback) {
        Feedback.TRY_AGAIN -> {
            icon = "🤔"
            title = "Almost!"
            detail = "Look carefully and try once more."
            accent = Sunshine
        }
        Feedback.CORRECT -> {
            icon = "🥳"
            title = "Brilliant!"
            detail = "You found the right answer."
            accent = LeafGreen
        }
        Feedback.REVEALED -> {
            icon = "🦉"
            title = "Let’s learn it!"
            detail = "The correct answer is $answer."
            accent = Grape
        }
        Feedback.NONE -> {
            icon = ""
            title = ""
            detail = ""
            accent = SkyBlue
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = if (feedback == Feedback.TRY_AGAIN) SunshineSoft else CardWhite),
        border = BorderStroke(2.dp, accent.copy(alpha = 0.55f)),
        modifier = Modifier.fillMaxWidth().padding(top = 14.dp).animateContentSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(icon, fontSize = 42.sp)
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.Black, fontSize = 20.sp, color = accent)
                Text(detail, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Ink)
                if (feedback == Feedback.REVEALED && tip.isNotBlank()) {
                    Text(tip, fontSize = 14.sp, color = Ink.copy(alpha = 0.72f), modifier = Modifier.padding(top = 3.dp))
                }
                if (feedback == Feedback.CORRECT) {
                    Text("⭐  ✨  🌟", fontSize = 20.sp, modifier = Modifier.padding(top = 3.dp))
                }
            }
        }
    }
}

@Composable
private fun ResultScreen(
    section: QuizSection,
    score: Int,
    speak: (String) -> Unit,
    onHome: () -> Unit,
    onAgain: () -> Unit,
) {
    LaunchedEffect(section.id, score) {
        speak("Wonderful work! You earned $score out of ${section.questions.size} stars.")
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SkySoft, Cream, SunshineSoft)))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(22.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("🏆", fontSize = 84.sp)
                Text("Wonderful work!", style = MaterialTheme.typography.headlineLarge, color = Grape, textAlign = TextAlign.Center)
                Text(section.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = 0.7f))
                Spacer(Modifier.height(18.dp))
                Text("$score / ${section.questions.size}", fontSize = 43.sp, fontWeight = FontWeight.Black, color = LeafGreen)
                Text("stars earned", fontSize = 17.sp, color = Ink.copy(alpha = 0.65f))
                Text("⭐  ⭐  ⭐", fontSize = 31.sp, modifier = Modifier.padding(vertical = 16.dp))
                Button(
                    onClick = onAgain,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                ) {
                    Text("Play again", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
                TextButton(onClick = onHome, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Choose another activity", color = Grape, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
            }
        }
    }
}

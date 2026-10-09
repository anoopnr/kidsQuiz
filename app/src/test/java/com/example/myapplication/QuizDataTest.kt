package com.aadil.quiz

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizDataTest {
    @Test
    fun questionBankIsCompleteAndValid() {
        assertTrue("Expected at least 10 activity sections", quizSections.size >= 10)
        assertEquals("Section IDs must be unique", quizSections.size, quizSections.map { it.id }.distinct().size)

        quizSections.forEach { section ->
            assertTrue("${section.title} needs at least five questions", section.questions.size >= 5)
            section.questions.forEachIndexed { index, question ->
                assertTrue("${section.title} question ${index + 1} has too few choices", question.choices.size >= 2)
                assertTrue(
                    "${section.title} question ${index + 1} has no valid answer",
                    question.correctIndex in question.choices.indices,
                )
                assertTrue("${section.title} question ${index + 1} has an empty prompt", question.prompt.isNotBlank())
            }
        }
    }

    @Test
    fun alphabetSectionUsesMissingLetterQuestions() {
        val alphabet = quizSections.first { it.id == "alphabet" }
        assertTrue(alphabet.questions.all { it.kind == QuestionKind.LETTER })
    }
}

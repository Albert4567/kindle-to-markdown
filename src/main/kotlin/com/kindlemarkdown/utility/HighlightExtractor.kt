package com.kindlemarkdown.utility

import com.kindlemarkdown.domain.Highlight
import mu.KotlinLogging
import com.kindlemarkdown.repository.BookRepository
import com.kindlemarkdown.repository.KindleFileRepository
import java.util.*
import kotlin.jvm.Throws

class HighlightExtractor private constructor() {
    companion object {
        val sharedInstance: HighlightExtractor = HighlightExtractor()
    }

    private val bookRepository: BookRepository = BookRepository.sharedInstance
    private val logger = KotlinLogging.logger {}

    @Throws(IndexOutOfBoundsException::class)
    fun extractHighlights() {
        logger.info { "Starting highlights extraction..." }
        val highlightSeparator = "=========="
        var highlightsFileContent: String = KindleFileRepository.sharedInstance.read()

        var splittedContent: List<String> = highlightsFileContent.split(highlightSeparator)
        splittedContent.forEach {
            var infos: List<String> = it.split("\r\n").filter { !it.isEmpty() }
            if (infos.isEmpty())
                return@forEach

            if (!bookRepository.containsBook(infos.get(0))) {
                logger.info { "Found new book: ${infos.get(0)}" }
                bookRepository.addBook(infos.get(0))
            }

            if (isHighlight(infos.get(1).substringBefore("|"))) {
                logger.info { "Adding highlight to ${infos.get(0)}" }
                addHighlight(infos.get(0), infos)
            } else {
                logger.info { "Adding note to ${infos.get(0)}" }
                bookRepository.addNoteTo(infos.get(0), extractNote(infos))
            }
        }
    }

    private fun addHighlight(bookName: String, infos: List<String>) {
        bookRepository.addHighlight(
            bookName,
            Highlight(
                infos.get(2)
            )
        )
    }

    private fun extractNote(noteContainer: List<String>): String = noteContainer.get(2)

    private fun isHighlight(infoContainer: String): Boolean = infoContainer.contains("Highlight")
}
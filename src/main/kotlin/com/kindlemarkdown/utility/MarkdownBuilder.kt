package com.kindlemarkdown.utility

import mu.KotlinLogging
import com.kindlemarkdown.repository.BookRepository
import java.io.File

class MarkdownBuilder private constructor() {
    companion object {
        val sharedInstance: MarkdownBuilder = MarkdownBuilder()
    }

    private val logger = KotlinLogging.logger {}

    fun build(fileName: String) {
        logger.info { "Starting markdown build process for $fileName" }
        val directory: String = "booksHighlights"
        if (!File(directory).mkdir()) {
            logger.warn { "Couldn't create directory $directory: it already exists or some error came up..." }
        }

        val file: File = File(".${File.separator}$directory${File.separator}$fileName.md")

        logger.info { "Building file content..." }
        var stringBuilder: String = buildString {
            BookRepository.sharedInstance.books.get(fileName)?.forEach {
                append("\n")
                append(" - ${it.content}")
                if (!it.note.isNullOrBlank()) {
                    append("\n")
                    append(" - **Note:** ${it.content}")
                }
                append("\n")
            }
        }

        logger.info { "Writing content to file..." }
        file.writeText(stringBuilder)
    }
}
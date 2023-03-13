package utility

import domain.Highlight
import repository.BookRepository
import repository.KindleFileRepository
import java.util.*
import kotlin.jvm.Throws

class HighlightExtractor private constructor() {
    companion object {
        val sharedInstance: HighlightExtractor = HighlightExtractor()
    }

    private val bookRepository: BookRepository = BookRepository.sharedInstance

    @Throws(IndexOutOfBoundsException::class)
    fun extractHighlights() {
        val highlightSeparator = "=========="
        var highlightsFileContent: String = KindleFileRepository.sharedInstance.read()

        var splittedContent: List<String> = highlightsFileContent.split(highlightSeparator)
        splittedContent.forEach {
            var infos: List<String> = it.split("\r\n").filter { !it.isEmpty() }
            if (infos.isEmpty())
                return@forEach

            if (!bookRepository.containsBook(infos.get(0)))
                bookRepository.addBook(infos.get(0))

            if (!infos.isEmpty() && isHighlight(infos.get(1).substringBefore("|"))) {
                addHighlight(infos.get(0), infos)
            } else {
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

    fun isHighlight(infoContainer: String): Boolean = infoContainer.contains("Highlight")
}
package repository

import domain.Highlight
import java.util.LinkedList

class BookRepository private constructor() {
    companion object {
        val sharedInstance: BookRepository = BookRepository()
    }

    var books: HashMap<String, LinkedList<Highlight>> = HashMap()
        private set

    fun addBook(bookName: String) {
        books.put(bookName, LinkedList())
    }

    fun containsBook(bookName: String): Boolean = books.containsKey(bookName)

    fun addHighlight(bookName: String, highlight: Highlight) {
        books.get(bookName)?.add(highlight)
    }

    fun addNoteTo(bookName: String, note: String) {
        books.get(bookName)?.last?.note = note
    }
}
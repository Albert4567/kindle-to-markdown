package com.kindlemarkdown

import com.kindlemarkdown.repository.BookRepository
import com.kindlemarkdown.utility.HighlightExtractor
import com.kindlemarkdown.utility.MarkdownBuilder

class Application

fun main() {
    HighlightExtractor.sharedInstance.extractHighlights()
    BookRepository.sharedInstance.books.keys.forEach {
        MarkdownBuilder.sharedInstance.build(it)
    }
}
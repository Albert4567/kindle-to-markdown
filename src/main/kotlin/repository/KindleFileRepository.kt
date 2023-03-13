package repository

import java.io.File

class KindleFileRepository private constructor() {
    private var myClippings: File = File("src/main/resources/My Clippings.txt")

    companion object {
        val sharedInstance: KindleFileRepository = KindleFileRepository()
    }

    fun read(): String = myClippings.readText()
}
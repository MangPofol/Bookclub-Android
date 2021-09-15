package com.example.bookclub.repository

import com.example.bookclub.model.BookModel
import com.example.bookclub.model.BookResData
import com.example.bookclub.model.NaverBookModel
import com.example.bookclub.service.BookService
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import retrofit2.Response
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.collections.ArrayList

class BookRepository {
    private val bookService: BookService = ApiClient.bookService
    private val bookNaverService: BookService = ApiClient.bookNaverService

    suspend fun getBooks(email: String, category: String): BookResData? {
        var books: List<BookModel> = ArrayList<BookModel>()
        var isSuccess: Boolean = false

        return bookService.getBooks(email, category).body()
    }

    suspend fun getNaverBooksByIsbn(isbn: String): NaverBookModel {
        var bookStr: String = bookNaverService.getNaverBooksByIsbn(isbn).body().toString()
        val dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
        val db: DocumentBuilder = dbf.newDocumentBuilder()
        val doc: Document = db.parse(InputSource(StringReader(bookStr)))
        val bookNodes: NodeList = doc.firstChild.firstChild.childNodes.item(7).childNodes

        return NaverBookModel(
            bookNodes.item(0).textContent,
            bookNodes.item(1).textContent,
            bookNodes.item(2).textContent
        )
    }

    suspend fun getNaverBooksByTitle(title: String): MutableList<NaverBookModel> {
        val regex: Regex = "<(.)>|<(/.)>".toRegex()
        val bookItems: MutableList<NaverBookModel> = ArrayList<NaverBookModel>()
        val bodyStr: String = bookNaverService.getNaverBooksByTitle(title).body().toString()
        val doc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(InputSource(StringReader(bodyStr)))

        val itemNodeList: NodeList = doc.getElementsByTagName("item")
        for (index in 0 until itemNodeList.length) {
            val book = itemNodeList.item(index)
            bookItems.add(
                NaverBookModel(
                    book.childNodes.item(0).textContent.replace(regex, ""),
                    book.childNodes.item(2).textContent,
                    book.childNodes.item(8).textContent
                )
            )
        }

        return bookItems
    }

    suspend fun createBook(book: BookModel): Response<BookModel> {
        return bookService.createBook(book)

    }
}
package com.mangpo.bookclub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mangpo.bookclub.ApplicationClass.Companion.database
import com.mangpo.bookclub.dao.BookDao
import com.mangpo.bookclub.model.entities.BookCategoryRequest
import com.mangpo.bookclub.model.entities.BookEntity
import com.mangpo.bookclub.model.entities.BookRequest
import com.mangpo.bookclub.model.remote.Book
import com.mangpo.bookclub.model.remote.KakaoBook
import com.mangpo.bookclub.model.remote.RecordResponse
import com.mangpo.bookclub.repository.BookRepositoryImpl
import kotlinx.coroutines.*

class BookViewModel: BaseViewModel() {
    private val bookRepository: BookRepositoryImpl = BookRepositoryImpl()
    private val bookDao: BookDao = database.bookDao()

    private val _kakaoBooks: MutableLiveData<List<Book>> = MutableLiveData()
    val kakaoBooks: LiveData<List<Book>> get() = _kakaoBooks

    private val _books: MutableLiveData<List<Book>> = MutableLiveData()
    val books: LiveData<List<Book>> get() = _books

    private val _createBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val createBookCode:  LiveData<Event<Int>> get() = _createBookCode
    var newBook: Book? = null

    private val _updateBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val updateBookCode: LiveData<Event<Int>> get() = _updateBookCode

    private val _deleteBookCode: MutableLiveData<Event<Int>> = MutableLiveData()
    val deleteBookCode: LiveData<Event<Int>> get() = _deleteBookCode

    private val _nowBooks: MutableLiveData<ArrayList<Book>> = MutableLiveData()
    val nowBooks: LiveData<ArrayList<Book>> get() = _nowBooks

    private val _afterBooks: MutableLiveData<ArrayList<Book>> = MutableLiveData()
    val afterBooks: LiveData<ArrayList<Book>> get() = _afterBooks

    private val _beforeBooks: MutableLiveData<ArrayList<Book>> = MutableLiveData()
    val beforeBooks: LiveData<ArrayList<Book>> get() = _beforeBooks

    private val _records: MutableLiveData<List<RecordResponse>> = MutableLiveData()
    val records: LiveData<List<RecordResponse>> get() = _records

    private fun kakaoBookToBook(kakaoBook: KakaoBook): Book = Book(
        name = kakaoBook.title,
        isbn = kakaoBook.isbn,
        image = kakaoBook.thumbnail
    )

    private fun setBookImg(position: Int, books: List<Book>, category: String) {
        if (position==books.size) {
            _books.postValue(books)

            when (category) {
                "NOW" -> _nowBooks.postValue((books as ArrayList<Book>))
                "AFTER" -> _afterBooks.postValue(books as ArrayList<Book>)
                "BEFORE" -> _beforeBooks.postValue(books as ArrayList<Book>)
            }

            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val img = bookDao.getImageByIsbn(books[position].isbn)

            if (img==null || img.isBlank()) {
                bookRepository.searchBooks(
                    query = books[position].isbn,
                    target = "isbn",
                    size = 1,
                    onResponse = {
                        viewModelScope.launch {
                            books[position].image = it.body()!!.documents[0].thumbnail
                            val job = launch(Dispatchers.IO) {
                                bookDao.insert(BookEntity(isbn = books[position].isbn, image = it.body()!!.documents[0].thumbnail))
                            }
                            job.join()
                            setBookImg(position + 1, books, category)
                        }
                    },
                    onFailure = {
                    }
                )
            } else {
                books[position].image = img
                setBookImg(position + 1, books, category)
            }
        }
    }

    fun searchBooks(title: String, target: String, size: Int) {
        bookRepository.searchBooks(
            query = title,
            target = target,
            size = size,
            onResponse = {
                if (it.code()==200) {
                    val kakaoBooks = it.body()!!.documents
                    val books: ArrayList<Book> = arrayListOf()

                    for (book in kakaoBooks) {
                        books.add(kakaoBookToBook(book))
                    }

                    _kakaoBooks.value = books
                }
            },
            onFailure = {
            }
        )
    }

    fun getBooksByCategory(category: String) {
        bookRepository.getBooksByCategory(
            category = category,
            onResponse = {
                if (it.code()==200)
                    setBookImg(0, it.body()!!.data, category)
            },
            onFailure = {
            }
        )
    }

    fun createBook(book: Book) {
        val isbn: String = book.isbn.split(" ")[0]
        val bookReq: BookRequest = BookRequest(name = book.name, isbn = isbn, category = book.category)

        bookRepository.createBook(
            book = bookReq,
            onResponse = {
                if (it.code()==201) {
                    newBook = it.body()!!
                    viewModelScope.launch (Dispatchers.IO) {
                        bookDao.insert(BookEntity(isbn = isbn, image = book.image))
                    }

                    when (it.body()!!.category) {
                        "NOW" -> _nowBooks.value?.add(it.body()!!)
                        "AFTER" -> _afterBooks.value?.add(it.body()!!)
                        else -> _beforeBooks.value?.add(it.body()!!)
                    }
                }

                _createBookCode.value = Event(it.code())
            },
            onFailure = {
                _createBookCode.value = Event(600)
            }
        )
    }

    fun updateBook(bookId: Int, category: String) {
        bookRepository.updateBook(
            bookId = bookId,
            categoryReq = BookCategoryRequest(category),
            onResponse = {
                if (it.code()==204) {
                    when (category) {
                        "NOW" -> _updateBookCode.value = Event(2040)
                        "AFTER" -> _updateBookCode.value = Event(2041)
                        "BEFORE" -> _updateBookCode.value = Event(2042)
                    }
                } else
                    _updateBookCode.value = Event(it.code())

            },
            onFailure = {
                _updateBookCode.value = Event(600)
            }
        )
    }

    fun deleteBook(bookId: Int) {
        bookRepository.deleteBook(
            bookId = bookId,
            onResponse = {
                _deleteBookCode.value = Event(it.code())
            },
            onFailure = {
                _deleteBookCode.value = Event(600)
            }
        )
    }

    fun getPostsByBookId(bookId: Int) {
        bookRepository.getPostsByBookId(
            bookId = bookId,
            onResponse = {
                _records.value = it.body()?.data
            },
            onFailure = {
                _records.value = null
            }
        )
    }
}
package com.example.my;
import com.example.my.repositories.BooksRepository;
import java.util.List;

class BooksActivityPresenter {
    private  BooksActivityView view;
    private BooksRepository booksRepository;

    public BooksActivityPresenter(BooksActivityView view, BooksRepository booksRepository) {
        this.view =view;
        this.booksRepository = booksRepository;
    }

    public void loadBooks() {
        List<Book> bookList = booksRepository.getBooks();
        view.displayBooks(bookList);
    }
}

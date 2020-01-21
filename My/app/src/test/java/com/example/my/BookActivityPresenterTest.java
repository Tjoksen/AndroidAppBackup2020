package com.example.my;

import com.example.my.repositories.BooksRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

import static junit.framework.TestCase.assertTrue;

public class BookActivityPresenterTest {
    String result="";

    // Simple subscription to a fix value
    @Test
    public void returnAValue(){
        result = "";
        Observable<String> observer = Observable.just("Hello"); // provides data
        observer.subscribe(s -> result=s); // Callable as subscriber
        assertTrue(result.equals("Hello"));
    }
     @Test
    public  void shouldPassBooksToView(){
        //given
        BooksActivityView view=new MockView();
        BooksRepository booksRepository= new MockBooksRepository();
        //when
        BooksActivityPresenter presenter = new BooksActivityPresenter(view,booksRepository);
         presenter.loadBooks();
        //then
        Assert.assertEquals(true,((MockView )(view)).displayBooksWithBooksCalled);
    }
    @Test
    public  void shouldHandleNoBooksFound(){

    }
    private  class MockView implements  BooksActivityView{
        boolean displayBooksWithBooksCalled;
        boolean displayBooksWithNoBooksCalled;
        @Override
        public void displayBooks(List<Book> bookList) {
            if(bookList.size()==3) displayBooksWithBooksCalled=true;
        }

        @Override
        public void displayNoBooks() {
            displayBooksWithNoBooksCalled=true;
        }
    }
    private  class  MockBooksRepository implements BooksRepository{

        @Override
        public List<Book> getBooks() {
            return Arrays.asList(new Book(),new Book(),new Book());
        }
    }
}

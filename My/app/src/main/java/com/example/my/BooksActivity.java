package com.example.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my.animalApp.AnimalMainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


public class BooksActivity extends AppCompatActivity{ //implements BooksActivityView {
   /* private  BooksActivityPresenter presenter;
    @BindView(R.id.buttonBook1)
    Button book1;
    @BindView(R.id.buttonBook2)
    Button book2;
    @BindView(R.id.button3)
    Button book3;

    @BindString(R.string.energetic)
    String energetic;
    private View.OnClickListener book1onClickListener;
    private View.OnClickListener book2onClickListener;
    private View.OnClickListener book3onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        book1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), AnimalMainActivity.class);
                startActivity(intent);
            }
        });

        book1.setOnClickListener(book1onClickListener);
        book2.setOnClickListener(book2onClickListener);
        book3.setOnClickListener(book3onClickListener);
        presenter = new BooksActivityPresenter(this, null);
        book1onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookObservable.subscribe();
            }
        };
        book2onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        book3onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

    @Override
    public void displayBooks(List<Book> bookList) {

    }

    @Override
    public void displayNoBooks() {

    }
    Observable<Book> BookObservable = Observable.create(new ObservableOnSubscribe<Book>() {
        @Override
        public void subscribe(ObservableEmitter<Book> emitter) throws Exception {
            try {
                List<Book> Books = new ArrayList<>();
               Books.add( new Book(getApplicationContext(),"Book 1"));
                Books.add(new Book( getApplicationContext(),"Book 2"));
                Books.add(new Book( getApplicationContext(),"Book 3"));
                for (Book Book : Books) {
                    emitter.onNext(Book);
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }
    });*/
}

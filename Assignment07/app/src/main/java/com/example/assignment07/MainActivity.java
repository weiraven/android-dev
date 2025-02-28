package com.example.assignment07;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements GenresFragment.GenresListener, BooksFragment.BooksListener, BookDetailsFragment.BookDetailsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main, new GenresFragment(), "genres-fragment")
                .commit();
    }

    @Override
    public void sendSelectedGenre(String genre) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, BooksFragment.newInstance(genre), "books-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void sendSelectedBook(Book book) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, BookDetailsFragment.newInstance(book), "book-details-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToGenres() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goBackToBooks() {
        getSupportFragmentManager().popBackStack();
    }
}
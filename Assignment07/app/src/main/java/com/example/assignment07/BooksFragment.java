package com.example.assignment07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.assignment07.databinding.FragmentBooksBinding;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private static final String ARG_PARAM_GENRE = "ARG_PARAM_GENRE";
    private String mGenre;
    private ArrayList<Book> booksList;

    public BooksFragment() {
        // Required empty public constructor
    }

    public static BooksFragment newInstance(String genre) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_GENRE, genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGenre = getArguments().getString(ARG_PARAM_GENRE);
        }
    }

    FragmentBooksBinding binding;
    BooksAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.textViewBooksGenreLabel.setText(mGenre);
        getActivity().setTitle(mGenre);

        booksList = Data.getBooksByGenre(mGenre);
        adapter = new BooksAdapter(getActivity(), booksList);
        binding.listViewBooks.setAdapter(adapter);

        binding.listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = booksList.get(position);
                mListener.sendSelectedBook(book);
            }
        });

        binding.buttonBooksBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToGenres();
            }
        });
    }

    // Need to make a custom adapter since books list has a custom layout for each book
    class BooksAdapter extends ArrayAdapter<Book> {
        public BooksAdapter(@NonNull Context context, @NonNull List<Book> objects) {
            super(context, R.layout.book_list_item, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.book_list_item, parent, false);
            }

            TextView textViewListBookTitle = convertView.findViewById(R.id.textViewListBookTitle);
            TextView textViewListAuthor = convertView.findViewById(R.id.textViewListAuthor);
            TextView textViewListGenre = convertView.findViewById(R.id.textViewListGenre);
            TextView textViewListYear = convertView.findViewById(R.id.textViewListYear);

            Book book = getItem(position);

            textViewListBookTitle.setText(book.getTitle());
            textViewListAuthor.setText(book.getAuthor());
            textViewListGenre.setText(book.getGenre());
            textViewListYear.setText(String.valueOf(book.getYear()));

            return convertView;
        }
    }

    BooksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (BooksListener) context;
    }

    interface BooksListener {
        void sendSelectedBook(Book book);
        void goBackToGenres();
    }

}
package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class bookAdapter extends RecyclerView.Adapter<bookAdapter.BookViewHolder> {
    private List<Book> books;

    public bookAdapter(List<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleText.setText(book.getTitle());
        holder.authorText.setText(book.getAuthor());
        holder.categoryText.setText(book.getCategory());
        holder.statusText.setText(book.getStatus());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        public BreakIterator bookAuthor;
        public BreakIterator bookTitle;
        public BreakIterator bookDesc;
        public Object bookImage;
        TextView titleText, authorText, categoryText, statusText;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_text);
            authorText = itemView.findViewById(R.id.author_text);
            categoryText = itemView.findViewById(R.id.category_text);
            statusText = itemView.findViewById(R.id.status_text);
        }
    }
}
package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class Book_Adapter extends RecyclerView.Adapter<Book_Adapter.BookViewHolder> {

    Context context;
    ArrayList<BookModel> bookList;

    public Book_Adapter(Context context, ArrayList<BookModel> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        BookModel book = bookList.get(position);

        holder.bookTitle.setText(book.getTitle());
        holder.bookAuthor.setText("by " + book.getAuthor());
        holder.bookDesc.setText(book.getDescription());


        // Load book image using Glide
        Glide.with(context)
                .load(book.getImageUrl())
                .placeholder(R.drawable.img_2) // default image
                .into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, bookDesc;
        ImageView bookImage;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookAuthor = itemView.findViewById(R.id.bookAuthor);
            bookDesc = itemView.findViewById(R.id.bookDesc);
            bookImage = itemView.findViewById(R.id.bookImage);
        }
    }
}

package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class libraryAdapter extends RecyclerView.Adapter<libraryAdapter.LibraryViewHolder> {
    private final List<library> libraryList;

    public libraryAdapter(List<library> libraryList) {
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_liberary_item, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        library library = libraryList.get(position);
        holder.bind(library);
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        private final TextView libraryName;
        private final TextView libraryAddress;
        private final TextView libraryMobile;
        private final Button joinButton;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            libraryName = itemView.findViewById(R.id.libraryName);
            libraryAddress = itemView.findViewById(R.id.libraryAddress);
            libraryMobile = itemView.findViewById(R.id.libraryMobile);
            TextView liberaryArea = itemView.findViewById(R.id.libraryArea);
            joinButton = itemView.findViewById(R.id.enterLibraryButton);
        }

        public void bind(library library) {
            libraryName.setText(library.getName());
            libraryAddress.setText(library.getAddress());
            libraryMobile.setText(library.getMobileNumber());

            if (library.isJoined()) {
                joinButton.setText("Joined");
                joinButton.setEnabled(false);
            } else {
                joinButton.setText("Join Library");
                joinButton.setEnabled(true);
            }

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    library.setJoined(true);
                    joinButton.setText("Joined");
                    joinButton.setEnabled(false);
                    Toast.makeText(v.getContext(), "Successfully joined " + library.getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
} 
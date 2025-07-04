
package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LiberaryAdapter extends RecyclerView.Adapter<LiberaryAdapter.LibraryViewHolder> {

    private List<LiberaryModel> libraryList;

    public LiberaryAdapter(List<LiberaryModel> libraryList) {
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_liberary_item, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        LiberaryModel library = libraryList.get(position);
        holder.name.setText(library.getName());
        holder.address.setText(library.getAddress());
        holder.area.setText("Area: " + library.getArea()); // ✅ Area set
        holder.mobile.setText("Mobile: " + library.getMobile());
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    public void updateList(List<LiberaryModel> newList) {
        libraryList = newList;
        notifyDataSetChanged();
    }

    static class LibraryViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, area, mobile;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.libraryName);
            address = itemView.findViewById(R.id.libraryAddress);
            area = itemView.findViewById(R.id.libraryArea);  // ✅ Add this line
            mobile = itemView.findViewById(R.id.libraryMobile);
        }
    }
}

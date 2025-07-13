package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        private ValueEventListener statusListener;

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

            // Remove previous listener if exists
            if (statusListener != null) {
                FirebaseDatabase.getInstance().getReference("student_requests").removeEventListener(statusListener);
            }

            // Check if user has already sent a request for this library
            checkExistingRequest(library);

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Send join request to Firebase
                    sendJoinRequest(library);
                }
            });
        }

        private void checkExistingRequest(library library) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                // User not logged in, show default button state
                updateButtonStatus(false, "", library);
                return;
            }

            // Check for existing requests in Firestore
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("join_request")
                    .whereEqualTo("studentId", currentUser.getUid())
                    .whereEqualTo("libraryName", library.getName())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        boolean hasRequest = false;
                        String requestStatus = "";

                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            JoinRequestModel request = document.toObject(JoinRequestModel.class);
                            if (request != null) {
                                hasRequest = true;
                                requestStatus = request.getStatus();
                                break;
                            }
                        }

                        updateButtonStatus(hasRequest, requestStatus, library);
                    })
                    .addOnFailureListener(e -> {
                        // Handle error - show default button state
                        updateButtonStatus(false, "", library);
                    });
        }

        private void updateButtonStatus(boolean hasRequest, String status, library library) {
            if (hasRequest) {
                library.setJoined(true);
                joinButton.setEnabled(false);

                switch (status.toLowerCase()) {
                    case "pending":
                        joinButton.setText("⏳ Pending");
                        joinButton.setBackgroundResource(R.drawable.status_pending_bg);
                        break;
                    case "approved":
                        joinButton.setText("✅ Approved");
                        joinButton.setBackgroundResource(R.drawable.button_approve_bg);
                        break;
                    case "rejected":
                        joinButton.setText("❌ Rejected");
                        joinButton.setBackgroundResource(R.drawable.button_reject_bg);
                        break;
                    default:
                        joinButton.setText("Join Now ✨");
                        joinButton.setBackgroundResource(R.drawable.join_button_gradient);
                        joinButton.setEnabled(true);
                        break;
                }
            } else {
                library.setJoined(false);
                joinButton.setText("Join Now ✨");
                joinButton.setEnabled(true);
                joinButton.setBackgroundResource(R.drawable.join_button_gradient);
            }
        }

        private void sendJoinRequest(library library) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(itemView.getContext(), "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate request ID
            String requestId = "req_" + System.currentTimeMillis();

            // Create request model for Firestore
            JoinRequestModel request = new JoinRequestModel(
                    requestId,
                    currentUser.getUid(),
                    currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Student",
                    currentUser.getEmail(),
                    library.getName(), // Using library name as ID for now
                    library.getName(),
                    "pending",
                    "Student wants to join this library",
                    com.google.firebase.Timestamp.now()
            );

            // Save to Firestore
            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("join_request")
                    .document(requestId)
                    .set(request)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(itemView.getContext(),
                                "Request sent successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(itemView.getContext(),
                                "Failed to send request: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
} 
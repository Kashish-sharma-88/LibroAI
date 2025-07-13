package com.example.libroai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class StdForumGroupAdapter extends RecyclerView.Adapter<StdForumGroupAdapter.ViewHolder> {
    
    private List<StdForumGroupModel> groupList;
    private Context context;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    
    public StdForumGroupAdapter(Context context, List<StdForumGroupModel> groupList) {
        this.context = context;
        this.groupList = groupList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_std_forum_group, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StdForumGroupModel group = groupList.get(position);
        
        holder.groupName.setText(group.getGroupName());
        holder.groupDescription.setText(group.getGroupDescription());
        holder.adminName.setText("Admin: " + group.getAdminName());
        holder.memberCount.setText(group.getMemberCount() + " members");
        holder.category.setText(group.getCategory());
        
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) return;
                DocumentReference groupDocRef = firestore.collection("forums").document(group.getGroupId());
                groupDocRef.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        List<String> members = (List<String>) snapshot.get("members");
                        if (members != null && members.contains(currentUser.getUid())) {
                            // User is already a member, open chat directly
                            Intent intent = new Intent(context, StdGroupChatActivity.class);
                            intent.putExtra("groupId", group.getGroupId());
                            context.startActivity(intent);
                        } else {
                            // Not a member, open join group activity
                            Intent intent = new Intent(context, StdJoinGroupActivity.class);
                            intent.putExtra("groupId", group.getGroupId());
                            intent.putExtra("groupName", group.getGroupName());
                            intent.putExtra("groupDescription", group.getGroupDescription());
                            intent.putExtra("adminName", group.getAdminName());
                            intent.putExtra("memberCount", group.getMemberCount());
                            intent.putExtra("category", group.getCategory());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public int getItemCount() {
        return groupList.size();
    }
    
    public void updateList(List<StdForumGroupModel> newList) {
        this.groupList = newList;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView groupName, groupDescription, adminName, memberCount, category;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.forum_group_card);
            groupName = itemView.findViewById(R.id.group_name);
            groupDescription = itemView.findViewById(R.id.group_description);
            adminName = itemView.findViewById(R.id.admin_name);
            memberCount = itemView.findViewById(R.id.member_count);
            category = itemView.findViewById(R.id.category);
        }
    }
} 
package com.apis.features.shareLote.already_shared_users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.model.User;
import java.util.List;

public class AlreadySharedUsersAdapter extends RecyclerView.Adapter<AlreadySharedUsersViewHolder>{

    final private List<User> users;
    final private Context context;

    public AlreadySharedUsersAdapter(List<User> users, Context context){
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public AlreadySharedUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shared_user, parent, false);
        return new AlreadySharedUsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlreadySharedUsersViewHolder holder, final int position) {
        holder.userEmail.setText(users.get(holder.getAbsoluteAdapterPosition()).getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
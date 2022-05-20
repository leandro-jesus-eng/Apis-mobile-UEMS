package com.apis.features.shareLote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.features.shareLote.ShareLoteViewHolder;
import com.apis.model.Lote;
import com.apis.model.User;
import java.util.List;

public class ShareLoteAdapter extends RecyclerView.Adapter<ShareLoteViewHolder>{

    final private List<User> users;
    final private Context context;

    public ShareLoteAdapter(List<User> users, Context context){
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ShareLoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ShareLoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShareLoteViewHolder holder, final int position) {
        holder.userEmail.setText(users.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}

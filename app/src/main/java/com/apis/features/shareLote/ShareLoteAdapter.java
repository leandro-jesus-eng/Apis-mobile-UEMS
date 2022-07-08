package com.apis.features.shareLote;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.features.shareLote.ShareLoteViewHolder;
import com.apis.model.ItemUser;
import com.apis.model.Lote;
import com.apis.model.User;

import java.util.ArrayList;
import java.util.List;

public class ShareLoteAdapter extends RecyclerView.Adapter<ShareLoteViewHolder>{

    final private List<ItemUser> users;
    final private Context context;

    public List<User> getSelectedUsers() {
        List<User> selectedUsers = new ArrayList<>();
        for(ItemUser itemUser : users) {
            if(itemUser.isSelected()) selectedUsers.add(itemUser.getUser());
        }
        return selectedUsers;
    }

    public ShareLoteAdapter(List<ItemUser> users, Context context){
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
        ItemUser itemUser = users.get(holder.getAbsoluteAdapterPosition());
        holder.userEmail.setText(itemUser.getUser().getEmail());

        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemUser.setSelected(!itemUser.isSelected());
                changeItemColor(itemUser, holder);
            }
        });
    }

    private void changeItemColor(ItemUser itemUser, ShareLoteViewHolder holder) {
        if (itemUser.isSelected()) {
            //holder.selectedIconButton.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            holder.selectedIconButton.setImageResource(R.drawable.ic_baseline_check_green);
        } else {
            holder.selectedIconButton.setImageResource(R.drawable.ic_baseline_check_white);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
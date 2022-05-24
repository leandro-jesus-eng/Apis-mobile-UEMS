package com.apis.features.shareLote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
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
    final private ArrayList<Integer> selectedUsers = new ArrayList<>();

    public List<Integer> getSelectedUserIds() {
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
        setItemColor(itemUser, holder);

        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemUser.setSelected(!itemUser.isSelected());
                setItemColor(itemUser, holder);
            }
        });
    }

    private void setItemColor(ItemUser itemUser, ShareLoteViewHolder holder) {
        if(itemUser.isSelected()) {
            /*
            if(!selectedUsers.contains(itemUser.getUser().getUserId())) {
                selectedUsers.add(itemUser.getUser().getUserId());
            }

             */
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            /*
            if(selectedUsers.contains(itemUser.getUser().getUserId())) {
                selectedUsers.remove(itemUser.getUser().getUserId());
            }
            
             */
            holder.listItem.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
package com.apis.features.shareLote.already_shared_users;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;

public class AlreadySharedUsersViewHolder extends RecyclerView.ViewHolder {

    final TextView userEmail;

    public AlreadySharedUsersViewHolder(View view) {
        super(view);
        userEmail = (TextView) view.findViewById(R.id.shared_user_email_textView);
    }
}

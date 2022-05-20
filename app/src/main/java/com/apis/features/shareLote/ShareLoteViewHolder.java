package com.apis.features.shareLote;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;

public class ShareLoteViewHolder extends RecyclerView.ViewHolder {

    final ConstraintLayout listItem;
    final TextView userEmail;
    final ImageButton selectedIconButton;

    public ShareLoteViewHolder(View view) {
        super(view);
        listItem = (ConstraintLayout) view.findViewById(R.id.user_item);
        userEmail = (TextView) view.findViewById(R.id.user_email_textView);
        selectedIconButton = (ImageButton) view.findViewById(R.id.selected_icon_imageButton);
    }
}
package com.apis.features.shareLote;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;

public class ShareLoteViewHolder extends RecyclerView.ViewHolder {

    final ConstraintLayout listItem;
    final TextView userEmail;
    final ImageView selectedIconButton;

    public ShareLoteViewHolder(View view) {
        super(view);
        listItem = (ConstraintLayout) view.findViewById(R.id.user_item);
        userEmail = (TextView) view.findViewById(R.id.user_email_textView);
        selectedIconButton = (ImageView) view.findViewById(R.id.selected_icon_imageButton);
    }
}
package com.apis.features.comportamento_reprodutivo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;

public class AdicionarCompReprodutivoViewHolder extends RecyclerView.ViewHolder{

    final CardView item;
    final TextView nomeVaca;
    final TextView idVaca;
    final ImageView imgVaca;

    public AdicionarCompReprodutivoViewHolder(View view) {
        super(view);
        item = (CardView) view.findViewById(R.id.cardVaca);
        nomeVaca = (TextView) view.findViewById(R.id.vaca_nome_rep_textView);
        idVaca = (TextView) view.findViewById(R.id.vaca_id_rep_textView);
        imgVaca = (ImageView) view.findViewById(R.id.vaca_rep_imageView);
    }

}

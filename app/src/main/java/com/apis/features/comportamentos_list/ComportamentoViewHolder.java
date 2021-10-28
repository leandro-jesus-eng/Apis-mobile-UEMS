package com.apis.features.comportamentos_list;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;

public class ComportamentoViewHolder extends RecyclerView.ViewHolder {

    final ConstraintLayout itemLista;
    final TextView data;
    final TextView hora;
    final TextView info;

    public ComportamentoViewHolder(View view) {
        super(view);
        itemLista = (ConstraintLayout) view.findViewById(R.id.itemListaComportamento);
        data = (TextView) view.findViewById(R.id.lbl_dataC);
        hora = view.findViewById(R.id.lbl_horaC);
        info = (TextView) view.findViewById(R.id.lbl_infoC);
    }

}

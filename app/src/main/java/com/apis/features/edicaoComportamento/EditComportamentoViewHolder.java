package com.apis.features.edicaoComportamento;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;

public class EditComportamentoViewHolder extends RecyclerView.ViewHolder {

    final TextView nomeTipo;
    final ImageButton btnExcluirTipo;
    final ImageButton btnAdicionarComportamento;
    final LinearLayout linearLayout;

    public EditComportamentoViewHolder(View view) {
        super(view);
        nomeTipo = (TextView) view.findViewById(R.id.nomeTipo);
        btnExcluirTipo = (ImageButton) view.findViewById(R.id.imgThrash);
        btnAdicionarComportamento = (ImageButton) view.findViewById(R.id.imgAddComportamento);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
    }
}

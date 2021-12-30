package com.apis.features.edicaoComportamento;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;

public class EditComportamentoViewHolder extends RecyclerView.ViewHolder {

    final EditText edNomeTipo;
    final ImageButton btnExcluirTipo;
    final ImageButton btnAdicionarComportamento;
    final LinearLayout linearLayout;

    public EditComportamentoViewHolder(View view) {
        super(view);
        edNomeTipo = (EditText) view.findViewById(R.id.edNomeTipo);
        btnExcluirTipo = (ImageButton) view.findViewById(R.id.imgThrash);
        btnAdicionarComportamento = (ImageButton) view.findViewById(R.id.imgAddComportamento);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
    }
}

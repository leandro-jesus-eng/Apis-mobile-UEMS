package com.apis.features.animal_list;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;

public class AnimalViewHolder extends RecyclerView.ViewHolder {

    final ConstraintLayout itemLista;
    final TextView nome;
    final TextView idAnimal;
    final ImageButton btnExcluir;
    final TextView dataAnotacao;
    final TextView dataTitle;

    public AnimalViewHolder(View view) {
        super(view);
        itemLista = (ConstraintLayout) view.findViewById(R.id.itemListaAnimal);
        nome = (TextView) view.findViewById(R.id.lbl_nome_animal);
        idAnimal = (TextView) view.findViewById(R.id.lbl_id_animal);
        btnExcluir = (ImageButton) view.findViewById(R.id.btnDeleteAnimal);
        dataAnotacao = (TextView) view.findViewById(R.id.textUpdateDate);
        dataTitle = (TextView) view.findViewById(R.id.textDataTitle);
    }
}

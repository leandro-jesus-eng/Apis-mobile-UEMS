package com.apis.features.edicaoComportamento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.features.animal_list.AnimalViewHolder;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.models.Animal;

import java.util.List;

public class EditComportamentoAdapter extends RecyclerView.Adapter<EditComportamentoViewHolder> {

    private List<String> tipos;

    private Context context;

    public EditComportamentoAdapter(List tipos, Context context){
        this.tipos = tipos;
        this.context = context;
    }

    public void submitList(List<String> tiposNome){
        tipos.addAll(tiposNome);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EditComportamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_tipo_comportamento, parent, false);
        return new EditComportamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EditComportamentoViewHolder holder, final int position){
        holder.nomeTipo.setText(tipos.get(holder.getAdapterPosition()));

        holder.btnAdicionarComportamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton botao = new RadioButton(holder.linearLayout.getContext());
                ViewGroup.LayoutParams lparams =
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                botao.setLayoutParams(lparams);
                botao.setText("test");
                holder.linearLayout.addView(botao);
            }
        });

        holder.btnExcluirTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipos.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tipos.size();
    }

}

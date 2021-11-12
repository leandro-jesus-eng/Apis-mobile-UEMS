package com.apis.features.edicaoComportamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;

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
                final View comportamento = LayoutInflater.from(context).inflate(
                        R.layout.item_comportamento_edit, holder.linearLayout, false
                );
                ImageButton deleteComportamento = (ImageButton) comportamento.findViewById(R.id.imgAddTipo);
                holder.linearLayout.addView(comportamento);

                deleteComportamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.linearLayout.removeView(comportamento);
                    }
                });

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

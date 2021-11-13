package com.apis.features.edicaoComportamento;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

public class EditComportamentoAdapter extends RecyclerView.Adapter<EditComportamentoViewHolder> {

    private List<TipoComportamento> tipos = new ArrayList<>();
    private String textoAtual;

    private Context context;

    public EditComportamentoAdapter(Context context){
        this.context = context;
    }

    public List<TipoComportamento> getTipos() {
        return tipos;
    }

    public void submitList(List<TipoComportamento> listTipos){
        tipos.addAll(listTipos);
        notifyDataSetChanged();
    }
    public void submitItem(TipoComportamento tipo){
        tipos.add(tipo);
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
        if(!holder.edNomeTipo.equals("")){
            holder.edNomeTipo.setText(tipos.get(holder.getAdapterPosition()).getDescricao());
        }
        textoAtual = holder.edNomeTipo.getText().toString();

        holder.edNomeTipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textoAtual = editable.toString();
            }
        });
        tipos.get(holder.getAdapterPosition()).setDescricao(textoAtual);

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

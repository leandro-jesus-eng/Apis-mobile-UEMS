package com.apis.features.edicaoComportamento;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.database.DbRepository;
import com.apis.models.Comportamento;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

public class EditComportamentoAdapter extends RecyclerView.Adapter<EditComportamentoViewHolder> {

    private List<TipoComportamento> tipos = new ArrayList<>();
    private List<Comportamento> comportamentos = new ArrayList<>();

    private List<TipoComportamento> tiposExcluidos = new ArrayList<>();
    private List<Comportamento> comportamentosExcluidos = new ArrayList<>();

    private Context context;

    public EditComportamentoAdapter(Context context){
        this.context = context;
    }

    public List<TipoComportamento> getTipos() {
        return this.tipos;
    }
    public List<Comportamento> getComportamentos() {
        return this.comportamentos;
    }
    public List<Comportamento> getComportamentosExcluidos() {
        return this.comportamentosExcluidos;
    }
    public List<TipoComportamento> getTiposExcluidos() {
        return this.tiposExcluidos;
    }


    public void submitTipoList(List<TipoComportamento> listTipos){
        tipos.clear();
        tipos.addAll(listTipos);
        notifyDataSetChanged();
    }

    public void submitComportamentoList(List<Comportamento> listComportamentos){
        comportamentos.clear();
        comportamentos.addAll(listComportamentos);
        notifyDataSetChanged();
    }

    public void submitItem(TipoComportamento tipo){
        tipos.add(tipo);
        notifyItemInserted(getItemCount() - 1);
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

        for (Comportamento comportamento : comportamentos) {
            if (comportamento.getIdTipo() == tipos.get(holder.getAdapterPosition()).getId()) {
                createComportamento(holder, holder.getAdapterPosition(), false, comportamento.getNome());
            }
        }

        holder.edNomeTipo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               tipos.get(holder.getAdapterPosition()).setDescricao(editable.toString());
            }
        });

        holder.btnAdicionarComportamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComportamento(holder, holder.getAdapterPosition(), true, "");
            }
        });


        holder.btnExcluirTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tiposExcluidos.add(tipos.get(holder.getAdapterPosition()));
                tipos.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    public void createComportamento(
            EditComportamentoViewHolder holder, int position, boolean novo, String nome){

        View comportamento = LayoutInflater.from(context).inflate(
                R.layout.item_comportamento_edit, holder.linearLayout, false
        );
        EditText edNomeComportamento = comportamento.findViewById(R.id.edNomeComportamento);

        if(novo){
            comportamentos.add(new Comportamento(0, "", tipos.get(position).getId()));
        }else {
            edNomeComportamento.setText(nome);
        }

        edNomeComportamento.addTextChangedListener(new TextWatcher() {
            Comportamento comportamento = findComportamentoByName(
                    edNomeComportamento.getText().toString());
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                comportamento.setNome(editable.toString());
            }
        });

        ImageButton deleteComportamento = (ImageButton) comportamento.findViewById(R.id.imgAddTipo);
        holder.linearLayout.addView(comportamento);

        deleteComportamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.linearLayout.removeView(comportamento);
                comportamentosExcluidos.add(
                        findComportamentoByName(edNomeComportamento.getText().toString()));
                comportamentos.remove(
                        findComportamentoByName(edNomeComportamento.getText().toString()));
            }
        });
    }

    private Comportamento findComportamentoByName(String nome){

        for(Comportamento comportamento : comportamentos){
            if(comportamento.getNome().equals(nome)){
                return comportamento;
            }
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return tipos.size();
    }

}

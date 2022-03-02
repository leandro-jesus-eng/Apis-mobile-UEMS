package com.apis.features.edicaoComportamento;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.model.Comportamento;
import com.apis.model.TipoComportamento;
import java.util.ArrayList;
import java.util.List;

public class EditComportamentoAdapter extends RecyclerView.Adapter<EditComportamentoViewHolder> {

    final private List<TipoComportamento> tipos = new ArrayList<>();
    private List<Comportamento> comportamentos = new ArrayList<>();

    final private List<TipoComportamento> tiposExcluidos = new ArrayList<>();
    final private List<Comportamento> comportamentosExcluidos = new ArrayList<>();

    final private Context context;

    public EditComportamentoAdapter(Context context){
        this.context = context;
    }

    public List<TipoComportamento> getTipos() {
        return this.tipos;
    }
    public List<TipoComportamento> getTiposExcluidos() {
        return this.tiposExcluidos;
    }
    public List<Comportamento> getComportamentos() {
        return this.comportamentos;
    }
    public List<Comportamento> getComportamentosExcluidos() {
        return this.comportamentosExcluidos;
    }

    public void submitTipoList(List<TipoComportamento> listTipos){
        tipos.clear();
        tipos.addAll(listTipos);
        notifyDataSetChanged();
    }

    public void submitComportamentoList(List<Comportamento> listComportamentos){
        comportamentos.clear();
        comportamentos.addAll(listComportamentos);
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
            holder.edNomeTipo.setText(tipos.get(holder.getAbsoluteAdapterPosition()).getDescricao());
        }

        for (Comportamento comportamento : comportamentos) {
            if (comportamento.getIdTipo() == tipos.get(holder.getAbsoluteAdapterPosition()).getId()) {
                createComportamento(holder, holder.getAbsoluteAdapterPosition(), false, comportamento.getNome());
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
                TipoComportamento tipoComportamento = tipos.get(holder.getAdapterPosition());
                List<Comportamento> tempListComportamento = new ArrayList<>();

                for (Comportamento comportamento : comportamentos) {
                    if (comportamento.getIdTipo() == tipoComportamento.getId()) {
                        comportamentosExcluidos.add(comportamento);
                        tempListComportamento.add(comportamento);
                    }
                }

                if(tempListComportamento.size() > 0){
                    holder.linearLayout.removeAllViews();
                    comportamentos.removeAll(tempListComportamento);
                }

                tiposExcluidos.add(tipoComportamento);
                tipos.remove(tipoComportamento);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    private void createComportamento(
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

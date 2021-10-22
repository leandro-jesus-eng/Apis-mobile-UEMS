package com.apis.features.comportamentos_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.R;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;

import java.util.ArrayList;

public class ComportamentoAdapter extends RecyclerView.Adapter<ComportamentoViewHolder>{

    private ArrayList<AnotacaoComportamento> comportamentos;
    private Context context;


    public ComportamentoAdapter(ArrayList comportamentos, Context context){
        this.comportamentos = comportamentos;
        this.context = context;
    }


    @NonNull
    @Override
    public ComportamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_comportamento, parent, false);
        ComportamentoViewHolder comportamentoViewHolder = new ComportamentoViewHolder(view);
        return comportamentoViewHolder;
    }

    @Override
    public void onBindViewHolder(ComportamentoViewHolder holder, final int position)
    {
        holder.dataHora.setText(comportamentos.get(position).getDataHora());
        holder.info.setText("Obs: " +comportamentos.get(position).getObs());

        final AnotacaoComportamento comportamento = comportamentos.get(position);

    }

    @Override
    public int getItemCount() {
        return comportamentos.size();
    }
}

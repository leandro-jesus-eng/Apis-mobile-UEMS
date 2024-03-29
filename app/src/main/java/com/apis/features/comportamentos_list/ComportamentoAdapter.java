package com.apis.features.comportamentos_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.model.AnotacaoComportamento;
import java.util.List;

public class ComportamentoAdapter extends RecyclerView.Adapter<ComportamentoViewHolder>{

    private List<AnotacaoComportamento> comportamentos;
    private Context context;

    public ComportamentoAdapter(List comportamentos, Context context){
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
    public void onBindViewHolder(ComportamentoViewHolder holder, final int position) {

        holder.hora.setText(comportamentos.get(position).getHora());
        holder.data.setText(comportamentos.get(position).getData() );
        holder.info.setText("Obs: " +comportamentos.get(position).getObs());

    }

    @Override
    public int getItemCount() {
        return comportamentos.size();
    }
}

package com.apis.features.comportamento_reprodutivo;

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
import com.apis.models.Animal;
import com.apis.models.Comportamento;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

public class AdicionarCompReprodutivoAdapter extends RecyclerView.Adapter<AdicionarCompReprodutivoViewHolder> {

    private List<Animal> animais = new ArrayList<>();
    private Context context;

    public AdicionarCompReprodutivoAdapter(Context context){
        this.context = context;
    }

    public void submitList(List<Animal> listAnimais){
        animais.clear();
        animais.addAll(listAnimais);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdicionarCompReprodutivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vaca_rep, parent, false);
        return new AdicionarCompReprodutivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdicionarCompReprodutivoViewHolder holder, final int position){
        //holder.idVaca.setText(animais.get(holder.getAdapterPosition()).getId());
        holder.nomeVaca.setText(animais.get(holder.getAdapterPosition()).getNome());
        holder.imgVaca.setImageResource(R.drawable.cow);
    }

    @Override
    public int getItemCount() {
        return animais.size();
    }

}

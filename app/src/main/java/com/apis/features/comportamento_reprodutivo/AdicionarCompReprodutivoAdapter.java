package com.apis.features.comportamento_reprodutivo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.apis.models.FileControl;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.List;

public class AdicionarCompReprodutivoAdapter extends RecyclerView.Adapter<AdicionarCompReprodutivoViewHolder> {

    private List<Animal> animais = new ArrayList<>();
    private Animal vacaEmAnotacao;
    private Context context;
    private boolean isMontando;

    public AdicionarCompReprodutivoAdapter(Context context, Boolean isMontando, Animal vacaEmAnotacao){
        this.context = context;
        this.isMontando = isMontando;
        this.vacaEmAnotacao = vacaEmAnotacao;
    }

    public void submitList(List<Animal> listAnimais){
        animais.clear();
        animais.addAll(listAnimais);
        notifyDataSetChanged();
    }

    public void isMontando(Boolean isMontando) {
        this.isMontando = isMontando;
    }

    @NonNull
    @Override
    public AdicionarCompReprodutivoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vaca_rep, parent, false);
        return new AdicionarCompReprodutivoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdicionarCompReprodutivoViewHolder holder, final int position){
        int id = animais.get(holder.getAdapterPosition()).getId();
        holder.idVaca.setText(String.valueOf(id));
        holder.nomeVaca.setText(animais.get(holder.getAdapterPosition()).getNome());
        holder.imgVaca.setImageResource(R.drawable.cow);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogText;
                if(isMontando){
                    dialogText = "A vaca "+vacaEmAnotacao.getNome()
                            +" montou em "+animais.get(holder.getAdapterPosition()).getNome();
                }else{
                    dialogText = "A vaca "+vacaEmAnotacao.getNome()
                            +" aceitou monta de "+animais.get(holder.getAdapterPosition()).getNome();
                }
                Toast.makeText(
                        context.getApplicationContext(),
                        animais.get(holder.getAdapterPosition()).getNome(),
                        Toast.LENGTH_SHORT
                ).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmar comportamento")
                        .setMessage(dialogText)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }
                        ).setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return animais.size();
    }

}

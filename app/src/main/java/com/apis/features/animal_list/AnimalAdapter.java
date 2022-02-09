package com.apis.features.animal_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.model.Animal;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalViewHolder> {

    private List<Animal> animais;
    private Context context;

    public AnimalAdapter(List animais, Context context){
        this.animais = animais;
        this.context = context;
    }

    public void removerAnimal(Animal animal){
        int position = animais.indexOf(animal);
        animais.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        AnimalViewHolder animalViewHolder = new AnimalViewHolder(view);
        return animalViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final AnimalViewHolder holder, final int position)
    {
        holder.nome.setText(animais.get(holder.getAdapterPosition()).getNome());
        holder.idAnimal.setText("ID: "+animais.get(holder.getAdapterPosition()).getId());
        holder.dataAnotacao.setText(animais.get(holder.getAdapterPosition()).getLastUpdate());
        if(animais.get(holder.getAdapterPosition()).getLastUpdate() != "Sem anotação!"){
            holder.dataTitle.setText("Última anotação:");
        }else{
            holder.dataTitle.setText("");
        }

        final Animal animal = animais.get(holder.getAdapterPosition());

        //Action botão excluir
        holder.btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este animal?")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DbRepository database = new DbRepository(view.getContext());
                                try {
                                    database.excluirAnimal(animal);
                                    removerAnimal(animal);
                                    Toast.makeText(context, "Excluído!", Toast.LENGTH_LONG).show();
                                }catch (Throwable t){
                                    Toast.makeText(context, "Erro ao excluir!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });


        //Acessar tela de Adição de Comportamento
        holder.itemLista.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Passa o id do animal clicado para a Activity AdicionarComportamento
                Intent intent = new Intent(context, AdicionarComportamento.class);
                intent.putExtra("animal_nome", animais.get(holder.getAdapterPosition()).getNome());
                intent.putExtra("animal_id", animais.get(holder.getAdapterPosition()).getId());
                intent.putExtra("lote_id", animais.get(holder.getAdapterPosition()).getLoteId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return animais.size();
    }
}

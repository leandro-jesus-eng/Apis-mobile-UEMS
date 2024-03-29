package com.apis.features.comportamento_reprodutivo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.apis.R;
import com.apis.data.repositories.DbRepository;
import com.apis.features.comportamentos_list.AdicionarComportamento;
import com.apis.features.lote_list.MainActivity;
import com.apis.model.Animal;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdicionarCompReprodutivoAdapter extends RecyclerView.Adapter<AdicionarCompReprodutivoViewHolder> {

    private List<Animal> animais = new ArrayList<>();
    private Animal outraVaca;
    private Animal vacaEmAnotacao;
    private Context context;
    private boolean isMontando;
    private DbRepository database = new DbRepository(context);

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
                String dialogTextVacaMontou = "";
                String dialogTextVacaMontada = "";
                String comportamentoVacaAnotacao;
                String comportamentoOutraVaca;
                outraVaca = animais.get(holder.getAdapterPosition());
                if(isMontando){
                    dialogTextVacaMontou = "O bovino "+vacaEmAnotacao.getNome()+" (id: "+vacaEmAnotacao.getId()+")"
                            +" montou na \n\nVaca "+outraVaca.getNome()+" (id: "+outraVaca.getId()+")";

                    comportamentoVacaAnotacao = "Monta na vaca "+outraVaca.getNome()+" (id: "+outraVaca.getId()+")";
                    comportamentoOutraVaca = "Aceita de monta da vaca "+vacaEmAnotacao.getNome()+" (id: "+vacaEmAnotacao.getId()+")";
                }else{
                    dialogTextVacaMontada = "A vaca "+vacaEmAnotacao.getNome()+" (id: "+vacaEmAnotacao.getId()+")"
                            +" aceitou monta de \n\nBovino "+outraVaca.getNome()+" (id: "+outraVaca.getId()+")";

                    comportamentoVacaAnotacao = "Aceita de monta da vaca "+outraVaca.getNome()+" (id:"+outraVaca.getId()+")";
                    comportamentoOutraVaca = "Monta na vaca "+vacaEmAnotacao.getNome()+" (id:"+vacaEmAnotacao.getId()+")";
                }

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.prompt_confirm_comportamento_rep, null);
                final TextView nomeAnimalMontou =  promptView.findViewById(R.id.nome_vaca_montou_textView);
                final TextView nomeAnimalMontado = promptView.findViewById(R.id.nome_vaca_montada_textView);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(promptView);
                nomeAnimalMontou.setText(dialogTextVacaMontou);
                nomeAnimalMontado.setText(dialogTextVacaMontada);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Intent intent = new Intent(context.getApplicationContext(), AdicionarComportamento.class);
                                        intent.putExtra("animal_nome", vacaEmAnotacao.getNome());
                                        intent.putExtra("animal_id", vacaEmAnotacao.getId());
                                        intent.putExtra("lote_id", vacaEmAnotacao.getLoteId());
                                        intent.putExtra("outra_vaca_nome", outraVaca.getNome());
                                        intent.putExtra("outra_vaca_id", outraVaca.getId());
                                        intent.putExtra("anotacao_outra_vaca", comportamentoOutraVaca);
                                        intent.putExtra("anotacao_vaca", comportamentoVacaAnotacao);
                                        context.startActivity(intent);

                                        ((Activity)context).finish();
                                        Toast.makeText(context.getApplicationContext(),
                                                "Salvo!", Toast.LENGTH_SHORT).show();
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

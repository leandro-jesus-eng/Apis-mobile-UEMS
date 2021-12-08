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
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;
import com.apis.models.DateTime;
import com.apis.models.FileControl;
import com.apis.models.TipoComportamento;

import java.util.ArrayList;
import java.util.Date;
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
                String dialogText;
                String comportamentoVacaAnotacao;
                String comportamentoOutraVaca;
                outraVaca = animais.get(holder.getAdapterPosition());
                if(isMontando){
                    dialogText = "A vaca "+vacaEmAnotacao.getNome()
                            +" montou em "+outraVaca.getNome();
                    comportamentoVacaAnotacao = "Monta em outra";
                    comportamentoOutraVaca = "Aceita de monta";
                }else{
                    dialogText = "A vaca "+vacaEmAnotacao.getNome()
                            +" aceitou monta de "+outraVaca.getNome();
                    comportamentoVacaAnotacao = "Aceita de monta";
                    comportamentoOutraVaca = "Monta em outra";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmar comportamento")
                        .setMessage(dialogText)
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DateTime dateTime = new DateTime();
                                        AnotacaoComportamento anotacaoComportamentoVacaEmAnotacao =
                                                new AnotacaoComportamento(
                                                        0,
                                                        vacaEmAnotacao.getNome(),
                                                        vacaEmAnotacao.getId(),
                                                        dateTime.pegarData(),
                                                        dateTime.pegarHora(),
                                                        comportamentoVacaAnotacao,
                                                        ""

                                        );
                                        AnotacaoComportamento anotacaoComportamentoOutraVaca =
                                                new AnotacaoComportamento(
                                                        0,
                                                        outraVaca.getNome(),
                                                        outraVaca.getId(),
                                                        dateTime.pegarData(),
                                                        dateTime.pegarHora(),
                                                        comportamentoOutraVaca,
                                                        ""

                                        );

                                        database.insertAnotacaoComportamento(
                                                anotacaoComportamentoVacaEmAnotacao
                                        );
                                        database.insertAnotacaoComportamento(
                                                anotacaoComportamentoOutraVaca
                                        );
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

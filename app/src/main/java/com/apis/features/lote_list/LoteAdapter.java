package com.apis.features.lote_list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apis.features.animal_list.ListaAnimais;
import com.apis.R;
import com.apis.database.DbController;
import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;
import com.apis.models.FileControl;
import com.apis.models.Lote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoteAdapter extends RecyclerView.Adapter<LoteViewHolder>{

    final private ArrayList<Lote> lotes;
    final private Context context;

    public LoteAdapter(ArrayList lotes, Context context){
        this.lotes = lotes;
        this.context = context;
    }

    public void removerLote(Lote lote){
        int position = lotes.indexOf(lote);
        lotes.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public LoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_lote, parent, false);
        LoteViewHolder loteViewHolder = new LoteViewHolder(view);
        return loteViewHolder;
    }

    @Override
    public void onBindViewHolder(LoteViewHolder holder, final int position)
    {
        holder.nome.setText(lotes.get(position).getNome());
        holder.experimento.setText("["+lotes.get(position).getId()+"] "+lotes.get(position).getExperimento());

        final Lote lote = lotes.get(position);

        //Action botão excluir
        holder.btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir este lote? Seus dados também serão excluídos.")
                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DbController database = new DbController(view.getContext());

                                //Apaga arquivo
                                FileControl fileControl = new FileControl(context);
                                fileControl.deleteLoteFile(context, lote.getId());

                                try {
                                    database.excluirLote(lotes.get(lote.getId()));
                                    ArrayList<Animal> animaisLote = database.returnAnimais(lote.getId());
                                    for (Animal animal : animaisLote) {
                                        database.excluirAnimal(animal);
                                    }

                                    removerLote(lote);

                                    Toast.makeText(context, "Excluído!", Toast.LENGTH_LONG).show();
                                } catch (Throwable throwable) {
                                    Toast.makeText(context, "Erro ao excluir!", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create()
                        .show();
            }
        });

        holder.btnExportarDados.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Tela de compartilhamento
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                File files[] = context.getExternalFilesDirs(null);
                String fileName = FileControl.getNameOfLoteCSV(lote);
                //Antes de fazer a consulta
                //Apaga o arquivo de dados do animal, se houver
                File f = null;
                if (files.length > 0) {
                    try {
                        f = new File(files[0], fileName);
                        f.delete();
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean existeDados = false;
                DbController database = new DbController(context);
                ArrayList<Animal> listAnimais = database.returnAnimais(lote.getId());
                try {
                    FileOutputStream out = new FileOutputStream(f, true);
                    for(Animal animal : listAnimais) {
                        List<AnotacaoComportamento> listComp = database.returnAnotacoesComportamento(animal.getId());
                        for (AnotacaoComportamento comportamento : listComp) {
                           out.write(comportamento.toString().getBytes());
                           out.write('\n');
                            existeDados = true;
                        }
                    }
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println(e.toString());
                } finally {
                }


               if(existeDados && f != null && f.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse(files[0].getAbsolutePath() + "/" +fileName));
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Dados Lote : " + lote.getNome());

                    context.startActivity(Intent.createChooser(intentShareFile, "Enviar para"));
                } else {
                    Toast.makeText(context, "Não existem dados para este lote!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //Acessar lote
        holder.itemLista.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListaAnimais.class);
                intent.putExtra("lote_nome", lotes.get(position).getNome());
                intent.putExtra("lote_id", lotes.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lotes.size();
    }
}

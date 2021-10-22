package com.apis.database;

import android.content.Context;
import android.widget.Toast;
import com.apis.database.DAOs.AnimalDao;
import com.apis.database.DAOs.AnotacaoComportamentoDao;
import com.apis.database.DAOs.ComportamentoDao;
import com.apis.database.DAOs.FormularioComportamentoDao;
import com.apis.database.DAOs.LoteDao;
import com.apis.database.DAOs.TipoComportamentoDao;
import com.apis.models.Animal;
import com.apis.models.AnotacaoComportamento;
import com.apis.models.Comportamento;
import com.apis.models.FileControl;
import com.apis.models.FormularioComportamento;
import com.apis.models.Lote;
import com.apis.models.TipoComportamento;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbController {

    final private Context context;
    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioComportamentoDao formularioComportamentoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;

    public DbController(Context ctx){
        context = ctx;
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
    }

    //Animais
    public ArrayList<Animal> returnAnimais(int idLote){
        return animalDao.returnAnimaisLote(idLote);
    }

    public Animal returnAnimal(int idLote, int animalId){
        return animalDao.returnAnimal(idLote, animalId);
    }

    public void insertAnimal(Animal animalNovo){
        animalDao.insertAnimal(animalNovo);
    }

    public boolean animalExiste(String nomeAnimal){
        ArrayList<Animal> result = animalDao.returnAllAnimais();

        for(Animal animal : result){
            if(animal.getNome().equals(nomeAnimal)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Animal> returnAnimaisPorId(int idLote){
        List<Animal> result = animalDao.returnAnimaisLote(idLote);
        ArrayList<Animal> animais = new ArrayList<>();
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        for(Animal animal : result){
            nomes.add(animal.getNome());
            ids.add(animal.getId());
        }
        Collections.sort(ids);
        for(int i=0; i < ids.size(); i++){
            animais.add(new Animal(nomes.get(i), idLote));
        }
        setUpdateAnimais(animais);
        return animais;
    }

    public ArrayList<Animal> retornarAnimaisPorOrdemDeAnotacao(ArrayList<Animal> animais){
        setUpdateAnimais(animais);
        Collections.sort(animais);

        return animais;
    }

    public void setUpdateAnimais(ArrayList<Animal> animais){
        for(Animal animal : animais){
            String dataHora;

            ArrayList<AnotacaoComportamento> result = anotacaoComportamentoDao.returnAllAnotacoesAnimal(animal.getId());

            dataHora = result.get(result.size() - 1).getDataHora();

            if(dataHora == null){
                animal.setLastUpdate("Sem anotação!");
            }else{
                animal.setLastUpdate(dataHora);
            }
        }
    }

    public String getLastUpdateAnimal(Animal animal){
        return animal.getLastUpdate();
    }


    //Lotes
    public void insertLote(Lote lote){
        loteDao.insertLote(lote);
    }

    public Lote returnLote(int idLote){
        return loteDao.returnLote(idLote);
    }

    public ArrayList<Lote> returnAllLotes(){
        return loteDao.returnAllLotes();
    }

    public boolean loteExiste(String nomeLote, String experimento){
        ArrayList<Lote> result = loteDao.returnAllLotes();

        for(Lote lote : result){
            if(lote.getNome().equals(nomeLote) && lote.getExperimento().equals(experimento) ){
                return true;
            }
        }
        return false;
    }

    public String returnNomeLote(int idLote){
        Lote result = loteDao.returnLote(idLote);

        return result.getNome();
    }

    //Comportamentos e afins
    public void insertAnotacaoComportamento(AnotacaoComportamento anotacao){
        anotacaoComportamentoDao.insertAnotacao(anotacao);

    }
    public ArrayList<AnotacaoComportamento> returnAnotacoesComportamento(int animalId){
        return anotacaoComportamentoDao.returnAllAnotacoesAnimal(animalId);

    }

    //Excluir
    public void excluirAnimal(Animal animal){
        animalDao.deleteAnimal(animal);
    }
    public void excluirLote(Lote lote){
        loteDao.deleteLote(lote);
    }
    public void excluirTipoComportamento(TipoComportamento tipoComportamento){
        tipoComportamentoDao.deleteTipo(tipoComportamento);
    }
    public void excluirAnotacaoComportamento(AnotacaoComportamento anotacaoComportamento){
        anotacaoComportamentoDao.deleteAnotacao(anotacaoComportamento);
    }
    public void excluirFormularioComportamento(FormularioComportamento formularioComportamento){
        formularioComportamentoDao.deleteFormulario(formularioComportamento);
    }
    public void excluirComportamento(Comportamento comportamento){
        comportamentoDao.deleteComportamento(comportamento);
    }

    public boolean excluirTudo() {
        animalDao.deleteAllAnimais();

        loteDao.deleteAllLotes();

        tipoComportamentoDao.deleteAllTipos();

        anotacaoComportamentoDao.deleteAllAnotacoes();

        formularioComportamentoDao.deleteAllFormularios();

        comportamentoDao.deleteAllComportamentos();

        FileControl fc = new FileControl(context);
        fc.deleteEverthing();

        return true;
    }

    //Exportar dados
    public boolean exportarDados(int idLote, int idAnimal){

        Lote lote = loteDao.returnLote(idLote);
        Animal animal = animalDao.returnAnimal(idLote, idAnimal);

        //Antes de fazer a consulta
        //Apaga o arquivo de dados do animal, se houver
        File files[] = context.getExternalFilesDirs(null);
        File f = null;
        if(files.length > 0) {
            f = new File( files[0] , FileControl.getNameOfAnimalCSV(lote, animal));
            f.delete();
        } else {
            Toast.makeText(context, "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
        }

        ArrayList<AnotacaoComportamento> result = anotacaoComportamentoDao.returnAllAnotacoesAnimal(idAnimal);

        for(int i = 0; i < result.size(); i++ ){
            int id_animal = result.get(i).getIdAnimal();
            String nome_animal = result.get(i).getNomeAnimal();
            String dataHora = result.get(i).getDataHora();
            String comportamento = result.get(i).getComportamento().getNome();
            String obs = result.get(i).getObs();

            String conteudo = id_animal+";"+nome_animal+";"+dataHora+";"+comportamento+";"+obs;

            try {
                try {
                   if (!f.exists()){
                        f.createNewFile();
                    }

                    FileOutputStream out = new FileOutputStream(f, true);
                    out.write(conteudo.getBytes());
                    out.write('\n');
                    out.flush();
                    out.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return true;
    }

}

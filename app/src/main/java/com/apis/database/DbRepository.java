package com.apis.database;

import android.content.Context;
import android.widget.Toast;

import com.apis.database.DAOs.AnimalDao;
import com.apis.database.DAOs.AnotacaoComportamentoDao;
import com.apis.database.DAOs.ComportamentoDao;
import com.apis.database.DAOs.FormularioComportamentoDao;
import com.apis.database.DAOs.LoteDao;
import com.apis.database.DAOs.RelationsDao;
import com.apis.database.DAOs.TipoComportamentoDao;
import com.apis.database.relations.AnimalWithAnotacao;
import com.apis.database.relations.FormularioWithTipoComportamento;
import com.apis.database.relations.LoteAndFormulario;
import com.apis.database.relations.LoteWithAnimal;
import com.apis.database.relations.TipoComportamentoWithComportamento;
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
import java.util.Date;
import java.util.List;

public class DbRepository {

    final private Context context;
    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioComportamentoDao formularioComportamentoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;
    final private RelationsDao relationsDao;

    public DbRepository(Context ctx){
        context = ctx;
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
        relationsDao = Database.getInstance(ctx).relationsDao();
    }

    //RELATIONS

    //Retorna Anotações de um animal
    public List<AnimalWithAnotacao> getAnimalWithAnotacao(int idAnimal){
        return relationsDao.getAnimalWithAnotacao(idAnimal);
    }

    //Retorna Comportamentos de um Tipo
    public List<TipoComportamentoWithComportamento> getTipoComportametnoWithComportamento(int idTipo){
        return relationsDao.getTipoComportamentoWithComportamento(idTipo);
    }

    //Retorna tipos de um Formulario
    public List<FormularioWithTipoComportamento> getFormularioWithTipoComportamento(int idFormulario){
        return relationsDao.getFormularioWithTipoComportamento(idFormulario);
    }

    //Retorna animais de um Lote
    public List<LoteWithAnimal> getLoteWithAnimal(int idLote){
        return relationsDao.getLoteWithAnimal(idLote);
    }

    //Retorna formulario de um Lote
    public List<LoteAndFormulario> getLoteAndFormulario(int idLote){
        return relationsDao.getLoteAndFormulario(idLote);
    }

    //ANIMAIS

    public ArrayList<Animal> getAnimais(int idLote){
        return animalDao.getAnimaisLote(idLote);
    }

    public Animal getAnimal(int idLote, int animalId){
        return animalDao.getAnimal(idLote, animalId);
    }

    public void insertAnimal(Animal animalNovo){
        animalDao.insertAnimal(animalNovo);
    }

    public boolean animalExiste(String nomeAnimal){
        ArrayList<Animal> result = animalDao.getAllAnimais();

        for(Animal animal : result){
            if(animal.getNome().equals(nomeAnimal)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Animal> getAnimaisPorId(int idLote){
        List<Animal> result = animalDao.getAnimaisLote(idLote);
        ArrayList<Animal> animais = new ArrayList<>();
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        for(Animal animal : result){
            nomes.add(animal.getNome());
            ids.add(animal.getId());
        }
        Collections.sort(ids);
        for(int i=0; i < ids.size(); i++){
            animais.add(new Animal(0, nomes.get(i), idLote));
        }
        setUpdateAnimais(animais);
        return animais;
    }

    public ArrayList<Animal> getAnimaisPorOrdemDeAnotacao(ArrayList<Animal> animais){
        setUpdateAnimais(animais);
        Collections.sort(animais);

        return animais;
    }

    public void setUpdateAnimais(ArrayList<Animal> animais){
        for(Animal animal : animais){
            String dataHora;

            List<AnimalWithAnotacao> result = relationsDao.getAnimalWithAnotacao(animal.getId());
            List<AnotacaoComportamento> anotacoes = result.get(0).anotacaoComportamentos;

            dataHora = anotacoes.get(result.size() - 1).getData_hora();

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

    public Lote getLote(int idLote){
        return loteDao.getLote(idLote);
    }

    public ArrayList<Lote> getAllLotes(){
        return loteDao.getAllLotes();
    }

    public boolean loteExiste(String nomeLote, String experimento){
        ArrayList<Lote> result = loteDao.getAllLotes();

        for(Lote lote : result){
            if(lote.getNome().equals(nomeLote) && lote.getExperimento().equals(experimento) ){
                return true;
            }
        }
        return false;
    }
    public String getNomeLote(int idLote){
        Lote result = loteDao.getLote(idLote);

        return result.getNome();
    }

    //Comportamentos e afins
    public void insertAnotacaoComportamento(AnotacaoComportamento anotacao){
        anotacaoComportamentoDao.insertAnotacao(anotacao);
    }

    public ArrayList<AnotacaoComportamento> getAnotacoesComportamento(int animalId){
        return anotacaoComportamentoDao.getAllAnotacoesAnimal(animalId);
    }

    public void insertComportamento(Comportamento comportamento){
        comportamentoDao.insertComportamento(comportamento);
    }

    public void insertTipoComportamento(TipoComportamento tipoComportamento){
        tipoComportamentoDao.insertTipo(tipoComportamento);
    }

    public void insertFormularioComportamento(FormularioComportamento formularioComportamento){
        formularioComportamentoDao.insertFormulario(formularioComportamento);
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

        Lote lote = loteDao.getLote(idLote);
        Animal animal = animalDao.getAnimal(idLote, idAnimal);

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

        List<AnimalWithAnotacao> result = relationsDao.getAnimalWithAnotacao(animal.getId());
        List<AnotacaoComportamento> anotacoes = result.get(0).anotacaoComportamentos;

        for(int i = 0; i < anotacoes.size(); i++ ){
            int id_animal = anotacoes.get(i).getIdAnimal();
            String nome_animal = anotacoes.get(i).getNomeAnimal();
            String dataHora = anotacoes.get(i).getData_hora();
            String comportamento = anotacoes.get(i).getNomeComportamento();
            String obs = anotacoes.get(i).getObs();

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

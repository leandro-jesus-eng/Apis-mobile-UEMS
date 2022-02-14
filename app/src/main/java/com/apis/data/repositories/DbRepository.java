package com.apis.data.repositories;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;

import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioComportamentoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.RelationsDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.Database;
import com.apis.data.database.relations.AnimalWithAnotacao;
import com.apis.data.database.relations.FormularioWithTipoComportamento;
import com.apis.data.database.relations.LoteAndFormulario;
import com.apis.data.database.relations.LoteWithAnimal;
import com.apis.data.database.relations.TipoComportamentoWithComportamento;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FileControl;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Comparator;
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
    final private FirestoreRepository firestoreRepository;

    public DbRepository(Context ctx){
        context = ctx;
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
        relationsDao = Database.getInstance(ctx).relationsDao();
        firestoreRepository = new FirestoreRepository(ctx);
    }

    //--RELATIONS---------------------------------------------------------------------------------//

    //Retorna Anotações de um animal
    public List<AnimalWithAnotacao> getAnimalWithAnotacao(int idAnimal){
        return relationsDao.getAnimalWithAnotacao(idAnimal);
    }

    //Retorna Comportamentos de um Tipo
    public List<TipoComportamentoWithComportamento> getTipoComportamentoWithComportamento(int idTipo){
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

    //--ANIMAIS-----------------------------------------------------------------------------------//

    public List<Animal> getAnimais(int idLote){
        return animalDao.getAnimaisLote(idLote);
    }

    public Animal getAnimal(int idLote, int animalId){
        return animalDao.getAnimal(idLote, animalId);
    }

    public void insertAnimal(Animal animalNovo){
        animalDao.insertAnimal(animalNovo);
        firestoreRepository.insertAnimalToFirestore(animalNovo);
    }

    public boolean animalExiste(String nomeAnimal){
        List<Animal> result = animalDao.getAllAnimais();

        for(Animal animal : result){
            if(animal.getNome().equals(nomeAnimal)){
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Animal> getAnimaisPorId(int idLote){
        List<Animal> animais = animalDao.getAnimaisLote(idLote);

        Comparator<Animal> compareById = Comparator.comparing(Animal::getId);

        Collections.sort(animais, compareById);

        return animais;
    }

    public List<Animal> getAnimaisPorOrdemDeAnotacao(List<Animal> animais){
        Collections.sort(animais);
        return animais;
    }

    public void setLastUpdateAnimal(int animalId, String lastUpdate){
        animalDao.setLastUpdate(animalId, lastUpdate);
    }

    public String getLastUpdateAnimal(Animal animal){
        return animal.getLastUpdate();
    }

    public void excluirAnimal(Animal animal){
        animalDao.deleteAnimal(animal);
        firestoreRepository.deleteAnimalInFirestore(animal);
    }

    //--LOTES-------------------------------------------------------------------------------------//
    public void insertLote(Lote lote){
        loteDao.insertLote(lote);
        firestoreRepository.insertLoteToFirestore(getLastLote());
    }

    public Lote getLote(int idLote){
        return loteDao.getLote(idLote);
    }

    public Lote getLastLote(){
        return loteDao.getAllLotes().get(getAllLotes().size() - 1);
    }

    public List<Lote> getAllLotes(){
        return loteDao.getAllLotes();
    }

    public boolean loteExiste(String nomeLote, String experimento){
        List<Lote> result = loteDao.getAllLotes();

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

    public void excluirLote(Lote lote){
        firestoreRepository.deleteLoteInFirestore(lote);
        loteDao.deleteLote(lote);
    }

    //--COMPORTAMENTOS----------------------------------------------------------------------------//

    public Comportamento getComportamento(int idTipo){
        return comportamentoDao.getComportamento(idTipo);
    }

    public void insertComportamento(Comportamento comportamento){
        comportamentoDao.insertComportamento(comportamento);
        firestoreRepository.insertComportamentoToFirestore(comportamento);
    }

    public void updateComportamento(Comportamento comportamento){
        comportamentoDao.updateComportamento(comportamento);
    }

    public void excluirComportamento(Comportamento comportamento){
        comportamentoDao.deleteComportamento(comportamento);
        firestoreRepository.deleteComportamentoInFirestore(comportamento);
    }

    //--TIPO_COMPORTAMENTOS-----------------------------------------------------------------------//

    public TipoComportamento getTipo(int id){
        return tipoComportamentoDao.getTipo(id);
    }

    public void insertTipoComportamento(TipoComportamento tipoComportamento){
        tipoComportamentoDao.insertTipo(tipoComportamento);
        firestoreRepository.insertTipoComportamentoToFirestore(tipoComportamento);
    }

    public void updateTipo(TipoComportamento tipoComportamento){
        tipoComportamentoDao.updateTipo(tipoComportamento);
    }

    public List<TipoComportamento> getAllTipos(){
        return tipoComportamentoDao.getAllTipos();
    }

    public void excluirTipoComportamento(TipoComportamento tipoComportamento){
        tipoComportamentoDao.deleteTipo(tipoComportamento);
        firestoreRepository.deleteTipoComportamentoInFirestore(tipoComportamento);
    }

    //--FORMULARIO_COMPORTAMENTOS-----------------------------------------------------------------//

    public void insertFormularioComportamento(FormularioComportamento formularioComportamento){
        formularioComportamentoDao.insertFormulario(formularioComportamento);
        firestoreRepository.insertFormularioComportamentoToFirestore(formularioComportamento);
    }

    public FormularioComportamento getFormularioPadrao(boolean ehPadrao){
        return formularioComportamentoDao.getFormularioPadrao(ehPadrao);
    }

    public FormularioComportamento getFormulario(int id){
        return formularioComportamentoDao.getFormulario(id);
    }

    public void excluirFormularioComportamento(FormularioComportamento formularioComportamento){
        formularioComportamentoDao.deleteFormulario(formularioComportamento);
        firestoreRepository.deleteFormularioComportamentoInFirestore(formularioComportamento);
    }

    //--ANOTAÇÃO_COMPORTAMENTOS-------------------------------------------------------------------//
    public void insertAnotacaoComportamento(AnotacaoComportamento anotacao){
        anotacaoComportamentoDao.insertAnotacao(anotacao);
        firestoreRepository.insertAnotacaoComportamentoToFirestore(anotacao);
    }

    public List<AnotacaoComportamento> getAnotacoesComportamento(int animalId){
        return anotacaoComportamentoDao.getAllAnotacoesOfOneAnimal(animalId);
    }

    public List<Comportamento> getAllComportamentos(){
        return comportamentoDao.getAllComportamentos();
    }

    public void excluirAnotacaoComportamento(AnotacaoComportamento anotacaoComportamento){
        anotacaoComportamentoDao.deleteAnotacao(anotacaoComportamento);
        firestoreRepository.deleteAnotacaoComportamentoInFirestore(anotacaoComportamento);
    }

    //--EXCLUIR_TUDO------------------------------------------------------------------------------//

    public boolean excluirTudo() {
        animalDao.deleteAllAnimais();
        loteDao.deleteAllLotes();
        tipoComportamentoDao.deleteAllTipos();
        anotacaoComportamentoDao.deleteAllAnotacoes();
        formularioComportamentoDao.deleteAllFormularios();
        comportamentoDao.deleteAllComportamentos();

        for(Animal animal : animalDao.getAllAnimais()){
            firestoreRepository.deleteAnimalInFirestore(animal);
        }
        for(Lote lote : loteDao.getAllLotes()){
            firestoreRepository.deleteLoteInFirestore(lote);
        }
        for(TipoComportamento tipoComportamento : tipoComportamentoDao.getAllTipos()){
            firestoreRepository.deleteTipoComportamentoInFirestore(tipoComportamento);
        }
        for(AnotacaoComportamento anotacaoComportamento : anotacaoComportamentoDao.getAllAnotacoesAnimal()){
            firestoreRepository.deleteAnotacaoComportamentoInFirestore(anotacaoComportamento);
        }
        for(FormularioComportamento formularioComportamento : formularioComportamentoDao.getAllFormularioComportamento()){
            firestoreRepository.deleteFormularioComportamentoInFirestore(formularioComportamento);
        }
        for(Comportamento comportamento : comportamentoDao.getAllComportamentos()){
            firestoreRepository.deleteComportamentoInFirestore(comportamento);
        }

        FileControl fc = new FileControl(context);
        fc.deleteEverthing();

        return true;
    }

    //--EXPORTAR_DADOS----------------------------------------------------------------------------//

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
            String dataHora = anotacoes.get(i).getData() +" "+anotacoes.get(i).getHora();
            String comportamento = anotacoes.get(i).getNomeComportamento();
            String obs = anotacoes.get(i).getObs();

            String conteudo = id_animal+";"+nome_animal+";"+dataHora+";"+comportamento+";"+obs;

            try {
                try {
                   if (!f.exists()){
                        f.createNewFile();
                    }

                    FileOutputStream out = new FileOutputStream(f, true);

                    ByteBuffer byteBuffer = StandardCharsets.ISO_8859_1.encode(conteudo);
                    out.write(byteBuffer.array());
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

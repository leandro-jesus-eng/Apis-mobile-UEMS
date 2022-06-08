package com.apis.data.repositories;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioLoteDao;
import com.apis.data.database.DAOs.FormularioPadraoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.RelationsDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.DAOs.UserDao;
import com.apis.data.database.Database;
import com.apis.data.database.relations.AnimalWithAnotacao;
import com.apis.data.database.relations.FormularioWithTipoComportamento;
import com.apis.data.database.relations.LoteAndFormulario;
import com.apis.data.database.relations.LoteWithAnimal;
import com.apis.data.database.relations.LoteWithUsers;
import com.apis.data.database.relations.TipoComportamentoWithComportamento;
import com.apis.data.database.relations.UserAndFormularioPadrao;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FileControl;
import com.apis.model.FormularioLote;
import com.apis.model.FormularioPadrao;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DbRepository {

    final private Context context;
    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioLoteDao formularioLoteDao;
    final private FormularioPadraoDao formularioPadraoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;
    final private RelationsDao relationsDao;
    final private FirestoreRepository firestoreRepository;
    final private UserDao userDao;

    public DbRepository(Context ctx){
        Database databaseInstance = Database.getInstance(ctx);
        context = ctx;
        animalDao = databaseInstance.animalDao();
        loteDao = databaseInstance.loteDao();
        formularioLoteDao = databaseInstance.formularioLoteDao();
        formularioPadraoDao = databaseInstance.formularioPadraoDao();
        tipoComportamentoDao = databaseInstance.tipoComportamentoDao();
        anotacaoComportamentoDao = databaseInstance.anotacaoComportamentoDao();
        comportamentoDao = databaseInstance.comportamentoDao();
        relationsDao = databaseInstance.relationsDao();
        userDao = databaseInstance.userDao();
        firestoreRepository = new FirestoreRepository(ctx);
    }

    //--RELATIONS---------------------------------------------------------------------------------//

    //Retorna Anotações de um animal
    public List<AnimalWithAnotacao> getAnimalWithAnotacao(int idAnimal){
        return relationsDao.getAnimalWithAnotacao(idAnimal);
    }

    //Retorna Comportamentos de um Tipo
    public List<TipoComportamentoWithComportamento> getTipoComportamentoWithComportamento(int idTipo) {
        return relationsDao.getTipoComportamentoWithComportamento(idTipo);
    }

    //Retorna tipos de um Formulario
    public List<FormularioWithTipoComportamento> getFormularioWithTipoComportamento(int idFormulario) {
        return relationsDao.getFormularioWithTipoComportamento(idFormulario);
    }

    //Retorna animais de um Lote
    public List<LoteWithAnimal> getLoteWithAnimal(int idLote) {
        return relationsDao.getLoteWithAnimal(idLote);
    }

    //Retorna formulario de um Lote
    public List<LoteAndFormulario> getLoteAndFormulario(int idLote) {
        return relationsDao.getLoteAndFormulario(idLote);
    }

    //Retorna Lotes com user
    public List<LoteWithUsers> getUserOfLote(int loteId) {
        return relationsDao.getUsersOfLote(loteId);
    }

    //Inserir Relação UserLote
    public void insertUserLoteCrossRef(UserLoteCrossRef userLoteCrossRef) {
        relationsDao.insertUserLoteCrossRef(userLoteCrossRef);
        firestoreRepository.insertUserLoteCrossRefToFirestore(userLoteCrossRef);
    }

    //Deletar Relação UserLote
    public void deleteUserLoteCrossRef(UserLoteCrossRef userLoteCrossRef) {
        relationsDao.deleteUserLoteCrossRef(userLoteCrossRef);
    }

    //Deletar Relação UserLote
    public List<UserLoteCrossRef> findUserLoteCrossRefByIds(Integer loteId) {
        List<UserLoteCrossRef> result = relationsDao.getAllUserLoteCrossRef();
        ArrayList<UserLoteCrossRef> selectedCrossRefs = new ArrayList<>();

        for (UserLoteCrossRef userLoteCrossRef : result) {
            if (userLoteCrossRef.loteId.equals(loteId)) {
                selectedCrossRefs.add(userLoteCrossRef);
            }
        }
        return selectedCrossRefs;
    }

    //Retorna usuario de um formulario
    public List<UserAndFormularioPadrao> getUserAndFormularioPadrao(int userId) {
        return relationsDao.getUserAndFormulario(userId);
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
        firestoreRepository.insertAnimalToFirestore(getLastAnimal());
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

    public Animal getLastAnimal(){
        return animalDao.getAllAnimais().get(animalDao.getAllAnimais().size() - 1);
    }

    public void setLastUpdateAnimal(int animalId, String lastUpdate){
        animalDao.setLastUpdate(animalId, lastUpdate);
        firestoreRepository.updateLastAnotacaoAnimalInFirebase(animalId, lastUpdate);
    }

    public String getLastUpdateAnimal(Animal animal){
        return animal.getLastUpdate();
    }

    public void excluirAnimal(Animal animal){
        List<AnotacaoComportamento> listAnotacoes = getAnotacoesComportamento(animal.getId());
        if(listAnotacoes != null){
            for (AnotacaoComportamento anotacaoComportamento : listAnotacoes){
                excluirAnotacaoComportamento(anotacaoComportamento);
                firestoreRepository.deleteAnotacaoComportamentoInFirestore(anotacaoComportamento);
            }
        }
        animalDao.deleteAnimal(animal);
        firestoreRepository.deleteAnimalInFirestore(animal);
    }

    //--LOTES-------------------------------------------------------------------------------------//
    public void insertLote(Lote lote, Integer userId){
        loteDao.insertLote(lote);
        firestoreRepository.insertLoteToFirestore(getLastLote());
        insertUserLoteCrossRef(new UserLoteCrossRef(userId, getLastLote().getLoteId()));
    }

    public Lote getLote(int idLote){
        return loteDao.getLote(idLote);
    }

    public Lote getLastLote(){
        return loteDao.getAllLotes().get(loteDao.getAllLotes().size() - 1);
    }

    public String getNomeLote(int idLote){
        Lote result = loteDao.getLote(idLote);

        return result.getNome();
    }

    public List<Lote> getAllLotes(){
        return loteDao.getAllLotes();
    }

    public void excluirLote(Lote lote, List<UserLoteCrossRef> userLoteCrossRefs){
        for(FormularioLote formularioComportamento : formularioLoteDao.getAllFormularioComportamento()){
            if(formularioComportamento.getLoteId() == lote.getLoteId()){
                deleteFormularioLote(formularioComportamento);
                firestoreRepository.deleteFormularioLoteInFirestore(formularioComportamento);
            }
        }
        loteDao.deleteLote(lote);
        firestoreRepository.deleteLoteInFirestore(lote);

        for(UserLoteCrossRef userLoteCrossRef : userLoteCrossRefs) {
            deleteUserLoteCrossRef(userLoteCrossRef);
            firestoreRepository.deleteUserLoteCrossRefToFirestore(userLoteCrossRef);
        }
    }

    public List<Lote> selectVisibleLotes(User currentUser) {
        List<Lote> visibleLotes = new ArrayList<>();

        for(Lote lote : getAllLotes()) {
            LoteWithUsers loteWithUsers =  getUserOfLote(lote.getLoteId()).get(0);
            List<User> usersWithPermission = loteWithUsers.userList;
            for(User userWithPermission : usersWithPermission) {
                if(currentUser.getEmail().equals(userWithPermission.getEmail())) {
                    visibleLotes.add(lote);
                }
            }
        }
        return visibleLotes;
    }

    //--COMPORTAMENTOS----------------------------------------------------------------------------//

    public Comportamento getComportamento(int idTipo){
        return comportamentoDao.getComportamento(idTipo);
    }

    public Comportamento getLastComportamento(){
        return comportamentoDao.getAllComportamentos().get(
                comportamentoDao.getAllComportamentos().size() - 1
        );
    }

    public List<Comportamento> getAllComportamentos(){
        return comportamentoDao.getAllComportamentos();
    }

    public void insertComportamento(Comportamento comportamento){
        comportamentoDao.insertComportamento(comportamento);
        firestoreRepository.insertComportamentoToFirestore(getLastComportamento());
    }

    public void updateComportamento(Comportamento comportamento){
        comportamentoDao.updateComportamento(comportamento);
        firestoreRepository.updateComportamentoInFirebase(comportamento);
    }

    public void excluirComportamento(Comportamento comportamento){
        comportamentoDao.deleteComportamento(comportamento);
        firestoreRepository.deleteComportamentoInFirestore(comportamento);
    }

    //--TIPO_COMPORTAMENTOS-----------------------------------------------------------------------//

    public TipoComportamento getTipo(int id){
        return tipoComportamentoDao.getTipo(id);
    }

    public TipoComportamento getLastTipoComportamento(){
        return tipoComportamentoDao.getAllTipos().get(tipoComportamentoDao.getAllTipos().size() - 1);
    }

    public void insertTipoComportamento(TipoComportamento tipoComportamento){
        tipoComportamentoDao.insertTipo(tipoComportamento);
        firestoreRepository.insertTipoComportamentoToFirestore(getLastTipoComportamento());
    }

    public void updateTipo(TipoComportamento tipoComportamento){
        tipoComportamentoDao.updateTipo(tipoComportamento);
        firestoreRepository.updateTipoComportamentoInFirebase(tipoComportamento);
    }

    public List<TipoComportamento> getAllTipos(){
        return tipoComportamentoDao.getAllTipos();
    }

    public void excluirTipoComportamento(TipoComportamento tipoComportamento){
        List<Comportamento> comportamentos = getTipoComportamentoWithComportamento(
                tipoComportamento.getId()).get(0).comportamentos;
        if(comportamentos != null){
            for(Comportamento comportamento : comportamentos){
                excluirComportamento(comportamento);
                firestoreRepository.deleteComportamentoInFirestore(comportamento);
            }
        }

        tipoComportamentoDao.deleteTipo(tipoComportamento);
        firestoreRepository.deleteTipoComportamentoInFirestore(tipoComportamento);
    }

    //--FORMULARIO_LOTE-----------------------------------------------------------------//

    public void insertFormularioLote(FormularioLote formularioComportamento){
        formularioLoteDao.insertFormulario(formularioComportamento);
        firestoreRepository.insertFormularioLoteToFirestore(getLastFormularioLote());
    }

    public FormularioLote getFormularioPadrao(boolean ehPadrao){
        return formularioLoteDao.getFormularioPadrao(ehPadrao);
    }

    public FormularioLote getFormularioLote(int id){
        return formularioLoteDao.getFormulario(id);
    }

    public FormularioLote getLastFormularioLote(){
        return formularioLoteDao.getAllFormularioComportamento().get(
                formularioLoteDao.getAllFormularioComportamento().size() - 1
        );
    }

    public void deleteFormularioLote(FormularioLote formularioComportamento){
        List<TipoComportamento> tipoComportamentos = getFormularioWithTipoComportamento(
                formularioComportamento.getId()).get(0).tiposComportamento;
        if(tipoComportamentos != null){
            for(TipoComportamento tipoComportamento : tipoComportamentos){
                excluirTipoComportamento(tipoComportamento);
                firestoreRepository.deleteTipoComportamentoInFirestore(tipoComportamento);
            }
        }

        formularioLoteDao.deleteFormulario(formularioComportamento);
        firestoreRepository.deleteFormularioLoteInFirestore(formularioComportamento);
    }

    //--FORMULARIO_PADRAO-----------------------------------------------------------------//

    public void insertFormularioPadrao(FormularioPadrao formularioPadrao){
        formularioPadraoDao.insertFormularioPadrao(formularioPadrao);
        firestoreRepository.insertFormularioPadraoToFirestore(
                getFormularioPadrao(formularioPadrao.getUserId())
        );
    }

    public FormularioPadrao getFormularioPadrao(int userId){
        return formularioPadraoDao.getFormularioPadrao(userId);
    }

    //--ANOTAÇÃO_COMPORTAMENTOS-------------------------------------------------------------------//
    public void insertAnotacaoComportamento(AnotacaoComportamento anotacao){
        anotacaoComportamentoDao.insertAnotacao(anotacao);
        firestoreRepository.insertAnotacaoComportamentoToFirestore(getLastAnotacaoComportamento());
    }

    public List<AnotacaoComportamento> getAnotacoesComportamento(int animalId){
        return anotacaoComportamentoDao.getAllAnotacoesOfOneAnimal(animalId);
    }

    public AnotacaoComportamento getLastAnotacaoComportamento(){
        return anotacaoComportamentoDao.getAllAnotacoesAnimal().get(
                anotacaoComportamentoDao.getAllAnotacoesAnimal().size() - 1
        );
    }

    public void excluirAnotacaoComportamento(AnotacaoComportamento anotacaoComportamento){
        anotacaoComportamentoDao.deleteAnotacao(anotacaoComportamento);
        firestoreRepository.deleteAnotacaoComportamentoInFirestore(anotacaoComportamento);
    }

    //--User--------------------------------------------------------------------------------------//

    public void insertUser(User user){
        userDao.insertUser(user);
        firestoreRepository.insertUserToFirestore(getUser(user.getEmail()));
    }

    public List<User> getAllUsers(){
        return userDao.getAllUsers();
    }

    public User getUser(String email){
        return userDao.getUser(email);
    }

    //--EXCLUIR_TUDO------------------------------------------------------------------------------//


    public boolean excluirTudo() {
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
        for(FormularioLote formularioComportamento : formularioLoteDao.getAllFormularioComportamento()){
            firestoreRepository.deleteFormularioLoteInFirestore(formularioComportamento);
        }
        for(FormularioPadrao formularioPadrao : formularioPadraoDao.getAllFormularioPadrao()){
            firestoreRepository.deleteFormularioPadraoInFirestore(formularioPadrao);
        }
        for(Comportamento comportamento : comportamentoDao.getAllComportamentos()){
            firestoreRepository.deleteComportamentoInFirestore(comportamento);
        }

        animalDao.deleteAllAnimais();
        loteDao.deleteAllLotes();
        tipoComportamentoDao.deleteAllTipos();
        anotacaoComportamentoDao.deleteAllAnotacoes();
        formularioLoteDao.deleteAllFormularios();
        formularioPadraoDao.deleteAllFormulariosPadrao();
        comportamentoDao.deleteAllComportamentos();
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

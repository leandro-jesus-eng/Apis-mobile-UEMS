package com.apis.data.repositories;

import android.content.Context;
import android.util.Log;
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
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioLote;
import com.apis.model.FormularioPadrao;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class FirestoreRepository {

    final private FirebaseFirestore firebaseFirestore;

    final String ROOT_PATH = "Apis_data/shared_configuration/";
    final String LOTE_DOC_PREFIX = "Lote-id:";
    final String ANIMAL_DOC_PREFIX = "Animal-id:";
    final String TIPO_COMPORTAMENTO_DOC_PREFIX = "TipoComportamento-id:";
    final String COMPORTAMENTO_DOC_PREFIX = "Comportamento-id:";
    final String FORMULARIO_LOTE_DOC_PREFIX = "FormularioLote-id:";
    final String FORMULARIO_PADRAO_DOC_PREFIX = "FormularioPadrao-id:";
    final String ANOTACAO_COMPORTAMENTO_DOC_PREFIX = "AnotacaoComportamento-id:";
    final String USER_DOC_PREFIX = "User-id:";
    final String USER_LOTE_CROSS_REF_DOC_PREFIX = "UserLoteCrossRef-id:";
    final String DOCUMENT_CHANGE_TYPE_ADDED = "ADDED";
    final String DOCUMENT_CHANGE_TYPE_REMOVED = "REMOVED";
    final String DOCUMENT_CHANGE_TYPE_MODIFIED = "MODIFIED";
    final String FIELD_NAME = "nome";
    final String FIELD_LAST_UPDATE = "lastUpdate";
    final String FIELD_DESCRIPTION = "descricao";
    final String ERROR_TAG = "ERROR";

    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioLoteDao formularioComportamentoDao;
    final private FormularioPadraoDao formularioPadraoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;
    final private UserDao userDao;
    final private RelationsDao relationsDao;
    final private EntitiesHandlerRepository entitiesHandlerRepository;

    public FirestoreRepository(Context ctx) {
        Database databaseInstance = Database.getInstance(ctx);
        firebaseFirestore = FirebaseFirestore.getInstance();
        entitiesHandlerRepository = new EntitiesHandlerRepository(ctx);
        animalDao = databaseInstance.animalDao();
        loteDao = databaseInstance.loteDao();
        formularioComportamentoDao = databaseInstance.formularioLoteDao();
        formularioPadraoDao = databaseInstance.formularioPadraoDao();
        tipoComportamentoDao = databaseInstance.tipoComportamentoDao();
        anotacaoComportamentoDao = databaseInstance.anotacaoComportamentoDao();
        comportamentoDao = databaseInstance.comportamentoDao();
        userDao = databaseInstance.userDao();
        relationsDao = databaseInstance.relationsDao();
    }

    public void setupRemoteChangeListener(){
        refreshLotesWithFirestore();
        refreshAnimaisWithFirestore();
        refreshTipoComportamentoWithFirestore();
        refreshComportamentosWithFirestore();
        refreshFormulariosLoteWithFirestore();
        refreshAnotacoesComportamentoWithFirestore();
        refreshUsersWithFirestore();
        refreshUserLoteCrossRefsWithFirestore();
        refreshFormulariosPadraoWithFirestore();
    }

    public void insertLoteToFirestore(Lote lote) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(LOTE_DOC_PREFIX + lote.getLoteId())
                    .set(lote, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteLoteInFirestore(Lote lote) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(LOTE_DOC_PREFIX + lote.getLoteId())
                    .delete();
            if(entitiesHandlerRepository.loteExiste(lote.getNome(), lote.getExperimento())){
                loteDao.deleteLote(lote);
            }
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertAnimalToFirestore(Animal animal) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animal.getId())
                    .set(animal, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteAnimalInFirestore(Animal animal) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animal.getId())
                    .delete();

            animalDao.deleteAnimal(animal);
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertTipoComportamentoToFirestore(TipoComportamento tipoComportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .set(tipoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteTipoComportamentoInFirestore(TipoComportamento tipoComportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .delete();
            if(entitiesHandlerRepository.tipoComportamentoExiste(tipoComportamento.getId())){
                tipoComportamentoDao.deleteTipo(tipoComportamento);
            }
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertComportamentoToFirestore(Comportamento comportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .set(comportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteComportamentoInFirestore(Comportamento comportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .delete();

            if(entitiesHandlerRepository.comportamentoExiste(comportamento.getId())){
                comportamentoDao.deleteComportamento(comportamento);
            }
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertFormularioLoteToFirestore(FormularioLote formularioLote) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioLote")
                    .document(FORMULARIO_LOTE_DOC_PREFIX + formularioLote.getId())
                    .set(formularioLote, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteFormularioLoteInFirestore(FormularioLote formularioLote) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioLote")
                    .document(FORMULARIO_LOTE_DOC_PREFIX + formularioLote.getId())
                    .delete();
            if(entitiesHandlerRepository.formularioLoteExiste(formularioLote.getId())){
                formularioComportamentoDao.deleteFormulario(formularioLote);
            }
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertFormularioPadraoToFirestore(FormularioPadrao formularioPadrao) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioPadrao")
                    .document(FORMULARIO_PADRAO_DOC_PREFIX + formularioPadrao.getId())
                    .set(formularioPadrao, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteFormularioPadraoInFirestore(FormularioPadrao formularioPadrao) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioPadrao")
                    .document(FORMULARIO_PADRAO_DOC_PREFIX + formularioPadrao.getId())
                    .delete();
            if(entitiesHandlerRepository.formularioPadraoExiste(formularioPadrao.getId())){
                formularioPadraoDao.deleteFormularioPadrao(formularioPadrao);
            }
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertAnotacaoComportamentoToFirestore(AnotacaoComportamento anotacaoComportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(ANOTACAO_COMPORTAMENTO_DOC_PREFIX + anotacaoComportamento.getId())
                    .set(anotacaoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteAnotacaoComportamentoInFirestore(AnotacaoComportamento anotacaoComportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(ANOTACAO_COMPORTAMENTO_DOC_PREFIX + anotacaoComportamento.getId())
                    .delete();
            anotacaoComportamentoDao.deleteAnotacao(anotacaoComportamento);
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertUserToFirestore(User user) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "User")
                    .document(USER_DOC_PREFIX + user.getUserId())
                    .set(user, SetOptions.merge());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void insertUserLoteCrossRefToFirestore(UserLoteCrossRef userLoteCrossRef) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "UserLoteCrossRef").document(
                    USER_LOTE_CROSS_REF_DOC_PREFIX + userLoteCrossRef.userId + "-" + userLoteCrossRef.loteId
            ).set(userLoteCrossRef, SetOptions.merge());
        } catch (Exception e) {
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void deleteUserLoteCrossRefToFirestore(UserLoteCrossRef userLoteCrossRef) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "UserLoteCrossRef").document(
                    USER_LOTE_CROSS_REF_DOC_PREFIX + userLoteCrossRef.userId + "-" + userLoteCrossRef.loteId
            ).delete();
        } catch (Exception e) {
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshLotesWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange loteChange : value.getDocumentChanges()){
                                Lote lote = loteChange.getDocument().toObject(Lote.class);

                                if(!entitiesHandlerRepository.loteExiste(
                                        lote.getNome(),
                                        lote.getExperimento()
                                ) && loteChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)){
                                    loteDao.insertLote(lote);
                                }

                                if(entitiesHandlerRepository.loteExiste(
                                        lote.getNome(),
                                        lote.getExperimento()
                                ) && loteChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)){
                                    loteDao.deleteLote(lote);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshAnimaisWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange animalChange : value.getDocumentChanges()){
                                Animal animal = animalChange.getDocument().toObject(Animal.class);

                                if(!entitiesHandlerRepository.animalExiste(animal.getNome()) &&
                                        animalChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    animalDao.insertAnimal(animal);
                                }

                                if(entitiesHandlerRepository.animalExiste(animal.getNome()) &&
                                        animalChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    animalDao.deleteAnimal(animal);
                                }

                                if(entitiesHandlerRepository.animalExiste(animal.getNome()) &&
                                        animalChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_MODIFIED)
                                ){
                                    animalDao.setLastUpdate(animal.getId(), animal.getLastUpdate());
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshTipoComportamentoWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange tipoComportamentoChange : value.getDocumentChanges()){
                                TipoComportamento tipoComportamento = tipoComportamentoChange
                                        .getDocument()
                                        .toObject(TipoComportamento.class);
                                if(!entitiesHandlerRepository.tipoComportamentoExiste(tipoComportamento.getId()) &&
                                        tipoComportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    tipoComportamentoDao.insertTipo(tipoComportamento);
                                }

                                if(entitiesHandlerRepository.tipoComportamentoExiste(tipoComportamento.getId()) &&
                                        tipoComportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    tipoComportamentoDao.deleteTipo(tipoComportamento);
                                }

                                if(entitiesHandlerRepository.tipoComportamentoExiste(tipoComportamento.getId()) &&
                                        tipoComportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_MODIFIED)
                                ){
                                    tipoComportamentoDao.updateTipo(tipoComportamento);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshComportamentosWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange comportamentoChange : value.getDocumentChanges()){
                                Comportamento comportamento = comportamentoChange
                                        .getDocument()
                                        .toObject(Comportamento.class);
                                if(!entitiesHandlerRepository.comportamentoExiste(comportamento.getId()) &&
                                        comportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    comportamentoDao.insertComportamento(comportamento);
                                }

                                if(entitiesHandlerRepository.comportamentoExiste(comportamento.getId()) &&
                                        comportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    comportamentoDao.deleteComportamento(comportamento);
                                }

                                if(entitiesHandlerRepository.comportamentoExiste(comportamento.getId()) &&
                                        comportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_MODIFIED)
                                ){
                                    comportamentoDao.updateComportamento(comportamento);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshFormulariosLoteWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioLote")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange formularioLoteChange : value.getDocumentChanges()){
                                FormularioLote formularioLote = formularioLoteChange
                                        .getDocument()
                                        .toObject(FormularioLote.class);
                                if(!entitiesHandlerRepository.formularioLoteExiste(formularioLote.getId()) &&
                                        formularioLoteChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    formularioComportamentoDao.insertFormulario(formularioLote);
                                }

                                if(entitiesHandlerRepository.formularioLoteExiste(formularioLote.getId()) &&
                                        formularioLoteChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    formularioComportamentoDao.deleteFormulario(formularioLote);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshFormulariosPadraoWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioPadrao")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange formularioPadraoChange : value.getDocumentChanges()){
                                FormularioPadrao formularioPadrao = formularioPadraoChange
                                        .getDocument()
                                        .toObject(FormularioPadrao.class);
                                if(!entitiesHandlerRepository.formularioPadraoExiste(formularioPadrao.getId()) &&
                                        formularioPadraoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    formularioPadraoDao.insertFormularioPadrao(formularioPadrao);
                                }

                                if(entitiesHandlerRepository.formularioPadraoExiste(formularioPadrao.getId()) &&
                                        formularioPadraoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    formularioPadraoDao.deleteFormularioPadrao(formularioPadrao);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshAnotacoesComportamentoWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange anotacaoComportamentoChange : value.getDocumentChanges()){
                                AnotacaoComportamento anotacaoComportamento = anotacaoComportamentoChange
                                        .getDocument()
                                        .toObject(AnotacaoComportamento.class);

                                if(!entitiesHandlerRepository.anotacaoComportamentoExiste(anotacaoComportamento.getId()) &&
                                        anotacaoComportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ){
                                    anotacaoComportamentoDao.insertAnotacao(anotacaoComportamento);
                                }

                                if(entitiesHandlerRepository.anotacaoComportamentoExiste(anotacaoComportamento.getId()) &&
                                        anotacaoComportamentoChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ){
                                    anotacaoComportamentoDao.deleteAnotacao(anotacaoComportamento);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshUsersWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "User")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange userChange : value.getDocumentChanges()){
                                User user = userChange.getDocument().toObject(User.class);

                                if (!entitiesHandlerRepository.userExists(user.getEmail()) &&
                                        userChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)
                                ) { userDao.insertUser(user); }
                                if (entitiesHandlerRepository.userExists(user.getEmail()) &&
                                        userChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)
                                ) { userDao.deleteUser(user); }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    private void refreshUserLoteCrossRefsWithFirestore() {
        try {
            firebaseFirestore.collection(ROOT_PATH + "UserLoteCrossRef")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange userChange : value.getDocumentChanges()){
                                UserLoteCrossRef userLoteCrossRef =
                                        userChange.getDocument().toObject(UserLoteCrossRef.class);

                                if (!entitiesHandlerRepository.userLoteCrosRefExists(
                                        userLoteCrossRef.userId, userLoteCrossRef.loteId
                                ) && userChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_ADDED)) {
                                    relationsDao.insertUserLoteCrossRef(userLoteCrossRef);
                                }
                                if (entitiesHandlerRepository.userLoteCrosRefExists(
                                        userLoteCrossRef.userId, userLoteCrossRef.loteId
                                ) && userChange.getType().toString().equals(DOCUMENT_CHANGE_TYPE_REMOVED)) {
                                    relationsDao.deleteUserLoteCrossRef(userLoteCrossRef);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }

    public void updateLastAnotacaoAnimalInFirebase(Integer animalId, String lastUpdate) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animalId)
                    .update(FIELD_LAST_UPDATE, lastUpdate);
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }
    public void updateComportamentoInFirebase(Comportamento comportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .update(FIELD_NAME, comportamento.getNome());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }
    public void updateTipoComportamentoInFirebase(TipoComportamento tipoComportamento) {
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .update(FIELD_DESCRIPTION, tipoComportamento.getDescricao());
        } catch (Exception e){
            Log.i(ERROR_TAG, e.toString());
        }
    }
}
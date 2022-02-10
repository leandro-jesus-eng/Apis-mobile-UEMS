package com.apis.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioComportamentoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.Database;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.List;

public class FirestoreRepository {

    final private FirebaseFirestore firebaseFirestore;

    final String ROOT_PATH = "Apis_data/shared_configuration/";
    final String LOTE_DOC_PREFIX = "Lote-id:";
    final String ANIMAL_DOC_PREFIX = "Animal-id:";
    final String TIPO_COMPORTAMENTO_DOC_PREFIX = "TipoComportamento-id:";
    final String COMPORTAMENTO_DOC_PREFIX = "Comportamento-id:";
    final String FORMULARIO_COMPORTAMENTO_DOC_PREFIX = "FormularioComportamento-id:";
    final String ANOTACAO_COMPORTAMENTO_DOC_PREFIX = "AnotacaoComportamento-id:";

    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioComportamentoDao formularioComportamentoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;

    public FirestoreRepository(Context ctx){
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void updateRemote(
            Lote lote,
            Animal animal,
            TipoComportamento tipoComportamento,
            Comportamento comportamento,
            FormularioComportamento formularioComportamento,
            AnotacaoComportamento anotacaoComportamento
    ){
        insertLoteToFirestore(lote);
        insertAnimalToFirestore(animal);
        insertTipoComportamentoToFirestore(tipoComportamento);
        insertComportamentoToFirestore(comportamento);
        insertFormularioComportamentoToFirestore(formularioComportamento);
        insertAnotacaoComportamentoToFirestore(anotacaoComportamento);
    }

    public void setupRemoteChangeListener(){
        getLotesFromFirestore();
        getAnimaisFromFirestore();
        getTipoComportamentoFromFirestore();
        getComportamentosFromFirestore();
        getFormularioComportamentoFromFirestore();
        getAnotacoesComportamentoFromFirestore();
    }

    private void insertLoteToFirestore(Lote lote){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(LOTE_DOC_PREFIX + lote.getId())
                    .set(lote, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertAnimalToFirestore(Animal animal){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animal.getId())
                    .set(animal, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertTipoComportamentoToFirestore(TipoComportamento tipoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .set(tipoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertComportamentoToFirestore(Comportamento comportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .set(comportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertFormularioComportamentoToFirestore(FormularioComportamento formularioComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioComportamento")
                    .document(FORMULARIO_COMPORTAMENTO_DOC_PREFIX + formularioComportamento.getId())
                    .set(formularioComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertAnotacaoComportamentoToFirestore(AnotacaoComportamento anotacaoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(ANOTACAO_COMPORTAMENTO_DOC_PREFIX + anotacaoComportamento.getId())
                    .set(anotacaoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getLotesFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange lotes : value.getDocumentChanges()){
                                loteDao.insertLote(lotes.getDocument().toObject(Lote.class));
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getAnimaisFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange animais : value.getDocumentChanges()){
                                animalDao.insertAnimal(animais.getDocument().toObject(Animal.class));
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getTipoComportamentoFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange tiposComportamento : value.getDocumentChanges()){
                                tipoComportamentoDao.insertTipo(
                                        tiposComportamento
                                                .getDocument()
                                                .toObject(TipoComportamento.class)
                                );
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getComportamentosFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange comportamentos : value.getDocumentChanges()){
                                comportamentoDao.insertComportamento(
                                        comportamentos
                                                .getDocument()
                                                .toObject(Comportamento.class)
                                );
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getFormularioComportamentoFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioComportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange formulariosComportamento : value.getDocumentChanges()){
                                formularioComportamentoDao.insertFormulario(
                                        formulariosComportamento
                                                .getDocument()
                                                .toObject(FormularioComportamento.class)
                                );
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void getAnotacoesComportamentoFromFirestore(){
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .addSnapshotListener((value, error) -> {
                        if(value != null){
                            for(DocumentChange anotacoesComportamento : value.getDocumentChanges()){
                                anotacaoComportamentoDao.insertAnotacao(
                                        anotacoesComportamento
                                                .getDocument()
                                                .toObject(AnotacaoComportamento.class)
                                );
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
}

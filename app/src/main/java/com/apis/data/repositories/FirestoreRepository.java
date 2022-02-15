package com.apis.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    final private DbRepository dbRepository;

    public FirestoreRepository(Context ctx) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        dbRepository = new DbRepository(ctx);
    }

    public void setupRemoteChangeListener(){
        getLotesFromFirestore();
        getAnimaisFromFirestore();
        getTipoComportamentoFromFirestore();
        getComportamentosFromFirestore();
        getFormularioComportamentoFromFirestore();
        getAnotacoesComportamentoFromFirestore();
    }

    public void insertLoteToFirestore(Lote lote){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(LOTE_DOC_PREFIX + lote.getId())
                    .set(lote, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteLoteInFirestore(Lote lote){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(LOTE_DOC_PREFIX + lote.getId())
                    .delete();
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void insertAnimalToFirestore(Animal animal){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animal.getId())
                    .set(animal, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteAnimalInFirestore(Animal animal){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animal.getId())
                    .delete();
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void insertTipoComportamentoToFirestore(TipoComportamento tipoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .set(tipoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteTipoComportamentoInFirestore(TipoComportamento tipoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .delete();
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void insertComportamentoToFirestore(Comportamento comportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .set(comportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteComportamentoInFirestore(Comportamento comportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .delete();
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void insertFormularioComportamentoToFirestore(FormularioComportamento formularioComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioComportamento")
                    .document(FORMULARIO_COMPORTAMENTO_DOC_PREFIX + formularioComportamento.getId())
                    .set(formularioComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteFormularioComportamentoInFirestore(FormularioComportamento formularioComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioComportamento")
                    .document(FORMULARIO_COMPORTAMENTO_DOC_PREFIX + formularioComportamento.getId())
                    .delete();
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void insertAnotacaoComportamentoToFirestore(AnotacaoComportamento anotacaoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(ANOTACAO_COMPORTAMENTO_DOC_PREFIX + anotacaoComportamento.getId())
                    .set(anotacaoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    public void deleteAnotacaoComportamentoInFirestore(AnotacaoComportamento anotacaoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(ANOTACAO_COMPORTAMENTO_DOC_PREFIX + anotacaoComportamento.getId())
                    .delete();
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
                                Lote lote = lotes.getDocument().toObject(Lote.class);
                                if(!dbRepository.loteExiste(lote.getNome(), lote.getExperimento())){
                                    dbRepository.insertLote(lote);
                                }
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
                                Animal animal = animais.getDocument().toObject(Animal.class);
                                if(!dbRepository.animalExiste(animal.getNome())){
                                    dbRepository.insertAnimal(animal);
                                }
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
                                TipoComportamento tipoComportamento = tiposComportamento
                                        .getDocument()
                                        .toObject(TipoComportamento.class);
                                if(!dbRepository.tipoComportamentoExiste(tipoComportamento.getId())){
                                    dbRepository.insertTipoComportamento(tipoComportamento);
                                }
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
                                Comportamento comportamento = comportamentos
                                        .getDocument()
                                        .toObject(Comportamento.class);
                                if(!dbRepository.comportamentoExiste(comportamento.getId())){
                                    dbRepository.insertComportamento(comportamento);
                                }
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
                                FormularioComportamento formularioComportamento = formulariosComportamento
                                        .getDocument()
                                        .toObject(FormularioComportamento.class);
                                if(!dbRepository.formularioComportamentoExiste(formularioComportamento.getId())){
                                    dbRepository.insertFormularioComportamento(formularioComportamento);
                                }
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
                                AnotacaoComportamento anotacaoComportamento = anotacoesComportamento
                                        .getDocument()
                                        .toObject(AnotacaoComportamento.class);

                                if(!dbRepository.anotacaoComportamentoExiste(anotacaoComportamento.getId())){
                                    dbRepository.insertAnotacaoComportamento(anotacaoComportamento);
                                }
                            }
                        }
                    });
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
    public void updateLastAnotacaoAnimalInFirebase(Integer animalId, String lastUpdate){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(ANIMAL_DOC_PREFIX + animalId)
                    .update("lastUpdate",lastUpdate);
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
    public void updateComportamentoInFirebase(Comportamento comportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(COMPORTAMENTO_DOC_PREFIX + comportamento.getId())
                    .update("nome", comportamento.getNome());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
    public void updateTipoComportamentoInFirebase(TipoComportamento tipoComportamento){
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(TIPO_COMPORTAMENTO_DOC_PREFIX + tipoComportamento.getId())
                    .update("descricao",tipoComportamento.getDescricao());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
}

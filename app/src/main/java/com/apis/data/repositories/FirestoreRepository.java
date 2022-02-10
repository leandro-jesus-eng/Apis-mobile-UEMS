package com.apis.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.apis.data.ObjectMapper;
import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioComportamentoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.RelationsDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.Database;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class FirestoreRepository {

    final private Context context;
    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioComportamentoDao formularioComportamentoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;
    final private RelationsDao relationsDao;
    final private FirebaseFirestore firebaseFirestore;
    final private ObjectMapper objectMapper ;

    final String ROOT_PATH = "Apis_data/shared_configuration/";

    public FirestoreRepository(Context ctx){
        context = ctx;
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
        relationsDao = Database.getInstance(ctx).relationsDao();
        objectMapper = new ObjectMapper();
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

    private void insertLoteToFirestore(Lote lote){
        Map<String, Object> mappedLote = objectMapper.mapLoteToFirestore(lote);
        try {
            firebaseFirestore.collection(ROOT_PATH + "Lote")
                    .document(Integer.toString(lote.getId()))
                    .set(mappedLote, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertAnimalToFirestore(Animal animal){
        Map<String, Object> mappedAnimal = objectMapper.mapAnimalToFirestore(animal);
        try {
            firebaseFirestore.collection(ROOT_PATH + "Animal")
                    .document(Integer.toString(animal.getId()))
                    .set(mappedAnimal, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertTipoComportamentoToFirestore(TipoComportamento tipoComportamento){
        Map<String, Object> mappedtipoComportamento = objectMapper.mapTipoComportamentoToFirestore(tipoComportamento);
        try {
            firebaseFirestore.collection(ROOT_PATH + "TipoComportamento")
                    .document(Integer.toString(tipoComportamento.getId()))
                    .set(mappedtipoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertComportamentoToFirestore(Comportamento comportamento){
        Map<String, Object> mappedComportamento = objectMapper.mapComportamentoToFirestore(comportamento);
        try {
            firebaseFirestore.collection(ROOT_PATH + "Comportamento")
                    .document(Integer.toString(comportamento.getId()))
                    .set(mappedComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertFormularioComportamentoToFirestore(FormularioComportamento formularioComportamento){
        Map<String, Object> mappedFormularioComportamento = objectMapper.mapFormularioComportamentoToFirestore(formularioComportamento);
        try {
            firebaseFirestore.collection(ROOT_PATH + "FormularioComportamento")
                    .document(Integer.toString(formularioComportamento.getId()))
                    .set(mappedFormularioComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }

    private void insertAnotacaoComportamentoToFirestore(AnotacaoComportamento anotacaoComportamento){
        Map<String, Object> mappedAnotacaoComportamento = objectMapper.mapAnotacaoComportamentoToFirestore(anotacaoComportamento);
        try {
            firebaseFirestore.collection(ROOT_PATH + "AnotacaoComportamento")
                    .document(Integer.toString(anotacaoComportamento.getId()))
                    .set(mappedAnotacaoComportamento, SetOptions.merge());
        } catch (Exception e){
            Log.i("ERROR", e.toString());
        }
    }
}

package com.apis.data.repositories;

import android.content.Context;

import com.apis.data.database.DAOs.AnimalDao;
import com.apis.data.database.DAOs.AnotacaoComportamentoDao;
import com.apis.data.database.DAOs.ComportamentoDao;
import com.apis.data.database.DAOs.FormularioComportamentoDao;
import com.apis.data.database.DAOs.LoteDao;
import com.apis.data.database.DAOs.RelationsDao;
import com.apis.data.database.DAOs.TipoComportamentoDao;
import com.apis.data.database.DAOs.UserDao;
import com.apis.data.database.Database;
import com.apis.data.database.relations.UserLoteCrossRef;
import com.apis.features.usecases.ManageUser;
import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioLote;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;
import com.apis.model.User;
import java.util.List;

public class EntitiesHandlerRepository {

    final private AnimalDao animalDao;
    final private LoteDao loteDao;
    final private FormularioComportamentoDao formularioComportamentoDao;
    final private TipoComportamentoDao tipoComportamentoDao;
    final private AnotacaoComportamentoDao anotacaoComportamentoDao;
    final private ComportamentoDao comportamentoDao;
    final private UserDao userDao;
    final private RelationsDao relationsDao;

    public EntitiesHandlerRepository(Context ctx) {
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
        formularioComportamentoDao = Database.getInstance(ctx).formularioComportamentoDao();
        tipoComportamentoDao = Database.getInstance(ctx).tipoComportamentoDao();
        anotacaoComportamentoDao = Database.getInstance(ctx).anotacaoComportamentoDao();
        comportamentoDao = Database.getInstance(ctx).comportamentoDao();
        userDao = Database.getInstance(ctx).userDao();
        relationsDao = Database.getInstance(ctx).relationsDao();
    }

    public boolean animalExiste(String nomeAnimal) {
        List<Animal> result = animalDao.getAllAnimais();

        for(Animal animal : result) {
            if(animal.getNome().equals(nomeAnimal)) {
                return true;
            }
        }
        return false;
    }

    public boolean loteExiste(String nomeLote, String experimento) {
        List<Lote> loteList = loteDao.getAllLotes();
        for (Lote lote : loteList) {
            if (lote.getNome().equals(nomeLote) && lote.getExperimento().equals(experimento)) {
                List<User> userList = relationsDao.getUsersOfLote(lote.getLoteId()).get(0).userList;
                for(User user : userList) {
                    if(user.getUserId() == new ManageUser().getUser().getUserId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean comportamentoExiste(Integer comportamentoId) {
        List<Comportamento> result = comportamentoDao.getAllComportamentos();

        for(Comportamento comportamento : result) {
            if(comportamento.getId() == comportamentoId) {
                return true;
            }
        }
        return false;
    }

    public boolean tipoComportamentoExiste(Integer tipoComportamentoId) {
        List<TipoComportamento> result = tipoComportamentoDao.getAllTipos();

        for(TipoComportamento tipoComportamento : result) {
            if(tipoComportamento.getId() == tipoComportamentoId) {
                return true;
            }
        }
        return false;
    }

    public boolean formularioComportamentoExiste(Integer formularioComportamentoId) {
        List<FormularioLote> result =
                formularioComportamentoDao.getAllFormularioComportamento();

        for(FormularioLote formularioComportamento : result) {
            if(formularioComportamento.getId() == formularioComportamentoId) {
                return true;
            }
        }
        return false;
    }

    public boolean anotacaoComportamentoExiste(Integer anotacaoComportamentoId) {
        List<AnotacaoComportamento> result =
                anotacaoComportamentoDao.getAllAnotacoesAnimal();

        for(AnotacaoComportamento anotacaoComportamento : result) {
            if(anotacaoComportamento.getId() == anotacaoComportamentoId) {
                return true;
            }
        }
        return false;
    }

    public boolean userExists(String email) {
        List<User> result = userDao.getAllUsers();

        for(User user : result){
            if(user.getEmail().equals(email)) return true;
        }
        return false;
    }

    public boolean userLoteCrosRefExists(Integer userId, Integer loteId) {
        List<UserLoteCrossRef> result = relationsDao.getAllUserLoteCrossRef();

        for (UserLoteCrossRef userLoteCrossRef : result) {
            if (userLoteCrossRef.userId.equals(userId) && userLoteCrossRef.loteId.equals(loteId)) {
                return true;
            }
        }
        return false;
    }
}
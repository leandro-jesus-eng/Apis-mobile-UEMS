package com.apis.data;

import com.apis.model.Animal;
import com.apis.model.AnotacaoComportamento;
import com.apis.model.Comportamento;
import com.apis.model.FormularioComportamento;
import com.apis.model.Lote;
import com.apis.model.TipoComportamento;

import java.util.HashMap;
import java.util.Map;

public class ObjectMapper {

    public Map<String, Object> mapLoteToFirestore(Lote lote){
        Map<String, Object> map = new HashMap<>();
        map.put("id", lote.getId());
        map.put("nome", lote.getNome());
        map.put("experimento", lote.getExperimento());
        return map;
    }

    public Map<String, Object> mapAnimalToFirestore(Animal animal) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", animal.getId());
        map.put("nome", animal.getNome());
        map.put("loteId", animal.getLoteId());
        map.put("lastUpdate", animal.getLastUpdate());
        return map;
    }

    public Map<String, Object> mapTipoComportamentoToFirestore(TipoComportamento tipoComportamento) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", tipoComportamento.getId());
        map.put("descricao", tipoComportamento.getDescricao());
        map.put("idFormularioComportamento", tipoComportamento.getIdFormularioComportamento());
        return map;
    }

    public Map<String, Object> mapComportamentoToFirestore(Comportamento comportamento){
        Map<String, Object> map = new HashMap<>();
        map.put("id", comportamento.getId());
        map.put("nome", comportamento.getNome());
        map.put("idTipo", comportamento.getIdTipo());
        return map;
    }

    public Map<String, Object> mapFormularioComportamentoToFirestore(FormularioComportamento formularioComportamento){
        Map<String, Object> map = new HashMap<>();
        map.put("id", formularioComportamento.getId());
        map.put("dataCriacao", formularioComportamento.getDataCriacao());
        map.put("formularioPadrao", formularioComportamento.isFormularioPadrao());
        map.put("loteId", formularioComportamento.getLoteId());
        return map;
    }

    public Map<String, Object> mapAnotacaoComportamentoToFirestore(AnotacaoComportamento anotacaoComportamento){
        Map<String, Object> map = new HashMap<>();
        map.put("id", anotacaoComportamento.getId());
        map.put("nomeAnimal", anotacaoComportamento.getNomeAnimal());
        map.put("idAnimal", anotacaoComportamento.getIdAnimal());
        map.put("data", anotacaoComportamento.getData());
        map.put("hora", anotacaoComportamento.getHora());
        map.put("nomeComportamento", anotacaoComportamento.getNomeComportamento());
        map.put("obs", anotacaoComportamento.getObs());
        return map;
    }
}

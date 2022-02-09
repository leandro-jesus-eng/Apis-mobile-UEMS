package com.apis.data;

import java.util.HashMap;
import java.util.Map;

public class ObjectMapper {

    public Map mapLoteToFirestore(Integer id, String nome, String experimento){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nome", nome);
        map.put("experimento", experimento);

        return map;
    }
}

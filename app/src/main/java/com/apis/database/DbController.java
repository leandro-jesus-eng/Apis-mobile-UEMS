package com.apis.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.apis.database.DAOs.AnimalDao;
import com.apis.database.DAOs.LoteDao;
import com.apis.models.Animal;
import com.apis.models.Comportamento;
import com.apis.models.FileControl;
import com.apis.models.Lote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DbController {

    final private Context context;
    final private AnimalDao animalDao;
    final private LoteDao loteDao;

    public DbController(Context ctx){
        context = ctx;
        animalDao = Database.getInstance(ctx).animalDao();
        loteDao = Database.getInstance(ctx).loteDao();
    }

    //Animais
    public List<Animal> returnAnimais(int idLote){
        return animalDao.returnAnimaisLote(idLote);
    }

    public Animal returnAnimal(int idLote, int animalId){
        return animalDao.returnAnimal(idLote, animalId);
    }

    public void insertAnimal(String nome, int loteId){
        animalDao.insertAnimal(nome, loteId);
    }

    public boolean animalExiste(String nomeAnimal){
        List<Animal> result = animalDao.returnAllAnimais(nomeAnimal);

        for(Animal animal : result){
            if(animal.getNome().equals(nomeAnimal)){
                return true;
            }
        }
        return false;
    }

    public List<Animal> returnAnimaisPorId(int idLote){
        List<Animal> result = animalDao.returnAnimaisLote(idLote);
        ArrayList<Animal> animais = new ArrayList<>();
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        for(Animal animal : result){
            nomes.add(animal.getNome());
            ids.add(animal.getId());
        }
        Collections.sort(ids);
        for(int i=0; i < ids.size(); i++){
            animais.add(new Animal(ids.get(i), nomes.get(i), idLote));
        }
        //TODO Adicionar setUpdateAnimais
        return animais;
    }

    public ArrayList<Animal> retornarAnimaisPorOrdemDeAnotacao(ArrayList<Animal> animais){

        setUpdateAnimais(animais);
        Collections.sort(animais);

        return animais;
    }

    public void setUpdateAnimais(ArrayList<Animal> animais){

        for(Animal animal : animais){
            String data = "";
            String hora = "";
            String dataHora;
            Cursor cursor = database.getWritableDatabase().rawQuery(
                    "SELECT * FROM Comportamento WHERE Animal_id = " + animal.getId(), null);

            while (cursor.moveToNext()) {
                data = cursor.getString(cursor.getColumnIndex("data"));
                hora = cursor.getString(cursor.getColumnIndex("hora"));
            }

            if(data.equals(hora)){
                animal.setLastUpdate("Sem anotação!");
            }else{
                dataHora = data+" "+hora;
                animal.setLastUpdate(dataHora);
            }
            cursor.close();
        }

    }

    //Lotes
    public void insertLote(String nome, String experimento){
        loteDao.insertLote(nome, experimento);
    }

    public Lote returnLote(int idLote){
        return loteDao.returnLote(idLote);
    }

    public List<Lote> returnAllLotes(){
        return loteDao.returnAllLotes();
    }

    public boolean loteExiste(String nomeLote, String experimento){
        List<Lote> result = loteDao.returnAllLotes();

        for(Lote lote : result){
            if(lote.getNome().equals(nomeLote) && lote.getExperimento().equals(experimento) ){
                return true;
            }
        }
        return false;
    }

    public String returnNomeLote(int idLote){
        Lote result = loteDao.returnLote(idLote);

        return result.getNome();
    }

    //Comportamentos
    public boolean adicionarComportamento(int id_animal, String nome_animal, String data, String hora, String comportamento, String obs){

        ContentValues cv = new ContentValues();
        cv.put("Animal_nome", nome_animal);
        cv.put("Animal_id", id_animal);
        cv.put("data", data);
        cv.put("hora", hora);
        cv.put("comportamento", comportamento);
        cv.put("observacao", obs);

        return database.getWritableDatabase().insert("Comportamento", null, cv) > 0;
    }
    public ArrayList<Comportamento> retornarComportamento(int animalId){

        Cursor cursor = database.getWritableDatabase().rawQuery("SELECT * FROM Comportamento WHERE Animal_id = "+animalId, null);

        ArrayList<Comportamento> comportamentos = new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String nome_animal = cursor.getString(cursor.getColumnIndex("Animal_nome"));
            int id_animal = cursor.getInt(cursor.getColumnIndex("Animal_id"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String hora = cursor.getString(cursor.getColumnIndex("hora"));
            String comportamento = cursor.getString(cursor.getColumnIndex("comportamento"));
            String obs =  cursor.getString(cursor.getColumnIndex("observacao"));
            comportamentos.add(new Comportamento(id, nome_animal, id_animal, data, hora, comportamento, obs));
        }
        cursor.close();
        return comportamentos;
    }

    //Excluir
    public boolean excluir(int id, String tableName){
        return database.getWritableDatabase().delete(tableName, "id=?", new String[]{ id + "" }) > 0;
    }

    public boolean apagarTudo() {

        database.getWritableDatabase().delete("Lote", "1", null);
        database.getWritableDatabase().delete("Comportamento", "1", null);
        database.getWritableDatabase().delete("Animal", "1", null);
        database.getWritableDatabase().delete("Preferencia", "1", null);

        FileControl fc = new FileControl(context);
        fc.deleteEverthing();

        return true;

    }

    //Exportar dados
    public boolean exportarDados(int idLote, int idAnimal){

        Lote lote = retornarLote(idLote);
        Animal animal = retornarAnimal(idLote, idAnimal);

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

        // TODO Refatorar para usar o 'retornarComportamentos (id_animal)'
        Cursor cursor = database.getWritableDatabase().rawQuery("SELECT * FROM Comportamento WHERE Animal_id = " + idAnimal, null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String nome_animal = cursor.getString(cursor.getColumnIndex("Animal_nome"));
            String id_animal = cursor.getString(cursor.getColumnIndex("Animal_id"));
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String hora = cursor.getString(cursor.getColumnIndex("hora"));
            String comportamento = cursor.getString(cursor.getColumnIndex("comportamento"));
            String obs = cursor.getString(cursor.getColumnIndex("observacao"));

            //String conteudo = "ID Animal;Data;Hora;Fisiologico;Reprodutivo;Uso de sombra;Observação;";
            String conteudo = id_animal+";"+nome_animal+";"+data+";"+hora+";"+comportamento+";"+obs;

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

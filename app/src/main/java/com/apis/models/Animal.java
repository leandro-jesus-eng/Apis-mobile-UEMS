package com.apis.models;

import android.content.Context;
import android.widget.Toast;
import com.apis.database.DbController;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Animal implements Serializable, Comparable<Animal> {
    private Context context;
    private int id;
    private String nome;
    private int LoteId;

    public Animal(int id, String nome, int LoteId){
        this.id = id;
        this.nome = nome;
        this.LoteId = LoteId;
    }

    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public int getLoteId(){ return this.LoteId; }

    @Override
    public boolean equals(Object o){
        return this.id == ((Animal)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

    public Context setContext(Context context){
        return this.context= context;
    }

    public String getLastUpdate(){
        DbController database = new DbController(setContext(context));
        return database.pegarUltimoUpdateAnimalEnxutado(this.getId());
    }

    @Override
     public int compareTo(Animal outroAnimal) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");

            Date date1 = dateFormat.parse(this.getLastUpdate());
            Date date2 = dateFormat.parse(outroAnimal.getLastUpdate());

            if(date1.after(date2)){
                return -1;
            }else if(date1.before(date2)){
                return 1;
            }

        } catch (ParseException ex) {
        }
        return 0;
    }


}

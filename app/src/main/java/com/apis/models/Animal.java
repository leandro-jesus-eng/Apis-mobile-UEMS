package com.apis.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "tb_animal")
public class Animal implements Serializable, Comparable<Animal> {

    @PrimaryKey(autoGenerate = true)
    final private int id;

    @ColumnInfo(name = "nome")
    final private String nome;

    @ColumnInfo(name = "loteId")
    final private int loteId;

    @ColumnInfo(name = "lastUpdate")
    private String lastUpdate;

    public Animal(int id, String nome, int LoteId){
        this.id = id;
        this.nome = nome;
        this.loteId = LoteId;
    }

    public int getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public int getLoteId(){ return this.loteId; }
    public String getLastUpdate(){
        return this.lastUpdate;
    }
    public void setLastUpdate(String data){
        this.lastUpdate = data;
    }

    @Override
    public boolean equals(Object o){
        return this.id == ((Animal)o).id;
    }

    @Override
    public int hashCode(){
        return this.id;
    }

    @Override
     public int compareTo(Animal outroAnimal) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");

            if(this.getLastUpdate().equals("Sem anotação!")){

                Date date1 = dateFormat.parse("10/10/3030 20:20:20");
                Date date2 = dateFormat.parse(outroAnimal.getLastUpdate());

                if(date1.after(date2)){
                    return 1;
                }else if(date1.before(date2)){
                    return -1;
                }

            }else{
                Date date1 = dateFormat.parse(this.getLastUpdate());
                Date date2 = dateFormat.parse(outroAnimal.getLastUpdate());

                if(date1.after(date2)){
                    return -1;
                }else if(date1.before(date2)){
                    return 1;
                }
            }

        } catch (ParseException ex) {

        }
        return 0;
    }


}

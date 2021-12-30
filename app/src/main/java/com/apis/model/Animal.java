package com.apis.model;

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

    public Animal(int id, String nome, int loteId, String lastUpdate){
        this.id = id;
        this.nome = nome;
        this.loteId = loteId;
        this.lastUpdate = lastUpdate;
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
            if(this.getLastUpdate().equals("Sem anotação!") && outroAnimal.getLastUpdate().equals("Sem anotação!")){
                return 0;
            }else if(this.getLastUpdate().equals("Sem anotação!") && !outroAnimal.getLastUpdate().equals("Sem anotação!")){
                return 1;
            }else if(!this.getLastUpdate().equals("Sem anotação!") && outroAnimal.getLastUpdate().equals("Sem anotação!")){
                return -1;
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat ("dd/MM/yyyy HH:mm:ss");
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

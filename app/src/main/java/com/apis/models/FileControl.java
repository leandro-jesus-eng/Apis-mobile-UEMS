package com.apis.models;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.apis.database.DbController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ContentHandler;
import java.util.ArrayList;

public class FileControl {

    private Context context;

    public FileControl(Context context) {
        this.context = context;
    }

    public static String getNameOfLoteCSV (Lote lote) {
        return "dados_Lote_" + lote.getNome().replace(" ", "") + ".csv";
    }


    public static String getNameOfAnimalCSV (Lote lote, Animal animal) {
        return "dados_Lote_" + lote.getNome().replace(" ", "") + "__Animal_" + animal.getNome().replace(" ", "") + ".csv";
    }

    private File getMarkedOptionsFile () throws IOException {

        File files[] = context.getExternalFilesDirs(null);
        File f = null;
        if(files.length > 0) {
            f = new File( files[0] , "marked_options.tmp");
            if (!f.exists()) {
                f.createNewFile();
            }
        } else {
            Toast.makeText(context, "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return f;
    }

    public void writeTmp(String value){
        try {
            File f = getMarkedOptionsFile();
            if(f == null) return;

            FileOutputStream out = new FileOutputStream(f, true);
            out.write(value.getBytes());
            out.write('\n');
            out.flush();
            out.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteValue(String value){

        ArrayList<String> valores = new ArrayList<String>();

        try {

            File f = getMarkedOptionsFile();
            if(f == null) return;

            FileReader arq = new FileReader(f);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();

            while (linha != null) {
                if(!linha.equals(value)){
                    valores.add(linha); //Adiciona os valores diferentes do especificado
                }

                linha = lerArq.readLine();
            }

            arq.close();

            f.delete();

        } catch (IOException e) {
            Log.e("FILE: ", "Erro na abertura do arquivo: "+ e.getMessage());
        }

        //Grava os novos valores
        while(!valores.isEmpty()){
            writeTmp(valores.remove(0));
        }

    }

    public void updateValue(String value){

        ArrayList<String> valores = new ArrayList<String>();
        boolean existeValorIgual = false;

        try {

            File f = getMarkedOptionsFile();
            if(f == null) return;

            FileReader arq = new FileReader(f);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();

            while (linha != null) {
                if(linha.equals(value)){
                    existeValorIgual = true;
                }
                linha = lerArq.readLine();
            }

            arq.close();
        } catch (IOException e) {
            Log.e("FILE: ", "Erro na abertura do arquivo: "+ e.getMessage());
        }

        if(existeValorIgual){
            deleteValue(value);
            Log.e("FILE: ", "Excluido!");
        } else {
            writeTmp(value);
            Log.e("FILE: ", "Salvo!");
        }
    }

    public void deleteTmpFile(){
        try {
            File f = getMarkedOptionsFile();
            if(f == null) return;
            f.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> returnValues() {

        ArrayList<String> valores = new ArrayList<String>();

        try {
            File f = getMarkedOptionsFile();
            if(f == null) return valores;

            FileReader arq = new FileReader(f);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();

            while (linha != null) {
                valores.add(linha);
                linha = lerArq.readLine();
            }

            arq.close();
        } catch (IOException e) {
            Log.e("FILE: ", "Erro na abertura do arquivo: " + e.getMessage());
        }

        return valores;
    }

    public void deleteLoteFile(Context context, int idLote){

        DbController database = new DbController(context);
        Lote lote = database.retornarLote(idLote);

        File files[] = context.getExternalFilesDirs(null);
        File f = null;
        if(files.length > 0) {
            f = new File( files[0] , FileControl.getNameOfLoteCSV(lote));
            f.delete();
        } else {
            Toast.makeText(context, "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteEverthing() {

        File files[] = context.getExternalFilesDirs(null);
        File f = null;
        if(files.length > 0) {

            if (files[0].isDirectory()) {
                String[] children = files[0].list();
                for (int i = 0; i < children.length; i++) {
                    new File(files[0], children[i]).delete();
                }
            }
        } else {
            Toast.makeText(context, "APP não possui permissão para salvar no dispositivo!", Toast.LENGTH_SHORT).show();
        }
    }
}

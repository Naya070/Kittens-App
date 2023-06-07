/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cats.catsapp;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author naya
 */
public class CatsService {

    public static void seeCats() throws IOException {
        //1. vamos a traer los datos de la API
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();

        Response response = client.newCall(request).execute();
        
        String stringJson = response.body().string();


        //Cortar los corchetes
        stringJson = stringJson.substring(1, stringJson.length());
        stringJson = stringJson.substring(0, stringJson.length() - 1);

        //crear un objeto de la clase gson
        Gson gson = new Gson();
        Cats cats = gson.fromJson(stringJson, Cats.class);

        //redimensionar en caso de necesitar
        Image image = null;
        try {
            URL url = new URL(cats.getUrl());
            image = ImageIO.read(url);

            ImageIcon backgroundCat = new ImageIcon(image);

            if (backgroundCat.getIconWidth() > 800) {
                Image background = backgroundCat.getImage();
                Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                backgroundCat = new ImageIcon(modified);
            }

            String menu = "Opciones: \n"
                    + "1. ver otra imagen \n"
                    + "2. Favorito \n"
                    + "3. Volver \n";

            String[] buttons = {"Ver otra imagen", "Favorito", "Volver"};
            String idCat = cats.getId();
            String option = (String) JOptionPane.showInputDialog(null, menu, idCat, JOptionPane.INFORMATION_MESSAGE, backgroundCat, buttons, buttons[0]);

            int selection = -1;
            //validamos que opción selecciona el usuario
            for (int i = 0; i < buttons.length; i++) {
                if (option.equals(buttons[i])) {
                    selection = i;
                }

                switch (selection) {
                    case 0:
                        seeCats();
                        break;
                    case 1:
                        favoriteCat(cats);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
}
    public static void favoriteCat(Cats cat) {
        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n\t\"image_id\":\""+cat.getId()+"\"\n}");
            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites")
              .post(body)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", cat.getApikey())
              .build();
            Response response = client.newCall(request).execute();            
                  
        }catch(IOException e){
            System.out.println(e);
        }

    }
    public static void seeFavorite(String apikey) throws IOException{
    
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/favourites")
          .get()
          .addHeader("Content-Type", "application/json")
          .addHeader("x-api-key", apikey)
          .build();

        Response response = client.newCall(request).execute();
        
        // guardamos el string con la respuesta
        String strJson = response.body().string();
        
        //creamos el objeto gson
        Gson gson = new Gson();
        
        CatsFav[] catsArray = gson.fromJson(strJson,CatsFav[].class);
        
        if(catsArray.length > 0){
            int min = 1;
            int max  = catsArray.length;
            int randomNumber = (int) (Math.random() * ((max-min)+1)) + min;
            int index = randomNumber-1;
            
            CatsFav randomCatFav = catsArray[index];
            
            //redimensionar en caso de necesitar
            Image image = null;
            try {
                URL url = new URL(randomCatFav.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon backgroundCat = new ImageIcon(image);

                if (backgroundCat.getIconWidth() > 800) {
                    Image background = backgroundCat.getImage();
                    Image modified = background.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    backgroundCat = new ImageIcon(modified);
                }

                String menu = "Opciones: \n"
                        + "1. ver otra imagen \n"
                        + "2. Eliminar Favorito \n"
                        + "3. Volver \n";

                String[] buttons = {"Ver otra imagen", "Eliminar Favorito", "Volver"};
                String idCat = randomCatFav.getId();
                String option = (String) JOptionPane.showInputDialog(null, menu, idCat, JOptionPane.INFORMATION_MESSAGE, backgroundCat, buttons, buttons[0]);

                int selection = -1;
                //validamos que opción selecciona el usuario
                for (int i = 0; i < buttons.length; i++) {
                    if (option.equals(buttons[i])) {
                        selection = i;
                    }

                    switch (selection) {
                        case 0:
                            seeFavorite(apikey);
                            break;
                        case 1:
                            deleteFavorite(randomCatFav);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            }

            
        }
        
    }
    public static void deleteFavorite(CatsFav catsfav){
        try{
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
              .url("https://api.thecatapi.com/v1/favourites/"+catsfav.getId()+"")
              .delete(null)
              .addHeader("Content-Type", "application/json")
              .addHeader("x-api-key", catsfav.getApikey())
              .build();

            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);
        }

    }
}

package ejercicio2a2;

import java.io.*;
import java.util.ArrayList;

public class Contador {

    public static void main(String[] args) {
        if (args.length < 3) return;

        String rutaFichero = args[0];
        char letraBuscar = args[1].charAt(0);
        String rutaSalida = args[2];
        long contador = 0;

        try {
            // Leemos el fichero línea a línea
            ArrayList<String> lineas = Utiles.getLineasFichero(rutaFichero);
            
            for (String linea : lineas) {
                for (int i = 0; i < linea.length(); i++) {
                    if (linea.charAt(i) == letraBuscar) {
                        contador++;
                    }
                }
            }

            // Guardamos el subtotal
            PrintWriter pw = Utiles.getPrintWriter(rutaSalida);
            pw.println(contador);
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
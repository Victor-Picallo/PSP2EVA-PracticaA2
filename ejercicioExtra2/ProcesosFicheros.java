package ejercicioExtra2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProcesosFicheros {

    public static void main(String[] args) {

        String nombreFicheroEntrada = args[0];
        String nombreFicheroSalida = args[1];
        int numeroMinimo = Integer.parseInt(args[2]);
        int sumaParcial = 0;

        try {
            ArrayList<String> lineas = Utiles.getLineasFichero(nombreFicheroEntrada);
            
            for (String linea : lineas) {
                try {
                    int numeroLeido = Integer.parseInt(linea.trim());
                    // Aplicamos el filtro solicitado
                    if (numeroLeido > numeroMinimo) {
                        sumaParcial += numeroLeido;
                    }
                } catch (NumberFormatException e) {
                    // Ignoramos líneas que no sean números
                }
            }

            // Guardamos el resultado parcial en su fichero .res
            PrintWriter pw = Utiles.getPrintWriter(nombreFicheroSalida);
            pw.println(sumaParcial);
            pw.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
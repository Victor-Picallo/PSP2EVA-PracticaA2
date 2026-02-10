package ejercicioExtra;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProcesosFicheros {

    public static void main(String[] args) {

        String nombreFicheroEntrada = args[0];
        String nombreFicheroSalida = args[1];

        int numeroMinimo = Integer.parseInt(args[2]);
        int numeroMaximo = Integer.parseInt(args[3]); // nuevo limite superior

        int sumaParcial = 0;

        try {
            ArrayList<String> lineas = Utiles.getLineasFichero(nombreFicheroEntrada);

            for (String linea : lineas) {
                try {
                    int numeroLeido = Integer.parseInt(linea.trim());
                    // sumar solo si está entre minimo y máximo (incluidos)
                    if (numeroLeido >= numeroMinimo && numeroLeido <= numeroMaximo) {
                        sumaParcial += numeroLeido;
                    }
                } catch (NumberFormatException e) {
                    // Ignoramos líneas que no sean números
                }
            }

            PrintWriter pw = Utiles.getPrintWriter(nombreFicheroSalida);
            pw.println(sumaParcial);
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

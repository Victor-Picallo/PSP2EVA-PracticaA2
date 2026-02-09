import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProcesosFicheros {

    public static void main(String[] args) {
        if (args.length < 3) return;

        String nombreFicheroEntrada = args[0];
        String nombreFicheroSalida = args[1];
        long numeroMinimo = Long.parseLong(args[2]);
        long sumaParcial = 0;

        try {
            ArrayList<String> lineas = Utiles.getLineasFichero(nombreFicheroEntrada);
            
            for (String linea : lineas) {
                try {
                    long valor = Long.parseLong(linea.trim());
                    // Aplicamos el filtro solicitado
                    if (valor > numeroMinimo) {
                        sumaParcial += valor;
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
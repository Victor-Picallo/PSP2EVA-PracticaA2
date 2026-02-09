package ejercicio2a2;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Executor {

    /*
     * El Executor lanza tres procesos hijo con un ExecutorService
     * Cada proceso hijo recibe los argumentos: datos.txt, la letra aleatoria y el .txt para el resultado
     * Cada hijo guarda el conteo en un archivo individual
     * Luego el executor coge los archivos individuales y los suma y guarda el total en "resultado_final.txt".
     */

    public static final String FICHERO_DATOS = "datos.txt"; // Fichero a analizar
    public static final String RESULTADO_GLOBAL = "resultado_final.txt";

    public static void main(String[] args) throws IOException, InterruptedException {
        String classpath = System.getProperty("java.class.path");
        String clase = "ejercicio2a2.Contador";
        String[] ficherosRes = {"res0.txt", "res1.txt", "res2.txt"};
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Random ran = new Random();

        for (int i = 0; i < 3; i++) {
            // Generamos la letra mayúscula aleatoria (Código ASCII entre 65 y 90)
            char letraAleatoria = (char) (ran.nextInt(26) + 'A');
            String fRes = ficherosRes[i];

            // Preparamos el proceso con los 3 argumentos
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath, clase, 
                    FICHERO_DATOS, String.valueOf(letraAleatoria), fRes
            );

            executor.execute(() -> {
                try {
                    System.out.println("Lanzando hijo para contar la letra: " + letraAleatoria);
                    Process p = pb.start();
                    p.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
            // Recuperamos y sumamos los subtotales usando la clase Utiles
            long totalGlobal = Utiles.getSuma(ficherosRes);

            // Guardamos el resultado final
            PrintWriter pw = Utiles.getPrintWriter(RESULTADO_GLOBAL);
            pw.println("Suma total de letras encontradas: " + totalGlobal);
            pw.close();

            System.out.println("\nProceso finalizado. Total acumulado: " + totalGlobal);
            System.out.println("Resultado guardado en: " + RESULTADO_GLOBAL);
        }
    }
}
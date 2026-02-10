package ejercicioExtra2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Executor {

    /*
     * Pedimos al usuario un numero a partir del cual sumar, solo se suman numeros superiores
     * Creamos con ExecutorService el pool de hilos, para lso procesos de la clase hijo
     * La clase hijo recibe los argumentos: nombreFicheroEntrada, nombreFicheroSalida, numeroMinimo
     * El Executor lanza un proceso por cada .txt con los datos y luego cuando terminan los procesos
     * Y la clase hija crea un .txt.res con la suma de cada archivo, y un .txt.err con los errores de cada proceso
     * Luego coge todos los .txt.res y los suma en un resultado_global.txt
     */

    public static final String SUFIJO_RESULTADO = ".res";
    public static final String SUFIJO_ERRORES = ".err";
    public static final String RESULTADOS_GLOBALES = "resultado_global.txt";

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce el numero minimo para la suma: ");
        String numeroMinimo = sc.nextLine();
        sc.close();

        String classpath = System.getProperty("java.class.path");
        String[] ficheros = { "informatica.txt", "gerencia.txt", "contabilidad.txt", "comercio.txt", "rrhh.txt" };
        String clase = "ejercicioExtra2.ProcesosFicheros";
        
        // Lista para almacenar las promesas (Future) de cada tarea
        List<Future<Long>> futures = new ArrayList<>();

        // 2. Definir el Executor
        ExecutorService executor = Executors.newFixedThreadPool(ficheros.length);

        // 3. Lanzar procesos
        for (int i = 0; i < ficheros.length; i++) {
            String fichEntrada = ficheros[i];
            String fichResultado = fichEntrada + SUFIJO_RESULTADO;
            String fichErrores = fichEntrada + SUFIJO_ERRORES;

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath, clase, fichEntrada, fichResultado, numeroMinimo
            );
            pb.redirectError(new File(fichErrores));

            // Usamos submit con un Callable para que el hilo nos devuelva el resultado parcial
            Future<Long> future = executor.submit(() -> {
                try {
                    Process p = pb.start();
                    p.waitFor(); 
                    
                    // Una vez que el proceso hijo termina y crea el .res, lo leemos
                    return Utiles.getSuma(new String[]{fichResultado});
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0L;
                }
            });
            
            futures.add(future);
        }

        // Cerramos el executor
        executor.shutdown();
        
        // 4. Suma final usando los Futures
        long totalGlobal = 0;
        for (Future<Long> f : futures) {
            try {
                // f.get() bloquea el main hasta que la tarea específica termina
                totalGlobal += f.get();
            } catch (Exception e) {
                System.err.println("Error al recuperar el resultado de un proceso.");
            }
        }

        // 5. Guardar resultado global
        PrintWriter pw = Utiles.getPrintWriter(RESULTADOS_GLOBALES);
        pw.println(totalGlobal);
        pw.close();
        
        System.out.println("Proceso finalizado. Total global (vía Future): " + totalGlobal);
    }
}
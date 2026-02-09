import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
        String[] ficheros = {"informatica.txt", "gerencia.txt", "contabilidad.txt", "comercio.txt", "rrhh.txt"};
        String clase = "ProcesosFicheros";
        String[] ficherosResultado = new String[ficheros.length];

        // 2. Definir el Executor
        ExecutorService executor = Executors.newFixedThreadPool(ficheros.length);

        // 3. Lanzar procesos
        for (int i = 0; i < ficheros.length; i++) {
            String fichEntrada = ficheros[i];
            String fichResultado = fichEntrada + SUFIJO_RESULTADO;
            String fichErrores = fichEntrada + SUFIJO_ERRORES;
            ficherosResultado[i] = fichResultado;

            // Creamos el ProcessBuilder aquí mismo
            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath, clase, fichEntrada, fichResultado, numeroMinimo
            );
            pb.redirectError(new File(fichErrores));

            // Mandamos al executor la tarea de iniciar y esperar al proceso
            executor.execute(() -> {
                try {
                    Process p = pb.start();
                    p.waitFor(); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 4. Sustituir el Thread.sleep(5000) por el cierre del executor
        executor.shutdown();
        
        // Espera a que todos terminen (bloquea el main hasta que acaben o pase el tiempo)
        if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
            // 5. Suma final
            long total = Utiles.getSuma(ficherosResultado);
            PrintWriter pw = Utiles.getPrintWriter(RESULTADOS_GLOBALES);
            pw.println(total);
            pw.close();
            
            System.out.println("Proceso finalizado. Total global: " + total);
        }
    }
}
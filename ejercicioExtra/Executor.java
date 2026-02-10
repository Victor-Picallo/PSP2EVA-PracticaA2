package ejercicioExtra;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Executor {

    /*
     * Pedimos al usuario dos números (límite inferior y superior) y solo se suman
     * los números que están entre esos dos valores (incluidos).
     * Creamos con ExecutorService el pool de hilos para lanzar procesos de la clase
     * hija.
     * La clase hija recibe los argumentos: nombreFicheroEntrada,
     * nombreFicheroSalida,
     * numeroMinimo y numeroMaximo.
     * El Executor lanza un proceso por cada .txt con los datos y, cuando terminan
     * los procesos, la clase hija crea un .txt.res con la suma de cada archivo
     * y un .txt.err con los errores de cada proceso.
     * Luego recoge todos los .txt.res y los suma en un resultado_global.txt.
     */

    public static final String SUFIJO_RESULTADO = ".res";
    public static final String SUFIJO_ERRORES = ".err";
    public static final String RESULTADOS_GLOBALES = "resultado_global.txt";

    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce el numero minimo para la suma: ");
        int numeroMinimo = sc.nextInt(); // ahora como int
        System.out.print("Introduce el numero maximo para la suma: ");
        int numeroMaximo = sc.nextInt(); // segundo número
        sc.close();

        String classpath = System.getProperty("java.class.path");
        String[] ficheros = { "informatica.txt", "gerencia.txt", "contabilidad.txt", "comercio.txt", "rrhh.txt" };
        String clase = "ejercicioExtra.ProcesosFicheros";
        String[] ficherosResultado = new String[ficheros.length];

        ExecutorService executor = Executors.newFixedThreadPool(ficheros.length);

        for (int i = 0; i < ficheros.length; i++) {
            String fichEntrada = ficheros[i];
            String fichResultado = fichEntrada + SUFIJO_RESULTADO;
            String fichErrores = fichEntrada + SUFIJO_ERRORES;
            ficherosResultado[i] = fichResultado;

            ProcessBuilder pb = new ProcessBuilder(
                    "java", "-cp", classpath, clase,
                    fichEntrada,
                    fichResultado,
                    String.valueOf(numeroMinimo), // arg2
                    String.valueOf(numeroMaximo) // arg3
            );
            pb.redirectError(new File(fichErrores));

            executor.execute(() -> {
                try {
                    Process p = pb.start();
                    p.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();

        if (executor.awaitTermination(1, TimeUnit.MINUTES)) {
            long total = Utiles.getSuma(ficherosResultado);
            PrintWriter pw = Utiles.getPrintWriter(RESULTADOS_GLOBALES);
            pw.println(total);
            pw.close();

            System.out.println("Proceso finalizado. Total global: " + total);
        }
    }
}

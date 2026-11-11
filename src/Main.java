import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Copa copa = new Copa();
        cargarDatosIniciales(copa);
        System.out.println("Bienvenido al programa de ArenaGT");
        System.out.println("Equipos iniciales: EQ1 Faro, EQ2 Luna, EQ3 Sol, EQ4 Atlas");
        System.out.println("Eventos iniciales: E1 y E2 finalizados, E3 programado, E4 sin programar, X1 exhibicion");
        try (Scanner entrada = new Scanner(System.in)) {
            ejecutarMenu(copa, entrada);
        }
    }

    private static void cargarDatosIniciales(Copa copa) {
        copa.registrarEquipo(new Equipo("EQ1", "Faro", "Empresa Norte"));
        copa.registrarEquipo(new Equipo("EQ2", "Luna", "Empresa Norte"));
        copa.registrarEquipo(new Equipo("EQ3", "Sol", "Empresa Sur"));
        copa.registrarEquipo(new Equipo("EQ4", "Atlas", "Empresa Sur"));

        copa.registrarEncuentro("E1", new EncuentroFutbolSala(copa.getEquipo("EQ1"), copa.getEquipo("EQ2")));
        copa.registrarEncuentro("E2", new EncuentroBaloncesto(copa.getEquipo("EQ3"), copa.getEquipo("EQ4")));
        copa.registrarEncuentro("E3", new EncuentroFutbolSala(copa.getEquipo("EQ1"), copa.getEquipo("EQ3")));
        copa.registrarEncuentro("E4", new EncuentroBaloncesto(copa.getEquipo("EQ2"), copa.getEquipo("EQ4")));
        copa.registrarExhibicion("X1", new Exhibicion("Clinica de baloncesto"));

        copa.programarEvento("E1", new Programacion(LocalDate.of(2026, 11, 12), 9, "Cancha Norte"));
        copa.programarEvento("E2", new Programacion(LocalDate.of(2026, 11, 12), 10, "Gimnasio"));
        copa.programarEvento("E3", new Programacion(LocalDate.of(2026, 11, 13), 14, "Cancha Norte"));
        copa.programarEvento("X1", new Programacion(LocalDate.of(2026, 11, 13), 9, "Gimnasio"));
        copa.registrarResultado("E1", 2, 2);
        copa.registrarResultado("E2", 78, 74);
    }

    private static void ejecutarMenu(Copa copa, Scanner entrada) {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n1. Registrar equipo\n2. Crear encuentro\n3. Crear exhibicion");
            System.out.println("4. Programar o reprogramar evento de exhibición\n5. Registrar resultado");
            System.out.println("6. Mostrar tabla de posiciones\n7. Mostrar calendario\n0. Salir");
            try {
                int opcion = leerEntero(entrada, "Opcion: ");
                switch (opcion) {
                    case 1: {
                        System.out.print("Codigo: ");
                        String codigo = entrada.nextLine();
                        System.out.print("Nombre: ");
                        String nombre = entrada.nextLine();
                        System.out.print("Empresa: ");
                        String empresa = entrada.nextLine();
                        copa.registrarEquipo(new Equipo(codigo, nombre, empresa));
                        System.out.println("Equipo registrado");
                        break;
                    }
                    case 2: {
                        System.out.print("Identificador del encuentro: ");
                        String id = entrada.nextLine();
                        int deporte = leerEntero(entrada, "Deporte (1. Futbol sala, 2. Baloncesto): ");
                        if (deporte != 1 && deporte != 2) {
                            throw new IllegalArgumentException("Seleccione 1 o 2 para el deporte");
                        }
                        System.out.print("Codigo del primer equipo: ");
                        Equipo equipo1 = copa.getEquipo(entrada.nextLine());
                        System.out.print("Codigo del segundo equipo: ");
                        Equipo equipo2 = copa.getEquipo(entrada.nextLine());
                        Encuentro encuentro;
                        if (deporte == 1) encuentro = new EncuentroFutbolSala(equipo1, equipo2);
                        else encuentro = new EncuentroBaloncesto(equipo1, equipo2);
                        copa.registrarEncuentro(id, encuentro);
                        System.out.println("Encuentro creado sin programar: " + id.trim());
                        break;
                    }
                    case 3: {
                        System.out.print("Identificador de la exhibicion: ");
                        String id = entrada.nextLine();
                        System.out.print("Titulo: ");
                        copa.registrarExhibicion(id, new Exhibicion(entrada.nextLine()));
                        System.out.println("Exhibicion creada sin programar: " + id.trim());
                        break;
                    }
                    case 4: {
                        System.out.print("Identificador del evento: ");
                        String id = entrada.nextLine();
                        LocalDate fecha = leerFecha(entrada, "Fecha (AAAA-MM-DD): ");
                        int hora = leerEntero(entrada, "Hora entera (0 a 23): ");
                        System.out.print("Sede: ");
                        String sede = entrada.nextLine();
                        copa.programarEvento(id, new Programacion(fecha, hora, sede));
                        System.out.println("Programacion guardada.");
                        break;
                    }
                    case 5: {
                        System.out.print("Identificador del encuentro: ");
                        String id = entrada.nextLine();
                        int marcador1 = leerEntero(entrada, "Marcador del primer equipo: ");
                        int marcador2 = leerEntero(entrada, "Marcador del segundo equipo: ");
                        copa.registrarResultado(id, marcador1, marcador2);
                        System.out.println("Resultado registrado y puntos actualizados");
                        break;
                    }
                    case 6:
                        System.out.println("\nTABLA DE POSICIONES (codigo | nombre | empresa | puntos)");
                        for (Equipo equipo : copa.getTablaPosiciones()) System.out.println(equipo);
                        break;
                    case 7:
                        System.out.println("\nCALENDARIO");
                        boolean hayEventos = false;
                        for (Programable evento : copa) {
                            System.out.println(evento);
                            hayEventos = true;
                        }
                        if (!hayEventos) System.out.println("No hay eventos programados");
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("Hasta luego");
                        break;
                    default:
                        System.out.println("Opcion invalida");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("\nFin de la entrada. Hasta luego");
                continuar = false;
            }
        }
    }

    private static int leerEntero(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido");
            }
        }
    }

    private static LocalDate leerFecha(Scanner entrada, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(entrada.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida. Use AAAA-MM-DD, por ejemplo 2026-01-12");
            }
        }
    }
}

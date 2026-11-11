import java.time.LocalDate;

public class Programacion {
    private LocalDate fecha;
    private int hora;
    private String sede;

    public Programacion(LocalDate fecha, int hora, String sede) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacia");
        }
        if (hora < 0 || hora > 23) {
            throw new IllegalArgumentException("La hora debe estar entre 0 y 23");
        }
        if (sede == null || sede.trim().isEmpty()) {
            throw new IllegalArgumentException("La sede no puede estar vacia");
        }
        this.fecha = fecha;
        this.hora = hora;
        this.sede = sede.trim();
    }

    public LocalDate getFecha() { return fecha; }
    public int getHora() { return hora; }
    public String getSede() { return sede; }

    @Override
    public String toString() {
        return fecha + " | " + String.format("%02d:00", hora) + " | " + sede;
    }
}

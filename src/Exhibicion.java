import java.util.Collections;
import java.util.List;

public class Exhibicion implements Programable {
    private String titulo;
    private Programacion programacion;

    public Exhibicion(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        this.titulo = titulo.trim();
        programacion = null;
    }

    public String getTitulo() { return titulo; }

    @Override
    public EstadoEvento getEstado() {
        if (programacion == null) return EstadoEvento.SIN_PROGRAMAR;
        return EstadoEvento.PROGRAMADO;
    }

    @Override
    public Programacion getProgramacion() { return programacion; }

    @Override
    public List<Equipo> getEquiposOcupados() { return Collections.emptyList(); }

    @Override
    public void programar(Programacion nueva) {
        if (nueva == null) {
            throw new IllegalArgumentException("Debe indicar una programacion");
        }
        programacion = nueva;
    }

    @Override
    public String toString() {
        String texto = "Exhibicion | " + titulo + " | " + getEstado();
        if (programacion != null) texto += " | " + programacion;
        return texto;
    }
}

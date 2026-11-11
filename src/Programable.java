import java.util.List;

public interface Programable extends Comparable<Programable> {
    Programacion getProgramacion();
    EstadoEvento getEstado();
    List<Equipo> getEquiposOcupados();
    void programar(Programacion nueva);

    // El criterio del calendario se escribe solamente aqui.
    @Override
    default int compareTo(Programable otro) {
        Programacion actual = getProgramacion();
        Programacion siguiente = otro.getProgramacion();
        if (actual == null && siguiente == null) return 0;
        if (actual == null) return 1;
        if (siguiente == null) return -1;

        int comparacion = actual.getFecha().compareTo(siguiente.getFecha());
        if (comparacion == 0) {
            comparacion = Integer.compare(actual.getHora(), siguiente.getHora());
        }
        if (comparacion == 0) {
            comparacion = actual.getSede().compareToIgnoreCase(siguiente.getSede());
        }
        return comparacion;
    }
}

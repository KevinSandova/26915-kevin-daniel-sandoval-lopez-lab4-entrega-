import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Copa implements Iterable<Programable> {
    private Map<String, Equipo> equipos;
    private Map<String, Programable> eventos;
    private Map<String, Puntuable> puntuables;

    public Copa() {
        equipos = new HashMap<>();
        eventos = new HashMap<>();
        puntuables = new HashMap<>();
    }

    public void registrarEquipo(Equipo equipo) {
        if (equipo == null || equipo.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo necesita un codigo");
        }
        if (equipos.containsKey(equipo.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un equipo con ese codigo");
        }
        equipos.put(equipo.getCodigo(), equipo);
    }

    public void registrarEncuentro(String id, Encuentro encuentro) {
        validarIdEvento(id);
        if (encuentro == null) {
            throw new IllegalArgumentException("Debe indicar un encuentro");
        }
        // Deben ser los mismos objetos que conserva la copa.
        for (Equipo equipo : encuentro.getEquiposOcupados()) {
            if (equipos.get(equipo.getCodigo()) != equipo) {
                throw new IllegalArgumentException("Los dos equipos deben estar registrados en esta copa");
            }
        }
        if (eventos.containsValue(encuentro)) {
            throw new IllegalArgumentException("Este encuentro ya esta registrado");
        }
        if (encuentro.getEstado() != EstadoEvento.SIN_PROGRAMAR) {
            throw new IllegalArgumentException("Registre el encuentro antes de programarlo");
        }
        eventos.put(id.trim(), encuentro);
        puntuables.put(id.trim(), encuentro);
    }

    public void registrarExhibicion(String id, Exhibicion exhibicion) {
        validarIdEvento(id);
        if (exhibicion == null) {
            throw new IllegalArgumentException("Debe indicar una exhibicion");
        }
        if (eventos.containsValue(exhibicion)) {
            throw new IllegalArgumentException("Esta exhibicion ya esta registrada");
        }
        if (exhibicion.getEstado() != EstadoEvento.SIN_PROGRAMAR) {
            throw new IllegalArgumentException("Registre la exhibicion antes de programarla");
        }
        eventos.put(id.trim(), exhibicion);
    }

    public Equipo getEquipo(String codigo) {
        Equipo equipo = codigo == null ? null : equipos.get(codigo.trim());
        if (equipo == null) throw new IllegalArgumentException("No existe un equipo con ese codigo");
        return equipo;
    }

    public Programable getEvento(String id) {
        Programable evento = id == null ? null : eventos.get(id.trim());
        if (evento == null) throw new IllegalArgumentException("No existe un evento con ese identificador");
        return evento;
    }

    public void programarEvento(String id, Programacion nueva) {
        Programable evento = getEvento(id);
        if (nueva == null) throw new IllegalArgumentException("Debe indicar una programacion");
        validarConflictos(evento, nueva);
        evento.programar(nueva);
    }

    private void validarConflictos(Programable evento, Programacion nueva) {
        for (Programable otro : eventos.values()) {
            if (otro == evento || otro.getProgramacion() == null) continue;
            Programacion existente = otro.getProgramacion();
            if (existente.getFecha().equals(nueva.getFecha())
                    && existente.getHora() == nueva.getHora()) {
                if (existente.getSede().equalsIgnoreCase(nueva.getSede())) {
                    throw new IllegalArgumentException("La sede ya esta ocupada en esa fecha y hora");
                }
                for (Equipo equipo : evento.getEquiposOcupados()) {
                    if (otro.getEquiposOcupados().contains(equipo)) {
                        throw new IllegalArgumentException("Un equipo ya tiene otro encuentro en esa fecha y hora");
                    }
                }
            }
        }
    }

    private void validarIdEvento(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del evento no puede estar vacio");
        }
        if (eventos.containsKey(id.trim())) {
            throw new IllegalArgumentException("Ya existe un evento con ese identificador");
        }
    }

    public void registrarResultado(String id, int marcador1, int marcador2) {
        getEvento(id);
        Puntuable encuentro = puntuables.get(id.trim());
        if (encuentro == null) {
            throw new IllegalArgumentException("Las exhibiciones no admiten resultados");
        }
        encuentro.registrarResultado(marcador1, marcador2);
    }

    public List<Equipo> getTablaPosiciones() {
        List<Equipo> tabla = new ArrayList<>(equipos.values());
        Collections.sort(tabla);
        return Collections.unmodifiableList(tabla);
    }

    @Override
    public Iterator<Programable> iterator() {
        List<Programable> calendario = new ArrayList<>();
        for (Programable evento : eventos.values()) {
            if (evento.getProgramacion() != null) calendario.add(evento);
        }
        Collections.sort(calendario);
        return Collections.unmodifiableList(calendario).iterator();
    }
}

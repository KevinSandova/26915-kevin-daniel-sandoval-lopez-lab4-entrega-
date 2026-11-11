import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Encuentro implements Programable, Puntuable {
    private Equipo equipo1;
    private Equipo equipo2;
    private Integer marcador1;
    private Integer marcador2;
    private EstadoEvento estado;
    private Programacion programacion;

    protected Encuentro(Equipo equipo1, Equipo equipo2) {
        if (equipo1 == null || equipo2 == null) {
            throw new IllegalArgumentException("Se necesitan dos equipos registrados");
        }
        if (equipo1.getCodigo().equals(equipo2.getCodigo())) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra si mismo");
        }
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        marcador1 = null;
        marcador2 = null;
        programacion = null;
        estado = EstadoEvento.SIN_PROGRAMAR;
    }

    public Equipo getEquipo1() { return equipo1; }
    public Equipo getEquipo2() { return equipo2; }
    public Integer getMarcador1() { return marcador1; }
    public Integer getMarcador2() { return marcador2; }

    @Override
    public EstadoEvento getEstado() { return estado; }

    @Override
    public Programacion getProgramacion() { return programacion; }

    @Override
    public List<Equipo> getEquiposOcupados() {
        return Collections.unmodifiableList(Arrays.asList(equipo1, equipo2));
    }

    @Override
    public void programar(Programacion nueva) {
        if (estado == EstadoEvento.FINALIZADO) {
            throw new IllegalStateException("No se puede reprogramar un encuentro finalizado");
        }
        if (nueva == null) {
            throw new IllegalArgumentException("Debe indicar una programacion");
        }
        programacion = nueva;
        estado = EstadoEvento.PROGRAMADO;
    }

    @Override
    public final void registrarResultado(int marcador1, int marcador2) {
        if (estado != EstadoEvento.PROGRAMADO) {
            throw new IllegalStateException("El encuentro debe estar programado y sin resultado previo");
        }
        if (marcador1 < 0 || marcador2 < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos");
        }
        validarMarcadorDeporte(marcador1, marcador2);
        int puntos1 = calcularPuntos(marcador1, marcador2);
        int puntos2 = calcularPuntos(marcador2, marcador1);

        equipo1.sumarPuntos(puntos1);
        equipo2.sumarPuntos(puntos2);
        this.marcador1 = marcador1;
        this.marcador2 = marcador2;
        estado = EstadoEvento.FINALIZADO;
    }

    protected abstract void validarMarcadorDeporte(int marcador1, int marcador2);
    protected abstract int calcularPuntos(int marcadorPropio, int marcadorRival);

    @Override
    public String toString() {
        String texto = equipo1.getNombre() + " vs " + equipo2.getNombre() + " | " + estado;
        if (programacion != null) texto += " | " + programacion;
        if (marcador1 != null) texto += " | Marcador: " + marcador1 + " - " + marcador2;
        return texto;
    }
}

public class EncuentroFutbolSala extends Encuentro {
    public EncuentroFutbolSala(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarMarcadorDeporte(int marcador1, int marcador2) {
    }

    @Override
    protected int calcularPuntos(int marcadorPropio, int marcadorRival) {
        if (marcadorPropio > marcadorRival) return 3;
        if (marcadorPropio == marcadorRival) return 1;
        return 0;
    }

    @Override
    public String toString() { return "Futbol sala | " + super.toString(); }
}

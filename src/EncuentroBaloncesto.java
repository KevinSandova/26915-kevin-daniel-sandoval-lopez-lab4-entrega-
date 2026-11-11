public class EncuentroBaloncesto extends Encuentro {
    public EncuentroBaloncesto(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarMarcadorDeporte(int marcador1, int marcador2) {
        if (marcador1 == marcador2) {
            throw new IllegalArgumentException("En baloncesto no se permiten empates");
        }
    }

    @Override
    protected int calcularPuntos(int marcadorPropio, int marcadorRival) {
        if (marcadorPropio > marcadorRival) return 2;
        return 1;
    }

    @Override
    public String toString() { return "Baloncesto | " + super.toString(); }
}

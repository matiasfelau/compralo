package ar.edu.uade.compralo.compralo.utils;

public class PesoUtils {

    public static Double calcularPeso(Double peso) {
        return 1 / (1 / peso + 1);
    }
}

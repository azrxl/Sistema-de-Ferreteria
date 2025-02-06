package cr.ac.una.backend.logic.strategy;

public class CondicionC implements DescuentoStrategy {
    @Override
    public double generateDescuento(double valor) {
        return ((valor/100)*7.5);
    }
}

package cr.ac.una.backend.logic.strategy;

public class CondicionA implements DescuentoStrategy {
    @Override
    public double generateDescuento(double valor) {
        return (valor/100)*10;
    }
}

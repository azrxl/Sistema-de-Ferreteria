package cr.ac.una.backend.logic.strategy;

public class CondicionB implements DescuentoStrategy {
    @Override
    public double generateDescuento(double valor) {
        return (valor/100)*5;
    }
}
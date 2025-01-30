package cr.ac.una.logic.strategyDescuento;

public class CondicionC implements DescuentoStrategy{
    @Override
    public double generateDescuento(double valor) {
        return ((valor/100)*7.5);
    }
}

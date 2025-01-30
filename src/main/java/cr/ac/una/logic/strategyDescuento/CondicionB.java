package cr.ac.una.logic.strategyDescuento;

public class CondicionB implements DescuentoStrategy{
    @Override
    public double generateDescuento(double valor) {
        return (valor/100)*5;
    }
}
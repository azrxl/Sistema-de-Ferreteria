package cr.ac.una.logic.strategyDescuento;

public class CondicionA implements DescuentoStrategy{
    @Override
    public double generateDescuento(double valor) {
        return (valor/100)*10;
    }
}

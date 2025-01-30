package cr.ac.una.logic.strategyDescuento;

public class ContextDescuento {

    private DescuentoStrategy descuentoStrategy;

    public ContextDescuento() {
    }

    public void setDescuentoStrategy(DescuentoStrategy descuentoStrategy) {
        this.descuentoStrategy = descuentoStrategy;
    }

    public double executeStrategy(double monto) {
        return  descuentoStrategy.generateDescuento(monto);
    }
}

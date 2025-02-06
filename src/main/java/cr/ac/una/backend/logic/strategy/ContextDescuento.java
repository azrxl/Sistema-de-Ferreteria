package cr.ac.una.backend.logic.strategy;

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

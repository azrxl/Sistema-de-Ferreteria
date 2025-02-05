package cr.ac.una.logic.objects;

public class Factura {
    private final double subtotal;
    private final double descuentoUnidades;
    private final double descuentoDiferentes;
    private final double descuentoMonto;
    private final double totalDescuento;
    private final double totalFinal;

    public Factura(double subtotal, double descuentoUnidades, double descuentoDiferentes,
                   double descuentoMonto, double totalDescuento, double totalFinal) {
        this.subtotal = subtotal;
        this.descuentoUnidades = descuentoUnidades;
        this.descuentoDiferentes = descuentoDiferentes;
        this.descuentoMonto = descuentoMonto;
        this.totalDescuento = totalDescuento;
        this.totalFinal = totalFinal;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuentoUnidades() {
        return descuentoUnidades;
    }

    public double getDescuentoDiferentes() {
        return descuentoDiferentes;
    }

    public double getDescuentoMonto() {
        return descuentoMonto;
    }

    public double getTotalDescuento() {
        return totalDescuento;
    }

    public double getTotalFinal() {
        return totalFinal;
    }
}

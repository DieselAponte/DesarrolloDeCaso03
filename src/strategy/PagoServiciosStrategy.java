package strategy;

import model.Transferencia;

public class PagoServiciosStrategy implements Strategy{

    @Override
    public void ejecutar(Transferencia transferencia) {
        if (!validarReferencia(transferencia.getReferencia())) {
            System.out.println("Referencia inválida");
            return;
        }

        if (esPromocionValida()) {
            double descuento = transferencia.getMonto() * 0.05;
            transferencia.setMonto(transferencia.getMonto() - descuento);
        }

        transferencia.getCuentaOrigen().debitar(transferencia.getMonto());
        marcarFacturaComoPagada(transferencia.getReferencia());
    }

    private boolean validarReferencia(String referencia) {
        return referencia != null && !referencia.trim().isEmpty();
    }

    private boolean esPromocionValida() {
        return true; // lógica real futura
    }

    private void marcarFacturaComoPagada(String referencia) {
        // registrar que la factura fue pagada
    }
}

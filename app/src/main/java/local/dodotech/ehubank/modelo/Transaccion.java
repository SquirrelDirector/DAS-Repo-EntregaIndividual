package local.dodotech.ehubank.modelo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Christian on 18/03/2022.
 */

public class Transaccion {
    private CuentaBancaria origen;
    private CuentaBancaria destino;
    private BigDecimal cantidad;
    private Date fecha;
    private String concepto;

    /**
     * Constructor de transacciones
     * @param origen Cuenta bancaria de origen
     * @param destino Cuenta bancaria de destino
     * @param cantidad Cantidad de la transferencia en euros
     * @param fecha Fecha de transacción
     * @param concepto Concepto de la transacción
     */
    public Transaccion(CuentaBancaria origen, CuentaBancaria destino, BigDecimal cantidad, Date fecha, String concepto) {
        this.origen = origen;
        this.destino = destino;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.concepto=concepto;
    }

    /**
     * Obtiene la cuenta de bancaria de origen
     * @return Cuenta bancaria de origen
     */
    public CuentaBancaria getOrigen() {
        return origen;
    }

    /**
     * Obtiene la cuenta bancaria de destino
     * @return Cuenta bancaria de destino
     */
    public CuentaBancaria getDestino() {
        return destino;
    }

    /**
     * Obtiene la cantidad de la transacción
     * @return Cantidad, en euros
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * Obtiene la fecha de transacción
     * @return Fecha
     */
    public Date getFecha() {
        return fecha;
    }

    public String getConcepto() {
        return concepto;
    }
}

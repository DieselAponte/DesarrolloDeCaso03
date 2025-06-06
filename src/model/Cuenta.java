package model;

public class Cuenta {
    private int id;
    private String numero;
    private String banco;
    private double saldo;

    public Cuenta(int id,String numero, String banco, double saldo) {
        this.id = id;
        this.numero = numero;
        this.banco = banco;
        this.saldo = saldo;
    }

    public void acreditar(double monto){
        this.saldo -= monto;
    }

    public void debitar(double monto){
        this.saldo += monto;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
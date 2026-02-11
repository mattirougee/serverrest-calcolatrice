package serverrest;

public class OperazioneResponseV2 {

    private double operando1;
    private double operando2;
    private String operatore;
    private double risultato;
    private String operazione;

    private long timestamp;
    private String requestId;

    public OperazioneResponseV2() {}

    public OperazioneResponseV2(double operando1, double operando2,
                                String operatore, double risultato,
                                String requestId) {

        this.operando1 = operando1;
        this.operando2 = operando2;
        this.operatore = operatore;
        this.risultato = risultato;
        this.operazione = String.format("%.2f %s %.2f = %.2f",
                operando1, operatore, operando2, risultato);

        this.timestamp = System.currentTimeMillis();
        this.requestId = requestId;
    }

    public double getOperando1() { return operando1; }
    public double getOperando2() { return operando2; }
    public String getOperatore() { return operatore; }
    public double getRisultato() { return risultato; }
    public String getOperazione() { return operazione; }
    public long getTimestamp() { return timestamp; }
    public String getRequestId() { return requestId; }
}

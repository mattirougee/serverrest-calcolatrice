package serverrest.V1;

/**
 * Service V1 della Calcolatrice REST
 * Gestisce solo le operazioni base:
 * SOMMA, SOTTRAZIONE, MOLTIPLICAZIONE, DIVISIONE
 */
public class CalcolatriceServiceV1 {

    /**
     * Esegue l'operazione matematica richiesta
     *
     * @param operando1 Il primo operando
     * @param operando2 Il secondo operando
     * @param operatore L'operatore (SOMMA, SOTTRAZIONE, MOLTIPLICAZIONE, DIVISIONE)
     * @return Il risultato dell'operazione
     * @throws IllegalArgumentException se l'operatore non è valido o divisione per zero
     */
    public static double calcola(double operando1, double operando2, String operatore)
            throws IllegalArgumentException {

        if (operatore == null || operatore.trim().isEmpty()) {
            throw new IllegalArgumentException("Operatore non può essere vuoto");
        }

        // Normalizziamo l'operatore (case-insensitive)
        String op = operatore.toUpperCase().trim();

        switch (op) {
            case "SOMMA":
            case "+":
                return operando1 + operando2;

            case "SOTTRAZIONE":
            case "-":
                return operando1 - operando2;

            case "MOLTIPLICAZIONE":
            case "*":
            case "X":
                return operando1 * operando2;

            case "DIVISIONE":
            case "/":
                if (operando2 == 0) {
                    throw new IllegalArgumentException("Divisione per zero non consentita");
                }
                return operando1 / operando2;

                case "POTENZA":
            case "POW":
            case "^":
                return Math.pow(operando1, operando2);

            case "MODULO":
            case "MOD":
            case "%":
                return operando1 % operando2;

            case "RADICE":
            case "SQRT":
                if (operando2 == 0) throw new IllegalArgumentException("Indice radice non valido");
                return Math.pow(operando1, 1.0 / operando2);
                
            default:
                throw new IllegalArgumentException(
                        "Operatore non valido: " + operatore +
                        ". Operatori consentiti: SOMMA, SOTTRAZIONE, MOLTIPLICAZIONE, DIVISIONE"
                );
        }
    }
}

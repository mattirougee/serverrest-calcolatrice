package serverrest.V2;

public class CalcolatriceServiceV2 {

    public static double calcola(double operando1, double operando2, String operatore) {

        if (operatore == null || operatore.isBlank()) {
            throw new IllegalArgumentException("Operatore non può essere vuoto");
        }

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
                if (operando1 < 0) {
                    throw new IllegalArgumentException("Radice di numero negativo non consentita");
                }
                return Math.sqrt(operando1);

            default:
                throw new IllegalArgumentException(
                        "Operatore non valido: " + operatore +
                        ". Operatori consentiti: + - * / ^ % sqrt"
                );
        }
    }
}

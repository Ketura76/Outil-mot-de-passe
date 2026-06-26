package pigier.securite;

/**
 * Classe responsable de l'évaluation de la force d'un mot de passe
 * Attribue un score basé sur plusieurs critères de sécurité
 */
public class Evaluateur {

    /**
     * Évalue la force d'un mot de passe
     * @param motDePasse le mot de passe à analyser
     * @return le niveau de force : Très faible, Faible, Moyen, Fort, Très fort
     */
    public static String evaluer(String motDePasse) {
        int score = 0;

        // Critère 1 : longueur du mot de passe
        if (motDePasse.length() >= 8)  score++;
        if (motDePasse.length() >= 12) score++;
        if (motDePasse.length() >= 16) score++;

        // Critère 2 : contient des majuscules
        if (motDePasse.matches(".*[A-Z].*")) score++;

        // Critère 3 : contient des minuscules
        if (motDePasse.matches(".*[a-z].*")) score++;

        // Critère 4 : contient des chiffres
        if (motDePasse.matches(".*[0-9].*")) score++;

        // Critère 5 : contient des symboles
        if (motDePasse.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?].*")) score++;

        // Convertir le score en niveau de force
        return switch (score) {
            case 0, 1 -> "Très faible [***  ]";
            case 2    -> "Faible      [****  ]";
            case 3, 4 -> "Moyen       [***** ]";
            case 5, 6 -> "Fort        [******]";
            default   -> "Très fort   [******]";
        };
    }
}

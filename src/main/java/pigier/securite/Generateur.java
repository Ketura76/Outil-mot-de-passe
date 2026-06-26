package pigier.securite;

import java.security.SecureRandom;

/**
 * Classe responsable de la génération des mots de passe
 * Utilise SecureRandom pour une génération cryptographiquement sûre
 */
public class Generateur {

    private final int longueur;
    private final String caracteresDisponibles;

    // Jeux de caractères disponibles
    private static final String MAJUSCULES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String MINUSCULES = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHIFFRES   = "0123456789";
    private static final String SYMBOLES   = "!@#$%^&*()-_=+[]{}|;:,.<>?";

    /**
     * Constructeur - configure les options de génération
     */
    public Generateur(int longueur, boolean majuscules, boolean minuscules,
                      boolean chiffres, boolean symboles) {
        this.longueur = longueur;

        // Construire la liste de caractères selon les choix de l'utilisateur
        StringBuilder sb = new StringBuilder();
        if (majuscules) sb.append(MAJUSCULES);
        if (minuscules) sb.append(MINUSCULES);
        if (chiffres)   sb.append(CHIFFRES);
        if (symboles)   sb.append(SYMBOLES);

        // Si aucun type choisi, utiliser minuscules par défaut
        if (sb.isEmpty()) sb.append(MINUSCULES);

        this.caracteresDisponibles = sb.toString();
    }

    /**
     * Génère un mot de passe aléatoire selon la configuration
     * @return le mot de passe généré
     */
    public String generer() {
        SecureRandom random = new SecureRandom();
        StringBuilder motDePasse = new StringBuilder();

        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(caracteresDisponibles.length());
            motDePasse.append(caracteresDisponibles.charAt(index));
        }

        return motDePasse.toString();
    }
}

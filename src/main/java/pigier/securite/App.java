package pigier.securite;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * Classe principale - Point d'entrée de l'outil de génération de mots de passe
 * Communique avec le conteneur Docker Zxcvbn pour valider la force des mots de passe
 */
public class App {

    // URL du conteneur Docker Zxcvbn
    private static final String DOCKER_URL = "http://localhost:3000/evaluer";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===========================================");
        System.out.println("   OUTIL DE GÉNÉRATION DE MOTS DE PASSE   ");
        System.out.println("===========================================");

        // Demander la longueur du mot de passe
        System.out.print("\nLongueur du mot de passe (min 6) : ");
        int longueur = lireEntier(scanner, 6);

        // Demander les types de caractères avec validation
        boolean majuscules = lireOuiNon(scanner, "Inclure des MAJUSCULES ? (o/n) : ");
        boolean minuscules = lireOuiNon(scanner, "Inclure des MINUSCULES ? (o/n) : ");
        boolean chiffres   = lireOuiNon(scanner, "Inclure des CHIFFRES ? (o/n) : ");
        boolean symboles   = lireOuiNon(scanner, "Inclure des SYMBOLES ? (o/n) : ");

        // Demander le mode rafale
        System.out.print("Combien de mots de passe à générer ? : ");
        int nombre = lireEntier(scanner, 1);

        System.out.println("\n--- MOTS DE PASSE GÉNÉRÉS ---");

        // Créer le client HTTP pour communiquer avec Docker
        HttpClient client = HttpClient.newHttpClient();
        Generateur generateur = new Generateur(longueur, majuscules, minuscules, chiffres, symboles);

        for (int i = 1; i <= nombre; i++) {
            String motDePasse = generateur.generer();

            // Évaluation locale
            String forceLocale = Evaluateur.evaluer(motDePasse);

            // Évaluation via Docker Zxcvbn
            String forceDocker = evaluerViaDocker(client, motDePasse);

            System.out.println(i + ". " + motDePasse);
            System.out.println("   → Force locale  : " + forceLocale);
            System.out.println("   → Force Docker  : " + forceDocker);
            System.out.println();
        }

        System.out.println("===========================================");
        scanner.close();
    }

    /**
     * Lit une réponse o/n et redemande si la réponse est invalide
     * @param scanner le scanner
     * @param message la question à afficher
     * @return true si o, false si n
     */
    private static boolean lireOuiNon(Scanner scanner, String message) {
        while (true) {
            System.out.print(message);
            String reponse = scanner.next().trim().toLowerCase();
            if (reponse.equals("o")) return true;
            if (reponse.equals("n")) return false;
            System.out.println("  Reponse invalide ! Tape o (oui) ou n (non).");
        }
    }

    /**
     * Lit un entier et redemande si la valeur est inférieure au minimum
     * @param scanner le scanner
     * @param min la valeur minimale acceptée
     * @return l'entier valide
     */
    private static int lireEntier(Scanner scanner, int min) {
        while (true) {
            try {
                int valeur = scanner.nextInt();
                if (valeur >= min) return valeur;
                System.out.print("  Valeur minimale : " + min + ". Réessaie : ");
            } catch (Exception e) {
                System.out.print("  Entier invalide ! Réessaie : ");
                scanner.next();
            }
        }
    }

    /**
     * Envoie le mot de passe au conteneur Docker et récupère le score Zxcvbn
     * @param client le client HTTP Java
     * @param motDePasse le mot de passe à évaluer
     * @return le niveau de force retourné par Zxcvbn
     */
    private static String evaluerViaDocker(HttpClient client, String motDePasse) {
        try {
            String json = "{\"motDePasse\":\"" + motDePasse + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DOCKER_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            return body.split("\"niveau\":\"")[1].split("\"")[0];

        } catch (Exception e) {
            return "Docker non accessible";
        }
    }
}
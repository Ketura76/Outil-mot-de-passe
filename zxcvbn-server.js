// Serveur HTTP simple qui évalue la force d'un mot de passe avec Zxcvbn
const http = require('http');
const zxcvbn = require('zxcvbn');

const server = http.createServer((req, res) => {
    if (req.method === 'POST' && req.url === '/evaluer') {
        let body = '';

        // Récupérer les données envoyées par Java
        req.on('data', chunk => { body += chunk.toString(); });

        req.on('end', () => {
            try {
                const { motDePasse } = JSON.parse(body);
                const resultat = zxcvbn(motDePasse);

                // Convertir le score (0-4) en niveau lisible
                const niveaux = [
                    "Très faible [***  ]",
                    "Faible      [****  ]",
                    "Moyen       [***** ]",
                    "Fort        [******]",
                    "Très fort   [******]"
                ];

                res.writeHead(200, { 'Content-Type': 'application/json' });
                res.end(JSON.stringify({
                    score: resultat.score,
                    niveau: niveaux[resultat.score],
                    temps: resultat.crack_times_display.offline_slow_hashing_1e4_per_second
                }));
            } catch (e) {
                res.writeHead(400);
                res.end(JSON.stringify({ erreur: 'Données invalides' }));
            }
        });
    } else {
        res.writeHead(404);
        res.end();
    }
});

// Démarrer le serveur sur le port 3000
server.listen(3000, () => {
    console.log('Serveur Zxcvbn démarré sur le port 3000');
});

# Image de base Node.js pour faire tourner Zxcvbn
FROM node:18-alpine

# Créer le dossier de travail
WORKDIR /app

# Installer zxcvbn
RUN npm install zxcvbn

# Copier le script de validation
COPY zxcvbn-server.js .

# Exposer le port 3000
EXPOSE 3000

# Lancer le serveur
CMD ["node", "zxcvbn-server.js"]

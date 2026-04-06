# 🍻 Playground : Apéro IA #1 - Le Contexte est Roi

Bienvenue dans le bac à sable de l'Apéro IA #1 ! 
Ce mini-projet Spring Boot est conçu pour démontrer comment GitHub Copilot réagit face au manque de contexte, et comment reprendre le contrôle sur le code généré.

## 🛠️ Prérequis
- VS Code avec l'extension **GitHub Copilot Chat** installée.
- Java 17+ et Maven.
- Avoir effacé l'historique de votre chat Copilot (cliquez sur le `+` pour partir d'une feuille blanche).

---

## 🧪 Expérience 1 : La Vision Tunnel (Le Plantage)
*Objectif : Prouver que l'IA est aveugle par défaut.*

1. Fermez tous vos onglets dans VS Code.
2. Ouvrez **uniquement** le fichier `src/main/java/com/apero/demo/entity/Utilisateur.java`.
3. Dans le chat Copilot, tapez exactement ce prompt :
   > "Génère une classe UtilisateurService pour créer un nouvel utilisateur en base de données et vérifier si l'email existe déjà."
4. **Observez le résultat :** Copilot va inventer un Repository générique (ou utiliser un vieil `EntityManager`) et faire des injections bizarres. Pourquoi ? Parce qu'il n'a pas lu le `pom.xml` ni exploré les autres dossiers.

---

## 🪄 Expérience 2 : La Magie du RAG Local (`@workspace`)
*Objectif : Utiliser la recherche vectorielle de l'IDE pour injecter le contexte.*

1. Restez sur `Utilisateur.java`. Ne trichez pas, n'ouvrez pas les autres fichiers !
2. Dans le chat, tapez :
   > "@workspace Génère le UtilisateurService. Assure-toi d'utiliser le repository existant pour la vérification de l'email."
3. **Observez le résultat :** Copilot scanne le projet. Il trouve silencieusement l'interface `UtilisateurRepository` (qui contient la méthode `existsByEmail`) et génère un code qui s'intègre parfaitement.

---

## 🏛️ Expérience 3 : La Gouvernance (Le Twist Anti-Lombok)
*Objectif : Forcer l'IA à coder selon NOS règles d'architecture.*

Vous remarquerez que Copilot a sûrement utilisé Lombok (`@Autowired`, `@RequiredArgsConstructor`) car il l'a vu dans le `pom.xml`. Et notre entité utilise `@Data`. En tant que Tech Leads, nous savons que `@Data` sur une entité JPA est dangereux (Lazy Loading cassé, HashCode instable). 

On va forcer l'IA à nettoyer tout ça toute seule :

1. Ouvrez le fichier `.github/copilot-instructions.md` présent dans ce repo et lisez nos règles d'architecture strictes.
2. Ouvrez un **nouveau chat** Copilot (pour vider sa mémoire à court terme).
3. Tapez ce prompt :
   > "@workspace Analyse notre `Utilisateur` et notre `UtilisateurService`. Refactorise ces deux classes pour qu'elles respectent scrupuleusement nos règles globales de projet."
4. **Observez le résultat :** L'IA supprime Lombok, génère des getters/setters propres, sécurise le `equals/hashCode` sur l'ID, et refait l'injection de dépendances manuellement. Le contrat d'architecture est respecté !

---

## 🧠 À retenir (ROM vs RAM)
- **Les Paramètres (ROM) :** La culture générale de l'IA. Elle connaît le Java, mais pas notre projet.
- **Les Tokens (RAM) :** La fenêtre de contexte. C'est ici que l'IDE injecte nos fichiers grâce à la commande `@workspace`.
- **Garbage In = Garbage Out :** La qualité de l'IA dépend uniquement du contexte que vous lui fournissez !
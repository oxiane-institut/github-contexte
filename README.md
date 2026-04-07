# 🍻 Playground : Apéro IA #1 - L'Autopsie du Contexte

Bienvenue dans le bac à sable de l'Apéro IA #1 d'Oxiane ! 
Ce mini-projet Spring Boot est conçu pour démontrer comment GitHub Copilot construit son contexte en sous-marin, pourquoi il risque de polluer votre architecture, et comment reprendre le contrôle.

## 🛠️ Prérequis
- VS Code avec l'extension **GitHub Copilot Chat** installée.
- Java 17+ et Maven.
- Avoir effacé l'historique de votre chat Copilot (cliquez sur le `+` pour partir d'une feuille blanche).

---

## 🧊 Échauffement : L'Autocomplétion (La Base)
*Objectif : Le B.A.-BA de Copilot. L'IA agit comme un dactylo ultra-rapide en prédisant la suite de votre fichier.*

1. Ouvrez le fichier `src/main/java/com/oxiane/atelier/contexte/entity/Utilisateur.java`.
2. À la fin de la classe (juste avant l'accolade fermante), tapez ce simple commentaire :
   `// Méthode pour mettre le nom de l'utilisateur en majuscule`
3. Allez à la ligne et **attendez une seconde**.
4. **Observez le résultat :** Un texte grisé (*Ghost Text*) apparaît, vous proposant la méthode complète (ex: `public void uppercaseNom() { this.nom = this.nom.toUpperCase(); }`).
5. Appuyez sur la touche `Tab` pour accepter.
*Conclusion :* Pour des tâches simples et locales, Copilot est un super-compléteur de texte. Mais que se passe-t-il quand on a besoin d'interagir avec d'autres fichiers ?

---

## 🔍 Expérience 1 : L'Illusion et l'Autopsie
*Objectif : Prouver que l'IDE n'est plus aveugle, mais qu'il faut surveiller sa "RAM".*

1. Fermez tous vos onglets dans VS Code.
2. Ouvrez **uniquement** le fichier `src/main/java/com/oxiane/atelier/contexte/entity/Utilisateur.java`.
3. Dans le chat Copilot, tapez exactement ce prompt :
   > "Génère un UtilisateurService pour sauvegarder un utilisateur. N'oublie pas de vérifier en base que l'email n'est pas déjà pris."
4. **Observez le code :** Copilot a généré le service. Mieux encore, il a deviné l'existence de `UtilisateurRepository` et a utilisé la méthode standard de vérification. Magie ? Non.
5. **L'Autopsie :** Dans le chat, juste au-dessus de la réponse de l'IA, cliquez sur le menu déroulant **"Used X references"** (Références utilisées). 
   * *Conclusion :* Vous voyez que l'IDE a silencieusement scanné l'index du projet et injecté le Repository dans le prompt (la Fenêtre de Contexte) à votre insu.
6. **Le Problème (Le Bruit) :** Regardez l'architecture du code généré. L'IA a utilisé `@Autowired` sur un champ ou mis du Lombok partout. Elle a trouvé vos fichiers, mais elle a appliqué les mauvaises pratiques d'Internet (sa ROM).

---

## 🏛️ Expérience 2 : La Gouvernance (La reprise de contrôle)
*Objectif : Forcer l'IA à coder selon NOS règles d'architecture (Anti-Lombok).*

En tant que Tech Leads, nous savons que `@Data` sur une entité JPA est dangereux (Lazy Loading cassé, HashCode instable), et nous préférons l'injection par constructeur. On va forcer l'IA à nettoyer son propre code.

1. Créez un dossier `.github` à la racine du projet, et ajoutez-y un fichier nommé `copilot-instructions.md`.
2. Collez ces règles strictes à l'intérieur :
   ```markdown
   Tu es un Tech Lead Java expert. Pour ce projet, tu dois STRICTEMENT respecter ces conventions d'architecture :
   1. INTERDICTION d'utiliser Lombok (pas de @Data, @Getter, @Setter, @RequiredArgsConstructor, etc.).
   2. Pour les Entités JPA : génère explicitement les getters, setters, et le constructeur vide. N'implémente `equals()` et `hashCode()` que sur la base de l'ID.
   3. Pour l'injection de dépendances : utilise toujours un constructeur généré explicitement (interdiction stricte d'utiliser @Autowired sur les champs).
   4. Parle-moi toujours en français.
   ```
3. Sauvegardez le fichier.
4. Ouvrez un **nouveau chat** Copilot (le bouton `+`) pour vider sa mémoire à court terme.
5. Tapez ce prompt :
   > "@workspace Analyse notre `Utilisateur` et le `UtilisateurService` que tu viens de générer. Refactorise ces classes pour qu'elles respectent scrupuleusement nos règles globales de projet."
6. **Observez le résultat :** L'IA obéit au contrat. Elle supprime Lombok, génère des getters/setters propres, sécurise le `equals/hashCode` sur l'ID, et refait l'injection de dépendances manuellement via un constructeur explicite. Le contrat d'architecture est respecté !

---

## 🎯 Expérience 3 : Le Sniper (Zéro Effet de Bord)

*Objectif : Modifier chirurgicalement une règle métier sans polluer le contexte ni risquer de casser le reste du fichier.*

Si on utilise `@workspace` pour une petite modification, l'IA risque de réécrire tout le fichier (effet de bord). Soyons des snipers.

1. Ouvrez `UtilisateurService.java`.

2. Surlignez uniquement le contenu de la méthode de sauvegarde.

3. Appuyez sur Ctrl+I (Windows/Linux) ou Cmd+I (Mac) pour ouvrir le Chat Inline.

4. Tapez cette instruction :

   > "Ajoute une règle métier : si l'email ne se termine pas par '@oxiane.com', lève une IllegalArgumentException."

5. Observez le résultat : L'IA affiche un Diff (rouge/vert) directement dans votre éditeur, modifiant exclusivement cette méthode, sans avoir eu besoin de relire tout le projet. Acceptez la modification.

---

## 🧪 Expérience 4 : La Boucle Agentique (Génération et Exécution des Tests)
*Objectif : Vérifier que l'IA respecte les standards de test et utiliser l'IDE comme un Agent autonome.*

Maintenant que notre `UtilisateurService` est propre, nous devons le tester. L'IA a souvent tendance à utiliser de vieux frameworks (comme JUnit 4) si on ne la cadre pas.

1. **La Gouvernance des Tests :** Ajoutez cette ligne dans votre `.github/copilot-instructions.md` :
   > "Pour les tests : utilise exclusivement JUnit 5 et Mockito. Ne mets jamais d'assertions dans le vide. Utilise le pattern Arrange-Act-Assert (AAA)."
2. Ouvrez le chat Copilot et tapez :
   > "@workspace Génère les tests unitaires exhaustifs pour `UtilisateurService`."
3. **Observez le code généré :** L'IA crée la classe `UtilisateurServiceTest`. Elle utilise bien `@ExtendWith(MockitoExtension.class)`, `@Mock`, et `@InjectMocks`. Le pattern AAA est respecté (commentaires ou structure). Acceptez le code et sauvegardez le fichier dans `src/test/java/...`
4. **Le Mic-Drop (La Boucle Agentique) :** On ne va pas lancer les tests nous-mêmes. Demandez à l'IA de le faire :
   > "@workspace Lance les tests unitaires via Maven dans le terminal. Si un test échoue, analyse l'erreur et corrige le code."
5. **Observez l'Agent en action :** L'IDE va ouvrir le terminal, exécuter `mvn test`, lire la sortie standard, et vous confirmer que tout est au vert (ou corriger son propre test s'il a oublié un Mock !). Vous n'êtes plus codeur, vous êtes superviseur.

---

## 🤯 Bonus : Le Teaser Agentique (TDD)
*Objectif : Transformer l'IA en Gardien du Temple méthodologique.*

Et si on allait plus loin que la syntaxe ? 
1. Éditez le fichier `.github/copilot-instructions.md` et ajoutez cette règle à la fin :
   > "Pratique stricte du TDD : Avant de coder la moindre fonctionnalité métier, génère TOUJOURS la classe de test JUnit 5 d'abord (Red). Refuse de coder le service tant que le test n'existe pas."
2. Ouvrez un **nouveau chat** (`+`).
3. Demandez-lui : *"Crée la logique pour désactiver le compte d'un utilisateur."*
4. **Le résultat :** L'IA refuse de vous donner le code métier et génère directement `UtilisateurTest.java` !

---

## 🧠 À retenir
- **L'Autocomplétion (`Tab`) :** Parfait pour la frappe rapide sur le fichier courant.
- **Le Contexte Implicite :** L'IDE fouille votre projet tout seul pour remplir sa RAM (les Tokens). Utilisez toujours *Used references* pour déboguer ce qu'il a lu.
- **Le Sniper (`Ctrl+I`) :** Utilisez le Chat Inline sur une sélection précise pour éviter le "Blast Radius" (effets de bord).
- **Le Mimétisme :** Si vous ne cadrez pas l'IA, elle copiera les standards génériques ou votre dette technique.
- **Le Contrat (`copilot-instructions.md`) :** C'est l'outil ultime du Tech Lead en 2026. L'IA est un moteur surpuissant, vos instructions sont le volant.
HAFID Yahya 71800578
OUBADIA Tanel 71806010

PROJET JAVA FRACTALES (Julia et Mandelbrot)

Compilation et execution:
Il faut avoir JDK installé sur sa machine pour lancer le programme:
- sudo apt install openjdk-17-jdk-headless
- sudo apt install openjdk-17-jdk
Dans un terminal ouvert à la racine de notre programme (où se trouve le
makefile):
- Compiler le programme via la commande make
- Lancer le programme via la commande make run et suivre les instructions:
1) Pour la version en ligne commande, il suffit de répondre 2 à la première
demande
2) Pour la version en interface graphique, il faut répondre 1

Nous avons choisi de programmer la plupart des classes à l'aide de fabriques
statiques afin de sécuriser au maximum nos classes, avec des attributs privés.
Nous avons également choisi de suivre un modèle MVC qui nous permet de
séparer les méthodes et l'interface graphique.
Nous avons utilisé des expressions lambdas quand il le fallait sur les boutons.
Les erreurs et exceptions sont gérées correctement, et il est possible de zoomer
dans l'interface graphique à l'aide de la molette de la souris.

Concernant les deux versions, il faut obligatoirement suivre la syntaxe
proposée: les espaces doivent être respectés, et la puissance 0 doit être
représentée. Les puissances sont représentée via le symbole ^ suivi du degré.

Dans la version interface graphique, au lancement, il est possible de cliquer
sur le bouton "submit" qui permet de prendre en compte les valeurs par défaut
de tous les "text areas" et permet de créer une jolie figure sur l'écran.
Nous avons également fait le choix de rajouter un bouton "save" qui permet
à l'utilisateur de sauvegarder sa création s'il le veut.
Le nom par défaut d'une image créée (via la ligne de commande) est
le polynôme concaténé au complexe spécifié et au plan choisi (.png).

Il est possible de choisir entre Julia et Mandelbrot dans les deux versions.

Mandelbrot est plutôt facile à gérer une fois les méthodes de Julia
fonctionnels.

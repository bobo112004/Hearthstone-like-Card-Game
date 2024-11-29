# Tema 0 - GwentStone Lite

####  Tema este realizata de Popescu Bogdan Stefan, 322CAa

### Logica si implementare
* Proiectul este impartit in mai multe pachete numite corespunzator, fiecare
pachet continand clase cu aspecte comune sau care formeaza un concept.
* Astfel, proiectul este organizat dupa urmatoarea structura:
```
src/
├── Cards/
│   ├── BasicCard.java
│   ├── Decks.java
├── checker/
│   ├── Checker.java
│   ├── CheckerConstants.java
│   ├── Checkstyle.java
├── fileio/
│   ├── ActionsInput.java
│   ├── CardInput.java
│   ├── Coordinates.java
│   ├── DecksInput.java
│   ├── GameInput.java
│   ├── Input.java
│   ├── StartGameInput.java
├── Functionalities/
│   ├── Game.java
│   ├── GameBoard.java
│   ├── Player.java
├── Heroes/
│   ├── EmpressThorina.java
│   ├── GeneralKocioraw.java
│   ├── Hero.java
│   ├── KingMudface.java
│   ├── LordRoyce.java
├── main/
│   ├── Main.java
│   ├── Test.java
└── Minions/
    ├── Disciple.java
    ├── Minion.java
    ├── Miraj.java
    ├── Position.java
    ├── TheCursedOne.java
    ├── TheRipper.java

```
#### Pachetul Cards
* In acest pachet este definita clasa care sta la baza intregului proiect,
BasicCard, aceasta continand caracteristicile comune pe care le au toate cartile
(viata, mana, descriere, culori etc). Aceasta clasa urmeaza sa fie extinsa de
alte clase, mult mai specializate, anume de Minion si Hero.
* De asemenea, in acest pachet este definita si clasa Decks care contine toate
deck-urile folosite de un jucator si metoda care se ocupa de alegerea unui deck
care va fi folosit. Am optat ca un deck sa fie reprezentat printr-un ArrayList,
iar pachetul (toate deck-urile) sa fie reprezentat printr-un ArrayList de
ArrayList.

#### Pachetul Functionalities
* In pachetul Funtionalities am implementat clasele "entitatilor" necesare
pentru joc, anume Player, GameBoard si Game.
* Clasa Game gestioneaza desfasurarea efectiva a jocului, metoda playGame avand
acces la input si output. Metodele prezente in aceasta clasa au rolul de a tine
cont atat de tura si runda curenta, cat si de comenzile date ce trebuie
efectuate. Aceasta se va folosi de un vector de jucatori (2 in cazul nostru) si
de o tabla de joc pentru executia jocului.
* Clasa GameBoard reprezinta tabla de joc si implementeaza operatiile ce au
legatura cu aceasta, cum ar fi plasarea unei carti pe masa, dezghetarea cartilor
la sfarsitul unei ture etc. Masa pe care vor fi puse cartile de catre jucatori
este reprezentata printr-un ArrayList de ArrayList. Tabla de joc va fi folosita
atat in contextul clasei Game, cat si in contextul fiecarui minion sau erou in
parte atunci cand doreste sa atace.
* Fiecare jucator va fi reprezentat de un obiect Player ce va contine toate
informatiile necesare, cum ar fi deck-urile pe care jucatorul le poate folosi,
deck-ul curent amestecat, mana acestuia, mana etc.

#### Pachetul Heros
* In acest pachet am implementat clasa Hero derivata din clasa BasicCard,
adaugand functionalitatile specifice unui erou. Pentru folosirea unei abilitati
speciale am optat sa utilizez o metoda useAbility, ce va fi suprascrisa in clasa
fiecarui erou in parte, care se va ocupa de atacul special. Acest lucru ne
permite sa ne folosim de conceptul de polimorfism, tipul static al variabilei
erou fiind Hero.
* Clasele specializate pentru erou (Empress Thorina, King Mudface, General
Kocioraw si Lord Royce) extind la randul lor clasa Hero. Metoda useAbility este
suprascrisa in cadrul fiecarei clase cu implementarea dorita.

#### Pachetul Minions
* Structura acestui pachet este asemanatoare cu cea din cadrul pachetului Hero,
in sensul ca Minion va extinde clasa BasicCard pentru a retine informatii
specifice minionilor (daca este sau nu tank, pozitia pe care trebuie sa o ia pe
masa etc.). Metodele din cadrul acestei clase implementeaza interactiunile
Minion - Tabla, anume atacul pe care fiecare minion il poate face si folosirea
abilitatii speciale. Asemanator ca in cazul Hero, am utilizat o metoda care va
fi suprascrisa in cadrul claselor mai specializate.
* Astfel, clasele Disciple, Miraj, The Cursed One si The Ripper extind clasa
Minion si suprascriu metoda useSpecialAbility.
* Am utilizat un Enum pentru a retine pozitia cartilor pe masa pentru testarile
din cadrul implementarilor ce tin de asezarea acestora. Acesta este setat o 
singura data pentru fiecare carte in functie de numele minionului.

### Dificultati intampinate si rezolvarile acestora
* O prima provocare intampinata a fost "detasarea" de programarea in stil C. 
Obiectivul meu in cadrul acestei teme a fost acela de a gandi cat mai bine
posibil clasele si pachetele astfel incat sa nu fie nevoie sa scriu cod inutil,
iar clasele in sine sa fie cat mai independente (sa poata fi folosite in
contexte diferite pentru a usura implementarea). Am ajuns astfel la organizarea
curenta din cadrul temei.
* De asemenea, am optat ca programul sa se foloseasca de o anume genericitate in
cadrul comenzilor care pot fi executate de orice jucator. Mai exact, ceea ce am
urmarit a fost evitarea codului duplicat (de ex. metoda de plasare a cartii
pentru Player1 si Player2) si implementarea acestor metode fie in cadrul unui
jucator (adica in Player), fie cu parametri care, prin intermediul unor formule,
executa comanda dorita pentru orice jucator.
* Pentru identificarea randului Front si Back in functie de jucator, am
descoperit formulele `backRow = LAST_ROW_IDX * (PLAYERS_NR - playerIdx)` si
`frontRow = LAST_ROW_IDX - playerIdx`.

### Feedback
* Tema 0 a fost interesanta, cu mult diferita de temele anterioare (in sens
pozitiv). Cerinta a fost foarte bine formulata, cazurile speciale ce trebuiau
tratate si ordinea acestora erau deja mentionate, ceea ce a fost un ajutor mare
in rezolvarea temei.
* Tutorialul scurt din cod pentru JSON a fost de ajutor, desi ar fi putut sa fie
mai explicit (un comentariu, doua despre cateva linii de cod). Totusi,
link-urile din cadrul temei au completat aceste lipsuri.
package org.poo.Functionalities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Cards.Decks;
import org.poo.Minions.Minion;
import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;
import org.poo.Heroes.Hero;
import org.poo.Heroes.EmpressThorina;
import org.poo.Heroes.GeneralKocioraw;
import org.poo.Heroes.LordRoyce;
import org.poo.Heroes.KingMudface;

import java.util.ArrayList;

/**
 * Clasa Player tine cont de toate detaliile ce tin de jucator (deckuri, deck folosit, mana etc).
 * Listele de carti ce reprezinta deckul curent si mana sunt implementate folosind ArrayList.
 */
public final class Player {
    private int score;
    private int mana;
    private Decks allDecks;
    private ArrayList<Minion> currentDeck;
    private ArrayList<Minion> hand;
    private Hero hero;

    public Player(final DecksInput decksInput) {
        allDecks = new Decks(decksInput);
        score = 0;
        mana  = 0;
    }

    /**
     * Metoda returneaza o lista cu fiecare carte din deckul jucatorului. Se foloseste de metoda
     * printMinion() din cadrul Minion.
     * @return ArrayNode (format JSON) care contine informatiile fiecariei carti.
     */
    public ArrayNode printCurrentDeck() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Minion minion : currentDeck) {
            arrayNode.add(minion.printMinion());
        }
        return arrayNode;
    }

    /**
     * Metoda returneaza o lista cu toate cartile din mana jucatorului. Este asemanatoare cu metoda
     * {@link #printCurrentDeck()}.
     * @return ArrayNode (format JSON) care contine informatiile fiecarei carti.
     */
    public ArrayNode printPlayersHand() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (Minion minion : hand) {
            arrayNode.add(minion.printMinion());
        }
        return arrayNode;
    }

    /**
     * Metoda returneaza eroul jucatorului. Se foloseste de metoda printHero() din cadrul Hero.
     * @return ObjectNode (format JSON) cu informatiile cartii erou.
     */
    public ObjectNode printPlayersHero() {
        return hero.printHero();
    }

    /**
     * Metoda initializeaza un jucator. Se preia un deck din lista de deckuri si ii creeaza o copie
     * drept currentDeck ce va putea fi modificata / folosita, cat si eroul folosit de jucator.
     * Deckul ales va fi amestecat dupa un seed dat.
     * @param cardInput Cartea Hero din Input.
     * @param idxDeck Indexul deckului care va fi ales din lista cu toate deckurile.
     * @param seed Seedul folosit pentru Random si Shuffle.
     */
    public void initPlayer(final CardInput cardInput, final int idxDeck, final int seed) {
        currentDeck = new ArrayList<>();
        hand = new ArrayList<>();
        mana = 0;
        setHero(cardInput);
        currentDeck = allDecks.selectDeck(idxDeck, seed);
    }

    /**
     * Metoda se ocupa de mutarea unei carti din ArrayList-ul ce reprezinta deckul jucatorului in
     * ArrayList-ul ce reprezinta mana acestuia.
     */
    public void pickCard() {
        if (!currentDeck.isEmpty()) {
            Minion minion = currentDeck.removeFirst();
            hand.add(minion);
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public ArrayList<Minion> getHand() {
        return hand;
    }

    public Hero getHero() {
        return hero;
    }

    /**
     * Metoda preia o carte de la input de tip erou si o creeaza in cadrul jucatorului, in functie
     * de clasa specializata a acestuia.
     * @param cardInput Cartea erou primita de la Input.
     */
    public void setHero(final CardInput cardInput) {
        switch (cardInput.getName()) {
            case "Empress Thorina" -> hero = new EmpressThorina(cardInput);
            case "General Kocioraw" -> hero = new GeneralKocioraw(cardInput);
            case "King Mudface" -> hero = new KingMudface(cardInput);
            case "Lord Royce" -> hero = new LordRoyce(cardInput);
            default -> hero = new Hero(cardInput);
        }
    }
}

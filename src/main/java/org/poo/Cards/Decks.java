package org.poo.Cards;

import org.poo.Minions.Minion;
import org.poo.Minions.TheCursedOne;
import org.poo.Minions.TheRipper;
import org.poo.Minions.Disciple;
import org.poo.Minions.Miraj;

import org.poo.fileio.CardInput;
import org.poo.fileio.DecksInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/** Clasa Decks contine toate deck-urile date din input pe care le poate avea un
 * jucator. Aceasta se ocupa si cu alegerea unui deck care va fi folosit de jucator.
 * Lista de deckuri este implementata folosind ArrayList..
 */
public final class Decks {
    private ArrayList<ArrayList<Minion>> decks;

    public Decks(final DecksInput decksInput) {
        decks = new ArrayList<>();
        for (ArrayList<CardInput> currentDeck : decksInput.getDecks()) {
            ArrayList<Minion> deck = new ArrayList<>();
            decks.add(deck);
            for (CardInput currentCard : currentDeck) {
                deck.add(new Minion(currentCard));
            }
        }
    }

    /**
     * Metoda returneaza un deck din lista cu toate deck-urile ales de jucator care va fi folosit
     * pentru jocul curent.
     * @param idx Indexul deck-ului din lista de deck-uri.
     * @param seed Seed-ul pentru a amesteca deck-ul ales inainte de a fi dat jucatorului.
     * @return ArrayList ce reprezinta deck-ul pregatit pentru a fi folosit de jucator.
     */
    public ArrayList<Minion> selectDeck(final int idx, final int seed) {
        ArrayList<Minion> deck = decks.get(idx);
        ArrayList<Minion> currentDeck = new ArrayList<>();
        for (Minion minion : deck) {
            switch (minion.getName()) {
                case "Sentinel", "Berserker", "Goliath", "Warden" ->
                        currentDeck.add(new Minion(minion));
                case "Disciple" -> currentDeck.add(new Disciple(minion));
                case "Miraj" -> currentDeck.add(new Miraj(minion));
                case "The Cursed One" -> currentDeck.add(new TheCursedOne(minion));
                case "The Ripper" -> currentDeck.add(new TheRipper(minion));
                default -> currentDeck.add(new Minion(minion));
            }
        }
        Random rand = new Random(seed);
        Collections.shuffle(currentDeck, rand);
        return currentDeck;
    }

    public ArrayList<ArrayList<Minion>> getDecks() {
        return decks;
    }
}

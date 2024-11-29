package org.poo.Cards;

import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Clasa BasicCard ofera campurile si functionalitatile comune tuturor cartilor din cadrul jocului.
 * Aceasta este extinsa de clasele mai specializate Minion si Hero.
 */
public class BasicCard {
    private int mana;
    private int health;
    private boolean hasAttacked;
    private final String description;
    private final ArrayList<String> colors;
    private final String name;

    public BasicCard(final CardInput input) {
        this.mana = input.getMana();
        this.health = input.getHealth();
        this.description = input.getDescription();
        this.colors = input.getColors();
        this.name = input.getName();
    }

    public BasicCard(final BasicCard card) {
        this.mana = card.mana;
        this.health = card.health;
        this.description = card.description;
        this.colors = card.colors;
        this.name = card.name;
        this.hasAttacked = card.hasAttacked;
    }

    /**
     * Metoda care returneaza mana necesara pentru a folosi o carte.
     * @return Int care reprezinta costul mana al cartii.
     */
    public int getMana() {
        return mana;
    }

    /**
     * Metoda care returneaza viata pe care o are o carte.
     * @return Int care reprezinta viata cartii.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Metoda care seteaza viata unei carti.
     * @param health Viata pe care o va avea cartea.
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Metoda care returneaza starea de atac a unei carti.
     * @return Boolean cu starea curenta a cartii (daca a atacat sau nu deja).
     */
    public boolean getHasAttacked() {
        return hasAttacked;
    }

    /**
     * Metoda care actualizeaza starea de atac a unei carti.
     * @param hasAttacked Noua stare de atac a cartii.
     */
    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Metoda care returneaza descrierea unei carti.
     * @return String ce reprezinta descrierea cartii.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Metoda care returneaza o lista cu culorile folosite pentru aspectul vizual al cartii.
     * @return ArrayList de culori, fiecare culoare este de tip String.
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Metoda care returneaza numele unei carti.
     * @return String ce reprezinta numele cartii.
     */
    public String getName() {
        return name;
    }
}

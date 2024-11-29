package org.poo.Minions;

import org.poo.Functionalities.GameBoard;
import org.poo.fileio.Coordinates;

/**
 * Clasa specializata Disciple extinde clasa Minion cu metoda care implementeaza abilitatea
 * speciala a acestuia.
 */
public final class Disciple extends Minion {

    public Disciple(final Minion min) {
        super(min);
    }

    /**
     * Aceasta suprascrie metoda din clasa parinte Minion si implementeaza abilitatea speciala a
     * minionului Disciple, anume sa dea 2 puncte de viata unui minion aliat.
     * @param gameboard Tabla de joc cu cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care foloseste abilitatea.
     * @param attacked Coordonatele cartii pe care se va folosi abilitatea, nu poate fi null.
     * @return String cu eroarea daca abilitatea nu poate fi efectuate, null altfel.
     */
    @Override
    public String useSpecialAbility(final GameBoard gameboard, final int playerIdx,
                                    final Coordinates attacked) {
        if (attacked.getX() != gameboard.backRow(playerIdx)
                && attacked.getX() != gameboard.frontRow(playerIdx)) {
            return "Attacked card does not belong to the current player.";
        }
        Minion minion = gameboard.getBoard().get(attacked.getX()).get(attacked.getY());
        minion.setHealth(minion.getHealth() + 2);
        return null;
    }
}

package org.poo.Minions;

import org.poo.Functionalities.GameBoard;
import org.poo.fileio.Coordinates;

/**
 * Clasa specializata Miraj extinde clasa Minion cu metoda care implementeaza abilitatea speciala a
 * acestuia.
 */
public final class Miraj extends Minion {

    public Miraj(final Minion min) {
        super(min);
    }

    /**
     * Aceasta metoda suprascrie metoda cu acelasi nume din clasa parinte Minion si implementeaza
     * abilitatea speciala a minionului Miraj, anume sa faca schimb intre viata lui si a unui
     * minion atacat.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa foloseasca abilitatea.
     * @param attacked Coordonatele minionului asupra caruia va fi folosita abilitatea speciala, nu
     *                 poate fi null.
     * @return String cu eroarea daca abilitatea nu poate fi folosita, null altfel.
     */
    @Override
    public String useSpecialAbility(final GameBoard gameboard, final int playerIdx,
                                    final Coordinates attacked) {
        int enemyIdx = playerIdx % 2 + 1;
        if (attacked.getX() != gameboard.backRow(enemyIdx)
                && attacked.getX() != gameboard.frontRow(enemyIdx)) {
            return "Attacked card does not belong to the enemy.";
        }
        if (!gameboard.checkTank(enemyIdx, attacked)) {
            return "Attacked card is not of type 'Tank'.";
        }
        Minion attackedMinion = gameboard.getBoard().get(attacked.getX()).get(attacked.getY());
        int aux = attackedMinion.getHealth();
        attackedMinion.setHealth(getHealth());
        setHealth(aux);
        return null;
    }
}

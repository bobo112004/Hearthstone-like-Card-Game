package org.poo.Minions;

import org.poo.Functionalities.GameBoard;
import org.poo.fileio.Coordinates;

/**
 * Clasa specializata TheCursedOne extinde clasa Minion cu metoda care implementeaza abilitatea
 * speciala a acestuia.
 */
public final class TheCursedOne extends Minion {

    public TheCursedOne(final Minion min) {
        super(min);
    }

    /**
     * Aceasta metoda suprascrie metoda cu acelasi nume din clasa parinte Minion si implementeaza
     * abilitatea speciala a minionului TheCursedOne, anume sa faca schimb intre viata si atacul
     * unui minion.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa foloseasca abilitatea.
     * @param attacked Coordonatele minionului asupra caruia va fi folosita abilitatea speciala, nu
     *                 poate fi null.
     * @return String cu eroarea in cazul in care abilitatea nu poate fi efectuata, null altfel.
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
        attackedMinion.setHealth(attackedMinion.getAttackDamage());
        attackedMinion.setAttackDamage(aux);
        if (attackedMinion.getHealth() <= 0) {
            gameboard.getBoard().get(attacked.getX()).remove(attacked.getY());
        }
        return null;
    }
}

package org.poo.Heroes;

import org.poo.Functionalities.GameBoard;
import org.poo.Minions.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Clasa LordRoyce extinde clasa Hero cu metoda ce implementeaza abilitatea speciala
 * specifica acestui erou.
 */
public final class LordRoyce extends Hero {

    public LordRoyce(final CardInput input) {
        super(input);
    }

    /**
     * Metoda suprascrie aceeasi metoda din clasa parinte Hero si implementeaza abilitatea speciala
     * a lui Lord Royce, anume inghetarea minionilor de pe un rand.
     * @param gameboard Gameboardul ce ofera tabla de joc si cartile, nu poate fi null;
     * @param playerIdx Indexul playerului care foloseste abilitatea speciala,
     * @param row Randul pe care se doreste sa se foloseasca abilitatea.
     * @return String-ul cu eroarea in cazul in care actiunea nu este permisa, altfel null;
     */
    @Override
    public String useAbility(final GameBoard gameboard, final int playerIdx, final int row) {
        int enemyIdx = playerIdx % 2 + 1;
        if (row != gameboard.backRow(enemyIdx) && row != gameboard.frontRow(enemyIdx)) {
            return "Selected row does not belong to the enemy.";
        }
        ArrayList<ArrayList<Minion>> board = gameboard.getBoard();
        for (Minion minion : board.get(row)) {
            minion.setFrozen(true);
        }
        return null;
    }
}

package org.poo.Heroes;

import org.poo.Functionalities.GameBoard;
import org.poo.Minions.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Clasa EmpressThorina extinde clasa Hero cu metoda ce implementeaza abilitatea speciala
 * specifica acestui erou.
 */
public final class EmpressThorina extends Hero {

    public EmpressThorina(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Metoda suprascrie aceeasi metoda din clasa parinte Hero si implementeaza abilitatea speciala
     * a lui Empress Thorina, anume distrugerea cartii cu cea mai mare viata de pe rand.
     * @param gameboard Gameboardul ce ofera tabla de joc si cartile, nu poate fi null;
     * @param playerIdx Indexul playerului care foloseste abilitatea speciala.
     * @param row Randul pe care se doreste sa se foloseasca abilitatea.
     * @return String-ul cu eroarea in cazul in care actiunea nu este permisa, altfel null;
     */
    @Override
    public String useAbility(final GameBoard gameboard, final int playerIdx, final int row) {
        int enemyIdx = playerIdx % 2 + 1;
        if (row != gameboard.backRow(enemyIdx) && row != gameboard.frontRow(enemyIdx)) {
            return "Selected row does not belong to the enemy.";
        }
        int max = -1, idx = -1;
        ArrayList<ArrayList<Minion>> board = gameboard.getBoard();
        for (Minion minion : board.get(row)) {
            if (max < minion.getHealth()) {
                max = minion.getHealth();
                idx = board.get(row).indexOf(minion);
            }
        }
        if (idx >= 0) {
            board.get(row).remove(idx);
        }
        return null;
    }
}

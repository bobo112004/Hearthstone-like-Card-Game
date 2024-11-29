package org.poo.Heroes;

import org.poo.Functionalities.GameBoard;
import org.poo.Minions.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Clasa KingMudface extinde clasa Hero cu metoda ce implementeaza abilitatea speciala
 * specifica acestui erou.
 */
public final class KingMudface extends Hero {

    public KingMudface(final CardInput input) {
        super(input);
    }

    /**
     * Metoda suprascrie aceeasi metoda din clasa parinte Hero si implementeaza abilitatea speciala
     * a lui King Mudface, anume incrementarea vietii cartilor de pe rand.
     * @param gameboard Gameboardul ce ofera tabla de joc si cartile, nu poate fi null;
     * @param playerIdx Indexul playerului care foloseste abilitatea speciala,
     * @param row Randul pe care se doreste sa se foloseasca abilitatea.
     * @return String-ul cu eroarea in cazul in care actiunea nu este permisa, altfel null;
     */
    @Override
    public String useAbility(final GameBoard gameboard, final int playerIdx, final int row) {
        if (row != gameboard.backRow(playerIdx) && row != gameboard.frontRow(playerIdx)) {
            return "Selected row does not belong to the current player.";
        }
        ArrayList<ArrayList<Minion>> board = gameboard.getBoard();
        for (Minion minion : board.get(row)) {
            minion.setHealth(minion.getHealth() + 1);
        }
        return null;
    }
}

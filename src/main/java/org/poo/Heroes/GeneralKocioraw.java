package org.poo.Heroes;

import org.poo.Functionalities.GameBoard;
import org.poo.Minions.Minion;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Clasa GeneralKocioraw extinde clasa Hero cu metoda ce implementeaza abilitatea speciala
 * specifica acestui erou.
 */
public final class GeneralKocioraw extends Hero {

    public GeneralKocioraw(final CardInput input) {
        super(input);
    }

    /**
     * Metoda suprascrie aceeasi metoda din clasa parinte Hero si implementeaza abilitatea speciala
     * a lui General Kocioraw, anume incrementarea attackDamage-ului minionilor de pe un rand.
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
            minion.setAttackDamage(minion.getAttackDamage() + 1);
        }
        return null;
    }
}

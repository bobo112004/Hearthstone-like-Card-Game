package org.poo.Functionalities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Minions.Minion;
import org.poo.fileio.Coordinates;
import org.poo.Minions.Position;

import java.util.ArrayList;

/**
 * Clasa GameBoard contine tabla pe care vor fi puse cartile de catre jucatori precum si metodele
 * ce se ocupa de efectuarea diferitelor operatii asupra tablei (plasarea cartilor, afisare etc).
 */
public final class GameBoard {
    private ArrayList<ArrayList<Minion>> board;
    private static final int ROWS_NR = 4;
    private static final int COLS_NR = 5;
    private static final int LAST_ROW_IDX = 3;
    private static final int PLAYERS_NR = 2;

    public GameBoard() {
        board = new ArrayList<>();
        for (int i = 0; i < ROWS_NR; i++) {
            board.add(new ArrayList<>());
        }
    }

    /**
     * Metoda care se ocupa de plasarea unei carti din mana jucatorului pe masa pe pozitia adecvata
     * in functie de tipul cartii.
     * @param player Playerul care efectueaza actiunea (plasarea cartii pe masa), nu poate fi null.
     * @param playerIdx Indexul playerului care efectueaza actiunea.
     * @param cardIdx Indexul la care se afla cartea dorita in mana jucatorului.
     * @return String cu eroarea in cazul in care actiunea nu poate fi indeplinita, null altfel.
     */
    public String placeCard(final Player player, final int playerIdx, final int cardIdx) {
        Minion minion = player.getHand().get(cardIdx);
        int frontRowIdx = frontRow(playerIdx);
        int backRowIdx = backRow(playerIdx);
        if (minion.getMana() > player.getMana()) {
            return "Not enough mana to place card on table.";
        }
        if (minion.getPosition() == Position.FRONT && board.get(frontRowIdx).size() > COLS_NR) {
            return "Cannot place card on table since row is full.";
        }
        if (minion.getPosition() == Position.BACK && board.get(backRowIdx).size() > COLS_NR) {
            return "Cannot place card on table since row is full.";
        }
        player.setMana(player.getMana() - minion.getMana());
        if (minion.getPosition() == Position.FRONT) {
            player.getHand().remove(cardIdx);
            board.get(frontRowIdx).add(minion);
        }
        if (minion.getPosition() == Position.BACK) {
            player.getHand().remove(cardIdx);
            board.get(backRowIdx).add(minion);
        }
        return null;
    }

    /**
     * Metoda care returneaza toate cartile de pe masa, incepand de pe primul rand.
     * @return ArrayNode (JSON) in care vor fi puse toate cartile de pe masa, sub forma unor liste.
     */
    public ArrayNode printCardsOnTable() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode allCards = mapper.createArrayNode();
        for (ArrayList<Minion> row : board) {
            ArrayNode rowList = mapper.createArrayNode();
            for (Minion minion : row) {
                rowList.add(minion.printMinion());
            }
            allCards.add(rowList);
        }
        return allCards;
    }

    /**
     * Metoda care returneaza doar cartile inghetate de pe tabla de joc. Este asemanatoare cu
     * metoda {@link #printCardsOnTable()} din cadrul aceleiasi clase.
     * @return ArrayNode (JSON) in care vor fi puse toate cartile inghetate sub forma unei liste.
     */
    public ArrayNode printFrozenCardsOnTable() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode allFrozenCards = mapper.createArrayNode();
        for (ArrayList<Minion> row : board) {
            for (Minion minion : row) {
                if (minion.isFrozen()) {
                    allFrozenCards.add(minion.printMinion());
                }
            }
        }
        return allFrozenCards;
    }

    /**
     * Metoda verifica daca actiunea pe care jucatorul doreste sa o indeplineasca se poate
     * efectua, fie prin targetarea unui Tank, fie prin lipsa cartilor Tank care "opresc" atacul.
     * @param playerIdx Indexul player-ului care doreste sa indeplineasca o actiune ce poate fi
     *                  "oprita" de un tank.
     * @param coordinates Coordonatele cartii targetate.
     * @return  Boolean true daca actiunea poate fi indeplinita, false in caz contrar.
     */
    public boolean checkTank(final int playerIdx, final Coordinates coordinates) {
        if (coordinates != null) {
            if (board.get(coordinates.getX()).get(coordinates.getY()).isTank()) {
                return true;
            }
        }
        for (Minion minion : board.get(frontRow(playerIdx))) {
            if (minion.isTank()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Metoda returneaza o carte aflata la o anumita pozitie pe tabla. Se foloseste de metoda
     * printMinion() din cadrul Minion.
     * @param x Randul (indexul din lista de liste) pe care se afla cartea.
     * @param y Coloana (indexul din lista) la care se afla cartea in randul respectiv.
     * @return ObjectNode (format JSON) reprezentand cartea, sau null in cazul in care nu exista.
     */
    public ObjectNode getCardAtPosition(final int x, final int y) {
        if (board.size() < x || board.get(x).size() < y) {
            return null;
        }
        return board.get(x).get(y).printMinion();
    }

    /**
     * Metoda care se asigura de faptul ca la sfarsitul unei ture, cartile jucatorului aferent
     * nu mai sunt inghetate si pot ataca tura viitoare din nou.
     * @param playerIdx Indexul player-ului care a terminat tura.
     */
    public void resetCards(final int playerIdx) {
        for (Minion minion : board.get(frontRow(playerIdx))) {
            minion.setFrozen(false);
            minion.setHasAttacked(false);
        }
        for (Minion minion : board.get(backRow(playerIdx))) {
            minion.setFrozen(false);
            minion.setHasAttacked(false);
        }
    }

    /**
     * Metoda ce returneaza indexul corespunzator randului din fata pentru un jucator.
     * @param playerIdx Indexul playerului care efectueaza actiunea.
     * @return Indexul randului din fata aferent jucatorului.
     */
    public int frontRow(final int playerIdx) {
        return LAST_ROW_IDX - playerIdx;
    }

    /**
     * Metoda ce returneaza indexul corespunzator randului din spate pentru un jucator.
     * @param playerIdx Indexul playerului care efectueaza actiunea.
     * @return Int reprezentand indexul randului din spate aferent jucatorului.
     */
    public int backRow(final int playerIdx) {
        return LAST_ROW_IDX * (PLAYERS_NR - playerIdx);
    }

    public ArrayList<ArrayList<Minion>> getBoard() {
        return board;
    }

}



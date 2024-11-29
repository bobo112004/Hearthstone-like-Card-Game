package org.poo.Minions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Cards.BasicCard;
import org.poo.Functionalities.GameBoard;
import org.poo.fileio.CardInput;
import org.poo.fileio.Coordinates;
import org.poo.Heroes.Hero;

import java.util.ArrayList;

/**
 * Clasa Minion extinde clasa BasicCard, adaugand functionalitati specifice minionilor (tank,
 * inghetat, atacul asupra unei carti etc). Aceasta clasa poate fi extinsa la clasele specializate
 * de minioni care au abilitati speciale specifice.
 */
public class Minion extends BasicCard {
    private int attackDamage;
    private boolean isTank = false;
    private boolean isFrozen = false;
    private final Position position;

    public Minion(final CardInput input) {
        super(input);
        attackDamage = input.getAttackDamage();
        if (this.getName().equals("Sentinel") || this.getName().equals("Berserker")
                || this.getName().equals("The Cursed One") || this.getName().equals("Disciple")) {
            position = Position.BACK;
        } else {
            position = Position.FRONT;
            if (this.getName().equals("Goliath") || this.getName().equals("Warden")) {
                isTank = true;
            }
        }
    }

    public Minion(final Minion minion) {
        super(minion);
        attackDamage = minion.attackDamage;
        isTank = minion.isTank;
        isFrozen = minion.isFrozen;
        position = minion.position;
    }

    /**
     * Metoda implementeaza operatia de atac a unui minion asupra altui minion inamic.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa atace.
     * @param attacked Coordonatele minionului care va fi atacat, nu poate fi null.
     * @return String cu eroarea in cazul in care atacul nu este valid, null altfel,
     */
    public String minionUsesAttack(final GameBoard gameboard, final int playerIdx,
                                   final Coordinates attacked) {
        int enemyIdx = playerIdx % 2 + 1;
        ArrayList<ArrayList<Minion>> board = gameboard.getBoard();
        if (attacked.getX() != gameboard.backRow(enemyIdx)
                && attacked.getX() != gameboard.frontRow(enemyIdx)) {
            return "Attacked card does not belong to the enemy.";
        }
        if (getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        if (isFrozen) {
            return "Attacker card is frozen.";
        }
        if (!gameboard.checkTank(enemyIdx, attacked)) {
            return "Attacked card is not of type 'Tank'.";
        }
        Minion attackedMinion = board.get(attacked.getX()).get(attacked.getY());
        setHasAttacked(true);
        attackedMinion.setHealth(attackedMinion.getHealth() - attackDamage);
        if (attackedMinion.getHealth() <= 0) {
            board.get(attacked.getX()).remove(attacked.getY());
        }
        return null;
    }

    /**
     * Metoda implementeaza abilitatea speciala a unui minion asupra altui minion. Aceasta
     * efectueaza verificarile necesare ca operatia sa fie valida, apoi apeleaza metoda din cadrul
     * aceleiasi clase {@link #useSpecialAbility(GameBoard, int, Coordinates)} pentru abilitate.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa foloseasca abilitatea.
     * @param attacked Coordonatele minionului asupra caruia se va efectua abilitatea, nu poate fi
     *                 null.
     * @return String cu eroarea in cazul in care abilitatea nu poate fi folosita, null altfel.
     */
    public String minionUsesAbility(final GameBoard gameboard, final int playerIdx,
                                    final Coordinates attacked) {
        if (isFrozen()) {
            return "Attacker card is frozen.";
        }
        if (getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        String error = useSpecialAbility(gameboard, playerIdx, attacked);
        if (error == null) {
            setHasAttacked(true);
        }
        return error;
    }

    /**
     * Aceasta metoda implementeaza abilitatea speciala propriu-zisa, urmand sa fie suprascrisa
     * in clasele specializate ale minionilor care au abilitati speciale.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa foloseasca abilitatea.
     * @param attacked Coordonatele minionului asupra caruia va fi folosita abilitatea speciala, nu
     *                 poate fi null.
     * @return String cu eroarea in cazul in care abilitatea nu se poate efectua, null altfel.
     */
    public String useSpecialAbility(final GameBoard gameboard, final int playerIdx,
                                    final Coordinates attacked) {
        return null;
    }

    /**
     * Metoda implementeaza atacul unui minion asupra eroului jucatorului inamic.
     * @param gameboard Tabla de joc cu toate cartile, nu poate fi null.
     * @param playerIdx Indexul jucatorului care ordona minionului sa atace.
     * @param enemyHero Eroul jucatorului inamic, nu poate fi null;
     * @return String cu eroarea in cazul in care atacul nu poate fi efectuat, null altfel.
     */
    public String minionAttacksHero(final GameBoard gameboard, final int playerIdx,
                                    final Hero enemyHero) {
        if (getHasAttacked()) {
            return "Attacker card has already attacked this turn.";
        }
        if (isFrozen) {
            return "Attacker card is frozen.";
        }
        int enemyIdx = playerIdx % 2 + 1;
        if (!gameboard.checkTank(enemyIdx, null)) {
            return "Attacked card is not of type 'Tank'.";
        }
        enemyHero.setHealth(enemyHero.getHealth() - attackDamage);
        setHasAttacked(true);
        return null;
    }

    /**
     * Metoda returneaza toate informatiile specifice unui minion.
     * @return ObjectNode (format JSON) cu informatiile necesare.
     */
    public ObjectNode printMinion() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("mana", getMana());
        objectNode.put("attackDamage", attackDamage);
        objectNode.put("health", getHealth());
        objectNode.put("description", getDescription());
        ArrayNode colorsNode = mapper.createArrayNode();
        for (int i = 0; i < getColors().size(); i++) {
            colorsNode.add(getColors().get(i));
        }
        objectNode.set("colors", colorsNode);
        objectNode.put("name", getName());
        return objectNode;
    }

    /**
     * Metoda care returneaza attackDamage-ul unui minion.
     * @return Int ce reprezinta atacul unui minion.
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Metoda care seteaza attackDamage-ul unui minion.
     * @param attackDamage Noul atac al minionului.
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Metoda care returneaza/verifica daca un minion este de tip Tank sau nu.
     * @return Boolean ce reprezinta daca minionul este sau nu tank.
     */
    public boolean isTank() {
        return isTank;
    }

    /**
     * Metoda care returneaza/verifica daca un minion este inghetat (nu poate ataca).
     * @return Boolean ce reprezinta daca minionul este sau nu inghetat in cadrul turei curente.
     */
    public boolean isFrozen() {
        return isFrozen;
    }

    /**
     * Metoda care seteaza frozen a minionului.
     * @param frozen Noua stare a minionului.
     */
    public void setFrozen(final boolean frozen) {
        isFrozen = frozen;
    }

    /**
     * Metoda care returneaza pozitia unui minion pe tabla de joc (in fata sau in spate).
     * @return Position care da pozitia minionului de pe tabla (in fata sau in spate).
     */
    public Position getPosition() {
        return position;
    }
}

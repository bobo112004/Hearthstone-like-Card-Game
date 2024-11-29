package org.poo.Heroes;

import org.poo.Functionalities.GameBoard;
import org.poo.Functionalities.Player;
import org.poo.Cards.BasicCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;

/**
 * Clasa Erou extine clasa BasicCard si poate fi extinsa la randul ei de alte clase mai
 * specializate in functie de erou. Eroii vor avea intotdeauna setat health cu BASEHEALTH = 30.
 */
public class Hero extends BasicCard {

    private static final int BASE_HEALTH = 30;
    public Hero(final CardInput input) {
        super(input);
        setHealth(BASE_HEALTH);
    }

    /**
     * Metoda returneaza toate informatiile cartii de tip Hero.
     * @return ObjectNode (format JSON) ce va contine informatiile cartii.
     */
    public ObjectNode printHero() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("mana", getMana());
        objectNode.put("description", getDescription());
        ArrayNode colorsNode = mapper.createArrayNode();
        for (int i = 0; i < getColors().size(); i++) {
            colorsNode.add(getColors().get(i));
        }
        objectNode.set("colors", colorsNode);
        objectNode.put("name", getName());
        objectNode.put("health", getHealth());
        return objectNode;
    }

    /**
     * Metoda ce se ocupa cu folosirea abilitatii unui erou si cu verificarea conditiilor necesare.
     * Aceasta se foloseste de metoda {@link #useAbility(GameBoard, int, int)} din cadrul aceleiasi
     * clase pentru a executia propriu-zisa a abilitatii.
     * @param gameboard Tabla de joc cu toate cartile.
     * @param player Jucatorul care foloseste abilitatea speciala a eroului sau.
     * @param playerIdx Indexul jucatorului.
     * @param row Randul pe care se va folosi abilitatea speciala.
     * @return String cu eroarea in cazul in care abilitatea nu poate fi folosita, null altfel.
     */
    public String heroUsesAbility(final GameBoard gameboard, final Player player,
                                  final int playerIdx, final int row) {
        if (player.getMana() < getMana()) {
            return "Not enough mana to use hero's ability.";
        }
        if (getHasAttacked()) {
            return "Hero has already attacked this turn.";
        }
        String error =  useAbility(gameboard, playerIdx, row);
        if (error == null) {
            player.setMana(player.getMana() - getMana());
            setHasAttacked(true);
        }
        return error;
    }

    /**
     * Aceasta metoda va fi suprascrisa in fiecare clasa specializata in functie de abilitatea
     * specifica fiecarui erou.
     * @param gameboard Tabla de joc cu cartile.
     * @param playerIdx Indexul jucatorului care foloseste abilitatea speciala.
     * @param row Randul pe care se va folosi abilitatea specciala.
     * @return String cu eroarea in cazul in care abilitatea nu poate fi folosita, null altfel.
     *         In acest caz, nu se intampla nimic deci se va returna null.
     */
    public String useAbility(final GameBoard gameboard, final int playerIdx, final int row) {
        return null;
    }
}

package org.poo.Functionalities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.Heroes.Hero;
import org.poo.Minions.Minion;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import org.poo.fileio.StartGameInput;

import java.util.ArrayList;

/**
 * Clasa Game contine desfasurarea efectiva a jocului, mai exact se ocupa cu punerea in
 * practica a actiunilor fiecarui jucator.
 */
public final class Game {
    private static int gamesPlayed;
    private static int turn;
    private static int round;
    private static boolean gameEnded;
    private static Player[] players;
    private static GameBoard gameBoard;
    private static final int MAX_MANA = 10;

    private Game() {
    }

    /**
     * Metoda care se ocupa de desfasurarea jocului si tine cont de starea acestuia. Se foloseste
     * de un commandHandler ce va executa fiecare comanda.
     * @param input Inputul dat de catre AI care contine toate detaliile despre joc.
     * @param output ArrayNodeul din main in care sunt puse erorile si output-urile comenzilor.
     */
    public static void playGame(final Input input, final ArrayNode output) {
        players = new Player[2];
        players[0] = new Player(input.getPlayerOneDecks());
        players[1] = new Player(input.getPlayerTwoDecks());
        gamesPlayed = 1;
        for (GameInput currentGame : input.getGames()) {
            StartGameInput start = currentGame.getStartGame();
            ArrayList<ActionsInput> actions = currentGame.getActions();

            gameBoard = new GameBoard();
            gameEnded = false;
            initiateGame(start);
            checkNewRound(start);

            for (ActionsInput action : actions) {
                commandHandler(action, output, start);
                if (players[(turn + 1) % 2].getHero().getHealth() <= 0 && !gameEnded) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode node = mapper.createObjectNode();
                    if (turn == 0) {
                        node.put("gameEnded", "Player one killed the enemy hero.");
                    } else {
                        node.put("gameEnded", "Player two killed the enemy hero.");
                    }
                    output.add(node);
                    gameEnded = true;
                    players[turn].setScore(players[turn].getScore() + 1);
                }
            }
            gamesPlayed++;
        }
    }

    /**
     * Metoda se ocupa cu identificarea comenzii corecte si scrierea output-ului corespunzator.
     * @param action Inputul care contine toate detaliile pentru indeplinirea actiunii curente.
     * @param output ArrayNode-ul in care sunt puse erorile si outputurile, dupa caz.
     * @param start Inputul ce descrie starea in care se afla jocul la inceput.
     */
    public static void commandHandler(final ActionsInput action, final ArrayNode output,
                                      final StartGameInput start) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        int xCrd, yCrd;
        if (!gameEnded && action.getCommand().equals("endPlayerTurn")) {
            gameBoard.resetCards(turn + 1);
            players[0].getHero().setHasAttacked(false);
            players[1].getHero().setHasAttacked(false);
            turn = (turn + 1) % 2;
            checkNewRound(start);
            return;
        } else if (action.getCommand().equals("getPlayerDeck")) {
            node.put("command", action.getCommand());
            node.put("playerIdx", action.getPlayerIdx());
            node.set("output", players[action.getPlayerIdx() - 1].printCurrentDeck());
        } else if (action.getCommand().equals("getCardsInHand")) {
            node.put("command", action.getCommand());
            node.put("playerIdx", action.getPlayerIdx());
            node.set("output", players[action.getPlayerIdx() - 1].printPlayersHand());
        } else if (action.getCommand().equals("getPlayerHero")) {
            node.put("command", action.getCommand());
            node.put("playerIdx", action.getPlayerIdx());
            node.set("output", players[action.getPlayerIdx() - 1].printPlayersHero());
        } else if (action.getCommand().equals("getPlayerTurn")) {
            node.put("command", action.getCommand());
            node.put("output", turn + 1);
        } else if (action.getCommand().equals("getPlayerMana")) {
            node.put("command", action.getCommand());
            node.put("playerIdx", action.getPlayerIdx());
            node.put("output", players[action.getPlayerIdx() - 1].getMana());
        } else if (action.getCommand().equals("placeCard")) {
            String error = gameBoard.placeCard(players[turn], turn + 1, action.getHandIdx());
            if (error != null) {
                node.put("command", action.getCommand());
                node.put("error", error);
                node.put("handIdx", action.getHandIdx());
            }
        } else if (action.getCommand().equals("getCardsOnTable")) {
            node.put("command", action.getCommand());
            node.set("output", gameBoard.printCardsOnTable());
        } else if (action.getCommand().equals("cardUsesAttack")) {
            xCrd = action.getCardAttacker().getX();
            yCrd = action.getCardAttacker().getY();
            Minion minion = gameBoard.getBoard().get(xCrd).get(yCrd);
            String error = minion.minionUsesAttack(gameBoard, turn + 1, action.getCardAttacked());
            if (error != null) {
                node.put("command", action.getCommand());
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("x", action.getCardAttacker().getX());
                objectNode.put("y", action.getCardAttacker().getY());
                node.set("cardAttacker", objectNode);
                objectNode = mapper.createObjectNode();
                objectNode.put("x", action.getCardAttacked().getX());
                objectNode.put("y", action.getCardAttacked().getY());
                node.set("cardAttacked", objectNode);
                node.put("error", error);
            }
        } else if (action.getCommand().equals("getCardAtPosition")) {
            node.put("command", action.getCommand());
            node.put("x", action.getX());
            node.put("y", action.getY());
            ObjectNode objectNode = gameBoard.getCardAtPosition(action.getX(), action.getY());
            if (objectNode != null) {
                node.set("output", gameBoard.getCardAtPosition(action.getX(), action.getY()));
            } else {
                node.put("output", "No card available at that position.");
            }
        } else if (action.getCommand().equals("cardUsesAbility")) {
            xCrd = action.getCardAttacker().getX();
            yCrd = action.getCardAttacker().getY();
            Minion minion = gameBoard.getBoard().get(xCrd).get(yCrd);
            String error = minion.minionUsesAbility(gameBoard, turn + 1, action.getCardAttacked());
            if (error != null) {
                node.put("command", action.getCommand());
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("x", action.getCardAttacker().getX());
                objectNode.put("y", action.getCardAttacker().getY());
                node.set("cardAttacker", objectNode);
                objectNode = mapper.createObjectNode();
                objectNode.put("x", action.getCardAttacked().getX());
                objectNode.put("y", action.getCardAttacked().getY());
                node.set("cardAttacked", objectNode);
                node.put("error", error);
            }
        } else if (action.getCommand().equals("useAttackHero")) {
            xCrd = action.getCardAttacker().getX();
            yCrd = action.getCardAttacker().getY();
            Minion minion = gameBoard.getBoard().get(xCrd).get(yCrd);
            Hero enemyHero = players[(turn + 1) % 2].getHero();
            String error = minion.minionAttacksHero(gameBoard, turn + 1, enemyHero);
            if (error != null) {
                node.put("command", action.getCommand());
                ObjectNode objectNode = mapper.createObjectNode();
                objectNode.put("x", action.getCardAttacker().getX());
                objectNode.put("y", action.getCardAttacker().getY());
                node.set("cardAttacker", objectNode);
                node.put("error", error);
            }
        } else if (action.getCommand().equals("useHeroAbility")) {
            int row = action.getAffectedRow();
            Hero hero = players[turn].getHero();
            String error = hero.heroUsesAbility(gameBoard, players[turn], turn + 1, row);
            if (error != null) {
                node.put("command", action.getCommand());
                node.put("affectedRow", action.getAffectedRow());
                node.put("error", error);
            }
        } else if (action.getCommand().equals("getFrozenCardsOnTable")) {
            node.put("command", action.getCommand());
            node.set("output", gameBoard.printFrozenCardsOnTable());
        } else if (action.getCommand().equals("getPlayerOneWins")) {
            node.put("command", action.getCommand());
            node.put("output", players[0].getScore());
        } else if (action.getCommand().equals("getPlayerTwoWins")) {
            node.put("command", action.getCommand());
            node.put("output", players[1].getScore());
        } else if (action.getCommand().equals("getTotalGamesPlayed")) {
            node.put("command", action.getCommand());
            node.put("output", gamesPlayed);
        }
        if (!node.isEmpty()) {
            output.add(node);
        }
    }

    /**
     * Metoda care verifica daca au trecut doua ture / o runda. Jucatorii primesc mana si trag o
     * carte din deck.
     * @param start Inputul ce descrie starea de inceput a jocului.
     */
    public static void checkNewRound(final StartGameInput start) {
        if (!gameEnded && turn == start.getStartingPlayer() - 1) {
            round++;
            players[0].pickCard();
            players[1].pickCard();
            players[0].setMana(players[0].getMana() + Math.min(round, MAX_MANA));
            players[1].setMana(players[1].getMana() + Math.min(round, MAX_MANA));
        }

    }

    /**
     * Metoda care pregateste un nou joc (tura si runda). Aceasta seteaza deckurile si eroii
     * folositi de jucatori.
     * @param start Inputul ce descrie starea de inceput a jocului.
     */
    public static void initiateGame(final StartGameInput start) {
        int deckIdx = start.getPlayerOneDeckIdx();
        players[0].initPlayer(start.getPlayerOneHero(), deckIdx, start.getShuffleSeed());
        deckIdx = start.getPlayerTwoDeckIdx();
        players[1].initPlayer(start.getPlayerTwoHero(), deckIdx, start.getShuffleSeed());
        turn = start.getStartingPlayer() - 1;
        round = 0;
    }
}

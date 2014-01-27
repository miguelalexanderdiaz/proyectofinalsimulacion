/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosimulacion.problemaBlackjack;

import java.util.ArrayList;

/**
 *
 * @author Neos
 */
public class Player {

    private ArrayList<Card> hand;
    private int money;
    private int type;

    public Player(int money, int type) {
        this.money = money;
        this.hand = new ArrayList<Card>();
        this.type = type;
    }

    /**
     * @return the hand
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * @param hand the hand to set
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    /**
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}

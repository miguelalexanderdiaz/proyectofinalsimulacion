/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosimulacion.problemaBlackjack;

import java.util.Scanner;

/**
 *
 * @author Neos
 */
public class Logic {

    static int bet = 0;
    static Deck deck = new Deck();
    static int bj = 0;

    public static void main(String[] args) throws Exception {

        Player player1 = new Player(2000, 0);
        Player dealer = new Player(0, 1);


        int exit = 0;
        int totalPlayer = 0;
        int totalDealer = 0;
        Scanner sc = new Scanner(System.in);
        while (exit == 0) {
            try {
                bj = 0;
                for (int i = 0; i < 2; i++) {
                    player1.getHand().add(deck.getCard());
                    dealer.getHand().add(deck.getCard());
                }
                System.out.println("Initial Table: ");
                printHand(player1, dealer, 0);

                makeBet(player1);

                System.out.println("Initial Table uncovered");
                printHand(player1, dealer, 1);

                totalPlayer = play(player1, 0);
                if (totalPlayer > 21) {
                    totalDealer = Card.sumaCartas(dealer.getHand());
                } else {
                    totalDealer = play(dealer, 0);
                }
                System.out.println("Final Table: ");
                printHand(player1, dealer, 1);
                printResult(totalPlayer, totalDealer, player1);
                System.out.println("Blackjack = " + bj);
                System.out.println("Player´s Money: " + player1.getMoney());
                System.out.println("Deck Size: " + deck.getCards().size());
                System.out.println("DeckYard Size: " + deck.getDeckYard().size());
                System.out.println("Presione 0 para salir");
                int option = sc.nextInt();
                if (option == 0) {
                    exit = 1;
                }
                player1.getHand().clear();
                dealer.getHand().clear();
            } catch (java.lang.IndexOutOfBoundsException e) {
                deck.shuffDeck();
                player1.getHand().clear();
                dealer.getHand().clear();
            }
        }


    }

    public static void printHand(Player player, Player dealer, int hide) {

        System.out.print("Player´s Hand: ");
        for (int i = 0; i < player.getHand().size(); i++) {
            System.out.print(player.getHand().get(i).toString() + " / ");
        }
        System.out.println();

        if (hide == 0) {
            System.out.print("Dealer´s Hand: X / ");
            for (int i = 1; i < dealer.getHand().size(); i++) {
                System.out.print(dealer.getHand().get(i).toString() + " / ");
            }
        } else {
            System.out.print("Dealer´s Hand: ");
            for (int i = 0; i < dealer.getHand().size(); i++) {
                System.out.print(dealer.getHand().get(i).toString() + " / ");
            }
        }
        System.out.println("\n");

    }

    public static void makeBet(Player player) {
        player.setMoney(player.getMoney() - 2000);
        bet += 2000;
        System.out.println("Bet´s Done");
        System.out.println("Pot: " + bet + "\n");
    }

    public static int play(Player player, int strategy) {
        switch (strategy) {
            case 0:
                int aces = 0;
                int exit = 0;
                int total = 0;
                int extraCards = 0;
                while (exit == 0) {
                    for (int i = 0; i < player.getHand().size(); i++) {
                        Card card = player.getHand().get(i);
                        if (card.getValue() == 1) {
                            aces++;
                        }
                    }
                    total = Card.sumaCartas(player.getHand());
                    if (extraCards == 0 && (total + 10) == 21 && aces > 0) {
                        bj = 1;
                    }
                    if (total >= 17) {
                        exit = 1;

                        return total;
                    }
                    if (aces > 0) {
                        if ((total + 10) >= 17 && (total + 10) <= 21) {
                            exit = 1;

                            return total + 10;
                        }
                    }
                    player.getHand().add(deck.getCard());
                    extraCards++;
                }

                break;
            case 1:

                break;

        }
        return 0;
    }

    public static void printResult(int totalPlayer, int totalDealer, Player player) {
        if (totalPlayer > 21) {
            System.out.println("Dealer wins with: " + totalDealer);
            System.out.println("Player loses with: " + totalPlayer);
            bet = 0;
            return;
        }
        if (totalDealer > 21) {
            System.out.println("Player wins with: " + totalPlayer);
            System.out.println("Dealer loses with: " + totalDealer);
            payPlayer(player);
            return;
        }
        if (totalPlayer == totalDealer) {
            System.out.println("Draw with: " + totalPlayer);
            return;
        }
        if (totalPlayer > totalDealer) {
            System.out.println("Player wins with: " + totalPlayer);
            System.out.println("Dealer loses with: " + totalDealer);
            payPlayer(player);
        } else {
            System.out.println("Dealer wins with: " + totalDealer);
            System.out.println("Player loses with: " + totalPlayer);
            bet = 0;

        }

    }

    public static void payPlayer(Player player) {
        if (bj == 0) {
            player.setMoney(player.getMoney() + bet);
        } else {
            player.setMoney(player.getMoney() + (int) (1.5 * bet));
        }
        bet = 0;
    }
}

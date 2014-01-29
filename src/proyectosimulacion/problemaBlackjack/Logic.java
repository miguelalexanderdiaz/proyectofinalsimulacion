/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosimulacion.problemaBlackjack;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;
import proyectosimulacion.ProjectFrame;

/**
 *
 * @author Neos
 */
public class Logic implements Runnable {
    
    int retraso;

    /**
     * Parametros de la clase
     */
    public StringBuilder log = new StringBuilder();
    private int bet = 0;
    private Deck deck = new Deck();
    private int bj = 0;
    private int blowUpTimes = 0;
    private ArrayList<Integer> handValues = new ArrayList<Integer>();
    private int moneyAll = 0;
    private int totalGames = 0;
    private int dealerWins = 0;
    private int playerWins = 0;
    private int draws = 0;
    private ProjectFrame pj;
    private int iterations = 0;
    private int strategy = 0;
    XYSeries series = null;

    /**
     * Constructor de
     *
     * @Logic, se utiliza para asignar el ProjectFrame que se va a actualizar y
     * la estrategia que usa el jugador
     * @param pj es el ProjectFrame para actualizar la GUI
     * @param strategy es la estrategia que el jugador va a usar (0,1,2)
     */
    public Logic(ProjectFrame pj, int strategy) {
        this.iterations = iterations;
        this.pj = pj;
        this.strategy = strategy;
        this.iterations = Integer.parseInt(pj.getIterationsSimulation3().getText());
        this.retraso=Integer.parseInt(pj.getDelaySimulation3().getText());
    }

    @Override
    public void run() {
        Player player1 = new Player(2000, 0);
        Player dealer = new Player(0, 1);

        int exit = 0;
        int totalPlayer = 0;
        int totalDealer = 0;
        int moneyBefore = 0;
        Scanner sc = new Scanner(System.in);
        for (int k = 0; k < iterations; k++) {
            try {
                Thread.sleep(retraso);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            exit = 0;
            while (exit == 0) {
                moneyAll = player1.getMoney();
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

                    totalPlayer = play(player1, this.strategy);
                    if (totalPlayer > 21) {
                        totalDealer = Card.sumaCartas(dealer.getHand());
                    } else {
                        totalDealer = play(dealer, 0);
                    }
                    System.out.println("Final Table: ");
                    printHand(player1, dealer, 1);
                    moneyBefore = player1.getMoney();
                    printResult(totalPlayer, totalDealer, player1);
                    System.out.println("Blackjack = " + bj);
                    System.out.println("Player´s Money Before: " + moneyBefore);
                    System.out.println("Winnings: " + (player1.getMoney() - moneyBefore));
                    System.out.println("Player´s Money: " + player1.getMoney());
                    System.out.println("Deck Size: " + deck.getCards().size());
                    System.out.println("DeckYard Size: " + deck.getDeckYard().size());
                    System.out.println("________________________________________________________");
                    System.out.println();

                    handValues.add(totalPlayer);
                    player1.getHand().clear();
                    dealer.getHand().clear();
                    totalGames++;
                } catch (java.lang.IndexOutOfBoundsException e) {
                    bet = 0;
                    deck.shuffDeck();
                    player1.getHand().clear();
                    dealer.getHand().clear();
                    this.log.append("Iteraccion Numero: ").append(k + 1).append("\n");
                    this.log.append("Dinero del jugador: ").append(moneyAll).append("\n");
                    this.log.append("Player Wins: ").append(playerWins).append("\n");
                    this.log.append("Dealer Wins: ").append(dealerWins).append("\n");
                    this.log.append("Draws: ").append(draws).append("\n");
                    this.log.append("Juegos Hasta el Momento: ").append(totalGames).append("\n");
                    this.log.append("Porcentaje Volado: ").append(((double) blowUpTimes / (double) totalGames) * 100).append("\n");
                    this.log.append("Porcentaje Wins: ").append(((double) playerWins / (double) totalGames) * 100).append("\n");
                    this.log.append("Mano promedio: ").append(arrayAverage(handValues)).append("\n\n\n");
                    double percentaje = ((double) (k + 1) / (double) iterations) * 100;
                    pj.setLogSimulation3(log.toString());
                    pj.setProgressBar3((int) percentaje);
                    insertPieChart(0);
                    insertPieChart(1);
                    insertBarChart();
                    insertLineChart(k + 1);
                    exit = 1;
                }
            }
        }
        if (bet != 0) {
            moneyAll += bet;
        }
        System.out.println("Resultados Finales: ");
        System.out.println("Dinero: " + moneyAll);
        System.out.println("Veces Volado: " + blowUpTimes);
        System.out.println("Player Wins: " + playerWins);
        System.out.println("Dealer Wins: " + dealerWins);
        System.out.println("Draws: " + draws);
        System.out.println("Juegos Totales: " + totalGames);
        System.out.println("Mano promedio: " + arrayAverage(handValues));
        System.out.println("Porcentaje volado: " + ((double) blowUpTimes / (double) totalGames) * 100);
        System.out.println("Porcentaje Wins: " + ((double) playerWins / (double) totalGames) * 100);
    }

    /**
     * la funcion
     *
     * @printHand es usada para imprimir las manos de los jugadores de la mesa
     * para este caso solo el jugador y el dealer.
     * @param player es el jugador actual en la mesa
     * @param dealer es el crupier de la mesa
     * @param hide es un entero que indica si ya se ha mostrado o no la carta
     * oculta del dealer
     * @return no devuelve nada
     */
    public void printHand(Player player, Player dealer, int hide) {

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

    /**
     * La funcion
     *
     * @makeBet se encarga de hacer la apuesta del jugador a la mesa, en este
     * caso la apuesta es estatica por un valor de 2000, y utiliza la variable
     * global
     * @bet para guardar el botin
     * @param player es el jugador que hace la apuesta
     * @return no devuelve nada
     */
    public void makeBet(Player player) {
        player.setMoney(player.getMoney() - 2000);
        bet += 2000;
        System.out.println("Bet´s Done");
        System.out.println("Pot: " + bet + "\n");
    }

    /**
     * La funcion
     *
     * @play se utiliza para determinar si un jugador de la mesa pide cartas o
     * se planta dependiendo de la estrategia utilizada
     * @param player es el jugador que está jugando actualmente
     * @param strategy es un entero que representa la estrategia con la que está
     * jugando el jugador
     * @return devuelve el valor de la suma de las cartas de la mano del
     * jugador, es decir, el total de su mano
     */
    public int play(Player player, int strategy) {
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

                aces = 0;
                exit = 0;
                total = 0;
                extraCards = 0;
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
                    if (total >= 16) {
                        exit = 1;

                        return total;
                    }
                    if (aces > 0) {
                        if ((total + 10) >= 16 && (total + 10) <= 21) {
                            exit = 1;

                            return total + 10;
                        }
                    }
                    player.getHand().add(deck.getCard());
                    extraCards++;
                }

                break;
            case 2:
                aces = 0;
                exit = 0;
                total = 0;
                extraCards = 0;
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
                    if (total >= 15) {
                        exit = 1;

                        return total;
                    }
                    if (aces > 0) {
                        if ((total + 10) >= 15 && (total + 10) <= 21) {
                            exit = 1;

                            return total + 10;
                        }
                    }
                    player.getHand().add(deck.getCard());
                    extraCards++;
                }


                break;
        }
        return 0;
    }

    /**
     * La funcion
     *
     * @printResult se utiliza para imprimir el resultado final de la mano
     * actual de la mesa, decide quien gano, quien perdio o si hubo un empate y
     * dependiendo de cada caso toma decisiones para hacer los pagos y vaciar el
     * botin de la mesa
     * @param totalPlayer es un entero que representa la suma de la mano del
     * jugador actual
     * @param totalDealer es un entero que representa la suma de la mano del
     * dealer
     * @param player es el jugador de la mesa que estaba jugando contra el
     * dealer
     */
    public void printResult(int totalPlayer, int totalDealer, Player player) {
        if (totalPlayer > 21) {
            System.out.println("Dealer wins with: " + totalDealer);
            System.out.println("Player loses with: " + totalPlayer);
            dealerWins++;
            blowUpTimes++;
            bet = 0;
            return;
        }
        if (totalDealer > 21) {
            System.out.println("Player wins with: " + totalPlayer);
            System.out.println("Dealer loses with: " + totalDealer);
            playerWins++;
            payPlayer(player);
            return;
        }
        if (totalPlayer == totalDealer) {
            System.out.println("Draw with: " + totalPlayer);
            draws++;
            return;
        }
        if (totalPlayer > totalDealer) {
            System.out.println("Player wins with: " + totalPlayer);
            System.out.println("Dealer loses with: " + totalDealer);
            playerWins++;
            payPlayer(player);
        } else {
            System.out.println("Dealer wins with: " + totalDealer);
            System.out.println("Player loses with: " + totalPlayer);
            dealerWins++;
            bet = 0;

        }

    }

    /**
     * La funcion
     *
     * @payPlayer se utiliza para pagarle al jugador si gana, dependiendo si
     * tiene blackjack o no
     * @param player es el jugador al que se le va a pagar
     * @return no devuelve nada
     */
    public void payPlayer(Player player) {
        if (bj == 0) {
            player.setMoney(player.getMoney() + 2 * bet);
        } else {
            player.setMoney(player.getMoney() + bet + (int) (1.5 * bet));
        }
        bet = 0;
    }

    /**
     * La funcion
     *
     * @arrayAverage, se utiliza para encontrar el promedio de los numeros en
     * una lista de enteros
     * @param list la lista donde estan los numeros a los que se desea hallar el
     * promedio
     * @return el promedio de los datos de la lista
     */
    public static int arrayAverage(ArrayList<Integer> list) {
        int sum = 0;
        int average = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += list.get(i);
        }
        average = sum / list.size();
        return average;
    }

    /**
     * La funcion
     *
     * @countCardNumber, se utiliza para contar el numero de veces que sale un
     * @int en el arreglo
     * @handValues
     * @param number, el numero a encontrar
     * @return las veces que se encontro el numero
     */
    public int countCardNumber(int number) {
        int value = 0;
        for (int i = 0; i < handValues.size(); i++) {
            if (handValues.get(i) == number) {
                value++;
            }
        }
        return value;
    }

    /**
     * La funcion
     *
     * @insertPieChart se utiliza para insertar los graficos de pie en la GUI
     * @param type el numero grafico a insertar de los dos posibles en el
     * problema (0,1)
     */
    public void insertPieChart(int type) {
        if (type == 0) {
            DefaultPieDataset dataset = new DefaultPieDataset();

            Double porcentaje = ((double) playerWins / (double) totalGames) * 100;
            dataset.setValue("Victorias del Jugador=" + new DecimalFormat("#.##").format(porcentaje), porcentaje);

            porcentaje = ((double) dealerWins / (double) totalGames) * 100;
            dataset.setValue("Victorias del Dealer=" + new DecimalFormat("#.##").format(porcentaje), porcentaje);

            porcentaje = ((double) draws / (double) totalGames) * 100;
            dataset.setValue("Empates=" + new DecimalFormat("#.##").format(porcentaje), porcentaje);

            String chartTitle = "Porcentaje de Victorias (Dealer y Jugador) y empates";

            JFreeChart chart = ChartFactory.createPieChart3D(
                    chartTitle, // chart title
                    dataset, // data
                    true, // include legend
                    true,
                    false);

            PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(290);
            plot.setDirection(Rotation.CLOCKWISE);
            plot.setForegroundAlpha(0.5f);
            
            
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(20, 20));
            chartPanel.setVisible(true);
            
            pj.setGraphPanel31(chartPanel);
            
        } else {
            DefaultPieDataset dataset = new DefaultPieDataset();

            Double porcentaje = ((double) blowUpTimes / (double) totalGames) * 100;
            dataset.setValue("Veces que se volo=" + new DecimalFormat("#.##").format(porcentaje), porcentaje);

            porcentaje = ((double) (totalGames - blowUpTimes) / (double) totalGames) * 100;
            dataset.setValue("Veces que no se volo=" + new DecimalFormat("#.##").format(porcentaje), porcentaje);

            String chartTitle = "Porcentaje de voladas";

            JFreeChart chart = ChartFactory.createPieChart3D(
                    chartTitle, // chart title
                    dataset, // data
                    true, // include legend
                    true,
                    false);

            PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(290);
            plot.setDirection(Rotation.CLOCKWISE);
            plot.setForegroundAlpha(0.5f);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(20, 20));
            chartPanel.setVisible(true);
            pj.setGraphPanel34(chartPanel);

        }
    }

    /**
     * La funcion
     *
     * @insertBarChart se utiliza para insertar el grafico de barras en la GUI
     */
    public void insertBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double mano10 = countCardNumber(10);
        double mano11 = countCardNumber(11);
        double mano12 = countCardNumber(12);
        double mano13 = countCardNumber(13);
        double mano14 = countCardNumber(14);
        double mano15 = countCardNumber(15);
        double mano16 = countCardNumber(16);
        double mano17 = countCardNumber(17);
        double mano18 = countCardNumber(18);
        double mano19 = countCardNumber(19);
        double mano20 = countCardNumber(20);
        double mano21 = countCardNumber(21);
        dataset.setValue(mano10, "Veces por mano", "10");
        dataset.setValue(mano11, "Veces por mano", "11");
        dataset.setValue(mano12, "Veces por mano", "12");
        dataset.setValue(mano13, "Veces por mano", "13");
        dataset.setValue(mano14, "Veces por mano", "14");
        dataset.setValue(mano15, "Veces por mano", "15");
        dataset.setValue(mano16, "Veces por mano", "16");
        dataset.setValue(mano17, "Veces por mano", "17");
        dataset.setValue(mano18, "Veces por mano", "18");
        dataset.setValue(mano19, "Veces por mano", "19");
        dataset.setValue(mano20, "Veces por mano", "20");
        dataset.setValue(mano21, "Veces por mano", "21");



        String titulo;

        titulo = "Valores de las partidas en que salieron las respectivas manos";


        JFreeChart chart = ChartFactory.createBarChart(titulo, "Valor de la Mano", "Veces que salio la mano", dataset,
                PlotOrientation.VERTICAL, false, true, false);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(20, 20));
        chartPanel.setVisible(true);
        pj.setGraphPanel32(chartPanel);


    }

    /**
     * La funcion
     *
     * @insertLineChart se utiliza para insertar el grafico de linea del dinero
     * del jugador por iteracion
     * @param iteracion, la iteracion en la cual se dibuja el punto en la
     * grafica
     */
    public void insertLineChart(int iteracion) {
        if (this.series == null) {
            series = new XYSeries("XYGraph");
        }
        series.add(iteracion, moneyAll);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        JFreeChart chart = ChartFactory.createXYLineChart("Dinero Final por Iteracion", "Número Iteración", "Dinero", dataset, PlotOrientation.VERTICAL, true, true, false);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(20, 20));
        chartPanel.setVisible(true);
        pj.setGraphPanel33(chartPanel);

    }
}

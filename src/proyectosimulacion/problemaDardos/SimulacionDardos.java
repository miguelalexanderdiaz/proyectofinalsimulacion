/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosimulacion.problemaDardos;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import proyectosimulacion.ProjectFrame;

/**
 *
 * @author migueldiaz
 */
public class SimulacionDardos extends Thread {

    private int puntuacionTotal;
    private ProjectFrame pf;
    private int iteraciones;
    private int retraso;
    private int tipoJugador;
    public StringBuilder log = new StringBuilder();
    private double conteoDiana = 0;
    private double conteoAmarillo = 0;
    private double conteoAzul = 0;
    private double conteoRojo = 0;
    private double conteoBlanco = 0;
    private XYSeries series = null;

    public SimulacionDardos(ProjectFrame pf, int tipoJugador) {
        this.pf = pf;
        this.iteraciones = Integer.parseInt(pf.getIterationsSimulation2().getText());
        this.retraso = Integer.parseInt(pf.getDelaySimulation2().getText());
        this.tipoJugador = tipoJugador;
    }
    //clase interna que contiene los resultados

    public class Resultado {

        int lanzamiento;
        int puntuacion;

        public Resultado(int lanzamiento, int puntuacion) {
            this.lanzamiento = lanzamiento;
            this.puntuacion = puntuacion;
        }

        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();

            str.append("lanzamiento: ").append(lanzamiento).append(" , ");
            str.append("puntuaición: ").append(puntuacion).append(" .\n");
            return str.toString();
        }
    }

    @Override
    public void run() {
        ArrayList <Resultado> resultados;

        for (int i = 0; i < iteraciones; i++) {
            try {
                Thread.sleep(retraso);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            resultados=simular();
            logBuilder(resultados);
            insertBarChart(i);
            insertLineChart(i, resultados);
            pf.setLogSimulation2(log.toString());
            double progreso = ((double) i / (double) iteraciones) * 100;
            pf.setProgressBar2((int) progreso);
        }
    }

    public ArrayList<Resultado> simular() {
        ArrayList<Resultado> lista = new ArrayList<>();
        this.puntuacionTotal = 0;
        for (int i = 0; i < 5; i++) {
            int lanzamiento = lanzamientoSegunJugador();
            int puntuacion = puntuacion(lanzamiento);
            lista.add(new Resultado(lanzamiento, puntuacion));
            this.puntuacionTotal += puntuacion;
        }
        return lista;
    }

    public int lanzamientoSegunJugador() {
        Random ran = new Random();
        int ruleta = ran.nextInt(11);
        int lanzamiento;
        //jugador aleatorio
        if (tipoJugador == 0) {
            lanzamiento = lanzar(0, 30);
        } //buen jugador
        else if (tipoJugador == 1) {
            //tiene 60% de probabilidad de acertar entre diana y amarillo
            if (ruleta >= 5) {
                lanzamiento = lanzar(0, 5);
            } //tiene 30% de probabilidad de acertar en azul        
            else if (ruleta >= 2 && ruleta < 5) {
                lanzamiento = lanzar(6, 12);
            } //tiene probabilidad de 10% de acertar entre rojo y blanco
            else {
                lanzamiento = lanzar(13, 30);
            }
        } //mal jugador
        else {
            //tiene 60% de probabilidad de acertar entre rojo y blanco
            if (ruleta >= 5) {
                lanzamiento = lanzar(13, 30);
            } //tiene 30% de probabilidad de acertar en azul        
            else if (ruleta >= 2 && ruleta < 5) {
                lanzamiento = lanzar(6, 12);
            } //tiene probabilidad de 10% de acertar entre diana y amarillo
            else {
                lanzamiento = lanzar(0, 5);
            }
        }
        return lanzamiento;

    }

    //realiza el lanzamientro entre un valor minimo y máximo dado
    public int lanzar(int min, int max) {
        Random ran = new Random();
        return ran.nextInt((max - min) + 1) + min;
    }

    //calcula la puntuación dado un lanzamiento
    public int puntuacion(int lanzamiento) {
        //diana
        if (lanzamiento >= 0 && lanzamiento <= 2) {
            conteoDiana++;
            return 50;
        } //amarillo
        else if (lanzamiento > 2 && lanzamiento <= 5) {
            conteoAmarillo++;
            return 25;
        } //azul
        else if (lanzamiento > 5 && lanzamiento <= 12) {
            conteoAzul++;
            return 15;
        } //rojo
        else if (lanzamiento > 12 && lanzamiento <= 20) {
            conteoRojo++;
            return 10;
        } //blanco
        else {
            conteoBlanco++;
            return 5;
        }
    }

    public void insertBarChart(int iteracion) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double totalLanzamientos = (iteracion + 1) * 5;
        double porcentajeDiana = (conteoDiana / totalLanzamientos) * 100;
        double porcentajeAmarillo = (conteoAmarillo / totalLanzamientos) * 100;
        double porcentajeAzul = (conteoAzul / totalLanzamientos) * 100;
        double porcentajeRojo = (conteoRojo / totalLanzamientos) * 100;
        double porcentajeBlanco = (conteoBlanco / totalLanzamientos) * 100;

        dataset.setValue(porcentajeDiana, "Porcentaje aciertos en anillo", "Diana");
        dataset.setValue(porcentajeAmarillo, "Porcentaje aciertos en anillo", "Amarillo");
        dataset.setValue(porcentajeAzul, "Porcentaje aciertos en anillo", "Azul");
        dataset.setValue(porcentajeRojo, "Porcentaje aciertos en anillo", "Rojo");
        dataset.setValue(porcentajeBlanco, "Porcentaje aciertos en anillo", "Blanco");


        String titulo;
        if (tipoJugador == 0) {
            titulo = "Valores porcentuales en los anillos para un jugador con distribucion uniforme";
        } else if (tipoJugador == 1) {
            titulo = "Valores porcentuales en los anillos para un buen jugador";
        } else {
            titulo = "Valores porcentuales en los anillos para un mal jugador";
        }

        JFreeChart chart = ChartFactory.createBarChart(titulo, "color anillos", "Porcentaje aciertos en anillo", dataset,
                PlotOrientation.VERTICAL, false, true, false);
        

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setVisible(true);
        pf.setGraphProbability2(chartPanel);



    }

    public void insertLineChart(int iteracion,ArrayList<Resultado> simulacion) {
        if (this.series == null) {
            series = new XYSeries("XYGraph");
        }
        series.add(iteracion+1,promedioSerie(simulacion));
        XYSeriesCollection dataset=new XYSeriesCollection();
        dataset.addSeries(series);
        
        JFreeChart chart= ChartFactory.createXYLineChart("Promedio", "Número Iteración", "Puntuación promedio por serie", dataset, PlotOrientation.VERTICAL, true, true, false);
        
        
        ChartPanel chartPanel=new ChartPanel(chart);
        chartPanel.setVisible(true);
        pf.setGrahpAverage2(chartPanel);
    
    }

    public void logBuilder(ArrayList<Resultado> simulacion) {

        for (int i = 0; i < simulacion.size(); i++) {
            this.log.append("Número intento: ").append((i + 1)).append(" , ");
            this.log.append(simulacion.get(i).toString());

        }
        this.log.append("Puntuación media para este experimento: ").append(promedioSerie(simulacion));
        this.log.append("\n\n");
    }

    public double promedioSerie(ArrayList<Resultado> simulacion) {
        double totalPuntuacion = 0;
        for (int i = 0; i < simulacion.size(); i++) {
            totalPuntuacion += simulacion.get(i).puntuacion;
        }
        return totalPuntuacion / simulacion.size();
    }
}

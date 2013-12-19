/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectosimulacion.problemaPuertas;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import proyectosimulacion.ProjectFrame;

/**
 *
 * @author migueldiaz
 */
public class SimulacionPuertas extends Thread {

    private ArrayList<Boolean> contenidoPuertas;
    public StringBuilder log = new StringBuilder();
    private int iteraciones;
    private int retraso;
    private ProjectFrame pf;
    private double victorias = 0;
    private double derrotas = 0;
    private int seleccionDeCambioDePuerta;

    public SimulacionPuertas(ProjectFrame pf, int seleccionDeCambioDePuerta) {
        this.pf = pf;
        this.iteraciones = Integer.parseInt(pf.getIterationsSimulation1().getText());
        this.retraso = Integer.parseInt(pf.getDelaySimulation1().getText());
        this.seleccionDeCambioDePuerta = seleccionDeCambioDePuerta;
    }

    @Override
    public void run() {
        for (int i = 0; i < iteraciones; i++) {
            try {
                Thread.sleep(retraso);
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            simularResultado();
            insertPieChart();
            pf.setLogSimulation1(log.toString());
            double progreso=((double)i/(double)iteraciones)*100;
            pf.setProgressBar1((int)progreso);
        }
    }

    public void inizializarPuertas() {
        contenidoPuertas = new ArrayList<>();
        int premio = eleccionAleatoria();
        for (int i = 0; i < 3; i++) {
            if (i == premio) {
                contenidoPuertas.add(Boolean.TRUE);
            } else {
                contenidoPuertas.add(Boolean.FALSE);
            }
        }
    }

    public Boolean simularResultado() {
        inizializarPuertas();
        int eleccion = eleccionAleatoria();
        Boolean cambio;

        switch (seleccionDeCambioDePuerta) {
            case 0:
                cambio = true;
                break;
            case 1:
                cambio = false;
                break;
            default:
                cambio = cambioAleatorio();
                break;
        }

        //int pSinPremio = puertaSinPremio(eleccion);

        //si hizo el cambio y la elección inicial no era la ganadora 
        //ó no cambio y la elección inicial era la ganadora
        if (cambio && !contenidoPuertas.get(eleccion)
                || !cambio && contenidoPuertas.get(eleccion)) {
            logBuilder(contenidoPuertas, eleccion, cambio, true);
            victorias++;
            return true;
        } //si hizo el cambio y la eleccion inicial era la ganadora
        //ó no cambio y la eleccion inicial era no ganadora
        else if (cambio && contenidoPuertas.get(eleccion)
                || !cambio && !contenidoPuertas.get(eleccion)) {
            logBuilder(contenidoPuertas, eleccion, cambio, false);
            derrotas++;
            return false;
        }

        return null;
    }

    public int eleccionAleatoria() {
        Random ran = new Random();
        return ran.nextInt(3);
    }

    public Boolean cambioAleatorio() {
        Random ran = new Random();
        int aux = ran.nextInt();
        if (aux % 2 == 0) {
            return true;
        }
        return false;
    }

    public int puertaSinPremio(int eleccion) {
        for (int i = 0; i < contenidoPuertas.size(); i++) {
            if (!contenidoPuertas.get(i) && i != eleccion) {
                return i;
            }
        }
        return -1;
    }

    public void logBuilder(ArrayList<Boolean> contenidoPuertas, int eleccion, Boolean cambio, Boolean resultado) {
        this.log.append("contenido puertas: ").append(contenidoPuertas.toString()).append("\n");
        this.log.append("elección jugador: ").append(eleccion + 1).append("\n");
        this.log.append("elección de cambio: ").append(cambio).append("\n");
        this.log.append("es ganador: ").append(resultado).append("\n");
        this.log.append("\n\n");
    }

    public void insertPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Double porcentaje=derrotas / (derrotas + victorias) * 100;
        dataset.setValue("Derrotas="+new DecimalFormat("#.##").format(porcentaje), porcentaje);
        
        porcentaje=victorias / (derrotas + victorias) * 100;
        dataset.setValue("Victorias="+ new DecimalFormat("#.##").format(porcentaje), porcentaje);
        
        String chartTitle="Porcentaje de ganancias y perdidas";
        if(seleccionDeCambioDePuerta==1){
            chartTitle="Porcentaje de ganancias y perdidas si no se cambia la puerta";
        }
        else if(seleccionDeCambioDePuerta==0){
            chartTitle="Porcentaje de ganancias y perdidas si se cambia la puerta";
        }


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
        chartPanel.setVisible(true);
        pf.setGraphPanel1(chartPanel);

    }
}

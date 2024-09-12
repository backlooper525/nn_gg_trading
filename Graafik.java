package Trading;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Graafik {
    
	
	public void teeGraafik(String data) {		
		
        final XYDataset dataset = createDataset();
        final CategoryDataset dataset2 = createDataset2();
        final XYDataset dataset3 = createDataset3(data);
        
        final JFreeChart chart = teeKaart1(dataset, "Konto vs Hind");
        final JFreeChart chart2 = teeKaart2(dataset2, "Tehingu P/L");
        final JFreeChart chart3 = teeKaart3(dataset3, "HR", "21");
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        final ChartPanel chartPanel2 = new ChartPanel(chart2);
        final ChartPanel chartPanel3 = new ChartPanel(chart3);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(700, 350));
        chartPanel2.setPreferredSize(new java.awt.Dimension(700, 200));       
        chartPanel3.setPreferredSize(new java.awt.Dimension(700, 200));
        
        JFrame frame = new JFrame("Graafik - " + data);
        frame.getContentPane().add(chartPanel, BorderLayout.NORTH);
        frame.getContentPane().add(chartPanel2, BorderLayout.CENTER);
        frame.getContentPane().add(chartPanel3, BorderLayout.SOUTH);
        frame.pack();
        RefineryUtilities.centerFrameOnScreen(frame);
        frame.setVisible(true);
    }   
    
    
    private XYDataset createDataset() {   	
    	final XYSeries series1 = new XYSeries("Konto");
    	final XYSeries series2 = new XYSeries("Hind");
    	
    	for(int i = 0; i <= Trader.viim_rida; i++) {    		
    		series1.add(i, Trader.kontomuut[i]);		
    		series2.add(i, Trader.buyholdMuut[i]);	
    	}


        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
                
        return dataset;        
    }   
    

    private CategoryDataset createDataset2() {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i <= Trader.viim_rida; i++) {    		
        	dataset.addValue(Trader.tehingukasum[i], "P/L", String.valueOf(i));
    	}
        return dataset;
    }
    

    private XYDataset createDataset3(String data) { 	  	
    	final XYSeries series = new XYSeries("HR Up");
    	final XYSeries series2 = new XYSeries("HR Down");
    	int alg = 0;
    	if(data.equalsIgnoreCase("trading")) {
    		alg = StockPrediction.trading_alg;
    	} else if(data.equalsIgnoreCase("validation")){   		
    		alg = StockPrediction.valid_alg;
    	} else if(data.equalsIgnoreCase("test")){   		
    		alg = StockPrediction.test_alg;
    	} else if(data.equalsIgnoreCase("train")){   		
    		alg = StockPrediction.train_alg;
    	}             
    	
    	for(int i = 0 + (alg - 1); i <= Trader.viim_rida + (alg - 1); i++) {    		        	
        	series.add(i, StockPrediction.HRUp[i]);
        	series2.add(i, StockPrediction.HRDwn[i]);
    	}
        
        final XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(series);
        dataSet.addSeries(series2);
        
        return dataSet;
    }
    
    
    private JFreeChart teeKaart1(final XYDataset dataset, String title) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            "",                      // x axis label
            "Muutus",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
//        legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
//        plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
//        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, false);
//        renderer.setSeriesShapesVisible(1, false);
//        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        DecimalFormat newFormat = new DecimalFormat("0.000");
        rangeAxis.setNumberFormatOverride(newFormat);
        
        
        // OPTIONAL CUSTOMISATION COMPLETED.
        final NumberAxis catAxis = (NumberAxis) plot.getDomainAxis();
        catAxis.setVisible(false);
        
        return chart;
    }

    
    private JFreeChart teeKaart2(final CategoryDataset dataset, String title) {
        
        // create the chart...
        //final JFreeChart chart = ChartFactory.createBarChart(title, "", "P/L", dataset);
        final JFreeChart chart = ChartFactory.createBarChart(title, "", "P/L", dataset, PlotOrientation.VERTICAL, false, true, false);
        
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

        CategoryPlot p = chart.getCategoryPlot(); 
        CategoryAxis axis = p.getDomainAxis();
        axis.setVisible(false);
        
        
        final NumberAxis rangeAxis = (NumberAxis) p.getRangeAxis();
        DecimalFormat newFormat = new DecimalFormat("0.000");
        rangeAxis.setNumberFormatOverride(newFormat);

        //final DecimalFormat formatter = new DecimalFormat("0.##%");
       // rangeAxis.setTickUnit(new NumberTickUnit(1, formatter));


        
        
        return chart;
        
    }
    
    
   private JFreeChart teeKaart3(final XYDataset dataset, String title, String period) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      // chart title
            "",                      // x axis label
            "HR MA1/MA2",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
//        legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
//        plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
//        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesLinesVisible(0, false);
//        renderer.setSeriesShapesVisible(1, false);
//        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        //rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        DecimalFormat newFormat = new DecimalFormat("0.000");
        rangeAxis.setNumberFormatOverride(newFormat);
        
        
        // OPTIONAL CUSTOMISATION COMPLETED.
        final NumberAxis catAxis = (NumberAxis) plot.getDomainAxis();
        catAxis.setVisible(false);
        
        
        
        return chart;
    }
    
   
}

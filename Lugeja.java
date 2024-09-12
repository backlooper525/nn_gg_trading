package Trading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Properties;


public class Lugeja extends Main {
	
	
	// Loeb mällu muutujad Properties failist
	public static void loeProperties() {
		
		riduFailis = Lugeja.getLines("C:\\Program Files\\GEVA\\hinnadata.txt");
		
		// ../param/Grammar/trading4.bnf
		System.out.println(System.getProperty("user.dir") + "\\param\\Parameters\\Trading.properties");
		//System.out.println("..\\param\\Parameters\\Trading.properties");
		//System.exit(0);
		
		
		//final String PARAMEETRITE_FAIL = "C:\\Program Files\\GEVA\\workspace2\\GEVA\\param\\Parameters\\Trading.properties";
		final String PARAMEETRITE_FAIL = ".\\param\\Parameters\\Trading.properties";
		//final String PARAMEETRITE_FAIL = System.getProperty("user.dir") + "\\param\\Parameters\\Trading.properties";
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(new File(PARAMEETRITE_FAIL)));
				
				popsize = Long.parseLong(properties.getProperty("population_size"));
				generatsioone = Integer.parseInt(properties.getProperty("generations"));
				samm = Integer.parseInt(properties.getProperty("samm"));
				threads = Integer.parseInt(properties.getProperty("threads"));
				moving_window = Boolean.parseBoolean(properties.getProperty("moving_window"));	
				esialg_treeningridu = Integer.parseInt(properties.getProperty("esialg_treeningridu"));
				
				gen_per_samm = Integer.parseInt(properties.getProperty("generatsioone_sammukohta"));
				data_algrida = Integer.parseInt(properties.getProperty("algrida"));
				
				//***
				if (Integer.parseInt(properties.getProperty("l6pprida")) == 0) {
					l6pprida = riduFailis;
				}
				
				symbol = properties.getProperty("symbol");
				l6ppkuup = properties.getProperty("l6ppkuup");
				tradingkuid = Integer.parseInt(properties.getProperty("tradingkuid"));
				validkuid = Integer.parseInt(properties.getProperty("validationkuid"));
				testdata_osak = Double.parseDouble(properties.getProperty("testdata_osak"));
				treeningaastaid = Integer.parseInt(properties.getProperty("treeningaastaid"));
				treeningkuid = Integer.parseInt(properties.getProperty("treeningkuid"));
						
				ttasu = Float.parseFloat(properties.getProperty("tehingutasu"));
				TestTrader.ttasu = (double)Float.parseFloat(properties.getProperty("tehingutasu"));			
				algkonto = Long.parseLong(properties.getProperty("algkonto"));
				TestTrader.algkonto = (double)Long.parseLong(properties.getProperty("algkonto"));				
				konto = algkonto;
				slippage = Float.parseFloat(properties.getProperty("slippage"));
				TestTrader.slippage = (double)Float.parseFloat(properties.getProperty("slippage"));		
				live = Boolean.parseBoolean(properties.getProperty("live"));
				konserv = Float.parseFloat(properties.getProperty("konservatiivsus"));
				
				TestTrader.useStopLoss = Boolean.parseBoolean(properties.getProperty("useStopLoss"));
				TestTrader.stoplossCrit = Double.parseDouble(properties.getProperty("stoplossCrit"));
				TestTrader.tradingMode = properties.getProperty("tradingMode");
				TestTrader.leverage = Double.parseDouble(properties.getProperty("leverage"));							

		} catch (Exception e) {
			System.out.println("\nERROR: Mingi jama lugemisel failist " + PARAMEETRITE_FAIL);
			e.printStackTrace();
			System.exit(0);
		}
		
		ridu_treidimiseks = riduFailis - esialg_treeningridu - data_algrida;
		samme = (int) (Math.floor(ridu_treidimiseks / samm));

		if (moving_window == true) {
			lisageneratsioone = (int) (samme * gen_per_samm);
		} else {
			lisageneratsioone = 0;
		}

		if (riduFailis <= esialg_treeningridu) {
			System.out.println("\nANDMERIDADE ARV ALLA TREENINGRIDADE ARVU!");
			System.exit(0);
		}
		
		
	}

	
	// Loeb hinnadata.txt mällu
	public static void loe() {
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(
					"C:\\Program Files\\GEVA\\hinnadata.txt"));
			String line = null;
			try {
				for (int i = 0; i <= riduFailis - 1; i++) {
					line = input.readLine();
					String[] massiiv = line.split("[,]", 0);

					
					// massiiv[0] on kuupäev
					kuup[i] = String.valueOf(massiiv[0]);
					opn[i] = Float.valueOf(massiiv[1]);
					high[i] = Float.valueOf(massiiv[2]);
					low[i] = Float.valueOf(massiiv[3]);
					clse[i] = Float.valueOf(massiiv[4]);
					vol[i] = Long.valueOf(massiiv[5]);
					avgvol[i] = Long.valueOf(massiiv[6]);
					sp500clse[i] = Float.valueOf(massiiv[7]);
					ma10[i] = Float.valueOf(massiiv[8]);
					ma20[i] = Float.valueOf(massiiv[9]);
					ma50[i] = Float.valueOf(massiiv[10]);
					ma200[i] = Float.valueOf(massiiv[11]);
					obv[i] = Long.valueOf(massiiv[12]);
					accdist[i] = Long.valueOf(massiiv[13]);
					rsi[i] = Long.valueOf(massiiv[14]);
					correl[i] = Float.valueOf(massiiv[15]);
					updays[i] =Integer.valueOf(massiiv[16]);
					downdays[i] =Integer.valueOf(massiiv[17]);
					high5[i] = Float.valueOf(massiiv[18]);
					high10[i] = Float.valueOf(massiiv[19]);
					high20[i] = Float.valueOf(massiiv[20]);
					high40[i] = Float.valueOf(massiiv[21]);
					high60[i] = Float.valueOf(massiiv[22]);
					high120[i] = Float.valueOf(massiiv[23]);
					high240[i] = Float.valueOf(massiiv[24]);
					low5[i] = Float.valueOf(massiiv[25]);
					low10[i] = Float.valueOf(massiiv[26]);
					low20[i] = Float.valueOf(massiiv[27]);
					low40[i] = Float.valueOf(massiiv[28]);
					low60[i] = Float.valueOf(massiiv[29]);
					low120[i] = Float.valueOf(massiiv[30]);
					low240[i] = Float.valueOf(massiiv[31]);	
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		kontomuut_m[esialg_treeningridu - 1 + data_algrida] = 0f;
		hinnamuut_m[esialg_treeningridu - 1 + data_algrida] = 0f;
	}

	// Loeb hinnadata2.txt mällu
	public static void loe2() {
		try {
			BufferedReader input = new BufferedReader(new FileReader(
					"C:\\Program Files\\GEVA\\hinnadata2.txt"));
			String line = null;
			try {
				while ((line = input.readLine()) != null) {
					String[] massiiv = line.split("[,]", 0);

					if (massiiv[0].equals(kuup[0])) {
						for (int i = 0; i <= riduFailis - 1; i++) {
							massiiv = line.split("[,]", 0);

							kuup2[i] = String.valueOf(massiiv[0]);
							Imonth[i] = Float.valueOf(massiiv[1]);
							VImonth[i] = Float.valueOf(massiiv[3]);
							Iyear[i] = Float.valueOf(massiiv[4]);
							IIyear[i] = Float.valueOf(massiiv[5]);
							Xyear[i] = Float.valueOf(massiiv[7]);
							ffunds[i] = Float.valueOf(massiiv[9]);
							Imontheurodollar[i] = Float.valueOf(massiiv[10]);
							VImontheurodollar[i] = Float.valueOf(massiiv[12]);
							moodysAAA_crprt[i] = Float.valueOf(massiiv[15]);
							
							nasdaq[i] = Float.valueOf(massiiv[22]);
							dow[i] = Float.valueOf(massiiv[23]);
							ftse[i] = Float.valueOf(massiiv[26]);
							hsi[i] = Float.valueOf(massiiv[30]);
							n225[i] = Float.valueOf(massiiv[31]);
							
							eurusd[i] = Float.valueOf(massiiv[32]);
							oil[i] = Float.valueOf(massiiv[38]);
							gld[i] = Float.valueOf(massiiv[39]);

							// Futuurid
							f_ffunds[i] = Float.valueOf(massiiv[40]);
							f_ffunds_oi[i] = Long.valueOf(massiiv[42]);
							f_tnote[i] = Float.valueOf(massiiv[43]);
							f_tnote_oi[i] = Long.valueOf(massiiv[45]);
							f_dollar[i] = Float.valueOf(massiiv[58]);
							f_dollar_oi[i] = Long.valueOf(massiiv[60]);
							f_oil[i] = Float.valueOf(massiiv[64]);
							f_oil_oi[i] = Long.valueOf(massiiv[66]);
							f_gld[i] = Float.valueOf(massiiv[67]);
							f_gld_oi[i] = Long.valueOf(massiiv[69]);
							f_copper[i] = Float.valueOf(massiiv[70]);
							f_copper_oi[i] = Long.valueOf(massiiv[72]);

							line = input.readLine();
						}
						break;
					}
				}
				if (kuup2[0]==null || !kuup[riduFailis-1].equals(kuup2[riduFailis-1])) {				//Juhuks, kui hinnadata.txt algus on liiga varajane, viimane tingimus on igaks juhuks, kui äkki mingil põhjusel kp-d ei ühti 
					System.out.println("");
					System.out.println("hinnadata.txt kuupäevad ei ole hinnadata2.txt kuupäevade ulatuses!");
					throw new EmptyStackException();
				}
			} catch (NullPointerException ex) {			//Juhuks, kui kuup2[0]!=null, kuid kui hinnadata.txt lõpp läheb üle
				System.out.println("");
				System.out.println("Mingi jama, ilmselt hinnadata.txt kuupäevad ei ole hinnadata2.txt kuupäevade ulatuses");
				throw new EmptyStackException();
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static void loereeglid() { // Loeb tekstifailist reeglid
		reeglid = new String[1000];
		int n = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(
					"C:\\Program Files\\GEVA\\populatsioon.txt"));
			String line = null;
			try {
				while ((line = input.readLine()) != null) {
					reeglid[n] = line;
					n++;
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (n != (int) popsize - 1) {
			System.out
					.println("Properties faili ja populatsioon.txt populatsioonide suurus ei ühti");
			throw new EmptyStackException();
		}
	}
		
	
	/**
	 * Tagastab ridade arvu failis
	 */
	public static int getLines(String fileName) {
		int count=0;
		BufferedReader input; 
		try {
			input = new BufferedReader(new FileReader(fileName));
			try {
				while(input.readLine()!=null) {				
					count++;
				}				
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			System.out.println("Ei leia faili: " + fileName);
			ex.printStackTrace();
		}
		return count;
	}	
	
	
}
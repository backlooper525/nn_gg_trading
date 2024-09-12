package Trading;

import Main.Test.TradingAbstractRun;

public abstract class TestTrader extends Main {

	public TestTrader() {
	}
	
	public abstract void treidi();
	
	public static ThreadLocal<Integer> p = new ThreadLocal<Integer>();
	public static double algkonto;
	public static double ttasu;
	public static double slippage;
	public static boolean useStopLoss;
	public static double stoplossCrit;
	public static String tradingMode;	
	public static double leverage;
	public static boolean useRegimeSwitch;

	public static double hind; 				// Viimane ostuhind.
	public static int osakuid;
	public static int ostup; 				// Viimane ostup‰ev
	public static int algrida;
	public static int l6pprida;
	public static boolean positsioon;
	public static int viim_rida;
	public static ThreadLocal<String> posType = new ThreadLocal<String>();

	public static Double buyholdMuut[]; 	// Double, sest on vaja mınes kohas null m‰‰rata neile
	public static Double kontomuut[];
	public static double ostuhind[];
	public static double myygihind[];
	public static double tehingukasum[];

	public static double konto;
	public static double kontoMax;
	public static double maxDrawdown;
	public static double bhkontoMax;
	public static double bhMaxDrawdown;
	public static int poskestus_sum;
	public static int poskestus; 			// Lahtise positsiooni kestus p‰evades
	public static int tehinguid;
	public static int pos_tehinguid;
	public static double parimtehing;
	public static double halvimtehing;
	public static double kum_kasum;
	public static double kum_kahjum;
	public static double kum_kasum_prots;
	public static double kum_kahjum_prots;
	
	static int gen;
	public static int sammu_nr;
	
	
	protected static ThreadLocal<Double> vol_muut = new ThreadLocal<Double>();
	protected static ThreadLocal<Double> vol_avgvol_suhe = new ThreadLocal<Double>();
	
	
	void resetValues(int alg, int l6pp) {		
		
		if(moving_window) {
			gen=TradingAbstractRun.gen;
	    	if(gen<generatsioone) {
	    		gen=generatsioone;
	    	}	    	
	    	sammu_nr=(int) Math.ceil((gen-generatsioone )/gen_per_samm);
	    	algrida=(esialg_treeningridu-samm)+(sammu_nr*samm)+data_algrida;		
	    	l6pprida=algrida+samm-1;
	    	if(l6pprida>=riduFailis-1){l6pprida=riduFailis-1;}
		} else {			//Kui ei ole kasutusel MWA, siis testi lıppedes lihtsalt arvutab tootlused ¸he reegli pıhjal   				
    		algrida=alg;
    		l6pprida=l6pp;
    	}		
		
		buyholdMuut = new Double[riduFailis];
		kontomuut = new Double[riduFailis];
		ostuhind = new double[riduFailis];
		myygihind = new double[riduFailis];
		tehingukasum = new double[riduFailis];
		positsioon = false;
		konto = algkonto;
		hind = 0.0; // Viimane ostuhind.
		osakuid = 0;
		ostup = 0;
		viim_rida = 0;
		kontoMax = -99999.0;
		maxDrawdown = 99999.0;
		bhkontoMax = -99999.0;
		bhMaxDrawdown = 99999.0;
		poskestus_sum = 0;
		poskestus = 0;
		tehinguid = 0;
		pos_tehinguid = 0;
		parimtehing = 0.0;
		halvimtehing = 0.0;
		kum_kasum = 0.0;
		kum_kahjum = 0.0;
		kum_kasum_prots = 0.0;
		kum_kahjum_prots = 0.0;
	}

	
	/**
	 * Parameetrid m‰‰ravad, kas treiditakse valideerimis- vıi tradingandmetega
	 * @param masterArray Millise prognoos-array pıhjal treiditakse
	 * @param clse = trading_clse | valid_clse
	 * @param ridu = validridu | tradingridu
	 */
	//public void trade(Float masterArray[][], int alg, int l6pp) {
	public void trade(int alg, int l6pp) {
		//teg‰lt pole alg l6ppu vaja nii edastada l‰bi BSFi
		
		resetValues(alg, l6pp);
		
		for (int pp = algrida-1; pp < l6pprida; pp++) {			
			p.set(pp);			//*** BSF tahab klassimuutujat

			//***
			vol_muut.set(Math.round(((vol[pp]/vol[pp-1])-1)*100.0)/100.0);
    		vol_avgvol_suhe.set(Math.round(vol[pp]/avgvol[pp]*100.0)/100.0);
			//***
			
			if (positsioon) {
				poskestus = pp - ostup;
			}
			
			double buyPrice = clse[pp];
			double sellPrice = clse[pp];
			double lowPrice = low[pp];
			
			//buy(p, clse[p.get()], "long");
			//sell(p, clse[p.get()], posType.get());			

			treidi();
			
			//if(ma20[p.get()]<ma200[p.get()]){buy(p.get(), clse[p.get()], "long");}else{if(clse[p.get()]<=ma20[p.get()]){sell(p.get(), clse[p.get()], posType);}}
			//System.out.println(p.get() +  " " + ma20[p.get()] + " " + ma200[p.get()] + " " + clse[p.get()]);
			

			// STOPLOSS EXIT
			if (useStopLoss) {
				double tehkas = Math.round(((osakuid * (lowPrice - slippage) - ttasu) / (osakuid * hind + ttasu) - 1) * 10000.0) / 10000.0;
				if (tehkas <= stoplossCrit) {
					double slPrice = (1 + stoplossCrit) * hind;
					sell(pp, slPrice, posType.get());
				}
			}

			// STATISTIKA. Kirjutab massiividesse hinna- ja kontomuutused, protsentides. Kui GEVA on lıpetanud kirjutab massiivid tekstifaili
			// Konto		
			double adjPrice;
			if (positsioon) {
				if (posType.get().equalsIgnoreCase("short")) {
					adjPrice = hind - (sellPrice - hind) * leverage;
				} else {
					adjPrice = hind + (sellPrice - hind) * leverage;
				}
			} else {
				adjPrice = 0.0; // vahet pole!
			}
			double kontoV22rtus = konto + ((double) osakuid * (adjPrice - slippage) - ttasu);
			if (kontoV22rtus > kontoMax) {
				kontoMax = kontoV22rtus;
			}
			// M‰‰rab max konto drawdowni
			if (kontoV22rtus / kontoMax - 1 < maxDrawdown) {
				maxDrawdown = Math.round((kontoV22rtus / kontoMax - 1) * 1000.0) / 1000.0;
			}
			if (positsioon == true) {
				kontomuut[pp] = Math.round((kontoV22rtus / algkonto - 1) * 1000.0) / 1000.0;
			} else {
				kontomuut[pp] = Math.round((konto / algkonto - 1) * 1000.0) / 1000.0;
			}
			// B&H "konto"
			double bhKonto = algkonto;
			double bhOsakuid = (int) ((algkonto - ttasu) / sellPrice);
			bhKonto -= ((bhOsakuid * sellPrice - slippage) + ttasu);
			bhKonto += ((bhOsakuid * sellPrice - slippage) - ttasu);
			if (bhKonto > bhkontoMax) {
				bhkontoMax = bhKonto;
			}
			if (bhKonto / bhkontoMax - 1 < bhMaxDrawdown) {
				bhMaxDrawdown = Math.round((bhKonto / bhkontoMax - 1) * 1000.0) / 1000.0;
			}
			buyholdMuut[pp] = Math.round((bhKonto / algkonto - 1) * 1000.0) / 1000.0;

			// Kui jıuab andmete lıppu ja positsioon on veel avatud, siis "m¸¸b" maha viimase ostuhinnaga, nagu viimast ostutehingut poleks toimududki.
			// Selliselt saab tulemus ıiglasem., kui lihtsalt andmete lıppedes positsioon sulgeda.
			if (pp == l6pprida - 1) {
				if (positsioon == false) {
					viim_rida = pp; 	//Kui positsioon on andmete lıppedes suletud, siis vıtab konto ja hinnamuut andmed lıpu seisuga.
				} else {
					konto += (osakuid * hind + ttasu);
					osakuid = 0;
					positsioon = false;
					for (int i = viim_rida + 1; i < l6pprida - 1; i++) {
						kontomuut[i] = (Double) null;
						buyholdMuut[i] = (Double) null; // ’iglasem on siis ka hinnamuut nullida
					}
				}
			}
		}
	}

	
	void buy(int p, double price, String posType) {		
		if (positsioon == false) {
			if (konto - ttasu * 2 > 0) { 		// Kaitse selle vastu, et konto ei l‰heks miinusesse.
				hind = price + slippage;
				osakuid = ((int) ((konto - ttasu * 2) / hind));
			} else {
				osakuid = 0;
			}					
			if (osakuid != 0) {				
				konto-= osakuid * hind + ttasu;
				positsioon = true;
				ostup = p;
				TestTrader.posType.set(posType);
				// Statistikasse
				ostuhind[p] = price + slippage;
			}
		}
	}

	
	void sell(int p, double price, String mode) {
		if (positsioon == true) {
			double adjPrice;
			if (mode.equalsIgnoreCase("short")) {
				adjPrice = hind - (price - hind) * leverage;
			} else {
				adjPrice = hind + (price - hind) * leverage;
			}
			konto+= osakuid * (adjPrice - slippage) - ttasu;
			viim_rida = p;
			poskestus_sum+= poskestus; // Statistikasse
			ostup = 0;
			poskestus = 0;
			// Statistikasse
			tehinguid++;
			myygihind[p] = adjPrice - slippage;
			tehingukasum[p] = Math.round(((osakuid * (adjPrice - slippage)) / (osakuid * hind) - 1) * 10000) / 10000.0f;
			if (tehingukasum[p] > parimtehing) {
				parimtehing = tehingukasum[p];
			}
			if (tehingukasum[p] < halvimtehing) {
				halvimtehing = tehingukasum[p];
			}
			if (tehingukasum[p] >= 0) {
				pos_tehinguid++;
				kum_kasum+= (osakuid * (adjPrice - slippage)) - (osakuid * hind);
				kum_kasum_prots+= tehingukasum[p];
			} else {
				kum_kahjum -= (osakuid * (adjPrice - slippage)) - (osakuid * hind);
				kum_kahjum_prots -= tehingukasum[p];
			}
			osakuid = 0;
			positsioon = false;
			//System.out.println("Konto: "+konto);
		}
	}

	
}

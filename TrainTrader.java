package Trading;

import Main.Test.TradingAbstractRun;

public abstract class TrainTrader extends Main {

	public TrainTrader() {
	}
	
	public abstract void treidi();
	
	
	public static double algkonto;
	public static double ttasu;
	public static double slippage;
	public static boolean useStopLoss;
	public static double stoplossCrit;
	public static String tradingMode;	
	public static double leverage;
	public static boolean useRegimeSwitch;
	
	public static int algrida;
	public static int l6pprida;
	
	// ThreadLocalid
	public static ThreadLocal<Integer> p = new ThreadLocal<Integer>();
	public static ThreadLocal<Double> hind = new ThreadLocal<Double>();				// Viimane ostuhind.
	public static ThreadLocal<Integer> osakuid = new ThreadLocal<Integer>();
	public static ThreadLocal<Integer> ostup = new ThreadLocal<Integer>();

	public static ThreadLocal<Boolean> positsioon = new ThreadLocal<Boolean>();
	public static ThreadLocal<Integer> viim_rida = new ThreadLocal<Integer>();
	public static ThreadLocal<String> posType = new ThreadLocal<String>();

//	public static Double buyholdMuut[]; 	// Double, sest on vaja mınes kohas null m‰‰rata neile
//	public static Double kontomuut[];
//	public static double ostuhind[];
//	public static double myygihind[];
//	public static double tehingukasum[];
	
	public static ThreadLocal<Double> konto = new ThreadLocal<Double>();
	public static ThreadLocal<Double> kontoMax = new ThreadLocal<Double>();
	public static ThreadLocal<Double> maxDrawdown = new ThreadLocal<Double>();
	public static ThreadLocal<Double> bhkontoMax = new ThreadLocal<Double>();
	public static ThreadLocal<Double> bhMaxDrawdown = new ThreadLocal<Double>();	
	public static ThreadLocal<Integer> poskestus_sum = new ThreadLocal<Integer>();
	public static ThreadLocal<Integer> poskestus = new ThreadLocal<Integer>();				// Lahtise positsiooni kestus p‰evades
	public static ThreadLocal<Integer> tehinguid = new ThreadLocal<Integer>();
	public static ThreadLocal<Integer> pos_tehinguid = new ThreadLocal<Integer>();
	
	public static ThreadLocal<Double> parimtehing = new ThreadLocal<Double>();
	public static ThreadLocal<Double> halvimtehing = new ThreadLocal<Double>();
	public static ThreadLocal<Double> kum_kasum = new ThreadLocal<Double>();
	public static ThreadLocal<Double> kum_kahjum = new ThreadLocal<Double>();
	public static ThreadLocal<Double> kum_kasum_prots = new ThreadLocal<Double>();
	public static ThreadLocal<Double> kum_kahjum_prots = new ThreadLocal<Double>();
	
	static int gen;
	public static int sammu_nr;
	
	
	protected static ThreadLocal<Double> vol_muut = new ThreadLocal<Double>();
	protected static ThreadLocal<Double> vol_avgvol_suhe = new ThreadLocal<Double>();
	
	
	void resetValues(int alg, int l6pp) {		
		
		algkonto = TestTrader.algkonto;
		ttasu = TestTrader.ttasu;
		slippage = TestTrader.slippage;
		useStopLoss = TestTrader.useStopLoss;
		stoplossCrit = TestTrader.stoplossCrit;
		tradingMode = TestTrader.tradingMode;	
		leverage = TestTrader.leverage;
		useRegimeSwitch = TestTrader.useRegimeSwitch;
		
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
		
//		buyholdMuut = new Double[riduFailis];
//		kontomuut = new Double[riduFailis];
//		ostuhind = new double[riduFailis];
//		myygihind = new double[riduFailis];
//		tehingukasum = new double[riduFailis];
		
		positsioon.set(false);
		konto.set(algkonto);
		hind.set(0.0); // Viimane ostuhind.
		osakuid.set(0);
		ostup.set(0);
		viim_rida.set(0);
		kontoMax.set(-99999.0);
		maxDrawdown.set(99999.0);
		bhkontoMax.set(-99999.0);
		bhMaxDrawdown.set(99999.0);
		poskestus_sum.set(0);
		poskestus.set(0);
		tehinguid.set(0);
		pos_tehinguid.set(0);
		parimtehing.set(0.0);
		halvimtehing.set(0.0);
		kum_kasum.set(0.0);
		kum_kahjum.set(0.0);
		kum_kasum_prots.set(0.0);
		kum_kahjum_prots.set(0.0);
	}

	
	/**
	 * Parameetrid m‰‰ravad, kas treiditakse valideerimis- vıi tradingandmetega
	 * @param masterArray Millise prognoos-array pıhjal treiditakse
	 * @param clse = trading_clse | valid_clse
	 * @param ridu = validridu | tradingridu
	 */
	//public void trade(Float masterArray[][], int alg, int l6pp) {
	public double trade(int alg, int l6pp) {
		resetValues(alg, l6pp);

		//System.out.println("Konto " + algkonto);
		
		for (int pp = algrida-1; pp < l6pprida; pp++) {			
			p.set(pp);			//*** BSF tahab klassimuutujat

			//***
			vol_muut.set(Math.round(((vol[pp]/vol[pp-1])-1)*100.0)/100.0);
    		vol_avgvol_suhe.set(Math.round(vol[pp]/avgvol[pp]*100.0)/100.0);
			//***
			
			if (positsioon.get()) {
				poskestus.set(pp - ostup.get());
			}
			
			double buyPrice = clse[pp];
			double sellPrice = clse[pp];
			double lowPrice = low[pp];
			
			//buy(p, clse[p.get()], "long");
			//sell(p, clse[p.get()], posType);			

			treidi();
			
			//if(ma20[p.get()]<ma200[p.get()]){buy(p.get(), clse[p.get()], "long");}else{if(clse[p.get()]<=ma20[p.get()]){sell(p.get(), clse[p.get()], posType);}}
			//System.out.println(p.get() +  " " + ma20[p.get()] + " " + ma200[p.get()] + " " + clse[p.get()]);
			

			// STOPLOSS EXIT
			if (useStopLoss) {
				double tehkas = Math.round(((osakuid.get() * (lowPrice - slippage) - ttasu) / (osakuid.get() * hind.get() + ttasu) - 1) * 10000.0) / 10000.0;
				if (tehkas <= stoplossCrit) {
					double slPrice = (1 + stoplossCrit) * hind.get();
					sell(pp, slPrice, posType.get());
				}
			}

			// STATISTIKA. Kirjutab massiividesse hinna- ja kontomuutused, protsentides. Kui GEVA on lıpetanud kirjutab massiivid tekstifaili
			// Konto		
			double adjPrice;
			if (positsioon.get()) {
				if (posType.get().equalsIgnoreCase("short")) {
					adjPrice = hind.get() - (sellPrice - hind.get()) * leverage;
				} else {
					adjPrice = hind.get() + (sellPrice - hind.get()) * leverage;
				}
			} else {
				adjPrice = 0.0; // vahet pole!
			}
			double kontoV22rtus = konto.get() + ((double) osakuid.get() * (adjPrice - slippage) - ttasu);
			if (kontoV22rtus > kontoMax.get()) {
				kontoMax.set(kontoV22rtus);
			}
			// M‰‰rab max konto drawdowni
			if (kontoV22rtus / kontoMax.get() - 1 < maxDrawdown.get()) {
				maxDrawdown.set(Math.round((kontoV22rtus / kontoMax.get() - 1) * 1000.0) / 1000.0);
			}
			
//			if (positsioon == true) {
//				kontomuut[pp] = Math.round((kontoV22rtus / algkonto - 1) * 1000.0) / 1000.0;
//			} else {
//				kontomuut[pp] = Math.round((konto / algkonto - 1) * 1000.0) / 1000.0;
//			}
//			// B&H "konto"
//			double bhKonto = algkonto;
//			double bhOsakuid = (int) ((algkonto - ttasu) / sellPrice);
//			bhKonto -= ((bhOsakuid * sellPrice - slippage) + ttasu);
//			bhKonto += ((bhOsakuid * sellPrice - slippage) - ttasu);
//			if (bhKonto > bhkontoMax) {
//				bhkontoMax = bhKonto;
//			}
//			if (bhKonto / bhkontoMax - 1 < bhMaxDrawdown) {
//				bhMaxDrawdown = Math.round((bhKonto / bhkontoMax - 1) * 1000.0) / 1000.0;
//			}
//			buyholdMuut[pp] = Math.round((bhKonto / algkonto - 1) * 1000.0) / 1000.0;

			// Kui jıuab andmete lıppu ja positsioon on veel avatud, siis "m¸¸b" maha viimase ostuhinnaga, nagu viimast ostutehingut poleks toimududki.
			// Selliselt saab tulemus ıiglasem., kui lihtsalt andmete lıppedes positsioon sulgeda.
			if (pp == l6pprida - 1) {
				if (!positsioon.get()) {
					viim_rida.set(pp); 	//Kui positsioon on andmete lıppedes suletud, siis vıtab konto ja hinnamuut andmed lıpu seisuga.
				} else {
					konto.set(konto.get() + (osakuid.get() * hind.get() + ttasu));
					osakuid.set(0);
					positsioon.set(false);
//					for (int i = viim_rida + 1; i < l6pprida - 1; i++) {
//						kontomuut[i] = (Double) null;
//						buyholdMuut[i] = (Double) null; // ’iglasem on siis ka hinnamuut nullida
//					}
				}
			}
		}
		System.out.println(algkonto + " " + konto.get());		
		return (konto.get());

	}

	
	void buy(int p, double price, String posType) {		
		if (!positsioon.get()) {
			if (konto.get() - ttasu * 2 > 0) { 		// Kaitse selle vastu, et konto ei l‰heks miinusesse.
				hind.set(price + slippage);
				osakuid.set(((int) ((konto.get() - ttasu * 2) / hind.get())));
			} else {
				osakuid.set(0);
			}					
			if (osakuid.get() != 0) {				
				konto.set(konto.get() - (osakuid.get() * hind.get() + ttasu));
				positsioon.set(true);
				ostup.set(p);
				TrainTrader.posType.set(posType);
//				// Statistikasse
//				ostuhind.get()[p] = price + slippage;
			}
		}
	}

	
	void sell(int p, double price, String mode) {
		if (positsioon.get()) {
			double adjPrice;
			if (mode.equalsIgnoreCase("short")) {
				adjPrice = hind.get() - (price - hind.get()) * leverage;
			} else {
				adjPrice = hind.get() + (price - hind.get()) * leverage;
			}
			konto.set(konto.get() + (osakuid.get() * (adjPrice - slippage) - ttasu));
			viim_rida.set(p);
			poskestus_sum.set(poskestus_sum.get() + poskestus.get()); // Statistikasse
			ostup.set(0);
			poskestus.set(0);
			// Statistikasse
			tehinguid.set(tehinguid.get() + 1);
			//myygihind.get()[p] = adjPrice - slippage;
			double tehingukasum = Math.round(((osakuid.get() * (adjPrice - slippage)) / (osakuid.get() * hind.get()) - 1) * 10000) / 10000.0f;
			if (tehingukasum > parimtehing.get()) {
				parimtehing.set(tehingukasum);
			}
			if (tehingukasum < halvimtehing.get()) {
				halvimtehing.set(tehingukasum);
			}
			if (tehingukasum >= 0) {
				pos_tehinguid.set(pos_tehinguid.get() + 1);
				kum_kasum.set(kum_kasum.get() + ((osakuid.get() * (adjPrice - slippage)) - (osakuid.get() * hind.get())));
				kum_kasum_prots.set(kum_kasum_prots.get() + tehingukasum);
			} else {
				kum_kahjum.set(kum_kahjum.get() - ((osakuid.get() * (adjPrice - slippage)) - (osakuid.get() * hind.get())));
				kum_kahjum_prots.set(kum_kahjum_prots.get() - tehingukasum);
			}
			osakuid.set(0);
			positsioon.set(false);
			//System.out.println("Konto: " + konto.get());
		}
	}

	
}

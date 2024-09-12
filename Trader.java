package Trading;

public class Trader extends Main {

	public static double algkonto;
	public static double ttasu;
	public static double slippage;
	public static boolean useStopLoss;
	public static double stoplossCrit;
	public static String tradingMode;	
	public static double leverage;
	public static boolean useRegimeSwitch;

	public static double buyPrice[];
	public static double sellPrice[];
	public static double low[];

	public static int p;
	public static double hind; 				// Viimane ostuhind.
	public static int osakuid;
	public static int ostup; 				// Viimane ostup‰ev
	public static int algrida;
	public static int l6pprida;
	public static boolean positsioon;
	public static int viim_rida;
	public static String posType;

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

	public static void resetValues(int ridu) {
		algrida = 0;
		l6pprida = ridu;
		buyholdMuut = new Double[ridu];
		kontomuut = new Double[ridu];
		ostuhind = new double[ridu];
		myygihind = new double[ridu];
		tehingukasum = new double[ridu];
		positsioon = false;
		konto = algkonto;
		p = 0;
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
	 * @param array Millise prognoos-array pıhjal treiditakse
	 * @param clse = trading_clse | valid_clse
	 * @param ridu = validridu | tradingridu
	 */
	public static void trade(double array[][], double buyPrice[], double sellPrice[], int alg, int ridu, double hrUp[], double hrDwn[], double low[], double[][] ruleData) {
		resetValues(ridu);
		String[] outputPair = output.split("--");
		int tPlus = Abiklass.getTime(outputPair[1]);
		int tPlusSum = tPlus;

		int pnncUpCol;
		double BiCCritUp = BiCDwn + biClassCrit;
		double BiCCritDwn = BiCUp - biClassCrit;
		double TriCCritUp = TriCZero + triClassCrit;
		double TriCCritDwn = TriCZero - triClassCrit;
		double critUp, critDwn;

		if (learningType.equalsIgnoreCase("2C-classification")) {
			critUp = BiCCritUp; // 2class
			critDwn = BiCCritDwn;
			pnncUpCol = 1;
		} else { // 3class
			critUp = TriCCritUp;
			critDwn = TriCCritDwn;
			pnncUpCol = 2;
		}

		boolean useRecallIndic = false;
		if (ridu == treeningridu) {
		} else if (!Trader.HRType.equalsIgnoreCase("none")) {
			useRecallIndic = true;
			if (Trader.HRType.equalsIgnoreCase("recall") && useRegimeSwitch) {
				System.out.println("ERROR: Regime switchi ei saa kasutada, kui HRType = recall, peab = precision!");
				System.exit(0);
			}
		}

		for (p = algrida; p < l6pprida; p++) {
			int pG = alg - 1 + p; // pGlobal
			int prognoos;
			if (useRecallIndic) {
				if (nntype.equalsIgnoreCase("pnn-c")) {
					if (array[p][pnncUpCol] > pnnProbCrit && hrUp[pG] >= HRCrit && hrUp[pG] > hrUp[pG - 1]) {
						prognoos = 1;
					} else if (array[p][0] > pnnProbCrit && hrDwn[pG] >= HRCrit && hrDwn[pG] > hrDwn[pG - 1]) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				} else if (!learningType.equalsIgnoreCase("regression")) {
					if (array[p][0] > critUp && hrUp[pG] >= HRCrit && hrUp[pG] > hrUp[pG - 1]) {
						prognoos = 1;
					} else if (array[p][0] < critDwn && hrDwn[pG] >= HRCrit && hrDwn[pG] > hrDwn[pG - 1]) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
					if (array[p][0] > critUp) {
						prognoos = 1;
					} else if (array[p][0] < critDwn && hrDwn[pG] >= HRCrit && hrDwn[pG] > hrDwn[pG - 1]) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				} else { // regression
					if (array[p][0] > regCrit && hrUp[pG] >= HRCrit && hrUp[pG] > hrUp[pG - 1]) {
						prognoos = 1;
					} else if (array[p][0] < -regCrit && hrDwn[pG] >= HRCrit && hrDwn[pG] > hrDwn[pG - 1]) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				}
			} else {
				if (nntype.equalsIgnoreCase("pnn-c")) {
					if (array[p][pnncUpCol] > pnnProbCrit) {
						prognoos = 1;
					} else if (array[p][0] > pnnProbCrit) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				} else if (!learningType.equalsIgnoreCase("regression")) {
					if (array[p][0] > critUp) {
						prognoos = 1;
					} else if (array[p][0] < critDwn) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				} else { // regression
					if (array[p][0] > regCrit) {
						prognoos = 1;
					} else if (array[p][0] < -regCrit) {
						prognoos = -1;
					} else {
						prognoos = 0;
					}
				}
			}
			if (positsioon) {
				poskestus = pG - ostup;
			}

			if (useRegimeSwitch) {
				if (hrUp[pG] >= hrDwn[pG]) {
					tradingMode = "long";
				} else {
					tradingMode = "short";
				}
			}

			// RULE SIGNAL
			boolean ruleBuy = true;
			if (ruleData[pG][0] > 0.0 && ruleData[pG - 1][0] < ruleData[pG][0] && ruleData[pG - 2][0] < ruleData[pG - 1][0]) {
				// if(ruleData[p][0] > 0.0) {
				ruleBuy = true;
			} else {
				ruleBuy = false;
			}
			// ruleBuy = true;

			// OST Vastavalt tradingmodele kasutab ainult long, short vıi
			// mılemat viisi
			if (tradingMode.equalsIgnoreCase("long")) {
				if (prognoos == 1 && ruleBuy) {
					buy(p, pG, buyPrice[pG], "long");
				}
			} else if (tradingMode.equalsIgnoreCase("short")) {
				if (prognoos == -1) {
					buy(p, pG, buyPrice[pG], "short");
				}
			} else if (!positsioon) { // long&short
				if (prognoos == 1) {
					buy(p, pG, buyPrice[pG], "long");
				} else if (prognoos == -1) {
					buy(p, pG, buyPrice[pG], "long");
				}
			}
			// M‹‹K
			if (useStopLoss) {
				double tehkas = Math.round(((osakuid * (low[pG] - slippage) - ttasu) / (osakuid * hind + ttasu) - 1) * 10000.0) / 10000.0;
				if (tehkas <= stoplossCrit) {
					double slPrice = (1 + stoplossCrit) * hind;
					sell(p, slPrice, posType);
					tPlusSum = tPlus;
				}
			}
			if (sellOnHorizon && poskestus == tPlus) { // Kui sellOnHorizon = TRUE, siis m¸¸b alati prognoosi horisondil, muidu siis, kui KA j‰rgmine progn on negatiivne
				sell(p, sellPrice[pG], posType);
			} else if ((poskestus == tPlusSum && prognoos == -1)
					|| outputPair[1].equals("close")) {
				sell(p, sellPrice[pG], posType);
				tPlusSum = tPlus;
			}

			//Statistikasse. Kirjutab massiividesse hinna- ja kontomuutused, protsentides. Kui GEVA on lıpetanud kirjutab massiivid tekstifaili
			//Konto		
			double adjPrice;
			if (positsioon) {
				if (posType.equalsIgnoreCase("short")) {
					adjPrice = hind - (sellPrice[pG] - hind) * leverage;
				} else {
					adjPrice = hind + (sellPrice[pG] - hind) * leverage;
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
				kontomuut[p] = Math.round((kontoV22rtus / algkonto - 1) * 1000.0) / 1000.0;
			} else {
				kontomuut[p] = Math.round((konto / algkonto - 1) * 1000.0) / 1000.0;
			}
			// B&H "konto"
			double bhKonto = algkonto;
			double bhOsakuid = (int) ((algkonto - ttasu) / sellPrice[alg - 1]);
			bhKonto -= ((bhOsakuid * sellPrice[alg - 1] - slippage) + ttasu);
			bhKonto += ((bhOsakuid * sellPrice[pG] - slippage) - ttasu);
			if (bhKonto > bhkontoMax) {
				bhkontoMax = bhKonto;
			}
			if (bhKonto / bhkontoMax - 1 < bhMaxDrawdown) {
				bhMaxDrawdown = Math.round((bhKonto / bhkontoMax - 1) * 1000.0) / 1000.0;
			}
			buyholdMuut[p] = Math.round((bhKonto / algkonto - 1) * 1000.0) / 1000.0;

			// Kui jıuab andmete lıppu ja positsioon on veel avatud, siis "m¸¸b" maha viimase ostuhinnaga, nagu viimast ostutehingut poleks toimududki.
			// Selliselt saab tulemus ıiglasem., kui lihtsalt andmete lıppedes positsioon sulgeda.
			if (p == l6pprida - 1) {
				if (positsioon == false) {
					viim_rida = p; 	//Kui positsioon on andmete lıppedes suletud, siis vıtab konto ja hinnamuut andmed lıpu seisuga.
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
			if (positsioon && poskestus == tPlusSum) {
				tPlusSum += tPlus;
			}
		}
	}

	public static void buy(int p, int pG, double price, String posType) {
		if (positsioon == false) {
			if (konto - ttasu * 2 > 0) { 		// Kaitse selle vastu, et konto ei l‰heks miinusesse.
				hind = price + slippage;
				osakuid = ((int) ((konto - ttasu * 2) / hind));
			} else {
				osakuid = 0;
			}
			if (osakuid != 0) {
				konto -= osakuid * hind + ttasu;
				positsioon = true;
				ostup = pG;
				Trader.posType = posType;
				// Statistikasse
				ostuhind[p] = price + slippage;
			}
		}
	}

	public static void sell(int p, double price, String mode) {
		if (positsioon == true) {
			double adjPrice;
			if (mode.equalsIgnoreCase("short")) {
				adjPrice = hind - (price - hind) * leverage;
			} else {
				adjPrice = hind + (price - hind) * leverage;
			}
			konto += osakuid * (adjPrice - slippage) - ttasu;
			viim_rida = p;
			poskestus_sum += poskestus; // Statistikasse
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
				kum_kasum += (osakuid * (adjPrice - slippage)) - (osakuid * hind);
				kum_kasum_prots += tehingukasum[p];
			} else {
				kum_kahjum -= (osakuid * (adjPrice - slippage)) - (osakuid * hind);
				kum_kahjum_prots -= tehingukasum[p];
			}
			osakuid = 0;
			positsioon = false;
			// System.out.println("Konto: "+konto);
		}
	}

}

package Trading;


import java.util.EmptyStackException;


public class Abiklass extends Main{
	

	/**
	 * Määrab tradingseti, valid. seti ja tradingseti algused/lõpud ja inputid/outputid
	 */
	public static void setDates() {
		l6pprida = getL6pprida(l6ppkuup);
		tradingridu = 21 * tradingkuid;			//avg 21 tradingpäeva kuus
		trading_alg = l6pprida - tradingridu +1 ;				//-1 sest siis nt 10 - 2 tradrida = 8, aga peab algama 9-st		
		if(treeningaastaid!=0 && treeningkuid == 0) {
			treeningridu = treeningaastaid * 252;
		} else if(treeningkuid!=0 && treeningaastaid == 0) {
			treeningridu = treeningkuid * 21;
		} else {
			System.out.println("Treeningaastate ja treeningkuude väärtused on mõlemad määratud! Üks neist peab olema 0!");
			throw new EmptyStackException();
		}		
		valid_l6pp = trading_alg - 1;
		validridu = validkuid * 21;
		valid_alg = valid_l6pp - validridu + 1;		
		test_l6pp = valid_alg - 1;
		testridu = (int)((treeningridu / (1- testdata_osak)) * testdata_osak);
		test_alg = test_l6pp - testridu + 1;
		train_l6pp = test_alg - 1;
		train_alg = train_l6pp - treeningridu +1;		
		
		ridu = treeningridu + testridu + validridu + tradingridu;
		if(train_alg < 1) {
			System.out.println("Treeningridu hetke settingute juures liiga vähe! train_alg = " + train_alg + ". Lisa data ridu või muuda settinguid!");
			throw new EmptyStackException();
		}
	}	
	
	/**
	 * Määrab massiividele mälu. Liste ei kasutata performance kaalutlustel
	 */
	public static void setMemory() {			
	
		kuup = new String[riduFailis];
		opn = new Float[riduFailis];
		high = new Float[riduFailis];
		low = new Float[riduFailis];
		clse = new Float[riduFailis];
		vol = new Long[riduFailis];
		avgvol = new Long[riduFailis];
		sp500clse = new Float[riduFailis];
		ma10 = new Float[riduFailis];
		ma20 = new Float[riduFailis];
		ma50 = new Float[riduFailis];
		ma200 = new Float[riduFailis];
		obv = new Long[riduFailis];
		accdist = new Long[riduFailis];
		rsi = new Long[riduFailis];
		correl = new Float[riduFailis];
		
		updays = new Integer[riduFailis];
		downdays = new Integer[riduFailis];
		high5 = new Float[riduFailis];
		high10 = new Float[riduFailis];
		high20 = new Float[riduFailis];
		high40 = new Float[riduFailis];	
		high60 = new Float[riduFailis];
		high120 = new Float[riduFailis];
		high240 = new Float[riduFailis];
		low5 = new Float[riduFailis];		
		low10 = new Float[riduFailis];		
		low20 = new Float[riduFailis];		
		low40 = new Float[riduFailis];		
		low60 = new Float[riduFailis];		
		low120 = new Float[riduFailis];		
		low240 = new Float[riduFailis];		


		// hinnadata2.txt andmed. Muutujate selgituseks vaata Data.xls
		// Treasury constant maturities
		kuup2 = new String[riduFailis];
		Imonth = new Float[riduFailis];
		VImonth = new Float[riduFailis];
		Iyear = new Float[riduFailis];
		IIyear = new Float[riduFailis];
		Xyear = new Float[riduFailis];

		ffunds = new Float[riduFailis];
		Imontheurodollar = new Float[riduFailis];
		VImontheurodollar = new Float[riduFailis];
		moodysAAA_crprt = new Float[riduFailis];
		nasdaq = new Float[riduFailis];
		dow = new Float[riduFailis];
		ftse = new Float[riduFailis];
		hsi = new Float[riduFailis];
		n225 = new Float[riduFailis];
		eurusd = new Float[riduFailis];
		oil = new Float[riduFailis];
		gld = new Float[riduFailis];

		// Futuurid
		f_ffunds = new Float[riduFailis];
		f_ffunds_oi = new Long[riduFailis];
		f_tnote = new Float[riduFailis];
		f_tnote_oi = new Long[riduFailis];
		f_dollar = new Float[riduFailis];
		f_dollar_oi = new Long[riduFailis];
		f_oil = new Float[riduFailis];
		f_oil_oi = new Long[riduFailis];
		f_gld = new Float[riduFailis];
		f_gld_oi = new Long[riduFailis];

		f_copper = new Float[riduFailis];
		f_copper_oi = new Long[riduFailis];

		// Algoritmis kasutatavad ja muudetavad muutujad
		kontomuut_m = new Float[riduFailis];
		hinnamuut_m = new Float[riduFailis];
		ostuhind_m = new Float[riduFailis];
		myygihind_m = new Float[riduFailis];
		tehingukasum_m = new Float[riduFailis];
		reegel_m = new String[riduFailis]; // Reegli massiiv
		
	}
			
	
	public static int getL6pprida(String kuup2ev) {
		int temp = 0;
		for(int i = 0; i < Main.riduFailis; i++) {
			if(Main.kuup[i].equals(kuup2ev)) {
				temp = i + 1;			//+1, sest vaja on mitte-massiiv indeksit
			}
		}
		if(temp == 0) {
			System.out.println("ERROR: Ei leia datafailist kuupäeva: " + kuup2ev);
			System.exit(0);
		}	
		return temp;
	}
    

	
}

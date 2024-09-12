package Trading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Properties;

import FitnessEvaluation.Trading.TestTrader;

public class Main {

	//------Traderi parameetrid-------
	
	public static boolean positsioon = false;
	public static double tehinguid; // Tehingute arv. Double sellepärast, et kasum. tehingute osakaalu ümardamisel on vaja
	public static double pos_tehinguid; // Kasumlike tehingute arv
	public static float kum_kasum; // Ainult kasum kokku
	public static float kum_kahjum; // Ainult kahjum kokku
	public static double kumkasum_prots;
	public static double kumkahjum_prots;
	public static int posits_kestus; // Loeb kokku positsioonide kestuse, lõppedes jagatakse siis tehingute arvuga ja saadakse keskmine positsiooni kestus
	public static double konto;
	public static double kontomax;
	public static double kontomin;
	public static float parimtehing = -10000;
	public static float halvimtehing = 10000;

	//---------------------
	
	
	public static String[] reeglid;
	
	// ********** Properties faili andmed
	
	public static String symbol;
	public static long popsize;
	
	//*** neid on siin praegu topelt, Traderis ka, hiljem see siit kustutada
	public static Float ttasu;
	public static Long algkonto;
	public static Float slippage;
	
	
	public static int generatsioone;
	public static float gen_per_samm; // Float sellepärast, et kasutatakse murru nimetajas ja murd läheb ümardamisele. Siis ei tohi nimetaja olla int
	public static int esialg_treeningridu;
	public static int samm;
	public static int lisageneratsioone;
	public static int data_algrida; // Millisest andmete reast alustab tegevust. Vajalik sellepärast, et algo tahab ka eelmiseid andmeid kasutada.
	public static boolean live; 	// Kas käin päris trading? Kui jah siis salvestab viimase populatsiooni tekstifaili
	public static float konserv; 	// Konservatiivsuse indeks
	public static int threads;
	
	public static int tradingkuid;
	public static int validkuid;
	public static double testdata_osak;
	public static int treeningaastaid; 		// if 0, siis ei võta arvesse
	public static int treeningkuid; 		// if 0, siis ei võta arvesse
	
	public static String l6ppkuup;
	public static int riduFailis; 	// Ridade arv hinnadata failis
	public static int ridu;						// Tradingseti, valid. seti ja tradingseti algused/lõpud need muutujad algavad 1 -st!
	public static int l6pprida; 
	public static int tradingridu;
	public static int trading_alg;
	public static int validridu;
	public static int valid_l6pp;
	public static int valid_alg;
	public static int testridu;
	public static int test_l6pp;
	public static int test_alg;
	public static int treeningridu;
	public static int train_l6pp;
	public static int train_alg;
	
	public static double masterArray[][];
	public static String labels[];
	public static int labelsLen;
	public static String kuup2evad[];
	
	// hinnadata.txt andmed
	public static String[] kuup;
	public static Float[] opn;
	public static Float[] high;
	public static Float[] low;
	public static Float[] clse;
	public static Long[] vol;
	public static Long[] avgvol;
	public static Float[] sp500clse;
	public static Float[] ma10;
	public static Float[] ma20;
	public static Float[] ma50;
	public static Float[] ma200;
	public static Long[] obv;
	public static Long[] accdist;
	public static Long[] rsi;
	public static Float[] correl;
	
	public static Integer[] updays;
	public static Integer[] downdays;
	public static Float[] high5;
	public static Float[] high10;
	public static Float[] high20;
	public static Float[] high40;	
	public static Float[] high60;
	public static Float[] high120;
	public static Float[] high240;
	public static Float[] low5;		
	public static Float[] low10;		
	public static Float[] low20;		
	public static Float[] low40;		
	public static Float[] low60;		
	public static Float[] low120;		
	public static Float[] low240;		

	// hinnadata2.txt andmed. Muutujate selgituseks vaata Data.xls
	// Treasury constant maturities
	public static String[] kuup2;
	public static Float[] Imonth;
	public static Float[] VImonth;
	public static Float[] Iyear;
	public static Float[] IIyear;
	public static Float[] Xyear;

	public static Float[] ffunds;
	public static Float[] Imontheurodollar;
	public static Float[] VImontheurodollar;
	public static Float[] moodysAAA_crprt;
	public static Float[] nasdaq;
	public static Float[] dow;
	public static Float[] ftse;
	public static Float[] hsi;
	public static Float[] n225;
	public static Float[] eurusd;
	public static Float[] oil;
	public static Float[] gld;

	// Futuurid
	public static Float[] f_ffunds;
	public static Long[] f_ffunds_oi;
	public static Float[] f_tnote;
	public static Long[] f_tnote_oi;
	public static Float[] f_dollar;
	public static Long[] f_dollar_oi;
	public static Float[] f_oil;
	public static Long[] f_oil_oi;
	public static Float[] f_gld;
	public static Long[] f_gld_oi;

	public static Float[] f_copper;
	public static Long[] f_copper_oi;

	// Algoritmis kasutatavad ja muudetavad muutujad
	public static Float[] kontomuut_m;
	public static Float[] hinnamuut_m;
	public static Float[] ostuhind_m;
	public static Float[] myygihind_m;
	public static Float[] tehingukasum_m;
	public static String[] reegel_m; // Reegli massiiv
	
	public static int kasutatud_reegleid;
	public static int viim_rida; // Rida, kus viimane ost tehti
	public static int samme;
	public static int ridu_treidimiseks;
	public static Boolean moving_window;


	// Loeb mällu muutujad, määrab massiivide ulatuse(mälu) ja aktsiate andmed
	public static void algustoimingud() {
		Lugeja.loeProperties();
		Abiklass.setMemory();		
		
		Lugeja.loe();
		Lugeja.loe2();
		
		kontomax = algkonto;
		kontomin = 0;
		
		//TestTrader.ostup.set(0);			//Need peab siin algatama
		//TestTrader.poskestus.set(0);

		if (live) {
			Lugeja.loereeglid();
		}
	}
	
	
}

package Trading;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Statistika extends Main{

	public static StringBuffer sb;
	
	//********** Kirjutab tradingu tulemused/andmed
	public static void kirjuta_tulemused() {
		sb = new StringBuffer();
		sb.append("Konto algseis: "+algkonto);
		sb.append(System.getProperty("line.separator"));
		sb.append("Konto lõppseis: "+(int)konto);
		sb.append(System.getProperty("line.separator"));		
		sb.append("Konto muutus: "+kontomuut_m[viim_rida]);
	    sb.append(System.getProperty("line.separator"));
	    sb.append("Buy&Hold muutus: "+hinnamuut_m[viim_rida]);
	    sb.append(System.getProperty("line.separator"));	    
	    sb.append("Return per annum: "+Math.round((kontomuut_m[viim_rida]/(viim_rida+1-esialg_treeningridu-data_algrida)*252)*1000)/1000.0);
	    sb.append(System.getProperty("line.separator"));	    
	    sb.append("Max drawdown: "+Math.round(kontomin*1000)/1000.0);	    
	    sb.append(System.getProperty("line.separator"));
	    sb.append("Testi kestus: "+(viim_rida+1-esialg_treeningridu-data_algrida));			//+1 sest array alg on 0
	    sb.append(System.getProperty("line.separator"));
	    sb.append("Tehingute arv: "+(int)tehinguid);
	    sb.append(System.getProperty("line.separator"));
	    
	    if(tehinguid!=0){			//Muidu tuleks nulliga jagamine ja error
	    	
	    	float win_prob = Math.round((pos_tehinguid/tehinguid)*100)/100.0f;
	    	float loss_prob = 1-win_prob;	    	
	    	float avg_win = Math.round((kum_kasum/pos_tehinguid)*100)/100.0f;
	    	float avg_loss = Math.round((kum_kahjum/(tehinguid-pos_tehinguid))*100)/100.0f;
	    	
	    	sb.append("Keskmine positsiooni kestus(p): "+posits_kestus/(int)tehinguid);
	    	sb.append(System.getProperty("line.separator"));
	    	sb.append("Kasumlike tehingute osakaal: "+Math.round((pos_tehinguid/tehinguid)*100)/100.0);			
		    sb.append(System.getProperty("line.separator"));
		    sb.append("Keskmine kasum: "+Math.round((kum_kasum/pos_tehinguid)*100)/100.0);
		    sb.append(System.getProperty("line.separator"));
		    sb.append("Keskmine kahjum: -"+Math.round((kum_kahjum/(tehinguid-pos_tehinguid))*100)/100.0);
		    sb.append(System.getProperty("line.separator"));		   
		    sb.append("Keskmine kasum /: "+Math.round((kumkasum_prots/pos_tehinguid)*10000.0)/10000.0);
		    sb.append(System.getProperty("line.separator"));
		    sb.append("Keskmine kahjum /: -"+Math.round((kumkahjum_prots/(tehinguid-pos_tehinguid))*10000.0)/10000.0);		    
		    sb.append(System.getProperty("line.separator"));
		    sb.append("Parim tehing: "+parimtehing);
		    sb.append(System.getProperty("line.separator"));
		    sb.append("Halvim tehing: "+halvimtehing);
		    sb.append(System.getProperty("line.separator"));
		    
		    sb.append("Expectancy: "+Math.round(((win_prob*(avg_win/avg_loss))-loss_prob)*100.0)/100.0);
		    //sb.append("Expectancy: "+Math.round((((Math.round((pos_tehinguid/tehinguid)*100)/100.0)*((Math.round((kum_kasum/pos_tehinguid)*100)/100.0)/(Math.round((kum_kahjum/(tehinguid-pos_tehinguid))*100)/100.0)))-((1-(Math.round((pos_tehinguid/tehinguid)*100)/100.0))*(Math.round((kum_kahjum/(tehinguid-pos_tehinguid))*100)/100.0)))*100.0)/100.0);
		    sb.append(System.getProperty("line.separator"));
	    }	    
	    
	    sb.append("Kasutatud reegleid: "+kasutatud_reegleid);
	    System.out.println(sb);
	}

	//Kirjutab konto muutuse ja muu data tekstifaili
	public static void kirjuta_txt() {
		String filename = "C:\\Program Files\\GEVA\\graafikudata.txt";
		try {
			File yourFile = new File(filename);
			yourFile.delete();
			File yourNewFile = new File(filename);
			yourNewFile.createNewFile();

			PrintWriter out = new PrintWriter(new FileWriter(filename));
			
			out.println("Data algrida: "+data_algrida);
			out.println("Treeningridu: "+esialg_treeningridu);
			out.println("Samm: "+samm);
			out.println(sb);
			out.println("");
			out.println("Konto muutus, Buy&Hold muutus, Ostuhind, Müügihind, Tehingu kasum/kahjum, Kasutatav reegel");
			for (int i = esialg_treeningridu-1+data_algrida; i <= riduFailis-1; i++) {
				out.println(kontomuut_m[i]+","+hinnamuut_m[i]+","+ostuhind_m[i]+","+myygihind_m[i]+","+tehingukasum_m[i]+","+reegel_m[i]);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void kirjuta_performance_txt() {
		try {
			FileWriter fileWriter = new FileWriter("C:\\Program Files\\GEVA\\performancelog.txt", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 			
			bufferedWriter.newLine(); 
			bufferedWriter.write(Math.round((pos_tehinguid/tehinguid)*100)/100.0+","+Math.round((kumkasum_prots/pos_tehinguid)*1000.0)/1000.0+","+Math.round((kumkahjum_prots/(tehinguid-pos_tehinguid))*1000.0)/1000.0
					+","+kontomuut_m[viim_rida]+","+hinnamuut_m[viim_rida]+","+(viim_rida+1-esialg_treeningridu));
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void l6putoimingud(){
		kirjuta_tulemused();
		if(tehinguid!=0){
			kirjuta_txt();
			kirjuta_performance_txt();
		}
	}

}

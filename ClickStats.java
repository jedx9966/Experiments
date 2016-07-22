package clickStats;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map.Entry;

public class ClickStats {

	private int sum = 0;	
	private ArrayList<Entry<Long, Integer>> clickList = new ArrayList<Entry<Long, Integer>>();
	private final int WINDOW = 5*60*1000; 
	
	private Long now() {
		return (System.currentTimeMillis());
	}
	
	private void cleanup(Long boundary) {
		Boolean done;

		//System.out.println("Cleanup IN : List Size = " + clickList.size() + " Sum = " + sum);				
		do {
			done = true;
			if (clickList.size() > 0) {		
				Entry<Long, Integer> b = clickList.get(0);
				if (b.getKey() < boundary) {
					//We have an Entry that is too old. Remove its value from the sum and remove entry from clickList
					//System.out.println("Remove = <" + b.getKey() + "," + b.getValue() + ">");				
					sum -= b.getValue();
					//System.out.println("Last Minute Sum = " + sum);							
					clickList.remove(b);
					//Loop again to check next entry
					done = false;
				} 
			}			
		} while (done == false);
		//System.out.println("Cleanup OUT : List Size = " + clickList.size() + " Sum = " + sum);				
	}
		
	void store (int value) {
		Long timeNow = now(); // Get current time
		Entry<Long, Integer> sample = new SimpleEntry<Long, Integer>(now(), value); //Create new Pair 

		// Add new pair to ArrayList
		//System.out.println(" Store --> " + timeNow + " <" +  sample.getKey() + "," + sample.getValue() + ">");
		clickList.add(sample);
		// Add new value to number of clicks in clickList
		sum += value;
		// Cleanup clickList from pairs that are too old
		cleanup (timeNow - WINDOW);


	}
	
	int mean() {		
		cleanup (now() - WINDOW);
		return sum;
	}
	
	
	public static void main (String args[]) throws IOException 
	{
		//Console cons;
		ClickStats cJed = new ClickStats();
		//long timeNow = clicks1.now();
		int n = 0;
		
		InputStreamReader read = new InputStreamReader(System.in);
        	BufferedReader valeurs = new BufferedReader(read);
        
		while (n>=0) {
			System.out.print (" ==> Action : ");
			try{
				n = Integer.parseInt(valeurs.readLine());
			} catch(NumberFormatException nfe) {
				System.err.println("Invalid Number");
			}
			if (n>0) {
				//long timeNow = cJed.now();
				//System.out.println ("Pair<" + timeNow + "," + n +  ">  min=" + (timeNow / 60000) );
				cJed.store (n);
			} else if (n==0) {
				System.out.println("Average = " + cJed.mean());
			}

		}
		System.out.println("Bye :-)!!!");	}
}

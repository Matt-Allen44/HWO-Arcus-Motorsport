package noobbot;

public class Calculator {
	public int ticktostop(double t, double i){
		double eff = 0.98;
    	double targ = t;
    	double ini = i;
    	double val = ini;
    	int ind = 0;
    	
    	while(val > targ){
    		if(val > targ){
    			ind++;
    			val = val*eff;
    		} 
    	}
    	return ind;
	}
	public double avgPPTtoStop(double t, double i){
		double eff = 0.98;
    	double targ = t;
    	double ini = i;
    	double val = ini;
    	double tot = 0;
    	int ind = 0;
    	
    	while(val > targ){
    		if(val > targ){
    			ind++;
    			val = val*eff;
    			tot = tot + val;
    		} 
    	}
    	return (double) (tot/ind);
		}
}

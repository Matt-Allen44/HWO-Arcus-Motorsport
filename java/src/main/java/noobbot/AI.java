package noobbot;

public class AI {
	public boolean isOverdrift(double angle, double spt, double slipmax, double angmax){
		boolean b = false;
		int errCode = 0;
		
			//SMART IDEA: Make AngMax relative to current slipmax -- allowing car to push slowly towards 55 degrees
		
			 if(angle > angmax && spt > slipmax/2){
	         	b = true;
	         	errCode = 1;
	         }
	         if(angle < -angmax  && spt > -slipmax/2){
	         	b = true; 
	         	errCode = 1;
	         }
	         
	         if(angle > 55 && spt > 0){
	        	 b = true;
	         }
	         
	         if(angle > 0){
	         	if(spt > slipmax){
	         		b = true;
	         		errCode = 2;
	         	}
	         }
	         
	         if(angle < -0){
	         	if(spt < -slipmax){
	         		b = true;
	         		errCode = 2;
	         		}
	      	}
	        if(b){
	        	//System.out.println("[REACTIVE HANDLING] THROTTLE MODIFIED AFTER ERROR CODE " + errCode);
	        } 	
	        
		return b;
	}
	
	public boolean slowForCorner(int carplace, double speed, double sp1, double sp2, double sp3){
		boolean b = false;
		int errCode = 0;
		
		for(int i = 1; i < 4; i++){ 
			double targ_speed, mult = 0;
			switch(i){
			case 1:
				mult  = sp1;
			break;
			case 2:
				mult = sp2;
			break;
			case 3:
				mult = sp3;
			break;
			}
			
			targ_speed = (double) 10 - (7*SysMem.track[carplace+i][3]);	
			if(speed > mult*targ_speed){
				b = true;
				errCode = i;
			}

	
		}
		if(b){
		//	System.out.println("[REALTIME CORNERING] THROTTLE MODIFIED AFTER ERROR CODE " + errCode);
		}
		
		
		
		return b;
	}
	
	public boolean isOverspeed(){
		return false;	
	}
}

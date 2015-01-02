package noobbot;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Main {

	public static PrintWriter writer;
	private static boolean custjoin = false;
	
	
    public static void main(String... args) throws IOException {
    	
    	if(args.length > 0){
    		
    		/*
    		 * Code for setting the connection details in a CMD enviroment
    		 */
    		System.out.println("Joining with ARGS");
    		SysMem.host = args[0];
    		SysMem.port = Integer.parseInt(args[1]);
    		SysMem.botName = args[2];
    		SysMem.botKey = args[3];
    	} else {
    		
    		/*
    		 * Custom join code, allows configuration from one of the IED
    		 * TODO: Refactor into a configuration file
    		 */
    		
    		custjoin = true;
    		System.out.println("Custom Joining");
            SysMem.host = "testserver.helloworldopen.com";
            SysMem.port = 8091;
            SysMem.botName = "Arcus";
            SysMem.botKey = "nbjWZGtA22XYiA";
    	}
            
        System.out.println("Connecting to " + SysMem.host + ":" + SysMem.port + " as " + SysMem.botName + "/" + SysMem.botKey);
        
        
        //Opening the connection to HWO servers
        final Socket socket = new Socket(SysMem.host, SysMem.port);
        
        //Getting IO streams for the given SOCKET
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
        final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
        
        if(custjoin){
        	
            /*
             * Code for the BOT to joinw hen run without a argument-less,
             * enviroment, can be customized from within the IDE
             */
        	
	          Frame f = new Frame();
	          f.drawFrame();
	        	
	          JsonObject message = new JsonObject();
		      message.addProperty("msgType", "createRace");
		      	
		      JsonObject data = new JsonObject();
		      JsonObject id = new JsonObject();
		      
		      Random r = new Random();
		      id.addProperty("name", SysMem.botName + r.nextInt(1000));
		      id.addProperty("key", SysMem.botKey);
		      
		      data.add("botId", id);
		      data.addProperty("trackName", SysMem.racetrack);
		      data.addProperty("password", "arcus9321");
		      data.addProperty("carCount", 1	);
		      
		      message.add("data", data);
		    
		      System.out.println(message.toString());
		      Main.writer.println(message.toString());
		      Main.writer.flush(); 
	      
        } else {
        	  JsonObject message = new JsonObject();
		      message.addProperty("msgType", "join");
		      JsonObject id = new JsonObject();
		      
		      Random r = new Random();
		      id.addProperty("name", SysMem.botName);
		      id.addProperty("key", SysMem.botKey);
		     
		      message.add("data", id);
			    
		      System.out.println(message.toString());
		      Main.writer.println(message.toString());
		      Main.writer.flush(); 
        }
        new Main(reader, writer);     
    }
    	
    	final Gson gson = new Gson();
    
	    //Opening the PrintStream for printing to a log file.
	    private static FileOutputStream fos;
		private static PrintStream log;	
    
    public Main(final BufferedReader reader, final PrintWriter writer) throws IOException {
	      
    	
    	try {
	    	
		    	double delta = System.currentTimeMillis();
		    	double deltapiece = 0;
		    	double deltaspeed = 0;
		    	
		    	this.writer = writer;
		        String line = null;
		
		        DateFormat df = new SimpleDateFormat("dd-MM-hh-mm-ss"); 
		        Date date = new Date();
		     
				fos = new FileOutputStream(df.format(date) + ".arcus", true);
			
			if(custjoin){
				log = new PrintStream(fos);	
				System.out.println("Set LOG");
			} else {
				log = System.out;
				System.out.println("Set LOG to CMD");
			} 
			
			double angle, deltaangle = 0;
			
			boolean org = true;
			double deltatick, calctime;	
	
		      //////////////////////////
		      // DECLERATIONS
		      /////////////////////////
		      AI bot = new AI();
		      
		        System.out.println("Waiting to Read");
	        while((line = reader.readLine()) != null) {
	            final MsgWrapper msgFromServer = gson.fromJson(line, MsgWrapper.class);
	            
	            if (msgFromServer.msgType.equals("carPositions")) {    
	            	
	            	
	            	SysMem.t = 1;
	                SysMem.tick++; 
	                // CAR POSITION
	                JsonParser jp = new JsonParser();
	                JsonObject msg = jp.parse(line).getAsJsonObject();
	                JsonElement indata = msg.get("data");
	            	JsonObject job = jp.parse(line).getAsJsonObject();
	            	int botId = 0;
	            	
	            	for(int i = 0; i < indata.getAsJsonArray().size(); i++){
	            		if(indata.getAsJsonArray().get(i).getAsJsonObject().get("id").getAsJsonObject().get("name").toString().replaceAll("\"", "").toLowerCase().trim().equals(SysMem.botName.toLowerCase().trim())){
	            			System.out.println("BOTID " + i);
	            			botId = i;
	            		}
	            	}
	            	
	            	double inpiece = Double.parseDouble(indata.getAsJsonArray().get(botId).getAsJsonObject().get("piecePosition").getAsJsonObject().get("inPieceDistance").toString());               
	            	
	            	angle = Double.parseDouble(indata.getAsJsonArray().get(botId).getAsJsonObject().get("angle").toString());    	
	            	SysMem.angle = angle;
	            	
	            	
	            	
	            	int carplace = Integer.parseInt(indata.getAsJsonArray().get(botId).getAsJsonObject().get("piecePosition").getAsJsonObject().get("pieceIndex").toString());       
	                SysMem.carplace = carplace;
	            	
	            	double ppt = inpiece-deltapiece;
	            	deltapiece = inpiece;
	                Double spt = angle-deltaangle;
	                
	                SysMem.spt = spt;
	                SysMem.ppt = ppt;
	              
	                //CONFIG
	                /*
	                 * Various setting to configure how aggresive the vehicle is.
	                 */
	                
	                	
	                //REALTIME RACING SPEED
	                /*
	                 * Calculate the correct speed for entering a corner and is also responsible for slowing after straights.
	                 */
	                	//93
	                	if(bot.slowForCorner(carplace, ppt, 1, 1.2, 1.6)){
	                		SysMem.t = 0;
	                	}
	                	
	                //REACTIVE CORNER UPDATING
		            /*
		             * @slipmax - Maxium increase in vehicle angle per tick, if exceeded throttle will be cut.
		             * @angmax - Maxium angle that may be reached befere throttle is cut.
		             */


	                	
		               if(bot.isOverdrift(angle, spt, SysMem.slipmax, SysMem.angmax)){
		            	  SysMem.t = 0;
		               }
		               
		            //////////////////////////
		            // SHORTEST ROUTE 
			           /*
			            * > Check if a switch piece is in the next 3 infront of you
			            * > If so continue to check till the next corner
			            * > Get angle and set switch to that direction           
			            */
		            	
		            	for(int i = 0; i < 5; i ++){
		            		if(carplace+i < SysMem.track.length){
			            		if(!SysMem.hasSwitched[carplace+i]){
			            			SysMem.hasSwitched[carplace+i] = true;
				            		if(SysMem.track[carplace+i][1] != 0){
				            			if(SysMem.track[carplace+i][1] > 0){
				            				send(new Switch("Right"));
				            				break;
				            			} else {
				            				send(new Switch("Left"));
				            				break;
				            			}
				            		}
				            	}
			            	}
		            	}
		            /////////////////////////
	
		            ////////////////////////
		            // AUTO TURBO
		            	
		            	if(SysMem.turbo){
		            		if(SysMem.track[carplace+2][1] == 0 && SysMem.track[carplace+3][1] == 0 && SysMem.track[carplace+4][1] == 0 ){
		            			System.out.println("TURBO ACTIVE");
		            			SysMem.turbo = false;
		            			send(new Turbo());
		            		}
		            	}
		            	
		            //////////////////////
	                send(new Throttle(SysMem.t));
	                log.flush();
	                //    System.out.println("Tick: " + SysMem.tick + " | PIECE" + carplace + " | PPT" + ppt  + "| SPT" + spt + " | A" + angle + " | CORNA" + SysMem.track[carplace][1] );
	                deltaangle = angle;
	                
	                /////////////////////
	                //LOG DATA 
	                
	                log.print("\n" + SysMem.tick + " | ");
	                log.print("THRO: " + SysMem.t);
	                log.print("| TUR: " + SysMem.turbo);
	                log.print("| POS: " + SysMem.carplace);
	                log.print(" | PPT: " + ppt);
	                log.print(" | TARG: " + (10 - (7*SysMem.track[carplace][3])));
	                log.print(" | ANG: " + SysMem.angle);
	                log.print(" | SPT: " + SysMem.spt);
	                
	                ////////////////////

            } else if (msgFromServer.msgType.equals("join")) {
            	 // Message received when you join a race
            	
            } else if (msgFromServer.msgType.equals("gameInit")) {
            	// Message received when the race is loading, gives track data to player
            	
            	JsonParser jp = new JsonParser();
            	JsonObject job = jp.parse(line).getAsJsonObject();

            	JsonArray trackdata = job.get("data").getAsJsonObject().get("race").getAsJsonObject().get("track").getAsJsonObject().get("pieces").getAsJsonArray();
                
            	SysMem.track = new double[trackdata.size()+4][4]; //making an array to store the shit\
            	SysMem.hasSwitched = new boolean[SysMem.track.length+10];   	
            	
            	for(int i = 0; i < SysMem.track.length-4; i++){
            		//Init it with a big number so we can use ZERO, without a risk
            		SysMem.track[i][0] = 99;
            		
        			String td = trackdata.get(i).toString().replaceAll("[{}=]", " ");	
        			String[] tda = td.split(",");
        			
        			tda[0].replace("\"", "//");
        	        			
        			String[] cornerarray  = trackdata.get(i).toString().split(",");
        			double cornerangle = 0;
        			double radius = 0;
        			
        			if(cornerarray.length > 1){
        				try {
        					cornerangle = Double.parseDouble(cornerarray[1].split(":")[1].split("\"")[0].replaceAll("}", ""));				
        					radius = Double.parseDouble(cornerarray[0].split(":")[1].split("\"")[0].replaceAll("}", ""));
        				} catch(NumberFormatException e){e.printStackTrace();}
        			}
        			
        			
        		if(i > 0){
	            	if(trackdata.get(i).toString().split(":")[0].equals("{\"length\"")){
	            			SysMem.track[i][0] = 0; //0 for straight
	            		} else {
	            			SysMem.track[i][0] = 1;
	            			SysMem.track[i][1] = cornerangle;
	            			SysMem.track[i][2] = radius;
	            			
	            			
	            			double sharp = cornerangle/radius;
	            			
	            			if(sharp < 0){
	            				sharp = sharp*-1;
	            			}
	            			
	            			SysMem.track[i][3] = sharp;
	            			}
            			}
            	}
            	
            	for(int i = 0; i < 4; i++){
	            	SysMem.track[trackdata.size()+i][0] = SysMem.track[0][0];
	            	SysMem.track[trackdata.size()+i][1] = SysMem.track[0][1];
	            	SysMem.track[trackdata.size()+i][2] = SysMem.track[0][2];
	            	SysMem.track[trackdata.size()+i][3] = SysMem.track[0][3];
            	}
            
            } else if (msgFromServer.msgType.equals("lapFinished")){
            	System.out.println("[NOTIFICATION] Completed Lap");
            	SysMem.lap++;
            	
            	for(int i = 0; i < SysMem.hasSwitched.length; i++){
            			SysMem.hasSwitched[i] = false;	
            	}
            
            	// Message received when game has finished - Bot will exit
            } else if (msgFromServer.msgType.equals("gameEnd")) {
            	date = new Date();	
            	log.flush();        	
            	System.out.println("TICKS: " + SysMem.tick);
           		System.out.println("Completed in " + (System.currentTimeMillis()-delta)/1000 + "s");
           		
           		send(new Ping());
            } else if (msgFromServer.msgType.equals("gameStart")) {
            	date = new Date();
            	System.out.println(df.format(date) + " | ARCUS MOTORSPORT BOT STARTED");
           		send(new Ping());
            } else if (msgFromServer.msgType.equals("crash")) {
            	System.out.println("\nWarning vehicle has derailed");
            	SysMem.spt -= 0.2;
            	System.out.println("NEWSLIP:" + SysMem.slipmax);
                log.println("\nVehicle Crash @ " + SysMem.carplace);
           		send(new Ping());
            } else if (msgFromServer.msgType.equals("turboAvailable")){
            	System.out.println("Turbo is available: " + msgFromServer.data.toString());
            	SysMem.turbo = true;
           		send(new Ping());
            } else if (msgFromServer.msgType.equals("tournamentEnd")){ 
            	System.exit(0);
            } else {
                send(new Ping());
            	} 	
	        }
    	} catch(Exception e){ e.printStackTrace();}
    }

    private void send(final SendMsg msg) {
        writer.println(msg.toJson());
        writer.flush();
    }
}

abstract class SendMsg {
    public String toJson() {
        return new Gson().toJson(new MsgWrapper(this));
    }

    protected Object msgData() {
        return this;
    }

    protected abstract String msgType();
}

class MsgWrapper {
    public final String msgType;
    public final Object data;
    

    MsgWrapper(final String msgType, final Object data) {
    	
    	this.msgType = msgType;
        this.data = data;
    }

    public MsgWrapper(final SendMsg sendMsg) {
        this(sendMsg.msgType(), sendMsg.msgData());
    }
}

class Join extends SendMsg {
    public final String name;
    public final String key;

    Join(final String name, final String key) {
    	this.name = name;
    	this.key = key;
    }

    @Override
    protected String msgType() {
        return "join+";
    }
}

class Ping extends SendMsg {
    @Override
    protected String msgType() {
        return "ping";
    }
}

class Throttle extends SendMsg {
    private double value;

    public Throttle(double value) {
        this.value = value;
    }

    @Override
    protected Object msgData() {
        return value;
    }

    @Override
    protected String msgType() {
        return "throttle";
    }
}
class Switch extends SendMsg {
	private String side;

	public Switch(String s){
		side = s;
	}
	@Override
    protected Object msgData() {
        return side;
    }
    
	@Override
	protected String msgType() {
		return "switchLane";
	}
	
}
class Turbo extends SendMsg {
	@Override
	protected String msgType(){
		return "turbo";
	}
	
	@Override
    protected Object msgData() {
        return "";
    }
}
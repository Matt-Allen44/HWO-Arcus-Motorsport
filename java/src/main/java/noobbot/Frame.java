package noobbot;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel {
	
	//////////////////
	/////COLOUR PALETE
	
	Color darkGray = new Color(50, 50, 50);
	Font f = new Font("arial", Font.BOLD, 24);
	Font sFont = new Font("arial", Font.PLAIN, 16);	
	/////////////////
	JFrame jf = new JFrame();
	
	public void drawFrame(){
		jf.setSize(400, 300);
		jf.setResizable(false);
		jf.setVisible(false);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(jf.EXIT_ON_CLOSE);
		jf.add(this);
		jf.setVisible(true);
	}
	
	public void paint(Graphics g){
	super.paint(g);
	
		g.setColor(darkGray);
		g.fillRect(0, 0, 700, 400);
		
		g.setColor(Color.white);
		
		g.setFont(f);
		g.drawString("ARCUS MOTORSPORT ", 10, 34);
		
		g.setFont(sFont);
		g.drawString("throt: " + SysMem.t + " @ " + SysMem.carplace, 10, 50);
		g.drawString("pp/t: " + SysMem.ppt, 10, 70);
		g.drawString("sp/t: " + SysMem.spt, 10, 90);
		
		g.drawString("Angle: "+ SysMem.angle , 10, 110);
		
		if(SysMem.tick > 5){
			g.drawString("Sharp: " + SysMem.track[SysMem.carplace][3], 10, 130);
		}
		
		if(SysMem.angle > 45){
			g.setColor(Color.RED);
			g.fillRect(10, 145, 250, 30);
			
			g.setColor(Color.WHITE);
			g.drawString("WARNING: OVERDRIFT", 20, 167);
		}
		
		if(SysMem.turbo){
			g.setColor(Color.GREEN);
			g.fillRect(10, 195, 250, 30);
			
			g.setColor(Color.WHITE);
			g.drawString("TURBO AVAILIBLE", 40, 215);
		}
		
		try {
			Thread.sleep(100);
			jf.setTitle("Lap: " + SysMem.lap + "  |  Tick:"  + SysMem.tick);
		} catch (InterruptedException e) {e.printStackTrace();}
		
	repaint();
	}
}

package ui.panels;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ui.ApplicationWindow;

/**
 * Panel containing background image and fixed dimensions for the settings menu
 * @author ItsNotAGoodTime
 *
 */
public class SettingsMenuBackground extends JPanel{
	
	//Serialize
	private static final long serialVersionUID = 1L;
	//Background image
	private Image backgroundImage; 

	/**
	 * Loads background image from file and sets panels layout
	 */
	public SettingsMenuBackground(){
		setLayout(null);
		setOpaque(false);
		try{
			backgroundImage = ImageIO.read(ApplicationWindow.class.getResource("images/gui/settingsMenu.png"));
		}catch(IOException e){
			System.out.println(e.getLocalizedMessage());
		}
		setBounds(0,0,650,300);
	}
	
	/**
	 * Repaints this panel using the background image
	 */
	public void paintComponent(Graphics g) { 
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, null);
	}
}

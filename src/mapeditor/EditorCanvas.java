package mapeditor;

import gameworld.Game;
import gameworld.location.InsideLocation;
import gameworld.location.Location;
import gameworld.location.OutsideLocation;
import gameworld.tile.EntranceExitTile;
import gameworld.tile.FloorTile;
import gameworld.tile.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import ui.ImageStorage;
import ui.RenderingWindow;


public class EditorCanvas extends JPanel {

	private Location location;
	private String view = "Shit";

	private int MAPHEIGHT = 15;
	private int MAPWIDTH = 15;
	private int TILESIZE = 64;
	private int cameraX = 0;
	private int cameraY = 0;

	Game.Direction direction = Game.Direction.NORTH;

	/**
	 * Create the panel.
	 */
	public EditorCanvas(Location location) {
		setLayout(null);
		setBounds(0,0,2000,750);

		if(location instanceof OutsideLocation){
			location = (OutsideLocation) location;
		} else {
			location = (InsideLocation) location;
		}
		this.location = location;
	}

	
	/**
	 * Returns image to match given name
	 * @param name - type of image wanted
	 * @return image based on name
	 */
	public static Image getImage(String name){
		switch(name){
		case "Gr":
			return ImageStorage.grass;
		case "Ro":
			return ImageStorage.rock;
		case "Wa":
			return ImageStorage.water;
		case "Bu":
			return ImageStorage.building;
		case "En":
			return ImageStorage.doorLR;
		}
		return null;
	}

	public void paint(Graphics g){
		Tile[][] tiles = location.getTiles();
		Tile[][] rooms = null;

		if(location instanceof OutsideLocation){
			OutsideLocation l = (OutsideLocation) location;
			rooms = l.getBuildingTiles();
		}

		Image offscreen = createImage(MAPWIDTH*TILESIZE, MAPHEIGHT*TILESIZE);
		Graphics offgc = offscreen.getGraphics();

//		for(int i = 0; i < tiles.length; i++){
//			for(int j = 0; j <tiles.length ; j++){
//				if(tiles[i][j]!=null){
//					System.out.print(tiles[i][j].containedEntity()+" ");
//				} else{
//					System.out.print("null ");
//				}
//			}
//			System.out.println("");
//		}
		
		switch(direction){
		case NORTH:
			if(view.equals("Render")){
				isometric(tiles,rooms, offgc);
			} else {
				shittyDraw(tiles,rooms,offgc);
			}
			break;
		case EAST:
			if(view.equals("Render")){
				isometric(rotate(tiles), rotate(rooms), offgc);
			} else {
				shittyDraw(rotate(tiles), rotate(rooms), offgc);
			}
			break;
		case SOUTH:
			if(view.equals("Render")){
				isometric(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);
			} else {
				shittyDraw(rotate(rotate(tiles)), rotate(rotate(rooms)), offgc);
			}
			break;
		case WEST:
			if(view.equals("Render")){
				isometric(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);
			} else {
				shittyDraw(rotate(rotate(rotate(tiles))), rotate(rotate(rotate(rooms))), offgc);
			}
			break;

		}

		g.drawImage(offscreen,0,0,null);
	}



	/**
	 * Iterates through arrays of tiles drawing the map terrain, then iterates through
	 * room tiles to draw buildings depending on the arrangement of tiles.
	 *
	 * new x position = x/2 + y/2 ( + or - constants to fit properly such as imageHeight)
	 * new y position = y/4 - x/4 ( + or - constants to fit properly such as imageHeight)
	 *
	 *
	 * @param tiles - 2D array of terrain to be drawn
	 * @param rooms - 2D array of rooms to be drawn
	 * @param g - the graphics
	 */
	public void isometric(Tile[][] tiles, Tile[][] rooms, Graphics g){
		g.fillRect(0,0,this.getWidth(), this.getHeight());

		// outside tiles
		for(int i = 0; i < tiles.length; i++){
			for(int j = tiles[i].length-1; j >=0 ; j--){
				FloorTile t = (FloorTile) tiles[i][j];
				Tile r = rooms[i][j];
				Image image = null;

				// DRAWING TERRAIN
				if(t!=null) {
					image = getImage(t.toString());
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2  - cameraY, null);
					
					// DRAWING ENTITY
					if(t.containedEntity()!=null){
						image = RenderingWindow.getImage(t.containedEntity().name());
						g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY - image.getHeight(null)/2, null);
					}

				}

				// DRAWING ROOMS
				if(r!=null) {
					// Drawing 2 block high walls
					if(r instanceof EntranceExitTile){
						if(j-1 >= 0 && rooms[i][j-1]==null){
							g.drawImage(ImageStorage.doorUD, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
						} else {
							g.drawImage(ImageStorage.doorLR, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
						}
					}
					else{
							g.drawImage(ImageStorage.building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE/2 - cameraY, null);
					}

					// wall block on top of wall / door for 2 high building.
					g.drawImage(ImageStorage.building, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, ((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 -TILESIZE - cameraY, null);

					// default img for non edge and corner roofs
					image = ImageStorage.building;

					// Western most point of building
					if(j-1 >= 0 && rooms[i][j-1] == null){
						image = ImageStorage.roofUD;
					}

					// Northern most point of building
					if(i+1 < rooms.length && rooms[i+1][j]==null){
						image = ImageStorage.roofLR;
					}
					// Outwards corner roof
					if(j-1 >= 0 && i+1<rooms.length && rooms[i][j-1]==null && rooms[i+1][j]==null){
						image = ImageStorage.roofCornerO;
					}
					// Inwards corner roof
					if(j-1 >= 0 && i+1 != rooms.length && rooms[i+1][j-1]==null && rooms[i][j-1] != null && rooms[i+1][j]!=null){
						image = ImageStorage.roofCornerI;
					}
					g.drawImage(image, (j*TILESIZE/2) + (i*TILESIZE/2) - cameraX, (int) (((i*TILESIZE/4)-(j*TILESIZE/4)) + this.getHeight()/2 - (TILESIZE*1.5)) - cameraY, null);
				}

			}

		}
	}

	/**
	 * Draws the map from a BOV for making maps because I couldn't get clicking on positions working properly. :~(
	 * @param tiles - array of location tiles
	 * @param rooms - array of location buildingtiles
	 * @param offgc - graphics to draw to
	 */
	public void shittyDraw(Tile[][] tiles, Tile[][] rooms, Graphics offgc){
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				FloorTile t = (FloorTile) tiles[i][j];
				Tile r = rooms[i][j];
				if(t!=null){
					Image image = getImage(t.toString());
					offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY, null);
					
					// DRAWING ENTITY
					if(t.containedEntity()!=null){
						System.out.println("DRAWING IMG");
						image = RenderingWindow.getImage(t.containedEntity().name());
						System.out.println(image);
						offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY - TILESIZE/2, null);
					}
				}
				if(r!=null){
					Image image = ImageStorage.building;
					offgc.drawImage(image, j*TILESIZE - cameraX, i*TILESIZE-image.getHeight(null)+TILESIZE - cameraY, null);
				} if(t==null && r==null) {
					offgc.setColor(Color.WHITE);
					offgc.fillRect(j*TILESIZE - cameraX, i*TILESIZE - cameraY, TILESIZE, TILESIZE);
					offgc.setColor(Color.BLACK);
					offgc.drawRect(j*TILESIZE - cameraX, i*TILESIZE - cameraY, TILESIZE, TILESIZE);
				}
			}
		}

	}

	public Tile[][] rotate(Tile[][] tiles){
		Tile[][] newTiles = new Tile[tiles.length][tiles[0].length];
		// drawing floor
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				newTiles[(newTiles.length-1)-j][i] = tiles[i][j];
			}
		}
		return newTiles;
	}



	public void setDirection(Game.Direction d){
		direction = d;
	}

	public void setView(String string) {
		view = string;

	}
	
	public int getCameraX() {
		return cameraX;
	}

	public void setCameraX(int cameraX) {
		this.cameraX = cameraX;
	}

	public int getCameraY() {
		return cameraY;
	}

	public void setCameraY(int cameraY) {
		this.cameraY = cameraY;
	}


}

package gameworld.tile;

import gameworld.Player;
import gameworld.entity.Entity;

import java.awt.Point;
import java.io.Serializable;

/**
 * A Tile represents a square in a location.
 * A tile may contain a Player or an Entity
 * @author Jasen
 *
 */
public abstract class Tile implements Serializable{

	private static final long serialVersionUID = 1L;
	private Point position;
	private Entity entity;
	private Player player;
	protected String name;
	private boolean passable;

	public Tile(String name, Point pos, boolean passable) {
		this.name = name;
		this.position = pos;
		this.setPassable(passable);
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Point getPosition() {
		return position;
	}

	public Entity containedEntity() {
		return entity;
	}

	public void setEntitiy(Entity e){
		this.entity = e;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String toString(){
		return name.substring(0,2);
	}

	public String getName() {
		return name;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}
}

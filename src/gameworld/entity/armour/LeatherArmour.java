package gameworld.entity.armour;

import gameworld.entity.Item;
import gameworld.location.Location;

import java.awt.Point;

public class LeatherArmour extends Armour {
	
	private static final int armourRating = 5;

	private static final long serialVersionUID = -632832829772054450L;

	public LeatherArmour(String name, String description, Point position,
			Location location) {
		super(name, description, position, location);
		setArmourRating(armourRating);
	}
	
	private LeatherArmour(LeatherArmour armour) {
		super(armour.name, armour.description, armour.position, armour.location);
		setArmourRating(armourRating);
	}

	@Override
	public Item clone() {
		return new LeatherArmour(this);
	}
	
}
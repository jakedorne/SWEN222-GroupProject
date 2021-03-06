package network;

import gameworld.Game;
import gameworld.Player;
import gameworld.entity.Container;
import gameworld.entity.Item;

import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * Network Events are used for transferring data through the socket input/output streams.
 *
 * Network Events can be of various types that hold different types of data therefore
 * the EventType must be checked before calling any of the getters as they will be null if the type is incorrect.
 * @author Matt Byers
 *
 */
public class NetworkEvent implements Serializable {

	private static final long serialVersionUID = 3692933671294745809L;

	//The type of event that occurred.
	private final EventType type;

	//KeyCode of the pressed key, -1 if this is not a KEY_PRESS event.
	private int keyCode = -1;

	//Message, null if not a MESSAGE event.
	private String message = null;

	//Username of the client that created the NetworkEvent.
	private final String user;

	//The direction the client should move the player in.
	private Game.Direction dir = null;

	//Game state used for broadcasting a gui update to clients.
	private Player state = null;

	//The item that needs to be added or set on this player.
	private Item item = null;

	//The starting index of an item in the inventory to be swapped.
	private int swapIndex1 = -1;

	//The destination index of an inventory item to be swapped, stays -1 if to be removed.
	private int swapIndex2 = -1;

	private Container container = null;


	/**
	 * Creates a network event of the KEY_PRESS type.
	 * @param e - The KeyEvent associated with the event.
	 * @param user - The user that created the event.
	 */
	public NetworkEvent(KeyEvent e, String user){
		this.type = EventType.KEY_PRESS;
		this.keyCode = e.getKeyCode();
		this.user = user;
	}

	/**
	 * Creates a network event of the type MESSAGE.
	 * @param msg - The message.
	 * @param user - The user that created the event.
	 */
	public NetworkEvent(String msg, String user){
		this.type = EventType.MESSAGE;
		this.message = msg;
		this.user = user;
	}
	
	/**
	 * Signals a client game state update.
	 * @param state - The state of the game.
	 * @param type - The type of event.
	 */
	public NetworkEvent(Player state, EventType type){
		this.type = type;
		this.user = "Server";
		this.state = state;
	}

	/**
	 * User events of this type don't require any extra parameters, therefore need to be given a specific type.
	 * @param user - The user of the event.
	 * @param type - The event type.
	 */
	public NetworkEvent(String user, EventType type){
		this.type = type;
		this.user = user;
	}

	/**
	 * Server Event, that tells the client to move this user on their local copy of Player, 
	 * in the given direction.
	 * @param user - User to move.
	 * @param dir - Direction to move them.
	 */
	public NetworkEvent(String user, Game.Direction dir){
		this.type = EventType.MOVE_PLAYER;
		this.user = user;
		this.dir = dir;
	}

	/**
	 * User event, can be multiple types, gives an item. 
	 * Therefore addItem is on case this would be called.
	 * @param user - The user to update.
	 * @param type - The type of NetworkEvent.
	 * @param item - The item to be stored.
	 */
	public NetworkEvent (String user, EventType type, Item item){
		this.user = user;
		this.type = type;
		this.item = item;
	}

	/**
	 * User Event, storing two indexes but a unspecified event type,
	 * used for swapping items normally.
	 * @param user - The user to update.
	 * @param type - The type of NetworkEvent.
	 * @param index1 - Start index.
	 * @param index2 - End index.
	 */
	public NetworkEvent (String user, EventType type, int index1, int index2){
		this.user  = user;
		this.type = type;
		this.swapIndex1 = index1;
		this.swapIndex2 = index2;
	}

	/**
	 * Server event, tells the specified client to display the given container.
	 * @param user - The user who is opening the container
	 * @param container - The container itself.
	 */
	public NetworkEvent(String user, Container container){
		this.user = user;
		this.type = EventType.DISPLAY_CONTAINER;
		this.container = container;
	}

	/**
	 * User event of varying type, stores and index and a container.
	 * Can be used for removing items from a container.
	 * @param user - The user who created the event
	 * @param type - The type of NetworkEvent
	 * @param index - The index.
	 * @param container - The container to be edited.
	 */
	public NetworkEvent(String user, EventType type , int index, Container container){
		this.user = user;
		this.type = type;
		this.container = container;
		this.swapIndex1 = index;
	}
	
	/**
	 * User event of varying type, can be used for adding items to containers.
	 * @param user - The user who created the event.
	 * @param type - The type of NetworkEvent
	 * @param item - The item.
	 * @param container - The container to be modified.
	 */
	public NetworkEvent(String user, EventType type, Item item, Container container){
		this.user = user;
		this.type = type;
		this.item = item;
		this.container = container;
	}
	

	//Getters
	public String getUser() { return user; }
	public EventType getType() { return type; }
	public int getKeyCode() { return keyCode; }
	public String getMessage() { return message; }
	public Player getState() { return state; }
	public Game.Direction getDir() { return dir; }
	public Item getItem() { return item; }
	public int getIndex1() { return swapIndex1; }
	public int getIndex2() { return swapIndex2; }
	public Container getContainer() { return container; }

	//All possible types of NetworkEvents.
	public enum EventType{
		KEY_PRESS,
		MESSAGE,
		UPDATE_GAME,
		UPDATE_INVENT,
		CYCLE_ANIMATIONS,
		MOVE_PLAYER,
		ADD_ITEM,
		SET_WEAPON,
		SET_ARMOUR,
		REMOVE_ITEM,
		SWAP_ITEM,
		DISPLAY_CONTAINER,
		CLOSE,
		REMOVE_ITEM_CONTAINER,
		USE_ITEM,
		ADD_ITEM_CONTAINER,
		DROP_ITEM;
	}

}

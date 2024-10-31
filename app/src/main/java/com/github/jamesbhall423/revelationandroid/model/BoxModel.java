package com.github.jamesbhall423.revelationandroid.model;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


import com.github.jamesbhall423.revelationandroid.action.*;
import static com.github.jamesbhall423.revelationandroid.model.CAction.LN;


import static com.github.jamesbhall423.revelationandroid.model.Pathfinder.constArray;
import static com.github.jamesbhall423.revelationandroid.model.Pathfinder.lineArray;

// All methods should be accessed on a single thread, namely the same thread as the view
public class BoxModel {
    public enum EndStatus {
        WIN, LOSS, BLOCKED, ONGOING, OTHER_LEFT
    }
	private int declaringPlayer = -1;
	private PriorityQueue<CAction> queue = new PriorityQueue<CAction>();
	private Queue<CAction> delayed = new ArrayDeque<CAction>();

    private SquareClass[][] board;
    private Player[] players;
    private boolean[] blocked;
    private boolean side;
    private EndStatus endStatus = EndStatus.ONGOING;
    private boolean change = false;
	private int numVoids = 0;
    private boolean[] finiteDeclares;
	private int[] times;
	private int[] time;
	private int[] endTimes;
	private int curPlayer = 0;
	private int curTime = 0;
	private SquareModel roadLast = null;
    private int displayPlayer;
    private boolean responsive = true;
	private CBuffer buffer;
    private List<CAction> notifications = new ArrayList<>();
	private List<ModelMenuItem> menu;
    private BoxViewUpdater updater;
	private boolean flipDisplay = false;
	private ModelMenuItem declareVictory;
	private CMap map;
    public BoxModel(CMap map,int displayPlayer,CBuffer buffer, BoxViewUpdater updater) {
		loadCMap(map, displayPlayer, buffer);
        this.updater = updater;
    }
	private void loadCMap(CMap map,int displayPlayer,CBuffer buffer) {
    	this.map = map;
		flipDisplay = displayPlayer>0;
		board = map.createBoard(flipDisplay, displayPlayer);
        this.displayPlayer = displayPlayer;
        this.buffer = buffer;
        this.blocked = new boolean[map.players.length];
        this.finiteDeclares = new boolean[map.players.length];
        this.players = map.players;
		menu = ModelMenuItem.createMenu(map, displayPlayer, this);
		for (ModelMenuItem item: menu) if (item.getAction()==CAction.DECLARE) {
			declareVictory = item;
		}
        for (int i = 0; i < finiteDeclares.length; i++) {
            finiteDeclares[i] = players[displayPlayer].declareVictories>0;
        }
        this.side = map.players[displayPlayer].side;
		time = new int[map.players.length];
		times = new int[players.length];
		endTimes = new int[players.length];
		Arrays.fill(time,1);
		Arrays.fill(times,1);
		Arrays.fill(endTimes,1);
		curTime=1;
		notifications.add(statsNotification());
		updateDeclareResponsive();
	}
	private CAction statsNotification() {
        String message = "";
    	if (displayPlayer>=0) message = players[displayPlayer].message+LN;
    	message += "Stats:"+LN;
    	message+=playerStats(0);
    	message+=playerStats(1);
    	return new StartNotification(player(),message);
	}
	private String playerStats(int player) {
    	String out = "Player "+(player+1)+LN;
    	out += "Reverts: "+players[player].numReverts+LN;
    	out += "Scans: "+players[player].scans+LN;
    	out += "Declares: "+players[player].declareVictories+LN;
    	return out;
	}
	public CMap cmap() {
    	return map;
	}
	public List<CAction> notifications() {
		return notifications;
	}
	public boolean playerSide(int player) {
		return players[player].side;
	}
	public BoxModel(int width, int height, BoxViewUpdater updater) {
		createFromScratch(width, height);
		this.updater = updater;
	}
	private void createFromScratch(int width, int height) {
		board = new SquareClass[height][width];
		for (int y = 0; y < height; y++) for (int x = 0; x < width; x++) {
			board[y][x] = new SquareClass(x, y, new SquareState());
		}
		displayPlayer = -1;
		players = new Player[2];
	}
    public BoxModel(CMap map,int displayPlayer,CBuffer buffer) {
		loadCMap(map, displayPlayer, buffer);
    }
	public BoxModel(int width, int height) {
		createFromScratch(width, height);
	}
	public void registerUpdater(BoxViewUpdater updater) {
		this.updater = updater;
	}
    public void step() {
    	if (endStatus!=EndStatus.ONGOING) return;
		change=false;
		while (buffer.hasObjects()) {
			CAction message = (CAction)(buffer.getObject().message);
			if (message.getClass()==Exit.class) {
			    message = message.create(1-displayPlayer,time[1-displayPlayer]+1);
            }
			if (message.getStartTime()>time[message.player()]) time[message.player()]=message.getStartTime();
			queue.add(message);
		} 
		CAction action = queue.peek();
		int top = CAction.VAL_TURN;
		while (action!=null&&action.typeVal()<=top&&action.player()==curPlayer&&action.getStartTime()==curTime) {
			numVoids=0;
			queue.poll();
			if (action.typeVal()!=CAction.VAL_DELAYED) process(action);
			else {
				delayed.add(action);
			}
			if (action.typeVal()==CAction.VAL_TURN) {
				top=CAction.VAL_MAX;
				times[action.player()] = action.endTime();
			}
			action = queue.peek();
		}
		if (top==CAction.VAL_MAX) {
			if (curPlayer==player()) dumpDelayed();
			curPlayer++;
			if (curPlayer>=times.length) {
				curTime++;
				curPlayer=0;
			}
		}
		numVoids++;
		if ((numVoids<3)&&(endTimes[player()]<=curTime||endTimes[player()]<=curTime+1&&player()<=curPlayer)) {
			if (!change) {
				setResponsive(true);
			}
		}
    }
    public String defaultTitle() {
    	String miniTitle = "Turn "+endTimes[player()];
    	if (endStatus == EndStatus.WIN) return miniTitle+ " VICTORY";
    	else if (endStatus == EndStatus.LOSS) return miniTitle+" DEFEAT";
    	else if (endStatus == EndStatus.BLOCKED) return miniTitle+" DRAW";
    	else if (endStatus == EndStatus.OTHER_LEFT) return miniTitle+" Opponent left";
    	else if (!responsive()) return miniTitle+" (waiting)";
		else return miniTitle;
	}
	public String instructions() {
    	String objective = "Objective:"+LN;
    	objective+= "Connect the left to right sides with blue dots and declare victory."+LN;
    	objective+= "Prevent the top and bottom sides from being connected with purple dots"+LN;
    	objective+= "Two squares are connected when they square a side (diagonals do not count) or the same teleporter number (top-left blue square)"+LN;
    	objective+= "You win if - "+LN;
		objective+= " - You have connected the left and right sides of the board and declared victory"+LN;
		objective+= " - Your opponent runs out of declare victories"+LN;
		objective+= "You lose if"+LN;
		objective+= " - Your opponent has connected the top and bottom sides of the board and declared victory"+LN;
		objective+= " - You run out of declare victories"+LN;
		objective+= "The game draws if"+LN;
		objective+= " - Both sides know they cannot win by declaring victory"+LN;
		String actions = "Actions:"+LN;
		actions += "You have four actions: place, scan, revert and declare victory"+LN;
		actions += "Actions that do not end the game are secret"+LN;
		actions += "However, in addition to changing the board, actions give information about the board state"+LN;
		actions += "Some actions have a limited number of uses"+LN;
		actions += "Information about the number of uses for both players, and the history of your actions can be found in the notification log"+LN;
		actions += "Place - "+LN;
		actions += " - Fills the square with your token, if no token is present"+LN;
		actions += " - Reveals the true state of the underlying square"+LN;
		actions += " - Takes time given by the terrain of the square"+LN;
		actions += "Revert - "+LN;
		actions += " - Can only be placed in locations you have not placed or found a token"+LN;
		actions += " - If the opponent had a token in that spot, you now have a token in that spot, and the opponent has nothing"+LN;
		actions += " - If the square was empty, its still empty"+LN;
		actions += " - Reveals the true state of the underlying square"+LN;
		actions += " - Takes time given by the terrain of the square"+LN;
		actions += "Scan - "+LN;
		actions += " - Reveals the true state of a three by three grid surrounding the selected square"+LN;
		actions += " - Takes one turn"+LN;
		actions += "Declare Victory - "+LN;
		actions += " - Win if you have connected the left and right sides of the board"+LN;
		actions += " - If you run out of uses without winning, you lose"+LN;
		actions += " - Takes one turn"+LN;
		String terrain = "Terrain:"+LN;
		terrain += "Fields (Brown): 1 turn to place or revert"+LN;
		terrain += "Forest (Green) 2 turns to place or revert"+LN;
		terrain += "Roads (Yellow): place or revert 2 squares connected by roads in one turn"+LN;
		terrain += "Mountain (Gray): cannot be placed or reverted"+LN;
		return objective+actions+terrain;


	}
    private void dumpDelayed() {
        while (!delayed.isEmpty()) {
            CAction next = delayed.remove();
            process(next);
        }
		updateDeclareResponsive();
    }
    
	private void updateDeclareResponsive() {
		if (declareVictory.numLeft()>0) {
			Pathfinder finder = new Pathfinder(board,new SquareCondition() {
				public boolean conditionFulfilled(SquareModel square) {
					return (square.getView(player())==square.valueSide(side));
				}
			});
			declareVictory.setEnabled(findPath(finder, side));
		}
	}
	/**
	 * Method distribute
	 *
	 *
	 * @param action
	 *
	 */
	public void distribute(CAction action) {
		if (action.getClass()==DoTurn.class) {
            if (!finiteDeclares[displayPlayer]) distribute(CAction.DECLARE.create(displayPlayer,time[displayPlayer]));
			endTimes[player()]=action.endTime();
			time[action.player()]=action.endTime();
			numVoids=0;
			change=true;
			setResponsive(false);
		}
		buffer.sendObject(action,CBuffer.ECHO);
	}
    public int player() {
        return displayPlayer;
    }
    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
        updater.updateGlobal();
		for (ModelMenuItem item: menu) item.updateGraphics();
    }
    public boolean responsive() {
        return responsive;
    }
	private void process(final CAction action) {
		if (endStatus!=EndStatus.ONGOING) return;
        action.process(this);
		if (action.player()==player()&&action.typeVal()!=CAction.VAL_TURN||action.isPublic())  {
            addNotification(action);
        }
	}
    public void addNotification(final CAction action) {
        notifications.add(action);
        updater.updateGlobal();
    }
    public boolean testBlocked() {
		return testBlocked(player(),side)&&testBlocked(1-player(),!side);
	}
	private boolean testBlocked(final int player, final boolean side) {
		Pathfinder finder = new Pathfinder(board,new SquareCondition() {
			public boolean conditionFulfilled(SquareModel square) {
				return (square.getView(player)!=square.valueSide(!side));
			}
		});
		boolean out = !findPath(finder, side);
		return out;
	}
	public boolean findPath(Pathfinder finder, boolean side) {
		if (side) return finder.path(constArray(0,modelHeight()),lineArray(modelHeight()),constArray(modelWidth()-1,modelHeight()),lineArray(modelHeight()));
		else return finder.path(lineArray(modelWidth()),constArray(0,modelWidth()),lineArray(modelWidth()),constArray(modelHeight()-1,modelWidth()));
	}
	public Spot findPathSpot(Pathfinder finder, boolean side) {
		if (side) return finder.pathEndSpot(constArray(0,modelHeight()),lineArray(modelHeight()),constArray(modelWidth()-1,modelHeight()),lineArray(modelHeight()));
		else return finder.pathEndSpot(lineArray(modelWidth()),constArray(0,modelWidth()),lineArray(modelWidth()),constArray(modelHeight()-1,modelWidth()));
	}
	public SquareModel[][] boardModel() {
		return board;
	}
    public int modelWidth() {
        return board[0].length;
    }
    public int modelHeight() {
        return board.length;
    }
    public int displayWidth() {
		if (flipDisplay) return modelHeight();
        else return modelWidth();
    }
    public int displayHeight() {
		if (flipDisplay) return modelWidth();
        else return modelHeight();
    }
	public SquareClass getModelSquare(int x, int y) {
		return board[y][x];
	}
	public boolean onBoardModel(int x, int y) {
		return (x>=0&&y>=0&&y<board.length&&x<board[y].length);
	}
	public boolean onDisplayModel(int x, int y) {
		if (flipDisplay) return onBoardModel(y,x);
		else return onBoardModel(x,y);
	}
	public SquareClass getDisplaySquare(int x, int y) {
		if (flipDisplay) return board[x][y];
		else return board[y][x];
	}
	public List<ModelMenuItem> menu() {
		return menu;
	}
	public boolean side() {
		return side;
	}
	public int getTime() {
		return time[player()];
	}
    public boolean allBlocked() {
        boolean allBlocked = true;
		for (int i = 0; i < blocked.length; i++) allBlocked = (allBlocked&&blocked[i]);
		return allBlocked;
    }
	public int testVictory(int player) {
		declaringPlayer = player;
        final boolean side = players[player].side;
		Pathfinder finder = new Pathfinder(board,new SquareCondition() {
			public boolean conditionFulfilled(SquareModel square) {
				return square.isPlayer(side);
			}
		});
		boolean win = findPath(finder, side);
		if (win) {
            if (side==this.side) setEndStatus(EndStatus.WIN);
            else setEndStatus(EndStatus.LOSS);
			return 1;
		}
		else if (finiteDeclares[player]&&--players[player].declareVictories<=0) {
			if (side==this.side) setEndStatus(EndStatus.LOSS);
            else setEndStatus(EndStatus.WIN);
			return -1;
		} else return 0;
	}
    public EndStatus getEndStatus() {
        return endStatus;
    }
    public void setEndStatus(EndStatus status) {
        this.endStatus=status;
		updater.updateEndStatus();
		buffer.close();
		setResponsive(false);
        updater.updateGlobal();
		updater.updateAllSquares();
    }
    public void updateBlocked(int player) {
        blocked[player] = true;
    }
	public boolean topToBottom() {
		return side;
	}
	public boolean leftToRight() {
		return !side;
	}
	public boolean clickSquare(int x, int y, boolean revert) {
		if (!responsive) return false;
		SquareModel square = getModelSquare(x, y);
		int time = square.time();
		if (time==0) return false;
		if (revert&&(square.getView(player())!=0)) return false;
		if (roadLast!=null) {
			if (square.getHighlight()) {
				highlightRoads(false);
				distributeSquareAction(x,y,revert);
				distribute(DoTurn.TURN.create(displayPlayer, this.time[displayPlayer]));
				roadLast=null;
				return true;
			} else return false;
		} else if (time<0) {
			roadLast=square;
			highlightRoads(true);
			distributeSquareAction(x,y,revert);
			return true;
		} else {
			distributeSquareAction(x,y,revert);
			for (int i = 0; i < time; i++) distribute(DoTurn.TURN.create(displayPlayer, this.time[displayPlayer]));
			return true;
		}
		
	}
	private void distributeSquareAction(int x, int y, boolean revert) {
		distribute(new SquareAction(player(), x, y, revert, side, this.time[displayPlayer]));
		updater.updateGlobal();
	}
	private void highlightRoads(boolean highlight) {
		int road = roadLast.getRoad();
		try {
			if ((road&SquareModel.LEFT)>0) getModelSquare(roadLast.getX()-1,roadLast.getY()).setHighlight(highlight);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if ((road&SquareModel.RIGHT)>0) getModelSquare(roadLast.getX()+1,roadLast.getY()).setHighlight(highlight);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if ((road&SquareModel.UP)>0) getModelSquare(roadLast.getX(),roadLast.getY()-1).setHighlight(highlight);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		try {
			if ((road&SquareModel.DOWN)>0) getModelSquare(roadLast.getX(),roadLast.getY()+1).setHighlight(highlight);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}
	public int playerDeclareVictory() {
		return declaringPlayer;
	}
	public boolean flipDisplay(){
		return flipDisplay;
	}
	public Player getPlayerCMap() {
		return players[player()];
	}
}

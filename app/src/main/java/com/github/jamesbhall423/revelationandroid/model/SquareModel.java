package com.github.jamesbhall423.revelationandroid.model;

public interface SquareModel {

    int LEFT = 1;
    int RIGHT = 2;
    int UP = 4;
    int DOWN = 8;

    /**
     * Method isEmpty
     *
     *
     * @return
     *
     */
    boolean isEmpty();

    /**
     * Method isPlayer
     *
     *
     * @param player1
     *
     * @return
     *
     */
    boolean isPlayer(boolean player1);

    /**
     * Method isView
     *
     *
     * @param player1
     *
     * @return
     *
     */
    boolean isView(boolean player1);

    /**
     * Method player
     *
     *
     * @return
     *
     */
    int player();

    /**
     * Method setPlayer
     *
     *
     * @param player
     *
     */
    void setPlayer(boolean player);

    /**
     * Method getTeleporter
     *
     *
     * @return
     *
     */
    int[] getTeleporter();

    /**
     * Method setTeleporter
     *
     *
     * @param dest
     *
     */
    void setTeleporter(int[] dest);

    /**
     * Method time
     *
     *
     * @return
     *
     */
    int time();

    /**
     * Method canPlace
     *
     *
     * @param player
     *
     * @return
     *
     */
    boolean canPlace(boolean player, boolean revert);

    /**
     * Method getView
     *
     *
     * @return
     *
     */
    int getView();

    /**
     * Method setView
     *
     *
     * @param observed
     *
     */
    void setView(int observed);

    void setHighlight(boolean highlight);

    boolean getHighlight();

    SquareType getType();

    void setType(SquareType type);

    int getRoad();

    void setRoad(int road);

    boolean called(boolean player);

    SquareState getState();

    SquareModel setState(SquareState state);

    void setPlayer(int player);

    void updatePlayer(int player);

    int valueSide(boolean b);
    
    int getX();

    int getY();

    int displayX();
    int displayY();
}
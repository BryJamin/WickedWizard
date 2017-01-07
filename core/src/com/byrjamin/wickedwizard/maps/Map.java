package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.player.Wizard;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Room[][] rooms;
    private int playPositionY;
    private int playPositionX;

    public Map(){

        rooms = new Room[][]{
                {null, null, new BattleRoom(), new BattleRoom(), new BattleRoom(),null, null},
                {null, new ItemRoom(), new BattleRoom(), new BattleRoom(), null, new BattleRoom(), null},
                {null, null, new BattleRoom(), new BattleRoom(), new BattleRoom(), new BossRoom(), null}};
        roomSetup();
        playPositionY = 0;
        playPositionX = 2;

    }

    public void roomSetup(){


        for(int i = 0; i < rooms.length; i++){

            boolean inBoundsI = (i >= 0) && (i < rooms.length);

            for(int j = 0; j < rooms[i].length; j++) {


                boolean inBoundsJ = (j >= 0) && (j < rooms[i].length);

                if (rooms[i][j] == null) {
                    continue;
                }

                if(j >= 1) {
                    if (rooms[i][j - 1] != null) {
                        rooms[i][j].addLeftExit();
                    }
                }

                if(j < rooms[i].length - 1) {
                    if (rooms[i][j + 1] != null) {
                        rooms[i][j].addRightExit();
                    }
                }


                if(i >= 1) {
                    if (rooms[i - 1][j] != null) {
                        rooms[i][j].addTopExit();
                    }
                }
                if(i < rooms.length - 1) {
                    if (rooms[i + 1][j] != null) {
                        rooms[i][j].setBottomExit();
                    }
                }
            }
        }
    }




    public void update(float dt, OrthographicCamera gamecam){
            rooms[playPositionY][playPositionX].update(dt, gamecam);

            if(rooms[playPositionY][playPositionX].isExitTransitionFinished()){
                if(rooms[playPositionY][playPositionX].isExitPointRight()){
                    Wizard w = rooms[playPositionY][playPositionX].getWizard();
                    playPositionX++;
                    rooms[playPositionY][playPositionX].setWizard(w);
                    rooms[playPositionY][playPositionX].enterRoom(Room.ENTRY_POINT.LEFT);
                } else if(rooms[playPositionY][playPositionX].isExitPointLeft()){
                    Wizard w = rooms[playPositionY][playPositionX].getWizard();
                    playPositionX--;
                    rooms[playPositionY][playPositionX].enterRoom(Room.ENTRY_POINT.RIGHT);
                    rooms[playPositionY][playPositionX].setWizard(w);
                    rooms[playPositionY][playPositionX].getWizard().setCurrentState(Wizard.STATE.STANDING);
                } else if(rooms[playPositionY][playPositionX].isExitPointUp()) {
                    Wizard w = rooms[playPositionY][playPositionX].getWizard();
                    playPositionY--;
                    rooms[playPositionY][playPositionX].enterRoom(Room.ENTRY_POINT.DOWN);
                    rooms[playPositionY][playPositionX].setWizard(w);
                    rooms[playPositionY][playPositionX].getWizard().setCurrentState(Wizard.STATE.STANDING);
                } else if(rooms[playPositionY][playPositionX].isExitPointDown()){
                    Wizard w = rooms[playPositionY][playPositionX].getWizard();
                    playPositionY++;
                    rooms[playPositionY][playPositionX].enterRoom(Room.ENTRY_POINT.UP);
                    rooms[playPositionY][playPositionX].setWizard(w);
                    rooms[playPositionY][playPositionX].getWizard().setCurrentState(Wizard.STATE.STANDING);
                }
            }
    }

    public boolean isTransitioning(){
        return rooms[playPositionY][playPositionX].state == Room.STATE.ENTRY || rooms[playPositionY][playPositionX].state == Room.STATE.EXIT;
    }


    public void draw(SpriteBatch batch){
        rooms[playPositionY][playPositionX].draw(batch);
    }

    public Room getActiveRoom() {
        return rooms[playPositionY][playPositionX];
    }
}

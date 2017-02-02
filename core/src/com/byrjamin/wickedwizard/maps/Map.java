package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.maps.rooms.TutorialRoom;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Room[][] rooms;
    private int mapY;
    private int mapX;

    private float mapBlinker;
    private boolean blink = true;

    private OrthographicCamera gamecam;

    ShapeRenderer mapRenderer = new ShapeRenderer();

    public Map(){

        rooms = new Room[][]{
                {null, null, new TutorialRoom(), new BattleRoom(2,1), new BattleRoom(),null, null},
                {null, null, null, new BattleRoom(), new BattleRoom(),null, null},
                {null, new ItemRoom(), new BattleRoom(), new BattleRoom(), null, null, null},
                {null, null, new BattleRoom(), new BattleRoom(), new BattleRoom(), new BossRoom(), null}};
        roomSetup();
        mapY = 0;
        mapX = 2;

    }

    public void roomSetup(){

        for(int i = 0; i < rooms.length; i++){

            for(int j = 0; j < rooms[i].length; j++) {
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

                rooms[i][j].setUpBoundaries();
            }
        }
    }




    public void update(float dt, OrthographicCamera gamecam){

        this.gamecam = gamecam;

        mapBlinker += dt;

        if(mapBlinker > 1.0){
            blink = !blink;
            mapBlinker = 0;
        }


        rooms[mapY][mapX].update(dt, gamecam);

        if(rooms[mapY][mapX].isExitTransitionFinished()){

            int x = rooms[mapY][mapX].getLeaveX();
            int y = rooms[mapY][mapX].getLeaveY();

            Wizard w = rooms[mapY][mapX].getWizard();

            mapX = mapX + x;
            mapY = mapY + y;


            System.out.println(mapX);

            rooms[mapY][mapX].enterRoom(w, x, y);

            System.out.println(y);
            System.out.println("transition?");

/*
            if(rooms[mapY][mapX].isExitPointRight()){
                rooms[mapY][mapX+1].enterRoom(rooms[mapY][mapX].getWizard(), Room.ENTRY_POINT.LEFT);
                mapX++;
            } else if(rooms[mapY][mapX].isExitPointLeft()){
                rooms[mapY][mapX-1].enterRoom(rooms[mapY][mapX].getWizard(), Room.ENTRY_POINT.RIGHT);
                mapX--;
            } else if(rooms[mapY][mapX].isExitPointUp()) {
                rooms[mapY-1][mapX].enterRoom(rooms[mapY][mapX].getWizard(), Room.ENTRY_POINT.DOWN);
                mapY--;
            } else if(rooms[mapY][mapX].isExitPointDown()){
                rooms[mapY+1][mapX].enterRoom(rooms[mapY][mapX].getWizard(), Room.ENTRY_POINT.UP);
                mapY++;
            }*/
        }


        //System.out.println(rooms[mapY][mapX].state);

    }

    public boolean isTransitioning(){
        return rooms[mapY][mapX].state == Room.STATE.ENTRY || rooms[mapY][mapX].state == Room.STATE.EXIT;
    }


    public void draw(SpriteBatch batch){

        rooms[mapY][mapX].draw(batch);



        batch.end();

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapRenderer.setColor(Color.CYAN);

        float SIZE = Measure.units(3);
        float mapy = 1000;
        float mapx = gamecam.position.x + 800;

        if(mapY + 1 < rooms.length){
            if(rooms[mapY + 1][mapX] != null){
                mapRenderer.rect(mapx, mapy - SIZE, SIZE, SIZE);
            }
        }

        if(mapY - 1 >= 0){
            if(rooms[mapY - 1][mapX] != null){
                mapRenderer.rect(mapx, mapy + SIZE, SIZE, SIZE);
            }
        }

        if(mapX + 1 < rooms[mapY].length){
            if(rooms[mapY][mapX + 1] != null){
                mapRenderer.rect(mapx + SIZE , mapy, SIZE, SIZE);
            }
        }

        if(mapX - 1 >= 0){
            if(rooms[mapY][mapX - 1] != null){
                mapRenderer.rect(mapx - SIZE, mapy, SIZE, SIZE);
            }
        }

        mapRenderer.setColor(Color.WHITE);
        mapRenderer.rect(mapx, mapy, SIZE, SIZE);


        mapRenderer.end();

        if(blink) {
            mapRenderer.begin(ShapeRenderer.ShapeType.Filled);
            mapRenderer.rect(mapx + SIZE / 4, mapy + SIZE / 4, SIZE / 2, SIZE / 2);
            mapRenderer.end();
        }


        batch.begin();




    }

    public Room getActiveRoom() {
        return rooms[mapY][mapX];
    }
}

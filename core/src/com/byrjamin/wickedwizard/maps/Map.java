package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Room[][] rooms;

    private Array<Room> roomArray = new Array<Room>();
    private Room currentRoom;
    private int mapY;
    private int mapX;

    private float mapBlinker;
    private boolean blink = true;

    private OrthographicCamera gamecam;

    ShapeRenderer mapRenderer = new ShapeRenderer();

    public Map(){
/*
        rooms = new Room[][]{
                {null, null, new TutorialRoom(), new BattleRoom(1,2), new BattleRoom(),null, null},
                {null, null, null, new BattleRoom(), new BattleRoom(),null, null},
                {null, new ItemRoom(), new BattleRoom(), new BattleRoom(), null, null, null},
                {null, null, new BattleRoom(), new BattleRoom(), new BattleRoom(), new BossRoom(), null}};
        roomSetup();*/

        currentRoom = new BattleRoom(1, 2, new MapCoords(0,0));

        roomArray.add(currentRoom);
        roomArray.add(new BattleRoom(new MapCoords(1,0)));
        roomArray.add(new BattleRoom(new MapCoords(3,1)));
        roomArray.add(new BattleRoom(new MapCoords(-1,0)));
        roomArray.add(new BattleRoom(new MapCoords(0,1)));
        roomArray.add(new BattleRoom(new MapCoords(0,2)));
        roomArray.add(new BattleRoom(new MapCoords(0,3)));
        roomArray.add(new ItemRoom(new MapCoords(1,1)));
        roomArray.add(new BattleRoom(new MapCoords(1,2)));
        roomArray.add(new BattleRoom(new MapCoords(1,3)));
        roomArray.add(new BattleRoom(new MapCoords(-1,1)));
        roomArray.add(new BattleRoom(new MapCoords(-1,2)));
        roomArray.add(new BossRoom(new MapCoords(-1,3)));

        for(Room r : roomArray){
            r.setUpBoundaries();
        }


        System.out.println("EXIT NUMBER is " + currentRoom.getRoomExits().size);




        for(int i = roomArray.size - 1; i >= 0; i--) {
            System.out.println(i + "i is");
            for(int j = roomArray.get(i).getRoomExits().size - 1; j >= 0; j--) {
                System.out.println(j + " j is");
                if(findRoom(roomArray.get(i).getRoomExits().get(j).getLeaveCoords()) == null) {
                    System.out.println(j + " j is");
                    roomArray.get(i).replaceDoorwithWall(roomArray.get(i).getRoomExits().get(j));
                    roomArray.get(i).getRoomExits().removeIndex(j);
                }
            }
        }

        for(int i = roomArray.size - 1; i >= 0; i--) {
            for(int j = roomArray.get(i).getRoomTeleporters().size - 1; j >= 0; j--) {
                if(findRoom(roomArray.get(i).getRoomTeleporters().get(j).getLeaveCoords()) == null) {
                    roomArray.get(i).getRoomTeleporters().removeIndex(j);
                }
            }
        }


        mapY = 0;
        mapX = 2;

    }

    public void roomSetup(){

/*        for(int i = 0; i < rooms.length; i++){

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
        }*/
    }




    public void update(float dt, OrthographicCamera gamecam){

        this.gamecam = gamecam;

        mapBlinker += dt;

        if(mapBlinker > 1.0){
            blink = !blink;
            mapBlinker = 0;
        }


        currentRoom.update(dt, gamecam);

        if(currentRoom.isExitTransitionFinished()){

            MapCoords mc = currentRoom.getLeaveMapCoords();
            MapCoords oc = currentRoom.getLeaveEntryMapCoords();
            Wizard w = currentRoom.getWizard();

            currentRoom = findRoom(mc);

            System.out.println("Leaving to " + mc);
            System.out.println("Entering From " + oc);

            currentRoom.enterRoom(w, oc, mc);
        }
    }


    public Room findRoom(MapCoords mc){

        for(Room r : roomArray) {
            if(r.containsCoords(mc)){
                return r;
            };
        }

        //TODO return an error
        return null;
    }

    public void draw(SpriteBatch batch){

        currentRoom.draw(batch);



/*        batch.end();

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


        batch.begin();*/




    }

    public Room getActiveRoom() {
        return currentRoom;
    }
}

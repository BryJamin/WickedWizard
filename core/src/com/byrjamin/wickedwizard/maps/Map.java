package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.maps.rooms.TutorialRoom;
import com.byrjamin.wickedwizard.maps.rooms.layout.BasicRoomLayout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Height2Layout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Width2Layout;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

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

        //currentRoom = new ItemRoom(1, 2, new MapCoords(0,0));
        Array<TextureAtlas.AtlasRegion> background = PlayScreen.atlas.findRegions("backgrounds/wall");
        Array<TextureAtlas.AtlasRegion> walls = PlayScreen.atlas.findRegions("brick");


        BasicRoomLayout t = new BasicRoomLayout(background, walls);
        Height2Layout height2Layout = new Height2Layout(background, walls);
        Width2Layout width2Layout = new Width2Layout(background, walls);
        Room temp;

        currentRoom = new TutorialRoom(new MapCoords(-1,0));
        t.applyLayout(currentRoom);
        roomArray.add(currentRoom);
        temp = new BattleRoom(new MapCoords(0,0));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(1,0));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(2,0));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(2,1));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(2,2));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(1,2));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new ItemRoom(new MapCoords(3,1));
        height2Layout.applyLayout(temp);
        roomArray.add(temp);
        temp = new BossRoom(new MapCoords(4,1));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(4,2));
        t.applyLayout(temp);
        roomArray.add(temp);
        temp = new BattleRoom(new MapCoords(5,2));
        width2Layout.applyLayout(temp);
        roomArray.add(temp);

        for(Room r : roomArray){
            r.turnOnRoomEnemyWaves();
        }

        for(int i = roomArray.size - 1; i >= 0; i--) {
            for(int j = roomArray.get(i).getRoomExits().size - 1; j >= 0; j--) {
                if(findDoor(roomArray.get(i).getRoomExits().get(j).getRoomCoords(),roomArray.get(i).getRoomExits().get(j).getLeaveCoords()) == null) {
                    roomArray.get(i).replaceDoorwithWall(roomArray.get(i).getRoomExits().get(j));
                    roomArray.get(i).getRoomExits().removeIndex(j);
                }
            }
        }

        for(int i = roomArray.size - 1; i >= 0; i--) {
            for(int j = roomArray.get(i).getRoomTeleporters().size - 1; j >= 0; j--) {
                if(findDoor(roomArray.get(i).getRoomTeleporters().get(j).getRoomCoords(), roomArray.get(i).getRoomTeleporters().get(j).getLeaveCoords()) == null) {
                    roomArray.get(i).getRoomTeleporters().removeIndex(j);
                }
            }
        }


        mapY = 0;
        mapX = 2;

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
            MapCoords oc = currentRoom.getRoomCoords();
            Wizard w = currentRoom.getWizard();

            currentRoom = findRoom(mc);
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

    public Room findDoor(MapCoords EnterFrom, MapCoords LeaveTo){
        for(Room r : roomArray) {
            System.out.println(r.containsExitWithCoords(EnterFrom, LeaveTo));
            if(r.containsExitWithCoords(EnterFrom, LeaveTo)){
                return r;
            };
        }
        return null;
    }




    public void jigsawMap(int numberofRooms){

        Array<Room> preMadeRooms = new Array<Room>();
        Array<Room> actualRooms = new Array<Room>();

        for(int i = 0; i < numberofRooms; i++) {
            preMadeRooms.add(new BattleRoom(new MapCoords(0,0)));
        }

        while(preMadeRooms.size > 1) {

            Random rand = new Random();
            int nextRoomNumber = rand.nextInt(preMadeRooms.size);

            preMadeRooms.get(i);

        }



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

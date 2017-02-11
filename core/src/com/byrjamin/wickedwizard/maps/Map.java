package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.helper.Measure;
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
/*        Array<TextureAtlas.AtlasRegion> background = PlayScreen.atlas.findRegions("backgrounds/wall");
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
        mapX = 2;*/

        jigsawMap(10, 1, 1);


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
            if(r.containsExitWithCoords(EnterFrom, LeaveTo)){
                return r;
            };
        }
        return null;
    }




    public void jigsawMap(int numberOfBattleRooms, int numberOfItemRooms, int numberOfBossRooms){

        int totalRooms = numberOfBattleRooms + numberOfItemRooms + numberOfBossRooms;

        Array<TextureAtlas.AtlasRegion> background = PlayScreen.atlas.findRegions("backgrounds/wall");
        Array<TextureAtlas.AtlasRegion> walls = PlayScreen.atlas.findRegions("brick");

        BasicRoomLayout t = new BasicRoomLayout(background, walls);
        Height2Layout height2Layout = new Height2Layout(background, walls);
        Width2Layout width2Layout = new Width2Layout(background, walls);

        Array<Room> roomPieces = new Array<Room>();
        Array<Room> bossPieces = new Array<Room>();
        Array<Room> itemPieces = new Array<Room>();
        Array<Room> actualRooms = new Array<Room>();

        for(int i = 0; i < numberOfBattleRooms; i++) {
            Room r = new BattleRoom(new MapCoords(0,0));
            if(i == 1){
                width2Layout.applyLayout(r);
            } else if(i == 2){
                height2Layout.applyLayout(r);
            } else {
                t.applyLayout(r);
            }

            roomPieces.add(r);
        }

        for(int i = 0; i < numberOfBossRooms; i++) {
            Room r = new BossRoom(new MapCoords(0,0));
            t.applyLayout(r);
            bossPieces.add(r);
        }

        for(int i = 0; i < numberOfItemRooms; i++) {
            Room r = new ItemRoom(new MapCoords(0,0));
            t.applyLayout(r);
            itemPieces.add(r);
        }


        TutorialRoom tRoom = new TutorialRoom(new MapCoords(0,0));
        t.applyLayout(tRoom);

        actualRooms.add(tRoom);

        OrderedSet<MapCoords> avaliableMapCoordsSet = new OrderedSet<MapCoords>();


        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        unavaliableMapCoords.addAll(tRoom.getMapCoordsArray());

        avaliableMapCoordsSet.addAll(tRoom.getAdjacentMapCoords());

/*        for(MapCoords m : unavaliableMapCoords){
            System.out.println("Unavaliable Coords are " + m.toString());
        }

        for(MapCoords m : avaliableMapCoords){
            System.out.println("Avaliable Coords are " + m.toString());
        }*/
        Random rand = new Random();
        while(roomPieces.size > 0) {
            int i = rand.nextInt(roomPieces.size);
            Room nextRoomToBePlaced = roomPieces.get(i);
            roomPieces.removeIndex(i);
            placeRoom(nextRoomToBePlaced, avaliableMapCoordsSet, unavaliableMapCoords, rand);
            actualRooms.add(nextRoomToBePlaced);
            unavaliableMapCoords.addAll(nextRoomToBePlaced.getMapCoordsArray());
            avaliableMapCoordsSet.addAll(nextRoomToBePlaced.getAdjacentMapCoords());
        }

        for(Room r : itemPieces){
            int range = (int) Math.floor(Math.sqrt(totalRooms));
            System.out.println("Range of Boss Rooms is" + range);
            if(placeRoom(r, avaliableMapCoordsSet, unavaliableMapCoords, rand)) {
                actualRooms.add(r);
                unavaliableMapCoords.addAll(r.getMapCoordsArray());
                unavaliableMapCoords.addAll(r.getAdjacentMapCoords());
            }
        }


        for(Room r : bossPieces){

            int range = (int) Math.floor(Math.sqrt(totalRooms));

            System.out.println("Range of Boss Rooms is" + range);
            //TODO if you find out a way to print out 1000 maps with this method see if -1 range is better than +0 range
            if(placeBossRoom(r, avaliableMapCoordsSet, unavaliableMapCoords, rand, range - 1)) {
                actualRooms.add(r);
            }
        }




        currentRoom = tRoom;
        roomArray.addAll(actualRooms);

        for(Room room : roomArray) {
            System.out.println(room.getStartCoords());
        }

        System.out.println("MAP SIZE IS SIZE" + roomArray.size);
    }


    //Boss rooms need to placed
    public boolean placeBossRoom(Room bossRoom, OrderedSet<MapCoords> avaliableMapCoordsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand, int minRange){
        Array<MapCoords> avaliableMapCoordsArray = new Array<MapCoords>();
        avaliableMapCoordsArray.addAll(avaliableMapCoordsSet.orderedItems());

        OrderedSet<MapCoords> newavaliableMapCoordsSet = new OrderedSet<MapCoords>();

        System.out.println(avaliableMapCoordsArray.size);

        for(MapCoords m : avaliableMapCoordsArray){
            if(m.getX() < minRange && m.getY() < minRange && m.getX() > -minRange && m.getY() > -minRange ) {
                System.out.println("INSIDE AND REMOVING VALUES");
                avaliableMapCoordsArray.removeValue(m, false);
            } else {
                newavaliableMapCoordsSet.add(m);
            }
        }

        if(newavaliableMapCoordsSet.size != 0) {
            return placeRoom(bossRoom, newavaliableMapCoordsSet, unavaliableMapCoords, rand);
        }

        return false;

    }



    public boolean placeRoom(Room room, OrderedSet<MapCoords> avaliableMapCoordsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){

        Array<MapCoords> avaliableMapCoordsArray = new Array<MapCoords>();
        avaliableMapCoordsArray.addAll(avaliableMapCoordsSet.orderedItems());

        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(avaliableMapCoordsArray.size <= 0){
                break;
            }

            int selector = rand.nextInt(avaliableMapCoordsArray.size);
            //The available co-ordinates we can shift to.
            MapCoords shiftCoords = avaliableMapCoordsArray.get(selector);
            //Mocks moving the room
            Array<MapCoords> mockCoords = room.mockShiftCoordinatePosition(shiftCoords);
            //Mocks the co-orindates the room leads to
            Array<MapCoords> mockAdjacentCoords = room.mockShiftCoordinatePositionAdjacent(shiftCoords);
            //Removes available map coords for the next loop.
            avaliableMapCoordsArray.removeIndex(selector);

            for (int j = 0; j < mockCoords.size; j++) {
                if (!unavaliableMapCoords.contains(mockCoords.get(j))) {
                   // roomPlaced = true;
                    for(MapCoords m : mockAdjacentCoords){
                        if(unavaliableMapCoords.contains(m)){
                            roomPlaced = true;
                            break;
                        }
                    }

                } else {
                    roomPlaced = false;
                    break;
                }

            }

            if (roomPlaced) {
                room.shiftCoordinatePosition(shiftCoords);
            }
        }

        return roomPlaced;

    }



    public void draw(SpriteBatch batch){

        currentRoom.draw(batch);



        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mapRenderer.setColor(1,1,1,0.5f);

        float SIZE = Measure.units(3f);
        float mapy = 1000;
        float mapx = gamecam.position.x + 800;

        MapCoords currentCoords = currentRoom.getStartCoords();

        mapRenderer.setColor(1,1,1,0.8f);
        mapRenderer.rect(mapx, mapy, SIZE, SIZE);
        //mapRenderer.end();

        if(blink) {
            mapRenderer.setColor(0,0,1,0.5f);
            //mapRenderer.begin(ShapeRenderer.ShapeType.Filled);
            mapRenderer.rect(mapx + SIZE / 4, mapy + SIZE / 4, SIZE / 2, SIZE / 2);
        }

        mapRenderer.setColor(0.8f,0.8f,0.8f,0.8f);
        for(Room r : roomArray){
            if(currentCoords != r.getStartCoords()){
                mapRenderer.setColor(0.5f,0.5f,0.5f,0.8f);
                int diffX = r.getStartCoords().getX() - currentCoords.getX();
                int diffY = r.getStartCoords().getY() - currentCoords.getY();
                mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

                if(r instanceof BossRoom){
                    mapRenderer.setColor(0,1,1,0.5f);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4 , mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

                if(r instanceof ItemRoom){
                    mapRenderer.setColor(1,0,1,0.5f);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4 , mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

            }
        }
        mapRenderer.end();

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapRenderer.rect(mapx, mapy, SIZE, SIZE);
        mapRenderer.setColor(0f,0f,0f,0.8f);
        for(Room r : roomArray){
            if(currentCoords != r.getStartCoords()){
                mapRenderer.setColor(0f,0f,0f,0.8f);
                int diffX = r.getStartCoords().getX() - currentCoords.getX();
                int diffY = r.getStartCoords().getY() - currentCoords.getY();
                mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);
            }
        }
        mapRenderer.end();


        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();




    }

    public Room getActiveRoom() {
        return currentRoom;
    }
}

package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.Gdx;
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
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.layout.BasicRoomLayout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Height2Layout;
import com.byrjamin.wickedwizard.maps.rooms.layout.LBlockLayout;
import com.byrjamin.wickedwizard.maps.rooms.layout.Width2Layout;
import com.byrjamin.wickedwizard.screens.PlayScreen;

import java.util.Random;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {

    private Room[][] rooms;

    private Array<Room> roomArray = new Array<Room>();
    private Array<MapCoords> mapCoordsArray = new Array<MapCoords>();
    private Room currentRoom;
    private int mapY;
    private int mapX;

    private float mapBlinker;
    private boolean blink = true;

    private OrthographicCamera gamecam;

    ShapeRenderer mapRenderer = new ShapeRenderer();

    public Map(){

        jigsawMap(13, 1,1);

        for(Room r : roomArray){
            r.turnOnRoomEnemyWaves();
        }

        for(int i = roomArray.size - 1; i >= 0; i--) {
            for(int j = roomArray.get(i).getRoomDoors().size - 1; j >= 0; j--) {

                RoomDoor rd = roomArray.get(i).getRoomDoors().get(j);

                if(findDoor(rd.getRoomCoords(),rd.getLeaveCoords()) == null) {
                    roomArray.get(i).replaceDoorwithWall(rd);
                    roomArray.get(i).getRoomDoors().removeIndex(j);
                    roomArray.get(i).getRoomExits().removeValue(rd, false);
                }
            }
        }

        for(int i = roomArray.size - 1; i >= 0; i--) {
            for(int j = roomArray.get(i).getRoomGrates().size - 1; j >= 0; j--) {
                RoomGrate rg =  roomArray.get(i).getRoomGrates().get(j);
                if(findDoor(rg.getRoomCoords(), rg.getLeaveCoords()) == null) {
                    roomArray.get(i).getRoomGrates().removeIndex(j);
                    roomArray.get(i).getRoomExits().removeValue(rg, false);
                }
            }
        }


        for(Room r : roomArray){
            mapCoordsArray.addAll(r.getMapCoordsArray());
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
            RoomExit currentExit = currentRoom.getCurrentExit();
            Wizard w = currentRoom.getWizard();
            currentRoom = findRoom(currentExit.getLeaveCoords());
            currentRoom.enterRoom(w, currentExit.getRoomCoords(), currentExit.getLeaveCoords());
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

        //TODO hasn't been properly built but can slot in fine
        LBlockLayout lBlockLayout = new LBlockLayout(background, walls);

        Array<Room> roomPieces = new Array<Room>();
        Array<Room> bossPieces = new Array<Room>();
        Array<Room> itemPieces = new Array<Room>();
        Array<Room> actualRooms = new Array<Room>();

        for(int i = 0; i < numberOfBattleRooms; i++) {
            Room r = new BattleRoom(new MapCoords(0,0));
            if(i == 1){
                System.out.println("pAST IT");
                height2Layout.applyLayout(r);
                System.out.println("pAST IT");
            } else if(i == 2){
                width2Layout.applyLayout(r);
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
        OrderedSet<RoomExit> avaliableExitsSet = new OrderedSet<RoomExit>();


        ObjectSet<MapCoords> unavaliableMapCoords = new ObjectSet<MapCoords>();
        unavaliableMapCoords.addAll(tRoom.getMapCoordsArray());

        avaliableMapCoordsSet.addAll(tRoom.getAdjacentMapCoords());
        avaliableExitsSet.addAll(tRoom.getRoomExits());

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
            placeRoomUsingDoors(nextRoomToBePlaced, avaliableExitsSet, unavaliableMapCoords, rand);
            actualRooms.add(nextRoomToBePlaced);
            unavaliableMapCoords.addAll(nextRoomToBePlaced.getMapCoordsArray());

            for (RoomExit re : nextRoomToBePlaced.getRoomExits()) {
                if(!unavaliableMapCoords.contains(re.getLeaveCoords())) {
                    avaliableExitsSet.add(re);
                }
            }

            System.out.println("Avaliable Exit leaving co-ordinate size BEFORE: " + avaliableExitsSet.size);
            System.out.println("Avaliable Exit leaving co-ordinates");
            for(RoomExit re : avaliableExitsSet){
                System.out.println(re.getLeaveCoords());
            }

            System.out.println("Avaliable Exit leaving co-ordinate size AFTER: " + avaliableExitsSet.size);

        }

        for(Room r : itemPieces){
            int range = (int) Math.floor(Math.sqrt(totalRooms));
            System.out.println("Range of Boss Rooms is" + range);
            if(placeRoomUsingDoors(r, avaliableExitsSet, unavaliableMapCoords, rand)) {
                actualRooms.add(r);
                unavaliableMapCoords.addAll(r.getMapCoordsArray());
                unavaliableMapCoords.addAll(r.getAdjacentMapCoords());
            }
        }


        for(Room r : bossPieces){

            int range = (int) Math.floor(Math.sqrt(totalRooms));

            System.out.println("Range of Boss Rooms is" + range);
            //TODO if you find out a way to print out 1000 maps with this method see if -1 range is better than +0 range
            if(placeBossRoom(r, avaliableExitsSet, unavaliableMapCoords, rand, range - 1)) {
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
    public boolean placeBossRoom(Room room, OrderedSet<RoomExit> avaliableExitsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand, int minRange){
        Array<RoomExit> avaliableMapCoordsArray = new Array<RoomExit>();
        avaliableMapCoordsArray.addAll(avaliableExitsSet.orderedItems());

        OrderedSet<RoomExit> newavaliableMapCoordsSet = new OrderedSet<RoomExit>();

        System.out.println(avaliableMapCoordsArray.size);

        for(RoomExit re : avaliableMapCoordsArray){
            if(re.getRoomCoords().getX() < minRange && re.getRoomCoords().getY() < minRange && re.getRoomCoords().getX() > -minRange && re.getRoomCoords().getY() > -minRange ) {
                System.out.println("INSIDE AND REMOVING VALUES");
                avaliableMapCoordsArray.removeValue(re, false);
            } else {
                newavaliableMapCoordsSet.add(re);
            }
        }

        if(newavaliableMapCoordsSet.size != 0) {
            return placeRoomUsingDoors(room, newavaliableMapCoordsSet, unavaliableMapCoords, rand);
        }

        return false;

    }

    public boolean placeRoomUsingDoors(Room room, OrderedSet<RoomExit> avaliableExitsSet, ObjectSet<MapCoords> unavaliableMapCoords, Random rand){

        Array<RoomExit> avaliableExitsArray = new Array<RoomExit>();
        avaliableExitsArray.addAll(avaliableExitsSet.orderedItems());


        boolean roomPlaced = false;

        while(!roomPlaced) {

            if(avaliableExitsArray.size <= 0){
                break;
            }

            int avaliableExitSelector = rand.nextInt(avaliableExitsArray.size);
            //The available co-ordinates we can shift to.
            RoomExit selectedAvaliableExit = avaliableExitsArray.get(avaliableExitSelector);

            avaliableExitsArray.removeIndex(avaliableExitSelector);

            Array<RoomExit> linkableExitsArray = new Array<RoomExit>();

            for(RoomExit re : room.getRoomExits()) {
                switch (selectedAvaliableExit.getDirection()){
                    case LEFT: if(re.getDirection() == RoomExit.EXIT_DIRECTION.RIGHT)
                        linkableExitsArray.add(re);
                        break;
                    case RIGHT: if(re.getDirection() == RoomExit.EXIT_DIRECTION.LEFT)
                        linkableExitsArray.add(re);
                        break;
                    case UP: if(re.getDirection() == RoomExit.EXIT_DIRECTION.DOWN)
                        linkableExitsArray.add(re);
                        break;
                    case DOWN: if(re.getDirection() == RoomExit.EXIT_DIRECTION.UP)
                        linkableExitsArray.add(re);
                        break;
                }
            }

            while(!roomPlaced && linkableExitsArray.size > 0) {

                if(linkableExitsArray.size <= 0){
                    break;
                }

                int linkableExitsSelector = rand.nextInt(linkableExitsArray.size);
                //The available co-ordinates we can shift to.
                RoomExit selectedLinkableExit = linkableExitsArray.get(linkableExitsSelector);

                linkableExitsArray.removeIndex(linkableExitsSelector);

                MapCoords shiftCoords = generateShiftCoords(selectedAvaliableExit.getLeaveCoords(), selectedLinkableExit.getRoomCoords());

                //Mocks moving the room
                Array<MapCoords> mockCoords = room.mockShiftCoordinatePosition(shiftCoords);

                for (int j = 0; j < mockCoords.size; j++) {
                    if (!unavaliableMapCoords.contains(mockCoords.get(j))) {
                         roomPlaced = true;
                    } else {
                        roomPlaced = false;
                        break;
                    }
                }

                if (roomPlaced) {
                    room.shiftCoordinatePosition(shiftCoords);
                    avaliableExitsSet.remove(selectedAvaliableExit);
                }

            }

        }

        return roomPlaced;

    }


    public MapCoords generateShiftCoords(MapCoords newPosition, MapCoords oldPosition) {
        int diffX = newPosition.getX() - oldPosition.getX();
        int diffY = newPosition.getY() - oldPosition.getY();
        return new MapCoords(diffX, diffY);
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
        float mapy = gamecam.position.y + Measure.units(15);
        float mapx = gamecam.position.x + Measure.units(40);

        MapCoords currentCoords = currentRoom.getWizardLocation();

        Array<MapCoords> currentRoomMapCoordsArray = currentRoom.getMapCoordsArray();

        for(MapCoords m : currentRoom.getMapCoordsArray()) {
            mapRenderer.setColor(1, 1, 1, 0.8f);
            mapRenderer.rect(mapx, mapy, SIZE, SIZE);

            mapRenderer.setColor(0.5f, 0.5f, 0.5f, 0.8f);
            int diffX = m.getX() - currentCoords.getX();
            int diffY = m.getY() - currentCoords.getY();
            mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

            if (currentRoom instanceof BossRoom) {
                mapRenderer.setColor(0, 1, 1, 0.5f);
                mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
            }

            if (currentRoom instanceof ItemRoom) {
                mapRenderer.setColor(1, 0, 1, 0.5f);
                mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
            }


        }
        //mapRenderer.end();

        if(blink) {
            mapRenderer.setColor(0,0,1,0.5f);
            //mapRenderer.begin(ShapeRenderer.ShapeType.Filled);
            mapRenderer.rect(mapx + SIZE / 4, mapy + SIZE / 4, SIZE / 2, SIZE / 2);
        }

        mapRenderer.setColor(0.8f,0.8f,0.8f,0.8f);


        for(Room r : roomArray){
            if(!r.getMapCoordsArray().contains(currentCoords, false)){

                for(MapCoords m : r.getMapCoordsArray()) {
                    mapRenderer.setColor(0.5f, 0.5f, 0.5f, 0.8f);
                    int diffX = m.getX() - currentCoords.getX();
                    int diffY = m.getY() - currentCoords.getY();
                    mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

                    if (r instanceof BossRoom) {
                        mapRenderer.setColor(0, 1, 1, 0.5f);
                        mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                    }

                    if (r instanceof ItemRoom) {
                        mapRenderer.setColor(1, 0, 1, 0.5f);
                        mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                    }
                }
            }
        }
        mapRenderer.end();

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapRenderer.rect(mapx, mapy, SIZE, SIZE);
        mapRenderer.setColor(0f,0f,0f,0.8f);
        for(Room r : roomArray){

            for(MapCoords m : r.getMapCoordsArray()) {
                mapRenderer.setColor(1f, 0f, 0f, 0.9f);
                int diffX = m.getX() - currentCoords.getX();
                int diffY = m.getY() - currentCoords.getY();

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

                //Left Line
                if(!r.getMapCoordsArray().contains(new MapCoords(m.getX() - 1,m.getY()), false)) {
                    mapRenderer.line(x, y, x, y + SIZE);
                }
                //Right Line
                if(!r.getMapCoordsArray().contains(new MapCoords(m.getX() + 1,m.getY()), false)) {
                    mapRenderer.line(x + SIZE, y, x + SIZE, y + SIZE);
                }

                //Top Line
                if(!r.getMapCoordsArray().contains(new MapCoords(m.getX(),m.getY() + 1), false)) {
                    mapRenderer.line(x, y + SIZE, x + SIZE, y + SIZE);
                }

                //Bottom Line
                if(!r.getMapCoordsArray().contains(new MapCoords(m.getX(),m.getY() - 1), false)) {
                    mapRenderer.line(x, y, x + SIZE, y);
                }
            }

            for(RoomExit re : r.getRoomExits()) {
                mapRenderer.setColor(0f, 1f, 0f, 0.9f);
                int diffX = re.getRoomCoords().getX() - currentCoords.getX();
                int diffY = re.getRoomCoords().getY() - currentCoords.getY();

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

                float MINI_SIZE = SIZE / 4;

                //Left Line
                if(re.getDirection() == RoomExit.EXIT_DIRECTION.LEFT) {
                    mapRenderer.line(x, y + MINI_SIZE, x, y + SIZE - MINI_SIZE);
                }

                //Right Line
                if(re.getDirection() == RoomExit.EXIT_DIRECTION.RIGHT) {
                    mapRenderer.line(x + SIZE, y + MINI_SIZE, x + SIZE, y + SIZE - MINI_SIZE);
                }

                //Top Line
                if(re.getDirection() == RoomExit.EXIT_DIRECTION.UP) {
                    mapRenderer.line(x + MINI_SIZE, y + SIZE, x + SIZE - MINI_SIZE, y + SIZE);
                }

                //Bottom Line
                if(re.getDirection() == RoomExit.EXIT_DIRECTION.DOWN) {
                    mapRenderer.line(x + MINI_SIZE, y, x + SIZE - MINI_SIZE, y);
                }
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

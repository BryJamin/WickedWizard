package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.maps.rooms.BossRoom;
import com.byrjamin.wickedwizard.maps.rooms.ItemRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomExit;

public class MapGUI {

    private float mapx;
    private float mapy;

    private float SIZE = Measure.units(3f);

    private float mapBlinker;

    private Array<Room> rooms;

    private Room currentRoom;
    private MapCoords currentCoords;

    private Color currentRoomColor = new Color(1, 1, 1, 0.8f);
    private Color roomColor = new Color(0.5f, 0.5f, 0.5f, 0.8f);
    private Color borderColor = new Color(0, 1, 1, 1);
    private Color doorColor = new Color(1f, 0f, 0f, 1f);
    private Color locationBlinkColor = new Color (0,0,1,0.5f);
    private Color bossRoomColor = new Color(0, 1, 1, 0.5f);
    private Color itemRoomColor = new Color (1, 0, 1, 0.5f);

    private boolean blink;

    ShapeRenderer mapRenderer = new ShapeRenderer();


    public MapGUI(float x, float y, Array<Room> rooms, Room currentRoom) {
        this.mapx = x;
        this.mapy = y;
        this.rooms = rooms;
        this.currentRoom = currentRoom;
    }



    public void update(float dt, OrthographicCamera gamecam, Array<Room> visitedRooms, Room currentRoom){

        this.currentRoom = currentRoom;
        this.currentCoords = currentRoom.getWizardLocation();
        this.rooms = visitedRooms;

        mapBlinker += dt;

        if(mapBlinker > 1.0){
            blink = !blink;
            mapBlinker = 0;
        }

        mapy = gamecam.position.y + Measure.units(15);
        mapx = gamecam.position.x + Measure.units(40);


    }



    public void draw(SpriteBatch batch) {

        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawMapSquares(batch);
        drawMapLines(batch);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();

    }


    public void drawMapSquares(SpriteBatch batch){
        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(Room r : rooms){
            for(MapCoords m : r.getMapCoordsArray()) {
                if(r == currentRoom) {
                    mapRenderer.setColor(currentRoomColor);
                } else {
                    mapRenderer.setColor(roomColor);
                }
                int diffX = m.getX() - currentCoords.getX();
                int diffY = m.getY() - currentCoords.getY();
                mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

                if (r instanceof BossRoom) {
                    mapRenderer.setColor(bossRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

                if (r instanceof ItemRoom) {
                    mapRenderer.setColor(itemRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }
            }
        }

        if(blink) {
            mapRenderer.setColor(locationBlinkColor);
            mapRenderer.rect(mapx + SIZE / 4, mapy + SIZE / 4, SIZE / 2, SIZE / 2);
        }

        mapRenderer.end();
    }

    public void drawMapLines(SpriteBatch batch){

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);


        for(Room r : rooms){

            for(MapCoords m : r.getMapCoordsArray()) {
                mapRenderer.setColor(borderColor);
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
                mapRenderer.setColor(doorColor);

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

    }



}

package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;

/**
 * Created by Home on 20/03/2017.
 */

public class ArenaGUI {


    private float mapx;
    private float mapy;

    private float SIZE = Measure.units(3f);

    private Array<Arena> arenas;
    private Array<Arena> undiscoveredArenas;

    private Arena currentRoom;
    private MapCoords currentCoords;

    private Color currentRoomColor = new Color(1, 1, 1, 0.8f);
    private Color roomColor = new Color(0.5f, 0.5f, 0.5f, 0.8f);
    private Color undiscoveredRoomColor = new Color(0.1f, 0.1f, 0.1f, 0.8f);
    private Color borderColor = new Color(0, 1, 1, 1);
    private Color doorColor = new Color(1f, 0f, 0f, 1f);
    private Color locationBlinkColor = new Color (0,0,1,0.5f);
    private Color bossRoomColor = new Color(0, 1, 1, 0.5f);
    private Color itemRoomColor = new Color (1, 0, 1, 0.5f);
    private Color shopRoomColor = new Color (234f / 255f, 185f / 255f, 157f / 255f, 1);

    private float mapBlinker;
    private boolean blink;

    private int range = 3;

    ShapeRenderer mapRenderer = new ShapeRenderer();


    public ArenaGUI(float x, float y, Array<Arena> arenas, Arena currentRoom) {
        this.mapx = x;
        this.mapy = y;
        this.arenas = arenas;
        this.currentRoom = currentRoom;
    }


    public void update(float dt, OrthographicCamera gamecam, OrderedSet<Arena> visitedArenas, OrderedSet<Arena> undiscoveredArenas, Arena currentRoom, MapCoords currentCoords){

        this.currentRoom = currentRoom;
        this.currentCoords = currentCoords;

        System.out.println(currentCoords);

        this.arenas = visitedArenas.orderedItems();
        this.undiscoveredArenas = undiscoveredArenas.orderedItems();

        mapBlinker += dt;

        if(mapBlinker > 1.0){
            blink = !blink;
            mapBlinker = 0;
        }
        mapy = gamecam.position.y + Measure.units(20);
        mapx = gamecam.position.x + Measure.units(40);
    }


    public void draw(SpriteBatch batch) {

        if(batch.isDrawing()) {
            batch.end();
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        drawMapContainer(batch);
        drawMapSquares(batch);
        drawMapLines(batch);
        Gdx.gl.glDisable(GL20.GL_BLEND);
        if(!batch.isDrawing()) {
            batch.begin();
        }

    }

    public void drawMapContainer(SpriteBatch batch){

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Filled);
        mapRenderer.setColor(undiscoveredRoomColor);
        mapRenderer.rect(mapx - SIZE * 2, mapy - SIZE * 2, SIZE * 5, SIZE * 5);
        mapRenderer.end();
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);
        mapRenderer.setColor(Color.WHITE);
        mapRenderer.rect(mapx - SIZE * 2, mapy - SIZE * 2, SIZE * 5, SIZE * 5);
        mapRenderer.end();

    }


    public void drawMapSquares(SpriteBatch batch){
        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(Arena arena : arenas){
            Color c = (arena == currentRoom) ? currentRoomColor : roomColor;
            drawArenaSquare(mapRenderer, arena, c);
        }

        for(Arena arena : undiscoveredArenas){
            drawArenaSquare(mapRenderer, arena, undiscoveredRoomColor);
        }

        if(blink) {
            mapRenderer.setColor(locationBlinkColor);
            mapRenderer.rect(mapx + SIZE / 4, mapy + SIZE / 4, SIZE / 2, SIZE / 2);
        }

        mapRenderer.end();
    }

    public void drawArenaSquare(ShapeRenderer mapRenderer, Arena arena, Color roomColor){

        for(MapCoords m : arena.cotainingCoords) {
            mapRenderer.setColor(roomColor);
            int diffX = m.getX() - currentCoords.getX();
            int diffY = m.getY() - currentCoords.getY();

            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {
                mapRenderer.rect(mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

                if (arena.roomType == Arena.RoomType.BOSS) {
                    mapRenderer.setColor(bossRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

                if (arena.roomType == Arena.RoomType.ITEM) {
                    mapRenderer.setColor(itemRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

                if (arena.roomType == Arena.RoomType.SHOP) {
                    mapRenderer.setColor(shopRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }

            }
/*
                if (r instanceof ItemRoom) {
                    mapRenderer.setColor(itemRoomColor);
                    mapRenderer.rect(mapx + (SIZE * diffX) + SIZE / 4, mapy + (SIZE * diffY) + SIZE / 4, SIZE / 2, SIZE / 2);
                }*/
        }

    }


    public void drawMapLines(SpriteBatch batch){

        mapRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        mapRenderer.begin(ShapeRenderer.ShapeType.Line);


        for(Arena arena : undiscoveredArenas){
            drawArenaBorder(mapRenderer, arena);
        }

        for(Arena arena : arenas){
            drawArenaBorder(mapRenderer, arena);
            drawArenaDoors(mapRenderer, arena);
        }


        mapRenderer.end();

    }


    public void drawArenaBorder(ShapeRenderer mapRenderer, Arena arena){

        for(MapCoords m : arena.cotainingCoords) {
            mapRenderer.setColor(borderColor);
            int diffX = m.getX() - currentCoords.getX();
            int diffY = m.getY() - currentCoords.getY();
            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

                //Left Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX() - 1, m.getY()), false)) {
                    mapRenderer.line(x, y, x, y + SIZE);
                }
                //Right Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX() + 1, m.getY()), false)) {
                    mapRenderer.line(x + SIZE, y, x + SIZE, y + SIZE);
                }

                //Top Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX(), m.getY() + 1), false)) {
                    mapRenderer.line(x, y + SIZE, x + SIZE, y + SIZE);
                }

                //Bottom Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX(), m.getY() - 1), false)) {
                    mapRenderer.line(x, y, x + SIZE, y);
                }
            }
        }
    }

    public void drawArenaDoors(ShapeRenderer mapRenderer, Arena arena){
        for(DoorComponent dc : arena.doors) {


            mapRenderer.setColor(doorColor);

            int diffX = dc.currentCoords.getX() - currentCoords.getX();
            int diffY = dc.currentCoords.getY() - currentCoords.getY();

            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

                float MINI_SIZE = SIZE / 4;

                //Left Line
                if (dc.exit == DoorComponent.DIRECTION.left) {
                    mapRenderer.line(x, y + MINI_SIZE, x, y + SIZE - MINI_SIZE);
                }

                //Right Line
                if (dc.exit == DoorComponent.DIRECTION.right) {
                    mapRenderer.line(x + SIZE, y + MINI_SIZE, x + SIZE, y + SIZE - MINI_SIZE);
                }

                //Top Line
                if (dc.exit == DoorComponent.DIRECTION.up) {
                    mapRenderer.line(x + MINI_SIZE, y + SIZE, x + SIZE - MINI_SIZE, y + SIZE);
                }

                //Bottom Line
                if (dc.exit == DoorComponent.DIRECTION.down) {
                    mapRenderer.line(x + MINI_SIZE, y, x + SIZE - MINI_SIZE, y);
                }
            }
        }
    }


}






/*


public class MapGUI {

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





 */

package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedSet;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.enums.Direction;

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

    private Color currentRoomColor = new Color(0.5f, 0.5f, 0.5f, 0.6f);
    private Color roomColor = new Color(0.5f, 0.5f, 0.5f, 0.6f);
    private Color undiscoveredRoomColor = new Color(0.1f, 0.1f, 0.1f, 0.6f);
    private Color mapBackGroundColor = new Color(0.1f, 0.1f, 0.1f, 0.2f);
    private Color mapBorderColor = new Color(Color.WHITE);
    private Color borderColor = new Color(0.8f, 0.8f, 0.8f, 1);
    private Color doorColor = new Color(1f, 0f, 0f, 1f);
    private Color locationBlinkColor = new Color (1,1,1,1f);
    private Color bossRoomColor = new Color(0, 1, 1, 0.5f);
    private Color itemRoomColor = new Color (1, 0, 1, 0.5f);
    private Color shopRoomColor = new Color (234f / 255f, 185f / 255f, 157f / 255f, 1);

    private float mapBlinker;

    private float lineThickness = 4;

    private boolean blink;

    private int range = 3;

    private TextureAtlas atlas;
    private TextureRegion block;


    public ArenaGUI(float x, float y, Array<Arena> arenas, Arena currentRoom, TextureAtlas atlas) {
        this.mapx = x;
        this.mapy = y;
        this.arenas = arenas;
        this.currentRoom = currentRoom;
        this.atlas = atlas;
        this.block = atlas.findRegion(TextureStrings.BLOCK);
    }

    public ArenaGUI(float x, float y, float size, int range, Array<Arena> arenas, Arena currentRoom, TextureAtlas atlas) {
        this.mapx = x;
        this.mapy = y;
        this.arenas = arenas;
        this.SIZE = size;
        this.range = range;
        this.currentRoom = currentRoom;
        this.atlas = atlas;
        this.block = atlas.findRegion(TextureStrings.BLOCK);
    }


    public void update(float dt, float x, float y, OrderedSet<Arena> visitedArenas, OrderedSet<Arena> undiscoveredArenas, Arena currentRoom, MapCoords currentCoords){

        this.currentRoom = currentRoom;
        this.currentCoords = currentCoords;

        //System.out.println(currentCoords);

        this.arenas = visitedArenas.orderedItems();
        this.undiscoveredArenas = undiscoveredArenas.orderedItems();

        mapBlinker += dt;

        if(mapBlinker > 1.0){
            blink = !blink;
            mapBlinker = 0;
        }
        mapx = x;
        mapy = y;
    }


    public void draw(SpriteBatch batch) {
        drawMapContainer(batch);
        drawMapSquares(batch);
        drawMapLines(batch);
    }

    public void drawMapContainer(SpriteBatch batch){

        batch.setColor(mapBackGroundColor);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), mapx - SIZE * (range - 1), mapy - SIZE * (range - 1), SIZE * (range * 2 - 1), SIZE * (range * 2 - 1));
        //TODO used to be         mapRenderer.rect(mapx - SIZE * 2, mapy - SIZE * 2, SIZE * 5, SIZE * 5); which I guess could allow a rectangl
        batch.setColor(mapBorderColor);

        float x = mapx - SIZE * (range - 1);
        float y = mapy - SIZE * (range - 1);
        float width = SIZE * (range * 2 - 1);
        float height = SIZE * (range * 2 - 1);


        float x2 = x + width;
        float y2 = y + height;

        drawLineSquare(batch, x, y, width, height, lineThickness);
    }

    private void drawLineSquare(SpriteBatch batch, float x, float y, float width, float height, float thickness){

        float x2 = x + width;
        float y2 = y + height;
        //left
        drawLeftLine(batch, x, y, thickness, height);
        drawRightLine(batch, x2, y, thickness, height);
        drawBottomLine(batch, x, y, width, thickness);
        drawTopLine(batch, x, y2, width, thickness);
    }

    private void drawLeftLine(SpriteBatch batch, float x, float y, float thickness, float height){
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x, y - thickness, thickness, height + (thickness * 2));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y - thickness, thickness, height + (thickness * 2));

    }

    private void drawRightLine(SpriteBatch batch, float x, float y, float thickness, float height){
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y - thickness, thickness, height + (thickness * 2));
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x, y - thickness, thickness, height + (thickness * 2));
    }

    private void drawBottomLine(SpriteBatch batch, float x, float y, float width, float thickness){
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y - thickness, width + (thickness * 2), thickness);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y, width + (thickness * 2), thickness);

    }

    private void drawTopLine(SpriteBatch batch, float x, float y, float width, float thickness){
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y - thickness,  width + (thickness * 2), thickness);
        batch.draw(atlas.findRegion(TextureStrings.BLOCK), x - thickness, y, width + (thickness * 2), thickness);
    }


    public void drawMapSquares(SpriteBatch batch){

        for(Arena arena : arenas){
            Color c = (arena == currentRoom) ? currentRoomColor : roomColor;
            drawArenaSquare(batch, arena, c);
        }

        for(Arena arena : undiscoveredArenas){
            drawArenaSquare(batch, arena, undiscoveredRoomColor);
        }

        if(blink) {
            batch.setColor(locationBlinkColor);
            batch.draw(block, mapx, mapy, SIZE, SIZE);
        }

    }

    public void drawArenaSquare(SpriteBatch batch, Arena arena, Color roomColor){

        for(MapCoords m : arena.cotainingCoords) {
            batch.setColor(roomColor);
            int diffX = m.getX() - currentCoords.getX();
            int diffY = m.getY() - currentCoords.getY();

            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {
                batch.draw(atlas.findRegion(TextureStrings.BLOCK), mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);

                if (arena.roomType == Arena.RoomType.BOSS) {
                    batch.setColor(bossRoomColor);
                    batch.draw(atlas.findRegion(TextureStrings.BLOCK), mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);
                }

                if (arena.roomType == Arena.RoomType.ITEM) {
                    batch.setColor(itemRoomColor);
                    batch.draw(atlas.findRegion(TextureStrings.BLOCK), mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);
                }

                if (arena.roomType == Arena.RoomType.SHOP) {
                    batch.setColor(shopRoomColor);
                    batch.draw(atlas.findRegion(TextureStrings.BLOCK), mapx + (SIZE * diffX), mapy + (SIZE * diffY), SIZE, SIZE);
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



        for(Arena arena : undiscoveredArenas){
            drawArenaBorder(batch, arena);
        }

        for(Arena arena : arenas){
            drawArenaBorder(batch, arena);
            drawArenaDoors(batch, arena);
        }

    }


    public void drawArenaBorder(SpriteBatch batch, Arena arena){

        for(MapCoords m : arena.cotainingCoords) {
            batch.setColor(borderColor);
            int diffX = m.getX() - currentCoords.getX();
            int diffY = m.getY() - currentCoords.getY();
            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

/*                if(diffX > 0){
                  x -= lineThickness * diffX;
                } else if(diffX < 0){
                    x += lineThickness * Math.abs(diffX);
                }

                if(diffY > 0){
                    y -= lineThickness * diffX;
                } else if(diffY < 0){
                    y += lineThickness * Math.abs(diffX);
                }*/

                //Left Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX() - 1, m.getY()), false)) {
                    drawLeftLine(batch, x, y, lineThickness, SIZE);
                }
                //Right Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX() + 1, m.getY()), false)) {
                    drawRightLine(batch, x + SIZE, y, lineThickness, SIZE);
                }

                //Top Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX(), m.getY() + 1), false)) {
                    drawTopLine(batch, x, y + SIZE, SIZE, lineThickness);
                }

                //Bottom Line
                if (!arena.cotainingCoords.contains(new MapCoords(m.getX(), m.getY() - 1), false)) {
                    drawBottomLine(batch, x, y, SIZE, lineThickness);
                }
            }
        }
    }

    public void drawArenaDoors(SpriteBatch batch, Arena arena){
        for(DoorComponent dc : arena.doors) {


            batch.setColor(doorColor);

            int diffX = dc.currentCoords.getX() - currentCoords.getX();
            int diffY = dc.currentCoords.getY() - currentCoords.getY();

            if((diffX < range && diffX > -range) &&  (diffY < range && diffY > -range)) {

                float x = mapx + (SIZE * diffX);
                float y = mapy + (SIZE * diffY);

                float MINI_SIZE = SIZE / 4;

                //Left Line
                if (dc.exit == Direction.LEFT) {
                    drawLeftLine(batch, x, y + MINI_SIZE, lineThickness, SIZE / 2);
                }

                //Right Line
                if (dc.exit == Direction.RIGHT) {
                    drawRightLine(batch, x + SIZE, y + MINI_SIZE, lineThickness, SIZE / 2);
                }

                //Top Line
                if (dc.exit == Direction.UP) {
                    drawTopLine(batch, x + MINI_SIZE, y + SIZE, SIZE / 2, lineThickness);
                }

                //Bottom Line
                if (dc.exit == Direction.DOWN) {
                    drawBottomLine(batch, x + MINI_SIZE, y, SIZE / 2, lineThickness);
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

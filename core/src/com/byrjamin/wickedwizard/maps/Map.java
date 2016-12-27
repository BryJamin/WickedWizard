package com.byrjamin.wickedwizard.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.maps.rooms.BattleRoom;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 24/12/2016.
 */
public class Map {




    private Array<Room> rooms;

    private Room activeRoom;

    private boolean isTransitioning = true;

    ShapeRenderer shapeRenderer;

    private float SCREENMOVEMENT = 100f;

    private float timer;

    Vector2 screenWipePosition;

    public Map(){
        rooms = new Array<Room>();
        rooms.add(new BattleRoom());
        rooms.add(new BattleRoom());

        activeRoom = rooms.first();

        timer = 2f;

        screenWipePosition = new Vector2(activeRoom.ARENA_WIDTH, 0);

        shapeRenderer = new ShapeRenderer();

    }




    public void update(float dt, OrthographicCamera gamecam){

        if(activeRoom != null && isTransitioning) {
            activeRoom.update(dt, gamecam);
/*            if (activeRoom.isUnlocked() && rooms.size > 1) {
                rooms.removeIndex(0);
                activeRoom = rooms.get(0);
            }*/
        }

        screenWipePosition.add(-SCREENMOVEMENT, 0);


        if(screenWipePosition.x < 0 && timer > 0){
            screenWipePosition.x = 0;
            timer-=dt;
            System.out.println(timer);
        }

    }


    public void draw(SpriteBatch batch){
        activeRoom.draw(batch);
        batch.end();

        //Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0,0,0,1f);
        shapeRenderer.rect(screenWipePosition.x, screenWipePosition.y, activeRoom.ARENA_WIDTH, activeRoom.ARENA_HEIGHT);
        shapeRenderer.end();

        batch.begin();




    }

    public Room getActiveRoom() {
        return activeRoom;
    }
}

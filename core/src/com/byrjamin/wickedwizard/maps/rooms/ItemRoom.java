package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.maps.MapCoords;
import com.byrjamin.wickedwizard.maps.rooms.spawns.RoomEnemyWaves;

/**
 * Created by Home on 24/12/2016.
 */
public class ItemRoom extends Room{

    private ItemGenerator ig = new ItemGenerator();

    public ItemRoom(MapCoords mapCoords){
        super(mapCoords);
        items.add(ig.getItem(2));
        items.get(0).setCenter(this.WIDTH / 2, this.HEIGHT / 4);
    }

    public ItemRoom(int scalex, int scaley, MapCoords mapCoords){
        super(mapCoords, scalex, scaley);
        items.add(ig.getItem(2));
        items.get(0).setCenter(this.WIDTH / 2, this.groundHeight() * 2);
    }

    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);
    }


    public void draw(SpriteBatch batch){
        super.draw(batch);
    }


}

package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 24/12/2016.
 */
public class ItemRoom extends Room{

    private ItemGenerator ig = new ItemGenerator();
    private Array<Item> items = new Array<Item>();

    public ItemRoom(){
        super();
        items.add(ig.getItem(2));
        items.get(0).setCenter(this.WIDTH / 2, this.HEIGHT / 4);
    }

    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        for(Item item : items){
            if(wizard.getBounds().overlaps(item.getBoundingRectangle())){
                if(!item.isDestroyed()) {
                    item.use(wizard);
                }
            }

            if(item.isDestroyed()){
                items.removeValue(item, true);
            }
        }

    }


    public void draw(SpriteBatch batch){
        super.draw(batch);
        for(Item item : items) {
            item.draw(batch);
            BoundsDrawer.drawBounds(batch, item.getBoundingRectangle());
        }
    }


}

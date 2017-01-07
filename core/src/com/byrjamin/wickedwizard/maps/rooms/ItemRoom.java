package com.byrjamin.wickedwizard.maps.rooms;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.item.ItemGenerator;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 24/12/2016.
 */
public class ItemRoom extends Room{

    private ItemGenerator ig = new ItemGenerator();
    private Item item;

    private Sprite itemSprite;


    public ItemRoom(){
        super();
        item = ig.getItem(2);
        spawnItem(item);
    }


    private void spawnItem(Item item) {
        itemSprite = PlayScreen.atlas.createSprite(item.getSpriteName());
        itemSprite.setSize(MainGame.GAME_UNITS * 7, MainGame.GAME_UNITS * 7);
        itemSprite.setCenter(this.WIDTH / 2, (this.HEIGHT / 4));
    }

    public void update(float dt, OrthographicCamera gamecam){
        super.update(dt, gamecam);

        if(wizard.getBounds().overlaps(itemSprite.getBoundingRectangle())){
            wizard.applyItem(item);
        }


    }


    public void draw(SpriteBatch batch){
        super.draw(batch);
        itemSprite.draw(batch);
    }


}

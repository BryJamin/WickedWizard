package com.byrjamin.wickedwizard.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.entity.player.Wizard;

/**
 * Created by Home on 14/12/2016.
 */
public abstract class Item extends Sprite{


    //Require Parameter
    protected String name;

    public abstract void use(Wizard wizard);

    private boolean isDestroyed;


    public Item(TextureRegion region){
        if(region != null) {
            setRegion(region);
            setBounds(getX(), getY(), Measure.units(10), Measure.units(10));
            isDestroyed = false;
        }
    }

    public void destroy(){
        isDestroyed = true;
    }

    public void draw(SpriteBatch batch){
        if(!isDestroyed){
            super.draw(batch);
        }
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}

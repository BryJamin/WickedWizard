package com.byrjamin.wickedwizard.entity.enemies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.entity.Entity;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.maps.rooms.Room;

/**
 * Created by Home on 26/01/2017.
 */
public class Dummy extends Enemy{

    public Dummy(float posX, float posY, float width, float height, TextureRegion currentFrame){
        super();
        position = new Vector2(posX, posY);
        WIDTH = width;
        HEIGHT = height;
        health = 1;
        collisionBound = new Rectangle(posX, posY, WIDTH, HEIGHT);
        bounds.add(collisionBound);
        this.currentFrame = currentFrame;

        this.setDyingAnimation(AnimationPacker.genAnimation(0.05f, TextureStrings.EXPLOSION));

    }



    @Override
    public void update(float dt, Room r){
        super.update(dt, r);

        if(getState() == STATE.DYING){
            dyingUpdate(dt);
        }

    }












}

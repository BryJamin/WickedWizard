package com.byrjamin.wickedwizard.utils.collider;

import com.badlogic.gdx.math.Rectangle;


public class HitBox {

    public Rectangle hitbox;
    public float offsetX;
    public float offsetY;

    public HitBox(Rectangle hitbox){
        this.hitbox = hitbox;
        this.offsetX = 0;
        this.offsetY = 0;
    }

    public HitBox(Rectangle hitbox, float offsetX, float offsetY){
        this.hitbox = hitbox;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }



}

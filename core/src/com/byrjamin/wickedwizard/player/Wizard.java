package com.byrjamin.wickedwizard.player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.arenas.EnemyBullets;
import com.byrjamin.wickedwizard.helper.AnimationPacker;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.spelltypes.Projectile;
import com.byrjamin.wickedwizard.helper.Reloader;
import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public class Wizard {

    private int HEIGHT = MainGame.GAME_UNITS * 10;
    private int WIDTH = MainGame.GAME_UNITS * 10;
    private static final int GRAVITY = -MainGame.GAME_UNITS;

    private Vector3 position;

    private Reloader reloader;

    private boolean isFalling = true;

    private Vector3 velocity;

    //STATS
    private int health = 3;
    private int armor;
    private float damage = 1;
    private float reloadRate = 0.3f;

    private Sprite sprite;

    private float animationTime;

    private Animation standing;
    private Animation firing;


    private Animation currentAnimation;


    private Array<Item> items;


    public Wizard() {
        sprite = PlayScreen.atlas.createSprite("squ_walk");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        sprite.setPosition(200, 400);

        items = new Array<Item>();

        reloader = new Reloader(reloadRate);


        //Note firing animation speed is equal to the reloadRate divided by 10.

        standing = AnimationPacker.genAnimation(1 / 10f, "squ_walk", Animation.PlayMode.LOOP);
        firing = AnimationPacker.genAnimation(reloadRate / 10, "squ_firing");

        currentAnimation = standing;


    }


    public void update(float dt, Arena arena){
        animationTime += dt;
        reloader.update(dt);
        applyGravity(dt, arena);

        if(currentAnimation != standing && currentAnimation.isAnimationFinished(animationTime)){
            currentAnimation = standing;
            //animationTime = 0;
        }


    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }
        this.getSprite().setRegion(currentAnimation.getKeyFrame(animationTime));
        this.getSprite().draw(batch);
        BoundsDrawer.drawBounds(batch, getSprite().getBoundingRectangle());
    }



    public void teleport(float posX, float posY){
        if(posY > PlayScreen.GROUND_Y) {
            this.getSprite().setCenter(posX, posY);
        }
    }

    public void reduceHealth(float i){
        health -= i;
    }

    public Vector2 getCenter(){
        return new Vector2(this.getSprite().getX(), this.getSprite().getY());
    }


    public void increaseDamage(float d){
        damage += d;
    }

    public void increaseHealth(int h){
        health += h;
    }

    public void addItem(Item i){
        items.add(i);
    }


    public void applyItem(Item i){
        this.damage += i.getDamageIncrease();
        this.health += i.getHealthIncrease();
        items.add(i);
    }

    public void applyGravity(float dt, Arena arena){
            this.getSprite().translateY(GRAVITY);
            Rectangle r = arena.getOverlappingRectangle(this.getSprite().getBoundingRectangle());
            if(r != null) {
                this.getSprite().setY(r.getY() + r.getHeight());
            }
    }

    public Projectile generateProjectile(float input_x, float input_y){

        startfireAnimation();

        System.out.println(this.getSprite().getX() + this.getSprite().getWidth() / 2);
        System.out.println(this.getSprite().getY() + this.getSprite().getHeight() / 2);



        return (new Projectile.ProjectileBuilder(this.getSprite().getX() + this.getSprite().getWidth() / 2, this.getSprite().getY() + this.getSprite().getHeight() / 2, input_x,input_y)
                .spriteString("frost")
                .damage(1)
                .HORIZONTAL_VELOCITY(25f)
                .build());




/*        return new Projectile.ProjectileBuilder(this.getSprite().getX() + this.getSprite().getWidth() / 2, this.getSprite().getY() + this.getSprite().getHeight() / 2, input_x, input_y)
                .spriteString("fire")
                .damage(damage)
                .HORIZONTAL_VELOCITY(0f)
                .build();*/
    }

    private void startfireAnimation() {
        currentAnimation = firing;
        animationTime = 0;
    }

/*    public float calculateAngle(float x1,float y1, float x2, float y2){
        return (Math.atan2(y2 - y1, x2 - x1));
    }*/


    public Sprite getSprite() {
        return sprite;
    }

    public int getHealth() {
        return health;
    }

    public Reloader getReloader() {
        return reloader;
    }

    public void setReloader(Reloader reloader) {
        this.reloader = reloader;
    }


    public float getX() {
        return getSprite().getX();
    }

    public float getY() {
        return getSprite().getY();
    }
}

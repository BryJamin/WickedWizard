package com.byrjamin.wickedwizard.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
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


    private Array<Item> items;


    public Wizard() {
        sprite = PlayScreen.atlas.createSprite("wiz");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        position = new Vector3(300, 400, 0);
        sprite.setPosition(position.x, PlayScreen.GROUND_Y);

        items = new Array<Item>();

        reloader = new Reloader(reloadRate);

    }


    public void update(float dt, Arena arena){
        reloader.update(dt);
        applyGravity(dt, arena);
    }


    public void teleport(float posX, float posY){
        if(posY > PlayScreen.GROUND_Y) {
            this.getSprite().setCenter(posX, posY);
        }
    }

    public void reduceHealth(float i){
        health -= i;
    }

    public void draw(SpriteBatch batch){
        if(this.getHealth() <= 0){
            return;
        }
        this.getSprite().draw(batch);
    };

    public Vector2 getCenter(){
        return new Vector2(this.getSprite().getX() + WIDTH / 2, this.getSprite().getY() + HEIGHT / 2);
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
        return new Projectile.ProjectileBuilder(getCenter().x, getCenter().y, input_x, input_y)
                .spriteString("fire")
                .damage(damage)
                .HORIZONTAL_VELOCITY(30f)
                .build();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getHealth() {
        return health;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Reloader getReloader() {
        return reloader;
    }

    public void setReloader(Reloader reloader) {
        this.reloader = reloader;
    }



}

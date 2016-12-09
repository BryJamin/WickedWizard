package com.byrjamin.wickedwizard.sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.arenas.EnemyBullets;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.helper.Reloader;
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

    private float reloadRate = 0.2f;

    private Vector3 velocity;

    private int health = 10;


    private int damage = 1;


    private Sprite sprite;


    public Wizard() {
        sprite = PlayScreen.atlas.createSprite("wiz");
        sprite.setSize((float) HEIGHT, (float) WIDTH);
        position = new Vector3(300, 400, 0);
        sprite.setPosition(position.x, PlayScreen.GROUND_Y);

        reloader = new Reloader(reloadRate);

    }


    public void update(float dt){
        reloader.update(dt);

        if(this.getSprite().getY() > PlayScreen.GROUND_Y) {
            this.getSprite().translateY(GRAVITY);
            if (this.getSprite().getY() <= PlayScreen.GROUND_Y) {
                this.getSprite().setY(PlayScreen.GROUND_Y);
            }
        }



    }


    public void teleport(float posX, float posY){
        if(posY > PlayScreen.GROUND_Y) {
            this.getSprite().setCenter(posX, posY);
        }
    }

    public void reduceHealth(int i){
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



    public void fire(float dt, Wizard wizard){
/*        reloadTimer -= dt;
        if (reloadTimer <= 0) {
            EnemyBullets.activeBullets.add(new Projectile.ProjectileBuilder(this.getSprite().getX(), this.getSprite().getY(), wizard.getSprite().getX(),wizard.getSprite().getY())
                    .spriteString("bullet")
                    .damage(1)
                    .HORIZONTAL_VELOCITY(5f)
                    .dispell(isVertical ? Projectile.DISPELL.HORIZONTAL : Projectile.DISPELL.VERTICAL)
                    .build());
            reloadTimer += fireRate;
            isVertical = !isVertical;
        }*/
    }

    public Projectile generateProjectile(float input_x, float input_y){
        return new Projectile.ProjectileBuilder(getCenter().x, getCenter().y, input_x, input_y)
                .spriteString("fire")
                .damage(damage)
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

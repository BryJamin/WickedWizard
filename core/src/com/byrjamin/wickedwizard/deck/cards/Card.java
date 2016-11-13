package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 23/10/2016.
 */
public abstract class Card {

    public enum CardType {
        FIRE, ICE, ARCANE, LIGHTNING;
    }

    public enum ProjectileType {
        INSTANT, PROJECTILE
    }

    private String projectileSpriteName;

    private Sprite sprite;

    private int manaCost;

    private int baseDamage;

    private float fireTracker;

    private float fireRate;

    private boolean canFire;


    private Vector3 position;

    private CardType cardType;

    private ProjectileType projectileType;

    public Card(int posX, int posY, CardType ct){
        this.cardType = ct;
        position = new Vector3(posX, posY, 0);
        Sprite sprite = PlayScreen.atlas.createSprite("card_sword");
        sprite.setSize(MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 15);
        sprite.setCenter(posX, posY);
        this.setSprite(sprite);
        this.setFireRate(0.2f);
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public boolean isCanFire() {
        return canFire;
    }

    public void setCanFire(boolean canFire) {
        this.canFire = canFire;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public void update(float dt){
        fireTracker -= dt;
            if (fireTracker <= 0) {
                canFire = true;
                System.out.println("Can fire");
                fireTracker += fireRate;
            } else {
                canFire = false;
            }
    }

    public String getProjectileSpriteName() {
        return projectileSpriteName;
    }

    public void setProjectileSpriteName(String projectileSpriteName) {
        this.projectileSpriteName = projectileSpriteName;
    }

    public Projectile generateProjectile(float x1, float y1, float x2, float y2){
        return new Projectile(x1, y1, x2, y2, projectileSpriteName);
    }



    public void draw(SpriteBatch batch){
        this.getSprite().draw(batch);
    }

}

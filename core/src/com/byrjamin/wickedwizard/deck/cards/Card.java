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
        INSTANT, PROJECTILE, HUMAN, ARC
    }


    private Projectile projectile;

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public Projectile returnProjectile(float x1,float y1, float x2, float y2){
        this.projectile.projectileSetup(x1, y1, x2, y2);
        return projectile;
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

    public void update(float dt){
        fireTracker -= dt;
        if (fireTracker <= 0) {
            canFire = true;
            fireTracker += fireRate;
        } else {
            canFire = false;
        }
    }

    public Projectile generateProjectile(float x1, float y1, float x2, float y2){

        return new Projectile.ProjectileBuilder(x1, y1, x2, y2)
                .spriteString(projectileSpriteName)
                .damage(getBaseDamage())
                .build();
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
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

    public String getProjectileSpriteName() {
        return projectileSpriteName;
    }

    public void setProjectileSpriteName(String projectileSpriteName) {
        this.projectileSpriteName = projectileSpriteName;
    }

    public void draw(SpriteBatch batch){
        this.getSprite().draw(batch);
    }

}

package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.byrjamin.wickedwizard.MainGame;
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

    private Sprite sprite;

    private int manaCost;

    private int baseDamage;

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

    public void draw(SpriteBatch batch){
        this.getSprite().draw(batch);
    }

}

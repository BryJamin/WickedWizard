package com.byrjamin.wickedwizard.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Home on 23/10/2016.
 */
public abstract class Card {

    public enum CardType {
        FIRE, ICE, ARCANE, LIGHTNING;
    }

    private Sprite sprite;

    private int manaCost;

    private boolean clicked = false;

    private int position;

    private CardType cardType;

    public Card(CardType ct){
        this.cardType = ct;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}

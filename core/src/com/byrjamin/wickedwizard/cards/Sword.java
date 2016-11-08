package com.byrjamin.wickedwizard.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 30/10/2016.
 */
public class Sword extends Card{

    public Sword(int posX, int posY, CardType ct) {
        super(posX, posY, ct);
        Sprite sprite = PlayScreen.atlas.createSprite("card_sword");
        sprite.setSize(MainGame.GAME_UNITS * 10, MainGame.GAME_UNITS * 15);
        sprite.setPosition(posX, posY);
        this.setSprite(sprite);
        this.setBaseDamage(2);
        this.setCardType(CardType.ICE);
    }
}

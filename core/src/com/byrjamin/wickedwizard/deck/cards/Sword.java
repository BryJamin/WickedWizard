package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 30/10/2016.
 */
public class Sword extends Card {

    public Sword(int posX, int posY) {
        super(posX, posY, CardType.ARCANE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_sword"));
        this.setBaseDamage(3);
    }
}

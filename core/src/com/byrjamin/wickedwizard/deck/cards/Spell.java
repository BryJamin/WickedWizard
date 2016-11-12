package com.byrjamin.wickedwizard.deck.cards;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 30/10/2016.
 */
public class Spell extends Card {


    public Spell(int posX, int posY) {
        super(posX, posY, CardType.FIRE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_spell"));
        this.setBaseDamage(3);
    }
}

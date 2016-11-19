package com.byrjamin.wickedwizard.deck.cards;

import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 17/11/2016.
 */
public class Teleport extends Card {
    public Teleport(int posX, int posY) {
        super(posX, posY, CardType.ARCANE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_tele"));
        this.setBaseDamage(3);
        this.setProjectileType(ProjectileType.HUMAN);
       // this.setProjectileSpriteName("");
    }
}

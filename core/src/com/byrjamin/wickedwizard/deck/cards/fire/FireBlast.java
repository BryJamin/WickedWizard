package com.byrjamin.wickedwizard.deck.cards.fire;

import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.deck.cards.Card;
import com.byrjamin.wickedwizard.deck.cards.spelltypes.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 19/11/2016.
 */
public class FireBlast extends Card {


    public FireBlast(int posX, int posY) {
        super(posX, posY, Card.CardType.FIRE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_spell"));
        this.setBaseDamage(10);
        this.setProjectileType(ProjectileType.PROJECTILE);
        this.setFireRate(1f);
        this.setProjectileSpriteName("frost");
    }

    public Projectile generateProjectile(float x1, float y1, float x2, float y2){
        return new Projectile.ProjectileBuilder(x1, y1, x2, y2)
                .spriteString("fire")
                .damage(getBaseDamage())
                .areaOfEffect(new Rectangle(0,0, MainGame.GAME_UNITS * 30,MainGame.GAME_UNITS * 30))
                .build();
    }
}

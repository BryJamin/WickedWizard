package com.byrjamin.wickedwizard.deck.cards;

import com.byrjamin.wickedwizard.deck.cards.spellanims.ArcProjectile;
import com.byrjamin.wickedwizard.deck.cards.spellanims.Projectile;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 17/11/2016.
 */
public class ArcSpell extends Card{

    public ArcSpell(int posX, int posY) {
        super(posX, posY, Card.CardType.ICE);
        this.getSprite().setRegion(PlayScreen.atlas.findRegion("card_arc"));
        this.setBaseDamage(3);
        this.setProjectileType(ProjectileType.PROJECTILE);
        this.setFireRate(0.1f);
        this.setProjectileSpriteName("frost");
    }



    public Projectile generateProjectile(float x1, float y1, float x2, float y2){
        return new ArcProjectile(x1, y1, x2, y2, "frost");
    }
}

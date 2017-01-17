package com.byrjamin.wickedwizard.item.Items;

import com.byrjamin.wickedwizard.item.Item;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 07/01/2017.
 */
public class HealthUp extends Item{


    public HealthUp() {
        super(PlayScreen.atlas.findRegion("frost"));
    }

    @Override
    public void use(Wizard wizard) {
        wizard.increaseHealth(1);
        destroy();
    }

}

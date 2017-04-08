package com.byrjamin.wickedwizard.archive.item.Items;

import com.byrjamin.wickedwizard.archive.item.Item;
import com.byrjamin.wickedwizard.archive.gameobject.player.Wizard;
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

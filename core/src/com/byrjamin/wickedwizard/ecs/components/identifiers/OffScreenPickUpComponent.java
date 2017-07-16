package com.byrjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;
import com.byrjamin.wickedwizard.ecs.components.object.PickUpComponent;
import com.byrjamin.wickedwizard.factories.items.PickUp;

/**
 * Created by Home on 16/07/2017.
 */

public class OffScreenPickUpComponent extends PickUpComponent {
    public OffScreenPickUpComponent() {
    }

    public OffScreenPickUpComponent(PickUp pickUp) {
        super(pickUp);
    }
}

package com.byrjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;

/**
 * Created by Home on 29/04/2017.
 */

public class PlatformComponent extends Component {

    public static final float FALLTHROUGH_TIME = 0.45f;

    public boolean canPassThrough;
    public float timer;
}

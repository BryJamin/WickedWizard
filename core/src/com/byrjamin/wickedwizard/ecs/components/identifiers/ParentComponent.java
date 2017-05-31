package com.byrjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 05/04/2017.
 */

public class ParentComponent extends Component {
    public Array<com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent> children = new Array<com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent>();

    private boolean isLinked = true;

    public ParentComponent(){

    }

    public ParentComponent(com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent c) {
        children.add(c);
    }

}

package com.bryjamin.wickedwizard.ecs.components.identifiers;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 05/04/2017.
 */

public class ParentComponent extends Component {
    public Array<ChildComponent> children = new Array<ChildComponent>();

    private boolean isLinked = true;

    public ParentComponent(){

    }

    public ParentComponent(ChildComponent c) {
        children.add(c);
    }

}

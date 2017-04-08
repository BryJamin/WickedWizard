package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 05/04/2017.
 */

public class ParentComponent extends Component {
    public Array<ChildComponent> children = new Array<ChildComponent>();
}

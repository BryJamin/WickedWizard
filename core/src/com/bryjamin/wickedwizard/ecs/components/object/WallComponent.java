package com.bryjamin.wickedwizard.ecs.components.object;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 04/03/2017.
 */
public class WallComponent extends Component {

    public Rectangle bound;

    public WallComponent(Rectangle bound){
        this.bound = bound;
    }

    public WallComponent(){
        this(new Rectangle(0,0, Measure.units(5), Measure.units(5)));
    }

}

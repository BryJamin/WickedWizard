package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthComponent extends Component{
    public float health;

    public HealthComponent(float health) {
        this.health = health;
    }

    public HealthComponent(){
        this(1);
    }

}

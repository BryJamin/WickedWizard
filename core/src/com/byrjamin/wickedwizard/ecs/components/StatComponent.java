package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 15/04/2017.
 */

public class StatComponent extends Component {

    public float damage = 1;
    public float fireRate = 1;
    public float speed = 1;

    public StatComponent(){

    }

    public StatComponent(float damage, float fireRate, float speed) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.speed = speed;
    }
}

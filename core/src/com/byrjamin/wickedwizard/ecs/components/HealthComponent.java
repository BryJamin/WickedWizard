package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 05/03/2017.
 */
public class HealthComponent extends Component{
    public float health;
    public float maxHealth;

    private float accumulatedDamage;

    public HealthComponent(float health) {
        this.health = health;
        this.maxHealth = health;
    }

    public HealthComponent(){
        this(1);
    }


    public void applyDamage(float accumulatedDamage){
        this.accumulatedDamage += accumulatedDamage;
    }

    public float getAccumulatedDamage() {
        return accumulatedDamage;
    }

    public void clearDamage(){
        accumulatedDamage = 0;
    }

}

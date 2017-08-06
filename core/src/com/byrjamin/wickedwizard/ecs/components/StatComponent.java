package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 15/04/2017.
 */

public class StatComponent extends Component {


    public int health = 6;
    public int maxHealth = 6;


    //Your damage based on the weapon
    public float damage = 0;

    public int armor = 0;

    //How fast you fire your weapon
    public float fireRate = 0;

    //How fast the character travels curren max should be 5?
    public float speed = 0;

    //Increases crit chance, money drops? etc?
    public float luck = 0;

    //How far you shoot depending on the weapon
    public float range = 0;

    //How fast bullets travel
    public float shotSpeed = 0;

    //Crit Chance
    public float accuracy = 0;


    //Flat crit is 5% and is increaed by accuracy and luck
    public float crit = 5;

    public StatComponent(){

    }

    public StatComponent(float damage, float fireRate, float speed) {
        this.damage = damage;
        this.fireRate = fireRate;
        this.speed = speed;
    }


    public void applyStats(StatComponent statComponent){
        this.health = statComponent.health;
        this.maxHealth = statComponent.maxHealth;
        this.damage = statComponent.damage;
        this.armor = statComponent.armor;
        this.fireRate = statComponent.fireRate;
        this.speed = statComponent.speed;
        this.luck = statComponent.luck;
        this.range = statComponent.range;
        this.shotSpeed = statComponent.shotSpeed;
        this.accuracy = statComponent.accuracy;
        this.crit = statComponent.crit;

    }


}

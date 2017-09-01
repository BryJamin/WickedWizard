package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 05/03/2017.
 */
public class WeaponComponent extends Component{

    public Weapon weapon;
    public StateTimer timer;
    public float defaultStartTime;

    public WeaponComponent(Weapon weapon, float chargeTime){
        //this.reloadTime = reloadTime;
        timer = new StateTimer(weapon.getBaseFireRate(), chargeTime);
        this.weapon = weapon;
        this.defaultStartTime = chargeTime;
    }

    public WeaponComponent(Weapon weapon){
        //this.reloadTime = reloadTime;
        timer = new StateTimer(weapon.getBaseFireRate(), 0);
        this.weapon = weapon;
    }

    public void addChargeTime(float chargeTime){
        timer.timeRemaining = chargeTime;
    }

    public WeaponComponent(){}

}

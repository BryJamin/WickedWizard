package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 05/03/2017.
 */
public class WeaponComponent extends Component{

    public Weapon weapon;
    public StateTimer timer;

    public WeaponComponent(Weapon weapon, float delayTime){
        //this.reloadTime = reloadTime;
        timer = new StateTimer(weapon.getBaseFireRate(), delayTime);
        this.weapon = weapon;
    }

    public WeaponComponent(){
        this(WeaponFactory.EnemyWeapon(), 0);
    }


}

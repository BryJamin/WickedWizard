package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.WeaponFactory;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 05/03/2017.
 */
public class WeaponComponent extends Component{

    public float reloadTime;
    public float currentTime;

    public Weapon weapon;

    public StateTimer timer;

    public WeaponComponent(Weapon weapon, float reloadTime, float currentTime){
        this.reloadTime = reloadTime;
        this.currentTime = currentTime;
        timer = new StateTimer(reloadTime, currentTime);
        this.weapon = weapon;
    }

    public WeaponComponent(Weapon weapon, float reloadTime){
        this(weapon, reloadTime, 0);
    }


    public WeaponComponent(){
        this(WeaponFactory.EnemyWeapon(), 2.0f, 0);
    }


}

package com.bryjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;

/**
 * Created by BB on 20/09/2017.
 */

public class AdditionalWeaponComponent extends Component {

    public Array<WeaponComponent> additionalWeapons = new Array<WeaponComponent>();

    public AdditionalWeaponComponent(){

    }

    public AdditionalWeaponComponent(WeaponComponent... weapons){
        additionalWeapons.addAll(weapons);
    }

    public void add(WeaponComponent weapon){
        this.additionalWeapons.add(weapon);
    }








}

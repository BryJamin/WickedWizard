package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;

/**
 * Created by Home on 18/04/2017.
 */

public class CurrencyComponent extends Component {

    public int money = 0;
    public int keys = 0;

    public CurrencyComponent(){

    }

    public CurrencyComponent(int money){
        this.money = money;
    }

    public CurrencyComponent(int money, int keys){
        this.money = money;
        this.keys = keys;
    }

    public void updateCurrency(CurrencyComponent c){
        this.money = c.money;
        this.keys = c.keys;
    }

}

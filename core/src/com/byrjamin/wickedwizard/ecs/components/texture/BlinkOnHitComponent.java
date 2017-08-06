package com.byrjamin.wickedwizard.ecs.components.texture;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 09/03/2017.
 */
public class BlinkOnHitComponent extends Component {


    public enum BLINKTYPE {
        FLASHING, CONSTANT
    }

    public BLINKTYPE blinktype = BLINKTYPE.CONSTANT;

    public float blinkTimer = 0.15f;
    public float resetTimer = 0.15f;
    public float timeUntilNoLongerBlinking;
    public float resetTimeUntilNoLongerBlinking;

    public Color preBlinkColor;
    public Color blinkColor;


    public BlinkOnHitComponent(){
        timeUntilNoLongerBlinking = 0.04f;
        resetTimeUntilNoLongerBlinking = 0.04f;
    }

    public BlinkOnHitComponent(float timeUntilNoLongerBlinking, BLINKTYPE blinktype){
        this();
        this.timeUntilNoLongerBlinking = timeUntilNoLongerBlinking;
        this.resetTimeUntilNoLongerBlinking = timeUntilNoLongerBlinking;
        this.blinktype = blinktype;
    }

    public boolean isBlinking;
    public boolean isHit;





}

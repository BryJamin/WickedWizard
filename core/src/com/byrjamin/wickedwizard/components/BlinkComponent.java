package com.byrjamin.wickedwizard.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by Home on 09/03/2017.
 */
public class BlinkComponent extends Component {


    public enum BLINKTYPE {
        FLASHING, CONSTANT
    }

    public BLINKTYPE blinktype = BLINKTYPE.CONSTANT;

    public float blinkTimer = 0.15f;
    public float resetTimer = 0.15f;
    public float blinkFrames;
    public float resetFrames;

    public Color preBlinkColor;
    public Color blinkColor;


    public BlinkComponent(){
        blinkFrames = 0.1f;
        resetFrames = 0.1f;
    }

    public BlinkComponent(float blinkFrames, BLINKTYPE blinktype){
        this();
        this.blinkFrames = blinkFrames;
        this.resetFrames = blinkFrames;
        this.blinktype = blinktype;
    }

    public boolean isBlinking;
    public boolean isHit;





}

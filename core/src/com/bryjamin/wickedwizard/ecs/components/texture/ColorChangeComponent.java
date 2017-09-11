package com.bryjamin.wickedwizard.ecs.components.texture;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by BB on 10/09/2017.
 */

public class ColorChangeComponent extends Component {


    public Color startColor;
    public Color currentColor;
    public Color endColor;

    public ColorChangeComponent(){
        startColor = new Color(Color.WHITE);
        currentColor = new Color(Color.WHITE);
        endColor = new Color(Color.WHITE);
    }

    public ColorChangeComponent(Color start, Color end, float targetColorChangeTime, boolean isEndless){
        this.startColor = start;
        this.currentColor = new Color(startColor);
        this.endColor = end;
        this.targetColorChangeTime = targetColorChangeTime;
        this.isEndless = isEndless;
    }




    public float currentColorChangeTime;
    public float targetColorChangeTime;
    public float resetTime;


    public boolean isEndless = true;














}

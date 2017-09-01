package com.bryjamin.wickedwizard.ecs.components.texture;

import com.artemis.Component;

/**
 * Created by Home on 01/04/2017.
 */

public class FadeComponent extends Component {

    public float minAlpha = 0;
    public float maxAlpha = 1;

    public float alphaTimer = 0;
    public float alphaTimeLimit;

    public float alpha;

    public  boolean fadeIn;
    public boolean flicker;


    public int count;

    public boolean isEndless = true;

    public FadeComponent(){
        isEndless = true;
        alphaTimeLimit = 1;
    }


    public FadeComponent(boolean fadeIn, float alphaTimeLimit, boolean isEndless, float minAlpha, float maxAlpha) {
        this(fadeIn, alphaTimeLimit, minAlpha, maxAlpha);
        this.isEndless = isEndless;
    }


    public FadeComponent(boolean fadeIn, float alphaTimeLimit,float minAlpha, float maxAlpha){
        this.fadeIn = fadeIn;
        this.alphaTimeLimit = alphaTimeLimit;
        alphaTimer = fadeIn ? 0 : alphaTimeLimit;
        this.minAlpha = minAlpha;
        this.maxAlpha = maxAlpha;
    }

    public FadeComponent(boolean fadeIn, float alphaTimeLimit, boolean isEndless) {
        this(fadeIn, alphaTimeLimit, isEndless, 0, 1);
    }

    public FadeComponent(boolean fadeIn, float alphaTimeLimit, int count) {
        this(fadeIn, alphaTimeLimit, 0, 1);
        this.count = count;
        this.isEndless = false;
    }

    public FadeComponent(float alphaTimeLimit, boolean isEndless) {
        this.alphaTimeLimit = alphaTimeLimit;
        this.isEndless = isEndless;
    }
}

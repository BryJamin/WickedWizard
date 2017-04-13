package com.byrjamin.wickedwizard.ecs.components.texture;

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
    public boolean isEndless = true;

    public FadeComponent(){
        isEndless = true;
        alphaTimeLimit = 1;
    }

    public FadeComponent(float alphaTimeLimit, boolean isEndless) {
        this.alphaTimeLimit = alphaTimeLimit;
        this.isEndless = isEndless;
    }
}
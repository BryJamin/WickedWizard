package com.byrjamin.wickedwizard.ecs.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by Home on 07/03/2017.
 */
public class AnimationComponent extends Component {

    public IntMap<Animation<TextureRegion>> animations;

    public AnimationComponent(){
        animations = new IntMap<Animation<TextureRegion>>();
    }

    public AnimationComponent(IntMap<Animation<TextureRegion>> animations) {
        this.animations = animations;
    }
}

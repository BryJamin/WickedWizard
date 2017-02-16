package com.byrjamin.wickedwizard.maps.rooms.components;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Home on 31/12/2016.
 */
public class Bleeding {

    public static void fixBleeding(TextureRegion region) {
        float x = region.getRegionX();
        float y = region.getRegionY();
        float width = region.getRegionWidth();
        float height = region.getRegionHeight();
        float invTexWidth = 1f / region.getTexture().getWidth();
        float invTexHeight = 1f / region.getTexture().getHeight();
        region.setRegion((x + .5f) * invTexWidth, (y+.5f) * invTexHeight, (x + width - .5f) * invTexWidth, (y + height - .5f) * invTexHeight);
    }

}

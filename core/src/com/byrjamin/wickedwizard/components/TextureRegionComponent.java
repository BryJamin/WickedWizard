package com.byrjamin.wickedwizard.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 03/03/2017.
 */
public class TextureRegionComponent extends Component{

    public TextureRegion region;

    public TextureRegionComponent(TextureRegion region){
        this.region = region;
    }

    public TextureRegionComponent(){
        region = PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING);
    }

}

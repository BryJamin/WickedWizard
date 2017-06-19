package com.byrjamin.wickedwizard.factories.arenas.skins;

/**
 * Created by Home on 19/06/2017.
 */

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 07/05/2017.
 */

public class ItemRoomSkin extends  AbstractSkin{

    private Color gold = new Color(232f/255f, 207/255f, 118/255f, 1);
    private Color azu = new Color(56f/255f, 63/255f, 112/ 255f, 1);

    public ItemRoomSkin(TextureAtlas atlas) {
        super(atlas);
    }

    @Override
    public Color getBackgroundTint() {
        return gold;
    }

    //999999

    @Override
    public Color getWallTint() {
        return azu;
    }



}

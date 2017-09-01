package com.bryjamin.wickedwizard.ecs.systems.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.bryjamin.wickedwizard.GameTest;
import com.bryjamin.wickedwizard.factories.items.ItemStore;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Home on 13/05/2017.
 */

public class LevelItemSystemTest extends GameTest {



    @Test
    public void testItemRegions() throws Exception {

        System.out.println(Gdx.files.getLocalStoragePath());
        System.out.println(Gdx.files.getExternalStoragePath());


        AssetManager assetManager = new AssetManager();

        TextureAtlasLoader textureAtlasLoader = new TextureAtlasLoader(new LocalFileHandleResolver());

        assetManager.setLoader(TextureAtlas.class,textureAtlasLoader);
        assetManager.load("/android/assets/sprite.atlas", TextureAtlas.class);
        assetManager.finishLoading();
        TextureAtlas atlas = assetManager.get("/android/assets/sprite.atlas", TextureAtlas.class);


        ItemStore itemStore = new ItemStore(new Random());

        for(ItemStore.ItemOptions i : itemStore.getItemOptionsArray()) {
            Assert.assertTrue(i.item.getValues().region.getLeft()
                    + " index "
                    + i.item.getValues().region.getRight()
                    + " is not inside of the sprite atlas",
                    atlas.findRegion(i.item.getValues().region.getLeft()) != null);
        }

    }

}

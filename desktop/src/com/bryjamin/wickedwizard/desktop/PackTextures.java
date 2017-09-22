package com.bryjamin.wickedwizard.desktop;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by BB on 15/09/2017.
 */

public class PackTextures {



    public static void main (String args[]){

        String projectPath = System.getProperty("user.dir");
        String inputDir = projectPath + "/images/tobepacked";
        String outputDir = projectPath;
        String packFileName = "sprite";

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.filterMin = Texture.TextureFilter.Nearest;
        settings.filterMag = Texture.TextureFilter.Nearest;
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.duplicatePadding = true;
        settings.combineSubdirectories = true;

        TexturePacker.process(settings, inputDir,outputDir,packFileName);




    }


}

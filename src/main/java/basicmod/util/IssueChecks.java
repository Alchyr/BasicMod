package basicmod.util;

import basicmod.BasicMod;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

/**
 * A class containing utility methods for checking and logging common issues.
 */
@SpireInitializer
public class IssueChecks {
    public static void initialize() {
        characterAssetsCheck();
    }



    private static void characterAssetsCheck() {
        boolean validCharAssets = false;

        FileHandle charResources = new LwjglFileHandle(BasicMod.characterPath(""), Files.FileType.Internal);
        if (charResources.exists() && charResources.isDirectory()) {
            validCharAssets = true;
        }
    }
}

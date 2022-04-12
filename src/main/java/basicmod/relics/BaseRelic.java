package basicmod.relics;

import basemod.abstracts.CustomRelic;
import basicmod.util.TextureLoader;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import static basicmod.BasicMod.relicPath;

public abstract class BaseRelic extends CustomRelic {
    protected String imageName;

    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        super(id, TextureLoader.getTexture(relicPath(unPrefix(id) + ".png")), tier, sfx);

        this.imageName = unPrefix(id);
        outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"));
    }

    @Override
    public void loadLargeImg() {
        if (largeImg == null) {
            this.largeImg = ImageMaster.loadImage(relicPath("large/" + imageName + ".png"));
        }
    }

    private static String unPrefix(String id) {
        return id.substring(id.indexOf(":") + 1);
    }
}
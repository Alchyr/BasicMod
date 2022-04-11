package basicmod.relics;

import basemod.abstracts.CustomRelic;
import basicmod.util.TextureLoader;

import static basicmod.BasicMod.relicPath;

public abstract class BaseRelic extends CustomRelic {
    public BaseRelic(String id, String textureName, RelicTier tier, LandingSound sfx) {
        super(id, TextureLoader.getTexture(relicPath(textureName + ".png")), tier, sfx);
        outlineImg = TextureLoader.getTextureNull(relicPath(textureName + "Outline.png"));
    }
}
package basicmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.RelicType;
import basicmod.util.GeneralUtils;
import basicmod.util.TextureLoader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;

import static basicmod.BasicMod.relicPath;

public abstract class BaseRelic extends CustomRelic {
    public AbstractCard.CardColor pool = null;
    public RelicType relicType = RelicType.SHARED;
    protected String imageName;

    //for character specific relics
    public BaseRelic(String id, String imageName, AbstractCard.CardColor pool, RelicTier tier, LandingSound sfx) {
        this(id, imageName, tier, sfx);

        setPool(pool);
    }

    public BaseRelic(String id, RelicTier tier, LandingSound sfx) {
        this(id, GeneralUtils.removePrefix(id), tier, sfx);
    }

    //To use a basegame relic image, just pass in the imagename used by a basegame relic instead of the ID.
    //eg. "calendar.png"
    public BaseRelic(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(testStrings(id), notPng(imageName) ? "" : imageName, tier, sfx);

        this.imageName = imageName;
        if (notPng(imageName)) {
            loadTexture();
        }
    }

    protected void loadTexture() {
        this.img = TextureLoader.getTextureNull(relicPath(imageName + ".png"), true);
        if (img != null) {
            outlineImg = TextureLoader.getTextureNull(relicPath(imageName + "Outline.png"), true);
            if (outlineImg == null)
                outlineImg = img;
        }
        else {
            ImageMaster.loadRelicImg("Derp Rock", "derpRock.png");
            this.img = ImageMaster.getRelicImg("Derp Rock");
            this.outlineImg = ImageMaster.getRelicOutlineImg("Derp Rock");
        }
    }

    @Override
    public void loadLargeImg() {
        if (notPng(imageName)) {
            if (largeImg == null) {
                this.largeImg = ImageMaster.loadImage(relicPath("large/" + imageName + ".png"));
            }
        }
        else {
            super.loadLargeImg();
        }
    }

    private void setPool(AbstractCard.CardColor pool) {
        switch (pool) { //Basegame pools are handled differently
            case RED:
                relicType = RelicType.RED;
                break;
            case GREEN:
                relicType = RelicType.GREEN;
                break;
            case BLUE:
                relicType = RelicType.BLUE;
                break;
            case PURPLE:
                relicType = RelicType.PURPLE;
                break;
            default:
                this.pool = pool;
                break;
        }
    }

    /**
     * Checks whether relic has localization set up correctly and gives a more accurate error message if it does not
     * @param ID the relic's ID
     * @return the relic's ID, to allow use in super constructor invocation
     */
    private static String testStrings(String ID) {
        RelicStrings text = CardCrawlGame.languagePack.getRelicStrings(ID);
        if (text == null) {
            throw new RuntimeException("The \"" + ID + "\" relic does not have associated text. Make sure " +
                    "there's no issue with the RelicStrings.json file, and that the ID in the json file matches the " +
                    "relic's ID. It should look like \"${modID}:" + GeneralUtils.removePrefix(ID) + "\".");
        }
        return ID;
    }

    private static boolean notPng(String name) {
        return !name.endsWith(".png");
    }
}
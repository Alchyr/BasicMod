package basicmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.HashMap;
import java.util.Locale;

import static basicmod.BasicMod.*;

public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();

    /**
     * @param filePath - String path to the texture you want to load relative to resources,
     * Example: resourcePath("missing.png")
     * @return <b>com.badlogic.gdx.graphics.Texture</b> - The texture from the path provided
     */
    public static Texture getTexture(final String filePath) {
        return getTexture(filePath, true);
    }
    public static Texture getTexture(final String filePath, boolean linear) {
        if (textures.get(filePath) == null) {
            try {
                loadTexture(filePath, linear);
            } catch (GdxRuntimeException e) {
                try
                {
                    return getTexture(resourcePath("missing.png"), false);
                }
                catch (GdxRuntimeException ex) {
                    logger.info("missing.png is missing!");
                    return null;
                }
            }
        }
        return textures.get(filePath);
    }
    public static Texture getTextureNull(final String filePath) {
        if (textures.get(filePath) == null) {
            try {
                loadTexture(filePath);
            } catch (GdxRuntimeException e) {
                return null;
            }
        }
        return textures.get(filePath);
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType)
    {
        String textureString = resourcePath("cards/" + cardType.name().toLowerCase(Locale.ROOT) + "/" + cardName + ".png");

        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists())
        {
            switch (cardType) {
                case ATTACK:
                    textureString = resourcePath("cards/attack/default.png");
                    break;
                case POWER:
                    textureString = resourcePath("cards/power/default.png");
                    break;
                default:
                    textureString = resourcePath("cards/skill/default.png");
                    break;
            }
        }

        return textureString;
    }

    private static void loadTexture(final String textureString) throws GdxRuntimeException {
        loadTexture(textureString, false);
    }

    private static void loadTexture(final String textureString, boolean linearFilter) throws GdxRuntimeException {
        Texture texture = new Texture(textureString);
        if (linearFilter)
        {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        else
        {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        logger.info("Loaded texture " + textureString);
        textures.put(textureString, texture);
    }

    public static Texture getPowerTexture(final String powerName)
    {
        String textureString = powerPath(powerName + ".png");
        return getTexture(textureString);
    }
    public static Texture getHiDefPowerTexture(final String powerName)
    {
        String textureString = powerPath("large/" + powerName + ".png");
        return getTextureNull(textureString);
    }
}
package basicmod.util;

import basemod.helpers.KeywordColorInfo;

public class KeywordInfo {
    public String ID = "";
    public String PROPER_NAME;
    public String DESCRIPTION;
    public String[] NAMES;
    public String[] EXTRA = new String[] {};
    public KeywordColorInfo COLOR;

    public KeywordInfo() {
    }

    public void prep() {
        for (int i = 0; i < NAMES.length; ++i)
        {
            NAMES[i] = NAMES[i].toLowerCase();
        }
    }
}
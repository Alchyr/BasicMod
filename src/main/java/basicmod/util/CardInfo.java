package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardInfo {
    public final String baseId; //The card's ID not including the mod prefix.
    public final int baseCost;
    public final AbstractCard.CardType cardType;
    public final AbstractCard.CardTarget cardTarget;
    public final AbstractCard.CardRarity cardRarity;
    public final AbstractCard.CardColor cardColor;

    public CardInfo(String baseId, int baseCost, AbstractCard.CardType cardType, AbstractCard.CardTarget cardTarget, AbstractCard.CardRarity cardRarity, AbstractCard.CardColor cardColor)
    {
        this.baseId = baseId;
        this.baseCost = baseCost;
        this.cardType = cardType;
        this.cardTarget = cardTarget;
        this.cardRarity = cardRarity;
        this.cardColor = cardColor;
    }
}
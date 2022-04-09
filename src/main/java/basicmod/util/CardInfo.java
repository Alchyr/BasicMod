package basicmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class CardInfo {
    public final String cardName;
    public final int cardCost;
    public final AbstractCard.CardType cardType;
    public final AbstractCard.CardTarget cardTarget;
    public final AbstractCard.CardRarity cardRarity;
    public final AbstractCard.CardColor cardColor;

    public CardInfo(String cardName, int cardCost, AbstractCard.CardType cardType, AbstractCard.CardTarget cardTarget, AbstractCard.CardRarity cardRarity, AbstractCard.CardColor cardColor)
    {
        this.cardName = cardName;
        this.cardCost = cardCost;
        this.cardType = cardType;
        this.cardTarget = cardTarget;
        this.cardRarity = cardRarity;
        this.cardColor = cardColor;
    }
}
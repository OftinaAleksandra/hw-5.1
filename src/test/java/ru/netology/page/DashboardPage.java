package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.util.List;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private SelenideElement HEADING = $("[data-test-id=dashboard]");
    private List<SelenideElement> REPLENISH_BUTTONS = $$("[data-test-id=action-deposit]");


    public DashboardPage() {
        HEADING.shouldBe(Condition.visible);
    }

    public MoneyTransferOnCard replenishCard(int index) {
        REPLENISH_BUTTONS.get(index).click();
        return new MoneyTransferOnCard();
    }

    public int getBalance(String cardNumber) {
        String cardInfo = $(withText(cardNumber)).getText();
        String sumInfo = cardInfo.substring(cardInfo.indexOf("баланс:") + 7, cardInfo.lastIndexOf("р.")).trim();
        return Integer.parseInt(sumInfo);
    }
}

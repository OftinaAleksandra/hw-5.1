package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;

public class MoneyTransferOnCard {
    private SelenideElement amountField = $("[data-test-id= amount] input");
    private SelenideElement cardNumberField = $("[data-test-id=from] input");
    private SelenideElement transfer = $("[data-test-id=action-transfer]");

    private SelenideElement ERROR_NOTIFICATION = $(".notification_status_error");

    public DashboardPage validTransferMoney(List<DataHelper.Card> cards, int amount, int senderIndex) {
        amountField.setValue(String.valueOf(amount));
        cardNumberField.setValue(cards.get(senderIndex).getCardNumber());
        transfer.click();
        return new DashboardPage();
    }

    public void invalidTransferMoney (List<DataHelper.Card> cards, int amount, int senderIndex) {
        amountField.setValue(String.valueOf(amount));
        cardNumberField.setValue(cards.get(senderIndex).getCardNumber());
        transfer.click();
    }

    public void assertErrorNotificationIsVisible() {
        ERROR_NOTIFICATION.shouldBe(Condition.visible);
    }

}

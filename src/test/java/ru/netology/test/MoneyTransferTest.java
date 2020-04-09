package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.data.DataHelper;
import ru.netology.page.*;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeAll
    public static void setUp() {
        System.setProperty("chromeoptions.args", "--no-sandbox,--headless,--disable-dev-shm-usage");
    }

    @BeforeEach
    void openPage() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val verificationPage = loginPage.validLogin(DataHelper.getAuthInfo());
        val verificationCode = DataHelper.getVerificationCodeFor(DataHelper.getAuthInfo());
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/validTransferParams.csv", numLinesToSkip = 1)
    @DisplayName("Тест на перевод средств на сумму 5000")
    void transferBetweenCards(int recipientIndex, int senderIndex, String recipientCardNumber, String senderCardNumber)  {
        int amountTransfer = 5000;
        val recipientCardBalanceBeforeTransfer = dashboardPage.getBalance(recipientCardNumber);
        val senderCardBalanceBeforeTransfer = dashboardPage.getBalance(senderCardNumber);

        val moneyTransfer = dashboardPage.replenishCard(recipientIndex);
        val cards = DataHelper.getCardInfo();
        dashboardPage = moneyTransfer.validTransferMoney(cards, amountTransfer, senderIndex);

        val recipientCardBalanceAfterTransfer = dashboardPage.getBalance(recipientCardNumber);
        val senderCardBalanceAfterTransfer = dashboardPage.getBalance(senderCardNumber);
        val differenceRecipientCard = recipientCardBalanceBeforeTransfer + amountTransfer;
        val differenceSenderCard = senderCardBalanceBeforeTransfer - amountTransfer;
        assertEquals(recipientCardBalanceAfterTransfer, differenceRecipientCard);
        assertEquals(senderCardBalanceAfterTransfer, differenceSenderCard);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/validTransferParams.csv", numLinesToSkip = 1)
    @DisplayName("Тест на перевод средств превышающий баланс счета")
    void transferAmountExceedsAvailableFunds (int recipientIndex, int senderIndex, String recipientCardNumber, String senderCardNumber){
        int amountTransfer = 15_000;

        val recipientCardBalanceAfterTransfer = dashboardPage.getBalance(recipientCardNumber);
        int senderCardBalanceBeforeTransfer = dashboardPage.getBalance(senderCardNumber);

        val moneyTransfer = dashboardPage.replenishCard(recipientIndex);
        val cards = DataHelper.getCardInfo();
        moneyTransfer.invalidTransferMoney(cards, amountTransfer, senderIndex);
        moneyTransfer.assertErrorNotificationIsVisible();
    }
}



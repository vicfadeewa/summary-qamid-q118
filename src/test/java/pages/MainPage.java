package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement paymentButton = $(byText("Купить"));
    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement paymentByCard = $(byText("Оплата по карте"));
    private final SelenideElement paymentByCredit = $(byText("Кредит по данным карты"));

    public PaymentPage payByCard() {
        paymentButton.click();
        paymentByCard.shouldBe(visible);
        return new PaymentPage();
    }

    public CreditPage payByCredit() {
        creditButton.click();
        paymentByCredit.shouldBe(visible);
        return new CreditPage();
    }


}
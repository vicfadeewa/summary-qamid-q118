package pages;

import com.codeborne.selenide.SelenideElement;
import data.DataHelper.CardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

/**
 * Страница ввода данных платёжной карты.
 * Содержит элементы формы и методы взаимодействия с ними.
 */
public class PaymentPage {

    // Поля ввода
    private final SelenideElement cardNumberField = $(byText("Номер карты")).parent().$(".input__control");
    private final SelenideElement expiryMonthField = $(byText("Месяц")).parent().$(".input__control");
    private final SelenideElement expiryYearField = $(byText("Год")).parent().$(".input__control");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$(".input__control");
    private final SelenideElement cvcField = $(byText("CVC/CVV")).parent().$(".input__control");

    // Кнопка отправки
    private final SelenideElement submitButton = $(byText("Продолжить"));

    // Сообщения об ошибках
    private final SelenideElement cardNumberError = $(byText("Номер карты")).parent().$(".input__sub");
    private final SelenideElement monthError = $(byText("Месяц")).parent().$(".input__sub");
    private final SelenideElement yearError = $(byText("Год")).parent().$(".input__sub");
    private final SelenideElement expiredCardError = $(byText("Истёк срок действия карты")).parent().$(".input__sub");

    private final SelenideElement ownerError = $(byText("Владелец")).parent().$(".input__sub");
    private final SelenideElement cvcError = $(byText("CVC/CVV")).parent().$(".input__sub");

    /**
     * Заполняет форму данными карты и нажимает "Продолжить".
     */
    public void fillAndSubmit(CardInfo cardInfo) {
        cardNumberField.setValue(cardInfo.getCardNumber());
        expiryMonthField.setValue(cardInfo.getMonth());
        expiryYearField.setValue(cardInfo.getYear());
        ownerField.setValue(cardInfo.getOwner());
        cvcField.setValue(cardInfo.getCardCVC());
        submitButton.click();
    }

    /**
     * Проверяет появление ошибок при пустой форме.
     */
    public void validateEmptyForm() {
        submitButton.click();
        cardNumberError.shouldBe(visible);
        monthError.shouldBe(visible);
        yearError.shouldBe(visible);
        ownerError.shouldBe(visible);
        cvcError.shouldBe(visible);
    }

    /**
     * Утверждает видимость ошибки у поля "Номер карты".
     */
    public void assertCardNumberError() {
        cardNumberError.shouldBe(visible);
    }

    /**
     * Утверждает видимость ошибки у поля "Месяц".
     */
    public void assertMonthError() {
        monthError.shouldBe(visible);
    }

    /**
     * Утверждает видимость ошибки у поля "Год".
     */
    public void assertYearError() {
        yearError.shouldBe(visible);
    }

    /**
     * Утверждает видимость ошибки "Истёк срок действия карты".
     */
    public void assertExpiredCardError(Duration duration) {
        expiredCardError.shouldBe(visible);
    }

    /**
     * Утверждает видимость ошибки у поля "Владелец".
     */
    public void assertOwnerError() {
        ownerError.shouldBe(visible, Duration.ofSeconds(10));

    }

    /**
     * Утверждает видимость ошибки у поля "CVC/CVV".
     */
    public void assertCvcError() {
        cvcError.shouldBe(visible);
    }

    /**
     * Ожидает успешного уведомления о платеже.
     */
    public void waitForSuccessNotification() {
        $(".notification_status_ok")
                .shouldBe(visible, Duration.ofSeconds(30));
    }

    /**
     * Ожидает уведомления об отклонённом платеже.
     */
    public void waitForDeclineNotification() {
        $(byCssSelector(
                "div.notification.notification_status_error.notification_has-closer" +
                        ".notification_stick-to_right.notification_theme_alfa-on-white"))
                .shouldBe(visible, Duration.ofSeconds(20));
    }
}

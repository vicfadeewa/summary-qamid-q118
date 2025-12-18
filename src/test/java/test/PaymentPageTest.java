package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.PaymentPage;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.CardInfo.*;


public class PaymentPageTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
    }

//    Позитивные сценарии


    @Test
        // Проверяет, что открывается страница оплаты картой
    void shouldGetPaymentPage() {
        val mainPage = new MainPage();
        mainPage.payByCard();
    }


    @Test
        // Проверяет, что открывается страница оплаты в кредит
    void shouldGetCreditPage() {
        val mainPage = new MainPage();
        mainPage.payByCredit();
    }


    @Test
        // Проверяет успешную оплату картой с валидными данными с утвержденной картой
    void shouldPayByCardSuccessfully() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForSuccessNotification();
    }


    @Test
        // Проверяет обработку оплаты картой, которая была отклонена, оформить Issue
    void shouldNotPayWithDeclinedCard() {
        val cardInfo = new DataHelper.CardInfo(getDeclinedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForDeclineNotification();
    }

    //    Негативные сценарии

    @Test
        // Проверяет реакцию системы на ввод слишком короткого номера карты
    void shouldNotPayByShortCard() {
        val cardInfo = new DataHelper.CardInfo(getShortCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCardNumberError();
    }

    @Test
        // Обработка неизвестной карты
    void shouldNotPayByUnknownCard() {
        val cardInfo = new DataHelper.CardInfo(getUnknownCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForDeclineNotification();
    }

    @Test
        // Обработка номера карты, содержащего спецсимволы
    void shouldNotPayByCardWithSigns() {
        val cardInfo = new DataHelper.CardInfo(getCardNumberWithSigns(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCardNumberError();
    }

    @Test
        // Обработка номера карты, содержащего буквы
    void shouldNotPayByCardWithLetters() {
        val cardInfo = new DataHelper.CardInfo(getCardNumberWithLetters(), getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCardNumberError();
    }


    @Test
        // Проверяет обработку отсутствия номера карты
    void shouldNotPayWithoutCard() {
        val cardInfo = new DataHelper.CardInfo(null, getValidMonth(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCardNumberError();
    }


    @Test
        // Проверяет обработку месяца, превышающего 12
    void shouldNotPayWithMonthOver12() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getMonthOver12(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        // Проверяет обработку месяца, содержащего буквы
    void shouldNotPayWithMonthWithLetters() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getMonthWithLetters(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        // Проверяет обработку месяца, содержащего спецсимволы
    void shouldNotPayWithMonthWithSigns() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getMonthWithSigns(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        // Проверяет обработку месяца из 1 цифры
    void shouldNotPayWithMonthWithOneDigit() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getMonthWithOneDigit(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        // Проверяет обработку значения 00
    void shouldNotPayWithMonthWithNulls() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getMonthWithNulls(), getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        //  Проверяет обработку отсутствующего месяца
    void shouldNotPayWithoutMonth() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), null, getValidYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertMonthError();
    }


    @Test
        // Проверяет обработку прошедшего года
    void shouldNotPayWithPastYear() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getPastYear(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertExpiredCardError(Duration.ofSeconds(20));
    }


    @Test
        // Проверяет обработку года, содержащего буквы
    void shouldNotPayWithYearWithLetters() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getYearWithLetters(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertYearError();
    }


    @Test
        // Проверяет обработку года, содержащего спецсимволы
    void shouldNotPayWithYearWithSigns() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getYearWithSigns(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertYearError();
    }


    @Test
        // Проверяет обработку одноциферного года
    void shouldNotPayWithYearWithOneDigit() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getYearWithOneDigit(), getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertYearError();
    }


    @Test
        // Проверяет обработку года со значение null
    void shouldNotPayWithoutYear() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), null, getOwnerName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertYearError();
    }


    @Test
        // Проверяет обработку имени держателя карты, состоящего только из имени, оформить Issue
    void shouldNotPayWithFirstName() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerFirstName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }


    @Test
        // Проверяет обработку имени держателя карты на кириллице, оформить Issue
    void shouldNotPayWithNameInRussian() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerNameInRussia(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }


    @Test
        // Проверяет обработку имени держателя карты, содержащего цифры, оформить Issue
    void shouldNotPayWithNameWithDigits() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerNameWithDigits(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }


    @Test
        // Проверяет обработку имени держателя карты, содержащего спецсимволы, оформить Issue
    void shouldNotPayWithNameWithSigns() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerNameWithSigns(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }


    @Test
        // Проверяет обработку слишком короткого имени держателя карты, оформить Issue
    void shouldNotPayWithShortName() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerNameShort(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }


    @Test
        // Проверяет успешную оплату картой с двойным именем держателя
    void shouldPayWithDoubleName() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerNameWithDoubleName(), getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForSuccessNotification();
    }

    @Test
        // Проверяет обработку отсутствующего имени держателя карты
    void shouldNotPayWithoutName() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), null, getCVC());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertOwnerError();
    }

    @Test
        // Проверяет обработку CVC‑кода, содержащего буквы
    void shouldNotPayWithCVCwithLetters() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVCwithLetters());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCvcError();
    }

    @Test
        // Проверяет обработку CVC‑кода, содержащего спецсимволы
    void shouldNotPayWithCVCwithSigns() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVCwithSigns());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCvcError();
    }

    @Test
        // Проверяет обработку одноциферного CVC‑кода
    void shouldNotPayWithCVCwithOneDigit() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), getCVCshort());
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCvcError();
    }

    @Test
        // Проверяет обработку отсутствующего CVC‑кода
    void shouldNotPayWithoutCVC() {
        val cardInfo = new DataHelper.CardInfo(getApprovedCardNumber(), getValidMonth(), getValidYear(), getOwnerName(), null);
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.assertCvcError();
    }

    @Test
        // Проверяет валидацию пустой формы оплаты
    void shouldNotPayWithoutData() {
        val mainPage = new MainPage();
        mainPage.payByCard();
        val paymentPage = new PaymentPage();
        paymentPage.validateEmptyForm();
    }

}

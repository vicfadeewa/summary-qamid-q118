package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import data.DatabaseCleaner;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.CardInfo.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PaymentIntegrationTest {

    private static final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    static void setupAllureReporting() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void cleanupAllureReporting() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void prepareTestEnvironment() {
        open(BASE_URL);
        DatabaseCleaner.clearPaymentTable();
    }

    @Test
    @DisplayName("Оплата одобренной картой должна завершиться статусом APPROVED")
    void testApprovedCardPayment() {
        // данные карты
        val cardInfo = new DataHelper.CardInfo(
                getApprovedCardNumber(),
                getValidMonth(),
                getValidYear(),
                getOwnerName(),
                getCVC()
        );

        // взаимодействие с UI
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForSuccessNotification();

        // проверка результата в БД
        assertEquals("APPROVED", DatabaseCleaner.fetchPaymentStatus());
    }

    @Test
    @DisplayName("Оплата отклоненной картой должна завершиться статусом DECLINED")
    void testDeclinedCardPayment() {
        // подготовка данных карты
        val cardInfo = new DataHelper.CardInfo(
                getDeclinedCardNumber(),
                getValidMonth(),
                getValidYear(),
                getOwnerName(),
                getCVC()
        );

        // взаимодействие с UI
        val mainPage = new MainPage();
        val paymentPage = mainPage.payByCard();
        paymentPage.fillAndSubmit(cardInfo);
        paymentPage.waitForDeclineNotification();

        // проверка результата в БД
        assertEquals("DECLINED", DatabaseCleaner.fetchPaymentStatus());
    }
}

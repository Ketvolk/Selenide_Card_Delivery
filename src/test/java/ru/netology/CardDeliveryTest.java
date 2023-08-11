package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryTest {

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    String planningDate = generateDate(10, "dd.MM.yyyy");

    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999/");
    }

    // Part #1
    @Test
    void shouldDeliverCardByKeyboard() {

        $("[data-test-id='city'] input").setValue("Майкоп");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $(".notification__title").shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15)).shouldBe(visible);
        $(".notification__content").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test
    void shouldNotDeliverIfCityIsNotInList() {

        $("[data-test-id='city'] input").setValue("Лондон");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotDeliverIfNameIsNotRussian() {

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Ann");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotDeliverIfNameIsNumber() {

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("123456");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotDeliverIfNameIsSymbols() {

        $("[data-test-id='city'] input").setValue("Майкоп");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("^&%");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotDeliverIfDateIsEarly() {

        String planningDate = generateDate(2, "dd.MM.yyyy");
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+71234567890");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='date']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    // Part #2
    @Test
    void shouldDeliverCardByListsForSevenDays() {

        long addDays = 7;
        String planningDay = generateDate(addDays, "d");
        String planningDate = generateDate(addDays, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Ма");
        $$(".menu .menu-item__control").find(exactText("Майкоп")).click();
        $(".icon").click();

        LocalDate planningDateL = LocalDate.now().plusDays(addDays);
        LocalDate defaultDate = LocalDate.now().plusDays(3);

        ElementsCollection arrow = $$(".calendar__arrow_direction_right");
        int monthDiff = (planningDateL.getYear() - defaultDate.getYear()) * 12 + (planningDateL.getMonthValue() - defaultDate.getMonthValue());
        while (monthDiff > 0) {
            $(arrow.get(1)).click();
            monthDiff = monthDiff-1;
        }
        $$(".calendar__day").find(exactText(planningDay)).click();

        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id=notification].notification_visible .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно!"));
        $("[data-test-id=notification].notification_visible .notification__content").shouldBe(visible).shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    void shouldDeliverCardByListsForALongTime() {
        long addDays = 153;
        String planningDay = generateDate(addDays, "d");
        String planningDate = generateDate(addDays, "dd.MM.yyyy");

        $("[data-test-id='city'] input").setValue("Ма");
        $$(".menu .menu-item__control").find(exactText("Майкоп")).click();
        $(".icon").click();

        LocalDate planningDateL = LocalDate.now().plusDays(addDays);
        LocalDate defaultDate = LocalDate.now().plusDays(3);
        int monthDiff = (planningDateL.getYear() - defaultDate.getYear()) * 12 + (planningDateL.getMonthValue() - defaultDate.getMonthValue());

        ElementsCollection arrow = $$(".calendar__arrow_direction_right");
        while (monthDiff > 0) {
            $(arrow.get(1)).click();
            monthDiff = monthDiff - 1;
        }
        $$(".calendar__day").find(exactText(planningDay)).click();

        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id=notification].notification_visible .notification__title").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Успешно!"));
        $("[data-test-id=notification].notification_visible .notification__content").shouldBe(visible).shouldHave(exactText("Встреча успешно забронирована на " + planningDate));
    }
}



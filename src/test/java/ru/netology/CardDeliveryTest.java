package ru.netology;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryTest {


    @BeforeEach
    void openBrowser() {
        open("http://localhost:9999/");
    }

    // Part #1
    @Test
    void ShouldDeliverCardByKeyboard() throws InterruptedException {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 10);
        Date date10 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate10 = formatter.format(date10);

        $("[data-test-id='city'] input").setValue("Майкоп");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDate10);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        Thread.sleep(15000);
        $("[data-test-id='notification']").shouldHave(Condition.text("Успешно!"));
    }

    @Test
    void ShouldNotDeliverIfCityIsNotInList() {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 10);
        Date date10 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate10 = formatter.format(date10);

        $("[data-test-id='city'] input").setValue("Лондон");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDate10);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldHave(Condition.text("Доставка в выбранный город недоступна"));
    }

     @Test
    void ShouldNotDeliverIfNameIsNotRussian() {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 10);
        Date date10 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate10 = formatter.format(date10);

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDate10);
        $("[data-test-id='name'] input").setValue("Ann");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotDeliverIfNameIsNumber() {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 10);
        Date date10 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate10 = formatter.format(date10);

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDate10);
        $("[data-test-id='name'] input").setValue("123456");
        $("[data-test-id='phone'] input").setValue("+79012345675");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotDeliverIfNameIsSymbols() {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 10);
        Date date10 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate10 = formatter.format(date10);

        $("[data-test-id='city'] input").setValue("Майкоп");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDate10);
        $("[data-test-id='name'] input").setValue("^&%");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void ShouldNotDeliverIfDateIsEarly() {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -5);
        Date dateNegative = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDated = formatter.format(dateNegative);

        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.UP), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(formattedDated);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+71234567890");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        $("[data-test-id='date']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"));
    }

    // Part #2
    @Test
    void ShouldDeliverCardByLists() throws InterruptedException {

        Calendar.getInstance();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, 7);
        Date date7 = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate7 = formatter.format(date7);
        $("[data-test-id='city'] input").setValue("Ма");
        $$(".menu .menu-item__control").find(exactText("Майкоп")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(formattedDate7);
        $("[data-test-id='name'] input").setValue("Васильков Василий");
        $("[data-test-id='phone'] input").setValue("+79012345678");
        $("[data-test-id='agreement']").click();
        $("[class='button__content']").click();
        Thread.sleep(15000);
        $("[data-test-id='notification']").shouldHave(Condition.text("Успешно!"));
    }
}

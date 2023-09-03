package ru.ibs.ui.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.ibs.ui.pages.ProductsPage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.ui.steps.UiHooks.*;

/**
 * Класс с шагами графического интерфейса
 */
public class UiSteps {

    /**
     * Экземпляр страницы "Товары"
     */
    private ProductsPage productsPage;

    @И("Перейти на страницу \"Товары\"")
    public void getPage(){
        chromeDriver.get(baseUrl);
        productsPage = new ProductsPage(chromeDriver);
    }

    @И("Открыта страница \"Товары\"")
    public void productPageIsOpen(){
        Assertions.assertTrue(productsPage
                        .getTableName()
                        .isDisplayed(),
                "Заголовок страницы не отображен");
    }

    @И("Таблица названа {string}")
    public void checkTableName(String tableName){
        Assertions.assertEquals(tableName,
                productsPage.getTableName().getText(),
                "Таблица не названа "+tableName);
    }

    @И("В таблице есть все заголовки {string}, {string},{string}")
    public void containsTableHeadNames(String header1, String header2, String header3){
        List<String> stringList = Arrays.asList(header1, header2, header3);
        Assertions.assertTrue(productsPage.getTableHeads()
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList())
                        .containsAll(stringList),
                "В таблице нет заголовков(есть,но не все) из параметров");
    }

    @И("В таблице нет строк с \"null\" идентификаторами")
    public void checkRows(){
        Assertions.assertTrue(
                productsPage.getIdentifiers()
                        .stream().
                        noneMatch(s -> s.getText() == null),
                "Идентификатор строки null");
    }

    @И("Отображена кнопка {string}")
    public void buttonWithTextIsDisplayed(String text){
        Assertions.assertTrue
                (productsPage.getButtonByText(text).isDisplayed(),
                        "Кнопка с текстом "+text+" не отображена");
    }
    @И("Кнопка {string} имеет текст {string}")
    public void buttonWithTextHasName(String text, String name){
        Assertions.assertEquals(
                name, productsPage.getButtonByText(text).getText(),
                "Кнопка "+text+"не имеет текст "+name);
    }
    @И("Кнопка {string} имеет цвет {string}")
    public void buttonWithTextHasColor(String text, String color){
        Assertions.assertEquals(color,
                Color.fromString(
                productsPage.getButtonByText(text)
                        .getCssValue("background-color"))
                .asHex(),
                "Цвет кнопки "+text+" не соответствует "+color);
    }

    @И("Нажать кнопку {string}")
    public void clickButtonWithText(String text){
        wait.until(ExpectedConditions.elementToBeClickable(productsPage.getButtonByText(text)));
        productsPage.getButtonByText(text).click();
        waitModal();
        //productsPage.waitModalWindow();
    }

    @И("Открыто всплывающее окно \"Добавление товара\"")
    public void modalWindowIsOpen(){
        Assertions.assertTrue(
                productsPage.getAddProductHeader()
                        .isDisplayed(),
                "Заголовок формы добавления не отображен");
    }
    @И("Закрыто всплывающее окно \"Добавление товара\"")
    public void modalWindowIsClosed(){
        Assertions.assertFalse(
                productsPage.getAddProductHeader()
                        .isDisplayed(),
                "Форма добавления не закрыта");
    }
    @И("Окно содержит поле \"Наименование\"")
    public void modalWindowContainsNameField(){

        Assertions.assertTrue(
                productsPage.getAddProductName()
                        .isDisplayed(),
                "Поле ввода 'Наименование' не отображен");
    }
    @И("Окно содержит поле \"Тип\"")
    public void modalWindowContainsTypeField(){

        Assertions.assertTrue(
                productsPage.getAddType()
                        .isDisplayed(),
                "Поле ввода 'Тип' не отображен");
    }
    @И("В поле \"Тип\" на выбор 2 варианта {string}, {string}")
    public void checkType(String string1, String string2){

        Assertions.assertTrue(productsPage.getAllAddTypes().size() == 2
                        && productsPage.getAllAddTypes()
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList())
                        .containsAll(Arrays.asList(string1, string2)),
                "Проверки на количество и/или содержимое поля 'Тип' не прошли");
    }
    @И("Окно содержит чек-бокс \"Экзотический\"")
    public void checkboxIsDisplayed(){
        Assertions.assertTrue(
                productsPage.getAddExoticCheckBox()
                        .isDisplayed(),
                "Чек-бокс не отображен");
    }
    @И("Установить чек-бокс \"Экзотический\" {string}")
    public void activateCheckBox(String exotic){
        productsPage.setAddExoticCheckBox(exotic);
    }
    @И("Чек-бокс \"Экзотический\" установлен {string}")
    public void checkboxIsActivated(String exotic){
        if(exotic.equals("true")){Assertions.assertTrue(
                productsPage.getAddExoticCheckBox()
                        .isSelected(),
                "Чек-бокс не активирован");}
        else{Assertions.assertFalse(
                productsPage.getAddExoticCheckBox()
                        .isSelected(),
                "Чек-бокс активирован");}
    }
    @И("Окно содержит кнопку \"Сохранить\"")
    public void containsSaveButton(){
        Assertions.assertTrue(
                productsPage.getSaveButton()
                        .isDisplayed(),
                "Кнопка 'Сохранить' не отображена");
    }

    @И("В поле \"Наименование\" ввести значение {string}")
    public void sendKeysToField(String string){
        productsPage.getAddProductName().sendKeys(string);
    }
    @И("В поле \"Тип\" выбрать значение {string}")
    public void chooseVariant(String type){
        productsPage.setAddType(type);
    }
    @И("Введено значение {string} в поле \"Наименование\"")
    public void checkFieldName(String string){
        //;
        Assertions.assertEquals(
                string, productsPage.getAddProductName()
                        .getAttribute("value"),
                "Имя в поле Наименование не соответствует введенному");
    }
    @И("Выбрано значение {string} в поле \"Тип\"")
    public void checkFieldType(String string){
        Assertions.assertTrue(productsPage
                        .getAddType().getDomProperty("textContent")
                        .contains(string),
                "Тип в поле Тип не соответствует введенному");
    }

    @И("Проверить, что в таблице есть строка со всеми данными {string} {string} {string}")
    public void checkAddProductData(String foodName, String type, String exotic){
        List<String> list = Arrays.asList(foodName,type,exotic);
        Assertions.assertTrue(productsPage
                .getRowsData()
                .stream()
                .map(WebElement::getText)
                .anyMatch(s -> {
                    for (String l :
                            list) {
                        if (!s.contains(l)) return false;
                    }
                    return true;
                }),
                "Данные в таблице не соответствуют добавляемым параметрам");
    }

    @И("Подождать модальное окно")
    public void waitModal(){
        if(productsPage.getModalClass().equals("dialog")){
            wait.until(ExpectedConditions.invisibilityOf(productsPage.getAddProductHeader()));
        }else{
            wait.until(ExpectedConditions.visibilityOf(productsPage.getAddProductHeader()));
        }
        productsPage.setModalClass(productsPage.getEditModal().getAriaRole());
    }
}

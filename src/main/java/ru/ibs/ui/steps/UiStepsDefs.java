package ru.ibs.ui.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.ibs.ui.pages.ProductsPage;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Класс с шагами графического интерфейса
 */
public class UiStepsDefs {

    /**
     * Экземпляр страницы "Товары"
     */
    private ProductsPage productsPage;
    /**
     * Экземпляр Chrome Driver
     */
    private WebDriver chromeDriver;

    /**
     * Explicitly wait
     */
    private WebDriverWait wait;

    /**
     * Стартовый URL
     */
    private String baseUrl;

    /**
     * Выполняется перед каждым тестом
     */
    @Before("@Ui")
    public void before() {
        System.setProperty("webdriver.chrome.driver", System.getenv("CHROME_DRIVER"));
        chromeDriver = new ChromeDriver();
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(10));
        chromeDriver.manage().window().maximize();
        chromeDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        Properties uiProperties = new Properties();
        try {
            uiProperties.load(getClass().getResourceAsStream("/ui.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Не удалось загрузить файл ui.properties",e);
        }
        baseUrl=uiProperties.getProperty("base.url");
    }

    /**
     * Выполняется после каждого теста
     */
    @After("@Ui")
    public void after() {
        productsPage=null;
        chromeDriver.quit();
    }

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
    @И("^Форма содержит (поле|чек-бокс) \"(.*)\"$")
    public void formContains(String type,String text){
        boolean displayed=false;
        switch (text) {
            case "Экзотический" -> displayed = productsPage
                    .getFormElementsWithId()
                    .stream()
                    .filter(s -> Objects.equals(s.getAttribute("id"), "exotic"))
                    .toList().get(0).isDisplayed();
            case "Наименование" -> displayed = productsPage
                    .getFormElementsWithId()
                    .stream()
                    .filter(s -> Objects.equals(s.getAttribute("name"), "name"))
                    .toList().get(0).isDisplayed();
            case "Тип" -> displayed = productsPage
                    .getFormElementsWithId()
                    .stream()
                    .filter(s -> Objects.equals(s.getAttribute("id"), "type"))
                    .toList().get(0).isDisplayed();
        }
            Assertions.assertTrue(displayed,"Элемент "+type +text+ " не отображен");

    }


    @И("^В поле \"Тип\" на выбор (\\d+) варианта:(.*)$")
    public void checkTypeVariants(int count, String string){
        List<String> list = Arrays.asList(string.replaceAll("\\s", "").trim().split(","));
        System.out.println(list);
        Assertions.assertEquals(list.size(), count,
                "Проверь параметры передаваемые в \"Тип\"!");
        Assertions.assertTrue(productsPage.getAllAddTypes()
                        .stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList())
                        .containsAll(list),
                "Проверки на содержимое поля 'Тип' не прошли");
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
    @И("Кнопка {string} отображена")
    public void buttonIsDisplayed(String name){
        Assertions.assertTrue(
                productsPage.getButtonByText(name).isDisplayed(),
                "Кнопка "+name+" не отображена");
    }

    @И("В поле \"Наименование\" ввести значение {string}")
    public void sendKeysToField(String string){
        productsPage.getAddProductName().sendKeys(string);
    }
    @И("В поле \"Тип\" выбрать значение {string}")
    public void chooseVariant(String type){
        productsPage.setAddType(type);
    }
    @И("^В (поле|чек-бокс) \"(.*)\" введено значение \"(.*)\"$")
    public void checkValueInField(String type,String name,String value){
        boolean displayed=false;
        switch (name) {
            case "Наименование" -> {
                Assertions.assertEquals(
                        value, productsPage.getAddProductName()
                                .getAttribute("value"),
                        "Имя в поле Наименование не соответствует введенному");
                displayed = true;
            }
            case "Тип" -> {
                Assertions.assertTrue(productsPage
                                .getAddType().getDomProperty("textContent")
                                .contains(value),
                        "Тип в поле Тип не соответствует введенному");
                displayed = true;

            }
            case "Экзотический" -> {
                if (value.contains("true")) {
                    Assertions.assertTrue(
                            productsPage.getAddExoticCheckBox()
                                    .isSelected(),
                            "Чек-бокс не активирован");
                } else {
                    Assertions.assertFalse(
                            productsPage.getAddExoticCheckBox()
                                    .isSelected(),
                            "Чек-бокс активирован");
                }
                displayed = true;
            }
        }
        Assertions.assertTrue(displayed,"Шаг проверки значений в "+type+name+" не работает");
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

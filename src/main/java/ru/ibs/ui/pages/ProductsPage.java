package ru.ibs.ui.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Модель страницы "Товары"
 */
@Getter
public class ProductsPage {

    protected WebDriver chromeDriver;
    /**
     * Название таблицы
     */
    @FindBy(xpath = "//div[@class='container-fluid']/h5")
    private WebElement tableName;
    /**
     * Заголовки таблицы(1ая строка)
     */
    @FindBy(xpath = "//div/h5/../table/thead//th")
    private List<WebElement> tableHeads;
    /**
     * Строки с данными
     */
    @FindBy(xpath = "//div/h5/../table/tbody//tr")
    private List<WebElement> rowsData;

    /**
     * Кнопка "Добавить"
     */
    @FindBy(xpath = "//button[@data-toggle='modal']")
    private WebElement addButton;
    /**
     * Заголовок формы добавления
     */
    @FindBy(xpath = "//div[@id='editModal']//div[@class='modal-header']/h5")
    private WebElement addProductHeader;
    /**
     * Поле ввода "Название"
     */
    @FindBy(xpath = "//div[@id='editModal']//div[@class='modal-body']//div/input[@id='name']")
    private WebElement addProductName;
    /**
     * Выпадающий список "Тип"
     */
    @FindBy(xpath = "//div[@id='editModal']//div[@class='modal-body']//div/select[@id='type']")
    private WebElement addType;
    /**
     * Чек-бокс "Экзотический"
     */
    @FindBy(xpath = "//div[@id='editModal']//div[@class='modal-body']//div/input[@id='exotic']")
    private WebElement addExoticCheckBox;
    /**
     * Кнопка "Сохранить"
     */
    @FindBy(xpath = "//div[@id='editModal']//div[@class='modal-footer']/button[@id='save']")
    private WebElement saveButton;

    /**
     * Список кнопок
     */
    @FindBy(xpath = "//button[text()]")
    private List<WebElement> buttonsWithText;

    /**
     * Edit Modal Flag
     */
    @FindBy(id = "editModal")
    private WebElement editModal;
    /**
     * Wait flag
     */
    String modalClass;

    /**
     * Конструктор с использованием PageFactory Pattern     *
     *
     * @param chromeDriver передаваемый драйвер
     */
    public ProductsPage(WebDriver chromeDriver) {
        PageFactory.initElements(chromeDriver, this);
        this.chromeDriver = chromeDriver;
        this.modalClass=getEditModal().getAriaRole();
    }

    /**
     * Функция установки поля "Тип"
     *
     * @param string Строка с полным текстом искомого "Типа"
     */
    public void setAddType(String string) {
        getAddType().findElement(By.xpath("//option[text()='" + string + "']")).click();

    }

    /**
     * Функция возвращает все варианты в выпадающем списке "Тип"
     *
     * @return Список всех элементов
     */
    public ArrayList<WebElement> getAllAddTypes() {
        return new ArrayList<>(getAddType().findElements(By.xpath("./option")));
    }

    /**
     * Функция установки чек-бокса
     * Не самая умная)
     *
     * @param exotic переменная вида "true"|"false"
     */
    public void setAddExoticCheckBox(String exotic) {
        if (exotic.equals("true")) getAddExoticCheckBox().click();
    }

    /**
     * Функция получения id строк
     *
     * @return Список идентификаторов
     */
    public List<WebElement> getIdentifiers() {
        return getRowsData()
                .stream()
                .map(s -> s.findElement(By.xpath("./th")))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает кнопку с Заданным текстом
     *
     * @param text заданный текст
     * @return кнопка
     */
    public WebElement getButtonByText(String text) {
        return getButtonsWithText()
                .stream()
                .filter(s -> s.getText().equals(text)).toList().get(0);
    }

    public void setModalClass(String ariaRole) {
        modalClass=ariaRole;
    }
}
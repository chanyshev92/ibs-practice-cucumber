package ru.ibs.db.steps;

import com.zaxxer.hikari.HikariDataSource;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.ibs.db.models.Food;
import ru.ibs.db.repositories.FoodsRepository;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Класс с шагами для доступа к БД
 */
public class DBSteps {
    private Food product;

    private DataSource dataSource;

    private FoodsRepository foodsRepository;

    /**
     * Выполняется перед каждым тестом
     */
    @Before("@Db")
    public void before() {
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(new BufferedReader(
                    new InputStreamReader(DBSteps.class.getResourceAsStream("/db.properties"))));
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось загрузить файл db.properties",e);
        }

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPassword(dbProperties.getProperty("db.password"));
        hikariDataSource.setUsername(dbProperties.getProperty("db.username"));
        hikariDataSource.setJdbcUrl(dbProperties.getProperty("db.url"));
        hikariDataSource.setMaximumPoolSize(Integer.parseInt(dbProperties.getProperty("db.hikari.maxPoolSize")));

        dataSource=hikariDataSource;
        foodsRepository=FoodsRepository.getInstance(dataSource);
    }

    /**
     * Выполняется после каждого теста
     */
    @After("@Db")
    public void after() {
        foodsRepository=null;
        dataSource=null;
    }

    @И("Соединение с БД установлено")
    public void isConnected(){
        Assertions.assertTrue(foodsRepository.isConnectedToDb(),
                "Соединение с Бд не установлено");
    }
    @И("В БД есть непустая таблица food")
    public void checkRepo(){
        Assertions.assertFalse(foodsRepository.findAll().isEmpty(),
                "Получена пустая таблица Food");
    }
    @И("Добавить в таблицу food продукт с параметрами {string} {string} {int}")
    public void saveProduct(String foodName, String foodType, int exotic){
        product = Food.builder()
                .foodName(foodName)
                .foodType(foodType)
                .foodExotic(exotic).build();
        foodsRepository.save(product);
    }

    @И("Проверить, что добавленный продукт присутствует в таблице")
    public void assertContains(){
        Assertions.assertTrue(foodsRepository.checkByDescription(product),
                "Продукт с введенными данными не найден в таблице");
    }

    @И("Удалить добавленный продукт")
    public void deleteProduct(){
        foodsRepository.delete(product);
    }
}

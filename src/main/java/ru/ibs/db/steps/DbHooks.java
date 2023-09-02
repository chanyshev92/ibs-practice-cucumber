package ru.ibs.db.steps;

import com.zaxxer.hikari.HikariDataSource;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import ru.ibs.db.repositories.FoodsRepository;
import ru.ibs.db.repositories.FoodsRepositoryImplementationSpringJdbc;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Класс для доступа к БД
 */
public class DbHooks {

    private static DataSource dataSource;

    static FoodsRepository foodsRepository;

    /**
     * Выполняется перед каждым тестом
     */
    @Before
    public void before() {
        Properties dbProperties = new Properties();

        try {
            dbProperties.load(new BufferedReader(
                    new InputStreamReader(DbHooks.class.getResourceAsStream("/db.properties"))));
        } catch (IOException e) {
            throw new IllegalArgumentException("Не удалось загрузить файл db.properties",e);
        }

        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setPassword(dbProperties.getProperty("db.password"));
        hikariDataSource.setUsername(dbProperties.getProperty("db.username"));
        hikariDataSource.setJdbcUrl(dbProperties.getProperty("db.url"));
        hikariDataSource.setMaximumPoolSize(Integer.parseInt(dbProperties.getProperty("db.hikari.maxPoolSize")));

        dataSource=hikariDataSource;
        foodsRepository=new FoodsRepositoryImplementationSpringJdbc(dataSource);
    }

    /**
     * Выполняется после каждого теста
     */
    @After
    public void after() {
        foodsRepository=null;
        dataSource=null;
    }

}

package ru.ibs.db.steps;

import io.cucumber.java.ru.И;
import org.junit.jupiter.api.Assertions;
import ru.ibs.db.models.Food;

import static ru.ibs.db.steps.DbHooks.foodsRepository;

/**
 * Класс с шагами для доступа к БД
 */
public class DBSteps {
    private Food product;

    @И("Соединение с БД установлено,в БД есть непустая таблица food")
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

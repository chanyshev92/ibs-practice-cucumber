package ru.ibs.db.repositories;

import ru.ibs.db.models.Food;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Имплементация репозитория через Spring JdbcTemplate
 */
public class FoodsRepositoryImplementationSpringJdbc implements FoodsRepository {

    private static final RowMapper<Food> rowMapper = (row, rowNum) -> new Food(row.getInt("food_id"),
            row.getString("food_name"),
            row.getString("food_type"),
            row.getInt("food_exotic"));

    JdbcTemplate jdbcTemplate;

    public FoodsRepositoryImplementationSpringJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Food product) {
        Object[] inserts = {product.getFoodName(), product.getFoodType(), product.getFoodExotic()};
        jdbcTemplate.update(SQL_INSERT, inserts);
        product.setFoodId(jdbcTemplate.queryForObject("select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =? ",Integer.TYPE,inserts));
    }

    @Override
    public List<Food> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    @Override
    public void delete(Food product) {
        Object[] inserts = {product.getFoodName(), product.getFoodType(), product.getFoodExotic()};
        jdbcTemplate.update(SQL_DELETE_BY_DESCRIPTION, inserts);
    }

    @Override
    public boolean checkByDescription(Food product) {
        return findAll()
                .stream()
                .anyMatch(food -> food.getFoodName().equals(product.getFoodName())
                        && food.getFoodType().equals(product.getFoodType())
                        && food.getFoodExotic() == product.getFoodExotic());
    }
}

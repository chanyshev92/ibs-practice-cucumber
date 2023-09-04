package ru.ibs.db.repositories;

import ru.ibs.db.models.Food;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

/**
 * Имплементация репозитория через Spring JdbcTemplate
 */
public class FoodsRepository {

    //language=SQL
    String SQL_SELECT_ALL = "select * from FOOD order by food_id";

    //language=SQL
    String SQL_INSERT = "insert into FOOD(FOOD_NAME,FOOD_TYPE,FOOD_EXOTIC) values (?,?,?)";

    //language=SQL
    String SQL_DELETE_BY_DESCRIPTION = "delete from FOOD where FOOD_ID = (select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =?)";


    private static final RowMapper<Food> rowMapper = (row, rowNum) -> new Food(row.getInt("food_id"),
            row.getString("food_name"),
            row.getString("food_type"),
            row.getInt("food_exotic"));

    private static FoodsRepository foodsRepository;

    private JdbcTemplate jdbcTemplate;

    public static FoodsRepository getInstance(DataSource dataSource){
        if (foodsRepository == null) {
            foodsRepository=new FoodsRepository(dataSource);
        }
        return foodsRepository;
    }
    public static void setInstanceNull(){
        foodsRepository=null;
    }

    private FoodsRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean isConnectedToDb(){
        return jdbcTemplate != null;
    }

    public void save(Food product) {
        Object[] inserts = {product.getFoodName(), product.getFoodType(), product.getFoodExotic()};
        jdbcTemplate.update(SQL_INSERT, inserts);
        product.setFoodId(jdbcTemplate.queryForObject("select max(FOOD_ID) from FOOD where FOOD_NAME=? and FOOD_TYPE=? and FOOD_EXOTIC =? ",Integer.TYPE,inserts));
    }

    public List<Food> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, rowMapper);
    }

    public void delete(Food product) {
        Object[] inserts = {product.getFoodName(), product.getFoodType(), product.getFoodExotic()};
        jdbcTemplate.update(SQL_DELETE_BY_DESCRIPTION, inserts);
    }

    public boolean checkByDescription(Food product) {
        return findAll()
                .stream()
                .anyMatch(food -> food.getFoodName().equals(product.getFoodName())
                        && food.getFoodType().equals(product.getFoodType())
                        && food.getFoodExotic() == product.getFoodExotic());
    }
}

# language: ru

@All
@Ui
Функция: Проверки через Ui

  Сценарий: Проверка отображения элементов на странице "Товары"
    * Перейти на страницу "Товары"
    * Открыта страница "Товары"
    * Таблица названа "Список товаров"
    * В таблице есть все заголовки "Наименование", "Тип","Экзотический"
    * В таблице нет строк с "null" идентификаторами
    * Кнопка "Добавить" отображена
    * Кнопка "Добавить" имеет текст "Добавить"
    * Кнопка "Добавить" имеет цвет "#007bff"
  Структура сценария: Проверка добавления товара с валидными данными
    * Перейти на страницу "Товары"
    * Открыта страница "Товары"
    * Нажать кнопку "Добавить"
    * Открыто всплывающее окно "Добавление товара"
    * Форма содержит поле "Наименование"
    * Форма содержит поле "Тип"
    * Форма содержит чек-бокс "Экзотический"
    * В поле "Тип" на выбор 2 варианта: Фрукт,Овощ
    * Кнопка "Сохранить" отображена
    * В поле "Наименование" ввести значение "<Наименование>"
    * В поле "Наименование" введено значение "<Наименование>"
    * В поле "Тип" выбрать значение "<Тип>"
    * В поле "Тип" введено значение "<Тип>"
    * Установить чек-бокс "Экзотический" "<Экзотический>"
    * В чек-бокс "Экзотический" введено значение "<Экзотический>"
    * Нажать кнопку "Сохранить"
    * Закрыто всплывающее окно "Добавление товара"
    * Проверить, что в таблице есть строка со всеми данными "<Наименование>" "<Тип>" "<Экзотический>"
    Примеры:
      | Наименование | Тип   | Экзотический |
      | Ананас       | Фрукт | true         |
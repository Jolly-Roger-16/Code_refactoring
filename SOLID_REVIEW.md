# SOLID Code Review

## Цель работы

Выполнить code review Android-приложения через принципы SOLID и провести последовательный рефакторинг кода.

Приложение написано на Kotlin и Jetpack Compose.
Для хранения данных используется Room Database.
Приложение поддерживает CRUD-операции для сущности Item.

## Исходное состояние

Проект содержит:

- Android-приложение на Jetpack Compose;
- навигацию между экранами;
- Room Database;
- ItemDao;
- ItemRepository;
- ItemViewModel;
- создание, чтение, редактирование и удаление элементов.

## S — Single Responsibility Principle

### Найденная проблема

Файл MainActivity.kt содержит несколько разных обязанностей:

- запуск Activity;
- создание базы данных;
- создание репозитория;
- настройка навигации;
- описание маршрутов;
- отображение верхней панели;
- отображение нижней панели;
- реализация экранов приложения.

Это означает, что файл имеет несколько причин для изменения
и нарушает принцип единственной ответственности.

### План исправления

Вынести маршруты приложения, навигацию, общие UI-компоненты
и отдельные экраны в самостоятельные файлы.

### Выполненное исправление 1

Описание маршрутов `RootRoute` вынесено из `MainActivity.kt`
в отдельный файл `navigation/RootRoute.kt`.

Теперь маршруты относятся к слою навигации, а не к Activity.
Это уменьшило количество обязанностей файла `MainActivity.kt`.

Связанный коммит:
`refactor: маршруты вынесены из MainActivity`

### Проблема 2: компоненты навигационного интерфейса находятся в MainActivity.kt

В MainActivity.kt находятся функции AppTopBar и BottomBar,
а также модель BottomItem и функция titleForRoute.

Эти элементы отвечают за отображение навигационного интерфейса,
а не за запуск Android Activity. Поэтому они являются отдельной
причиной для изменения файла MainActivity.kt.

### План исправления

Вынести AppTopBar и titleForRoute в файл
ui/components/AppTopBar.kt.

Вынести BottomItem и BottomBar в файл
ui/components/BottomBar.kt.

### Выполненное исправление 2

Компоненты AppTopBar и titleForRoute вынесены
в файл ui/components/AppTopBar.kt.

Модель BottomItem и компонент BottomBar вынесены
в файл ui/components/BottomBar.kt.

MainActivity.kt теперь использует готовые навигационные компоненты,
но не реализует их самостоятельно.

Связанный коммит:
`refactor: навигационные панели вынесены из MainActivity`

### Проблема 3: граф навигации находится в MainActivity.kt

Функция RootNavHost содержит описание всех маршрутов приложения,
переходов между экранами, аргумента `id`, deep link и вложенных
графов Home и Profile.

Настройка навигационного графа является отдельной ответственностью
и не относится к запуску Activity.

### План исправления

Вынести функцию RootNavHost в отдельный файл
navigation/AppNavigation.kt.

### Выполненное исправление 3

Функция RootNavHost вынесена из MainActivity.kt
в файл navigation/AppNavigation.kt.

Файл MainActivity.kt теперь отвечает за создание основного
Compose-интерфейса и передачу зависимостей в граф навигации,
а описание маршрутов и переходов находится в навигационном слое.

Связанный коммит:
`refactor: граф навигации вынесен из MainActivity`

### Проблема 4: CRUD-экраны находятся в MainActivity.kt

Функции ListScreen и DetailScreen находятся в MainActivity.kt,
хотя они реализуют самостоятельный UI-функционал для работы
с элементами базы данных.

ListScreen отвечает за отображение списка элементов,
создание элемента через диалог и удаление элемента.

DetailScreen отвечает за отображение данных выбранного элемента
и его редактирование.

Эти экраны не относятся к запуску Activity и должны находиться
в отдельном пакете экранов.

### План исправления

Вынести ListScreen в файл:

ui/screens/items/ItemListScreen.kt

Вынести DetailScreen в файл:

ui/screens/items/ItemDetailScreen.kt.

### Выполненное исправление 4

Функция ListScreen вынесена из MainActivity.kt
в ui/screens/items/ItemListScreen.kt.

Функция DetailScreen вынесена из MainActivity.kt
в ui/screens/items/ItemDetailScreen.kt.

AppNavigation.kt теперь использует ItemListScreen и ItemDetailScreen
из пакета ui.screens.items.

MainActivity.kt больше не содержит интерфейс основной CRUD-части
приложения.

Связанный коммит:
`refactor: CRUD экраны вынесены из MainActivity`

### Проблема 5: экраны Home находятся в MainActivity.kt

Функции HomeMainScreen и HomeInnerScreen реализуют интерфейс
раздела Home, однако находятся в MainActivity.kt.

Они не относятся к запуску Activity и являются самостоятельной
частью пользовательского интерфейса.

### План исправления

Вынести экран HomeMainScreen в файл:

ui/screens/home/HomeMainScreen.kt

Вынести экран HomeInnerScreen в файл:

ui/screens/home/HomeInnerScreen.kt.

### Выполненное исправление 5

Функция HomeMainScreen вынесена из MainActivity.kt
в файл ui/screens/home/HomeMainScreen.kt.

Функция HomeInnerScreen вынесена из MainActivity.kt
в файл ui/screens/home/HomeInnerScreen.kt.

AppNavigation.kt импортирует Home-экраны из пакета
ui.screens.home.

MainActivity.kt больше не содержит UI раздела Home.

Связанный коммит:
`refactor: экраны Home вынесены из MainActivity`

### Проблема 6: экраны Profile находятся в MainActivity.kt

Функции ProfileMainScreen и ProfileSettingsScreen реализуют
интерфейс раздела Profile, но находятся в MainActivity.kt.

Они не относятся к запуску Activity и являются самостоятельной
частью пользовательского интерфейса.

### План исправления

Вынести экран ProfileMainScreen в файл:

ui/screens/profile/ProfileMainScreen.kt

Вынести экран ProfileSettingsScreen в файл:

ui/screens/profile/ProfileSettingsScreen.kt.

### Выполненное исправление 6

Функция ProfileMainScreen вынесена из MainActivity.kt
в файл ui/screens/profile/ProfileMainScreen.kt.

Функция ProfileSettingsScreen вынесена из MainActivity.kt
в файл ui/screens/profile/ProfileSettingsScreen.kt.

AppNavigation.kt импортирует Profile-экраны из пакета
ui.screens.profile.

MainActivity.kt больше не содержит UI раздела Profile.

Связанный коммит:
`refactor: экраны Profile вынесены из MainActivity`

### Проблема 7: FormScreen находится в MainActivity.kt

Функция FormScreen реализует самостоятельный UI-экран формы,
но находится в MainActivity.kt.

MainActivity должна отвечать за создание Activity, настройку темы
и подключение корневой навигации, а не за интерфейс отдельного экрана.

### План исправления

Вынести FormScreen в отдельный файл:

ui/screens/form/FormScreen.kt.

## O — Open/Closed Principle

### Найденная проблема

Функция titleForRoute использует цепочку условий startsWith
для определения заголовка по строковому маршруту.

При добавлении нового маршрута потребуется изменять
существующую функцию.

### План исправления

Перенести метаданные маршрутов в класс RootRoute:

- route — строка маршрута;
- title — заголовок для верхней панели;
- showBottomBar — признак отображения нижней панели.

После этого функция titleForRoute и логика с проверками
currentRoute.startsWith(...) будут удалены или упрощены.

При добавлении нового экрана будет достаточно добавить новый объект
маршрута с нужными параметрами, без изменения уже существующей
цепочки условий when.

## L — Liskov Substitution Principle

При анализе кода нарушений не обнаружено.
В проекте нет иерархии взаимозаменяемых наследников,
которые изменяют ожидаемое поведение базового типа.

## I — Interface Segregation Principle

При анализе кода нарушений не обнаружено.
ItemDao является небольшим специализированным интерфейсом,
содержащим только операции для сущности Item.

## D — Dependency Inversion Principle

### Найденная проблема

В MainActivity напрямую создаются AppDatabase и ItemRepository:

val database = AppDatabase.getDatabase(this)
val repository = ItemRepository(database.itemDao())

Это создаёт жёсткую связь Activity с конкретной реализацией базы данных
и усложняет тестирование без Android-контекста.

### План исправления

Рассмотреть введение абстракции ItemRepository
и передачу зависимости через конструктор.

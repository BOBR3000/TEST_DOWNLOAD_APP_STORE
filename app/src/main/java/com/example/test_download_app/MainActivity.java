package com.example.test_download_app;

import android.content.res.AssetManager; // Импортируем класс для работы с ресурсами приложения
import android.os.Bundle;
import android.util.Log; // Импортируем класс для логирования
import android.widget.Toast; // Импортируем класс для показа сообщений пользователю

import androidx.appcompat.app.AppCompatActivity; // Импортируем базовый класс для Activity
import androidx.recyclerview.widget.LinearLayoutManager; // Импортируем класс для управления расположением элементов в списке
import androidx.recyclerview.widget.RecyclerView; // Импортируем класс для работы с прокручиваемыми списками

import com.google.gson.Gson; // Импортируем библиотеку Gson для работы с JSON
import com.google.gson.reflect.TypeToken; // Импортируем класс для работы с
// дополнительными типами (например, списками)

import java.io.BufferedReader; // Импортируем класс для чтения текстовых данных
import java.io.IOException; // Импортируем класс для обработки исключений
import java.io.InputStream; // Импортируем класс для работы с потоками данных
import java.io.InputStreamReader; // Импортируем класс для чтения данных из InputStream
import java.lang.reflect.Type; // Импортируем класс для работы с типами в Java
import java.util.List; // Импортируем интерфейс List для работы со списками

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // Поле для списка приложений
    private AppDatabase appDatabase; // Поле для базы данных приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Метод, вызываемый при создании Activity
        super.onCreate(savedInstanceState); // Вызываем метод родительского класса
        setContentView(R.layout.activity_main); // Устанавливаем макет для этой Activity

        appDatabase = AppDatabase.getInstance(this); // Получаем экземпляр базы данных
        recyclerView = findViewById(R.id.recyclerView); // Инициализация списка в главной Activity
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Устанавливаем менеджер компоновки для вертикального списка
        // Этот метод передаёт наш список к контейнеру LinearLayout

        loadApps(); // Загружаем приложения
    }

    private void loadApps() { // Метод для загрузки приложений из базы данных или файла
        new Thread(() -> { // Запускаем новый поток для работы с базой данных
            List<AppEntity> appsFromDb = appDatabase.appDao().getAllApps();
            // Получаем список приложений из базы данных
            if (appsFromDb.isEmpty()) { // Проверяем, пуста ли база данных
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Загрузка приложений...", Toast.LENGTH_SHORT).show());
                // Если база данных пуста, сообщаем пользователю о загрузке
                loadAppsFromAssets(); // Загружаем данные из JSON-файла
                // При первой загрузке приложения на устройство база данных пуста
            } else {
                // else при повторном открытии приложения выполняется всегда,
                // так как база данных уже заполнена и используется
                runOnUiThread(() -> { // Обновляем UI из основного потока
                    AppAdapter appAdapter = new AppAdapter(appsFromDb, this);
                    // Создаём адаптер с загруженными данными
                    recyclerView.setAdapter(appAdapter); // Устанавливаем адаптер для RecyclerView
                    Toast.makeText(MainActivity.this, "Приложения загружены из кэша", Toast.LENGTH_SHORT).show();
                });
            }
        }).start(); // Запускаем поток
    }

    private void loadAppsFromAssets() { // Метод для загрузки приложений из файла JSON
        try {
            AssetManager assetManager = getAssets(); // Получаем доступ к ресурсам приложения
            InputStream inputStream = assetManager.open("apps.json"); // Открываем JSON-файл из ресурсов
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)); // Читаем содержимое файла
            Gson gson = new Gson(); // Создаём экземпляр Gson для парсинга JSON
            Type appListType = new TypeToken<List<AppEntity>>() {}.getType(); // Определяем тип списка приложений
            List<AppEntity> appList = gson.fromJson(reader, appListType); // Парсим JSON в список объектов AppEntity
            reader.close(); // Закрываем BufferedReader

            // Сохранение загруженных данных в базу данных
            new Thread(() -> { // Запускаем новый поток для вставки данных в базу
                for (AppEntity app : appList) { // Проходим по списку загруженных приложений
                    appDatabase.appDao().insert(app); // Вставляем каждое приложение в базу данных
                }
                runOnUiThread(() -> { // Обновляем UI из основного потока
                    AppAdapter appAdapter = new AppAdapter(appList, this); // Создаём адаптер с загруженными данными
                    recyclerView.setAdapter(appAdapter); // Устанавливаем адаптер для RecyclerView
                    Toast.makeText(MainActivity.this, "Приложения загружены!", Toast.LENGTH_SHORT).show(); // Сообщаем пользователю о завершении загрузки
                });
            }).start(); // Запускаем поток
        } catch (IOException e) { // Обрабатываем возможные исключения при работе с файлами
            Log.e("MainActivity", "Ошибка при загрузке JSON файла", e); // Логируем ошибку
        }
    }

    // Для чего здесь реализован функционал через потоки (Tread)?
    //В методе loadApps() данные загружаются из базы данных в фоновом потоке,
    // чтобы не блокировать основной поток, пока база данных выполняет запрос.
    // В методе loadAppsFromAssets() чтение файла apps.json и вставка данных в базу данных происходят в
    // фоновом потоке, чтобы пользовательский интерфейс был доступен.
}


package com.example.test_download_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity; // Импортируем базовый класс для Activity

public class AppDetailActivity extends AppCompatActivity { // Activity для отображения деталей приложения

    private AppEntity app; // Поле для хранения объекта приложения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);

        // Получаем данные, переданные через Intent
        Bundle bundle = getIntent().getExtras(); // Получаем дополнительную информацию из Intent
        if (bundle != null) { // Проверяем, что данные существуют
            app = (AppEntity) bundle.getSerializable("app"); // Преобразуем данные обратно в объект AppEntity
        }

        // Инициализируем элементы пользовательского интерфейса
        TextView nameTextView = findViewById(R.id.appNameTextView);
        TextView descriptionTextView = findViewById(R.id.appDescriptionTextView);
        Button downloadButton = findViewById(R.id.downloadButton);

        if (app != null) { // Проверяем, что объект приложения не равен null
            nameTextView.setText(app.getName());
            descriptionTextView.setText(app.getDescription());

            // Устанавливаем обработчик нажатия на кнопку
            downloadButton.setOnClickListener(v -> {
                FileDownloader downloader = new FileDownloader(); // Создаём экземпляр класса для загрузки файлов
                // Запускаем загрузку файла с URL и именем файла из объекта app
                downloader.downloadFile(app.getApkUrl(), app.getFileName(), AppDetailActivity.this);
            });
        }
    }
}

//Этот класс управляет отображением деталей приложения и
// предоставляет пользователю возможность скачать APK-файл.

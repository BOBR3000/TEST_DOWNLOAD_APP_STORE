package com.example.test_download_app;

import android.content.ContentValues; // Импортируем класс для хранения значений, которые будут вставлены в базу данных
import android.content.Context; // Импортируем класс Context для доступа к ресурсам приложения
import android.net.Uri; // Импортируем класс Uri для работы с URI
import android.os.Environment; // Импортируем класс для работы с внешней средой
import android.provider.MediaStore; // Импортируем класс MediaStore для доступа к медиафайлам устройства
import android.util.Log; // Импортируем класс для логирования

import androidx.annotation.NonNull; // Импортируем аннотацию, указывающую, что параметр не может быть null

import okhttp3.Call; // Импортируем класс Call для выполнения запросов
import okhttp3.Callback; // Импортируем интерфейс Callback для обработки ответа
import okhttp3.OkHttpClient; // Импортируем клиент для выполнения HTTP-запросов
import okhttp3.Request; // Импортируем класс Request для создания HTTP-запросов
import okhttp3.Response; // Импортируем класс Response для обработки ответов

import java.io.IOException; // Импортируем класс для обработки исключений
import java.io.InputStream; // Импортируем класс для работы с потоками ввода
import java.io.OutputStream; // Импортируем класс для работы с потоками вывода

public class FileDownloader { // Класс для загрузки файлов

    private final OkHttpClient client = new OkHttpClient(); // Создаём экземпляр OkHttpClient для выполнения запросов
    private static final String TAG = "FileDownloader"; // Тег для логирования

    // Метод для загрузки файла по URL и сохранения его с указанным именем
    public void downloadFile(String url, String fileName, Context context) {
        // принимает URL файла,
        // имя файла и контекст,
        // используя его для выполнения HTTP-запроса.
        // Создаётся объект запроса Request, который отправляется с помощью OkHttpClient в
        // асинхронном режиме для предотвращения блокировки основного потока.
        Request request = new Request.Builder().url(url).build(); // Создаём HTTP-запрос

        // Асинхронное выполнение запроса
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) { //onFailure обрабатывает ошибки,
                // возникающие при запросе, и логгирует их.
                // Обработка ошибки при загрузке файла
                Log.e(TAG, "Ошибка загрузки файла", e); // Логируем ошибку
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // onResponse обрабатывает успешный ответ, проверяя код ответа и
                // выполняя действия по сохранению файла на устройстве.
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Неожиданный код: " + response); // Логируем неожиданный код
                    throw new IOException("Unexpected code " + response); // Генерируем исключение
                }

                // Создание записи в MediaStore для сохранения в папке Download
                ContentValues values = new ContentValues(); // Создаём объект для хранения данных
                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName); // Указываем имя файла
                values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive");
                // Указываем MIME-тип
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                // Указываем путь сохранения

                // Вставляем запись в MediaStore и получаем URI для сохранения файла
                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

                //Загружаемый файл считывается из ответа по частям и записывается в заданный URI.
                // Весь процесс обернут в try-with-resources, чтобы гарантировать закрытие потоков после завершения операций.
                if (uri != null) { // Проверяем, что URI не равен null
                    try {
                        assert response.body() != null; // Убеждаемся, что тело ответа не равно null
                        try (InputStream inputStream = response.body().byteStream(); // Получаем InputStream из тела ответа
                             OutputStream fileOutput = context.getContentResolver().openOutputStream(uri)) { // Открываем OutputStream для записи файла

                            byte[] buffer = new byte[2048]; // Буфер для чтения данных
                            int bytesRead; // Переменная для хранения количества прочитанных байт
                            // Цикл для чтения данных из InputStream и записи в OutputStream
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                assert fileOutput != null; // Убеждаемся, что файл готов для записи
                                fileOutput.write(buffer, 0, bytesRead); // Записываем считанные байты в файл
                            }
                            assert fileOutput != null; // Убеждаемся, что OutputStream не null
                            fileOutput.flush(); // Очищаем OutputStream
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Ошибка при сохранении файла", e); // Логируем ошибки при сохранении
                    }
                }
            }
        });
    }
}

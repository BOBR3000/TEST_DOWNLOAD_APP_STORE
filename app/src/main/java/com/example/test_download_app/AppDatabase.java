package com.example.test_download_app;

import androidx.room.Database; // Импортируем аннотацию Database для определения базы данных Room
import androidx.room.Room; // Импортируем класс Room для создания базы данных
import androidx.room.RoomDatabase; // Импортируем класс RoomDatabase, который является базовым классом для Room
import android.content.Context; // Импортируем класс Context для работы с контекстом приложения

@Database(entities = {AppEntity.class}, version = 1) // Аннотация, указывающая, что это база данных,
// содержащая сущности AppEntity и версия базы данных
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE; // Переменная для хранения единственного экземпляра базы данных

    // Абстрактный метод для получения DAO объекта для доступа к данным
    public abstract AppDao appDao();

    // Метод для получения единственного экземпляра базы данных
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) { // Проверяем, создан ли уже экземпляр базы данных
            // (это сделано для того, чтобы не было несколько экземплятор одно и той же БД, что приведёт к ошибке)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database") // Создаём базу данных с указанным именем
                    .fallbackToDestructiveMigration()
                    // Устанавливаем поведение при миграции:
                    // удалить и создать заново
                    .build(); // Завершаем создание базы данных
        }
        return INSTANCE; // Возвращаем единственный экземпляр базы данных
    }
}

//AppDatabase — это абстрактный класс, который расширяет RoomDatabase
//и представляет собой базу данных в приложении. Он помечен аннотацией @Database, указывающей на то, что
// этот класс является источником данных, который содержит информацию о структуре базы данных и связных сущностях.
//Функционал:
//Хранение экземпляра: В классе обычно реализуется шаблон Singleton для
// обеспечения единственного экземпляра базы данных в приложении. Это позволяет избежать того,
// чтобы база данных открывалась несколько раз, что может привести к проблемам
// с производительностью и целостностью данных.
//Доступ к DAO: Этот класс объявляет абстрактные методы для получения DAO (Data Access Object),
// что позволяет вам выполнять операции над данными в базе данных.


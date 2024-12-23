package com.example.test_download_app;

import androidx.room.Entity; // Импортируем аннотацию Entity для использования с Room
import androidx.room.PrimaryKey; // Импортируем аннотацию PrimaryKey для обозначения первичного ключа
import java.io.Serializable; // Импортируем интерфейс Serializable для сериализации объектов

@Entity(tableName = "apps") // Аннотация для Room, указывающая, что этот класс
// является сущностью, связанной с таблицей "apps"
public class AppEntity implements Serializable {

    @PrimaryKey(autoGenerate = true) // Аннотация для Room, указывающая, что поле id
    // будет первичным ключом и будет автоматически генерироваться
    private int id;

    private String name; // Поле для хранения имени приложения
    private String description; // Поле для хранения описания приложения
    private String apkUrl; // Поле для хранения URL-адреса APK-файла
    private String fileName; // Поле для хранения имени файла APK

    // Конструктор класса, принимающий параметры для инициализации полей сущности
    public AppEntity(String name, String description, String apkUrl, String fileName) {
        this.name = name; // Инициализируем поле name
        this.description = description; // Инициализируем поле description
        this.apkUrl = apkUrl; // Инициализируем поле apkUrl
        this.fileName = fileName; // Инициализируем поле fileName
    }

    // Геттеры и сеттеры для доступа и модификации полей

    public int getId() {
        return id; // Геттер для получения значения id
    }

    public void setId(int id) {
        this.id = id; // Сеттер для установки значения id
    }

    public String getName() {
        return name; // Геттер для получения имени приложения
    }

    public void setName(String name) {
        this.name = name; // Сеттер для установки имени приложения
    }

    public String getDescription() {
        return description; // Геттер для получения описания приложения
    }

    public void setDescription(String description) {
        this.description = description; // Сеттер для установки описания приложения
    }

    public String getApkUrl() {
        return apkUrl; // Геттер для получения URL-адреса APK-файла
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl; // Сеттер для установки URL-адреса APK-файла
    }

    public String getFileName() {
        return fileName; // Геттер для получения имени файла APK
    }

}

//AppEntity — это класс-сущность, который представляет собой структуру данных, соответствующую таблице в базе данных.
// Он помечен с помощью аннотации @Entity в Room, тем самым связывая его с таблицей.
//Функционал: Этот класс содержит поля (например, имя, описание, URL файла) и методы (геттеры и сеттеры) для
// работы с данными. Он позволяет Room автоматически создавать таблицу и управлять записями в базе данных.

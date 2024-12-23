package com.example.test_download_app;

import android.content.Intent; // Импортируем класс Intent для запуска новой Activity
import android.view.LayoutInflater; // Импортируем класс LayoutInflater для создания представлений из XML
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull; // Импортируем аннотацию, указывающую, что метод не может возвращать null
import androidx.recyclerview.widget.RecyclerView; // Импортируем класс RecyclerView для работы с прокручиваемыми списками
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    private final List<AppEntity> appList; // это список объектов AppEntity, содержащий информацию о каждом приложении,
    // которое будет отображаться в RecyclerView. Он инициализируется при создании экземпляра адаптера.

    // Конструктор адаптера, принимающий список приложений и контекст MainActivity
    public AppAdapter(List<AppEntity> appList, MainActivity mainActivity) {
        this.appList = appList; // Инициализируем переменную appList переданным списком
    }

    @NonNull
    @Override
    // Этот метод вызывается, когда RecyclerView требует нового представления.
    // Он использует LayoutInflater, чтобы создать новый элемент
    // пользовательского интерфейса из XML-ресурса (то есть он не переходит к новой активности
    // на основе стандартного макета simple_list_item_2.
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Используем LayoutInflater для создания нового представления из XML-ресурса
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        // Созданное представление передаётся в AppViewHolder, который будет управлять отображением данных.
        return new AppViewHolder(view, appList);
    }

    @Override
    // Этот метод связывает данные из appList с элементами представления.
    // При каждом вызове метода адаптера ему передаётся положение элемента, который нужно отобразить.
    // Он получает объект AppEntity по позиции и вызывает метод bind у ViewHolder,
    // чтобы установить соответствующие данные (имя и описание).
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        // Получаем объект AppEntity по текущей позиции
        AppEntity app = appList.get(position);
        // Вызываем метод bind у ViewHolder для установки данных в представление
        holder.bind(app);
    }

    @Override
    // Возвращает общее количество элементов в списке приложений.
    // Это важно для того, чтобы RecyclerView знал, сколько элементов ему следует отображать.
    public int getItemCount() {
        return appList.size(); // Возвращаем размер списка приложений
    }

    // Вложенный класс для ViewHolder, который хранит ссылки на элементы пользовательского интерфейса
    public static class AppViewHolder extends RecyclerView.ViewHolder {
        // Поля для хранения ссылок на текстовые представления
        TextView nameTextView;
        TextView descriptionTextView;
        List<AppEntity> appList; // Ссылка на список приложений

        // Конструктор ViewHolder, принимающий элемент представления
        AppViewHolder(View itemView, List<AppEntity> appList) {
            super(itemView); // Вызываем конструктор родительского класса
            this.appList = appList; // Инициализируем список приложений
            nameTextView = itemView.findViewById(android.R.id.text1); // Находим TextView для имени приложения
            descriptionTextView = itemView.findViewById(android.R.id.text2); // Находим TextView для описания приложения

            // Устанавливаем слушатель нажатий на элемент представления
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition(); // Получаем текущую позицию элемента
                if (position != RecyclerView.NO_POSITION) { // Проверяем, что позиция валидна, то есть элемент активен
                    AppEntity app = appList.get(position); // Получаем выбранное приложение по позиции
                    Intent intent = new Intent(itemView.getContext(), AppDetailActivity.class);
                    // Создаём Intent для перехода в новое Activity

                    // Используем Bundle для передачи данных через Intent
                    intent.putExtra("app", app); // Передаём объект AppEntity через Serializable (Сериализация)
                    itemView.getContext().startActivity(intent); // Запускаем новое Activity
                }
            });
        }

        // Метод для привязки данных приложения к элементам пользовательского интерфейса
        void bind(AppEntity app) {
            nameTextView.setText(app.getName()); // Устанавливаем имя приложения в соответствующий TextView
            descriptionTextView.setText(app.getDescription()); // Устанавливаем описание приложения в соответствующий TextView
        }
    }
}

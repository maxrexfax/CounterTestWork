# CounterTestWork
Активити (Activity)
1) содержит текстовое поле, которое показывает актуальное время последнего запуска Сервиса (значение хранится в Настройках)
2) содержит текстовое поле, которое всегда показывает актуальное (текущее) значение счетчика (хранится в Настройках)
3) так же содержит 2 кнопки - вкл/выкл, которые соответственно включают или выключают Сервис
4) помимо прочего Сервис должен выключаться, как только закрывается приложение

Сервис (Service)
1) при своем старте запускает Поток и записывает в Настройки текущее время
2) при выключении завершает Поток

Поток (Thread)
1) при запуске получает последнее значение счетчика из Настроек
2) выполняет бесконечный цикл в котором с интервалом 5 секунд добавляет 1 к значению счетчика
3) по завершению работы потока записывать измененное значение счетчика в Настройки

Настройки (SharedPreferences)
1) содержит время последнего запуска Сервиса
2) содержит значение счетчика

Рекомендуется
1) отправлять данные из Сервиса при помощи метода sendBroadcast
2) получать (обновлять) данные в Активити при помощи BroadcastReceiver
3) предоставить исходные коды с помощью github.com

Будет плюсом
1) написать на Kotlin
2) использовать ConstraintLayout
3) использовать Architecture Components

# Указываем базовый образ с Python
FROM python:3.9-slim

# Устанавливаем переменные окружения для корректной работы Flask
ENV PYTHONUNBUFFERED=1 \
    FLASK_ENV=production \
    FLASK_APP=app.py \
    TELEGRAM_TOKEN=TOKEN \
    FLASK_PORT=5000

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы приложения в контейнер
COPY . /app

# Устанавливаем зависимости
RUN pip install --no-cache-dir -r requirements.txt

# Открываем порт для Flask
EXPOSE $FLASK_PORT

# Указываем команду для запуска сервиса
CMD ["python", "app.py"]
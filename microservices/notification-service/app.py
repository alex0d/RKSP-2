from flask import Flask, request, jsonify
from telegram import Bot
import py_eureka_client.eureka_client as eureka_client
from flask_cors import CORS

app = Flask(__name__)
CORS(app)


TELEGRAM_TOKEN = "7611914700:AAGNGtsLeoAZ4ERMTc7LJyljORZaceXEKss"
bot = Bot(token=TELEGRAM_TOKEN)

FLASK_PORT = 5000

# Initialize Eureka Client
eureka_client.init(
    eureka_server="http://eureka-server:8761/eureka",  # URL вашего Eureka сервера
    app_name="notification-service",             # Название приложения
    instance_port=FLASK_PORT,                     # Порт текущего микросервиса
    instance_host="notification-server"
)

@app.route("/api/notifications/send", methods=["POST"])
def send_data():
    try:
        data = request.json
        if not data or 'tg' not in data or 'message' not in data:
            return jsonify({"error": "Invalid payload, 'tg' and 'message' are required."}), 400
        tg_id = data['tg']
        message = data['message']
        bot.send_message(chat_id=tg_id, text=message)

        return jsonify({"status": "success", "message": "Message sent successfully."})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=FLASK_PORT)

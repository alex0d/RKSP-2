import pytest
import requests
import json
from typing import Dict

BASE_URL = "http://localhost:8080"


class TestInvestmentAPI:
    @pytest.fixture
    def test_user(self) -> Dict:
        """Фикстура с тестовыми данными пользователя"""
        return {
            "username": "testuser",
            "password": "testpassword",
            "email": "test@example.com",
            "tg": "test_tg"
        }

    @pytest.fixture
    def auth_token(self, test_user) -> str:
        """Фикстура для получения токена авторизации"""
        # Сначала регистрируем пользователя
        requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )

        # Затем логинимся и получаем токен
        response = requests.post(
            f"{BASE_URL}/api/auth/login",
            data={
                "username": test_user["username"],
                "password": test_user["password"]
            }
        )
        return response.json()["access_token"]

    def test_register_success(self, test_user):
        """Тест успешной регистрации"""
        response = requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )
        assert response.status_code == 200
        assert "message" in response.json()
        assert response.json()["message"] == "User successfully registered"

    def test_register_duplicate_user(self, test_user):
        """Тест регистрации с существующим именем пользователя"""
        # Первая регистрация
        requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )

        # Повторная регистрация
        response = requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )
        assert response.status_code == 400
        assert "Username already registered" in response.json()["detail"]

    def test_login_success(self, test_user):
        """Тест успешной авторизации"""
        # Регистрируем пользователя
        requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )

        # Пытаемся залогиниться
        response = requests.post(
            f"{BASE_URL}/api/auth/login",
            data={
                "username": test_user["username"],
                "password": test_user["password"]
            }
        )
        assert response.status_code == 200
        assert "access_token" in response.json()
        assert "token_type" in response.json()
        assert response.json()["token_type"] == "bearer"

    def test_login_wrong_password(self, test_user):
        """Тест авторизации с неверным паролем"""
        # Регистрируем пользователя
        requests.post(
            f"{BASE_URL}/api/auth/register",
            json=test_user
        )

        # Пытаемся залогиниться с неверным паролем
        response = requests.post(
            f"{BASE_URL}/api/auth/login",
            data={
                "username": test_user["username"],
                "password": "wrongpassword"
            }
        )
        assert response.status_code == 401

    def test_get_stocks_unauthorized(self):
        """Тест получения списка акций без авторизации"""
        response = requests.get(f"{BASE_URL}/api/investments/stocks")
        assert response.status_code == 200
        stocks = response.json()
        assert isinstance(stocks, list)
        for stock in stocks:
            assert "symbol" in stock
            assert "name" in stock
            assert "price" in stock

    def test_buy_stocks_authorized(self, auth_token):
        """Тест покупки акций с авторизацией"""
        headers = {"Authorization": f"Bearer {auth_token}"}
        order_data = {
            "symbol": "AAPL",
            "quantity": 10
        }

        response = requests.post(
            f"{BASE_URL}/api/investments/buy",
            headers=headers,
            json=order_data
        )
        assert response.status_code == 200
        assert "message" in response.json()
        assert response.json()["message"] == "Stock purchased successfully"

    def test_buy_stocks_unauthorized(self):
        """Тест покупки акций без авторизации"""
        order_data = {
            "symbol": "AAPL",
            "quantity": 10
        }

        response = requests.post(
            f"{BASE_URL}/api/investments/buy",
            json=order_data
        )
        assert response.status_code == 401

    def test_buy_invalid_stock(self, auth_token):
        """Тест покупки несуществующей акции"""
        headers = {"Authorization": f"Bearer {auth_token}"}
        order_data = {
            "symbol": "INVALID",
            "quantity": 10
        }

        response = requests.post(
            f"{BASE_URL}/api/investments/buy",
            headers=headers,
            json=order_data
        )
        assert response.status_code == 404

    def test_get_portfolio_authorized(self, auth_token):
        """Тест получения портфеля с авторизацией"""
        headers = {"Authorization": f"Bearer {auth_token}"}

        response = requests.get(
            f"{BASE_URL}/api/investments/portfolio",
            headers=headers
        )
        assert response.status_code == 200
        portfolio = response.json()
        assert isinstance(portfolio, list)
        for item in portfolio:
            assert "symbol" in item
            assert "quantity" in item
            assert "current_price" in item
            assert "total_value" in item

    def test_get_portfolio_unauthorized(self):
        """Тест получения портфеля без авторизации"""
        response = requests.get(f"{BASE_URL}/api/investments/portfolio")
        assert response.status_code == 401

    def test_full_investment_flow(self, auth_token):
        """Тест полного процесса инвестирования"""
        headers = {"Authorization": f"Bearer {auth_token}"}

        # 1. Проверяем доступные акции
        stocks_response = requests.get(
            f"{BASE_URL}/api/investments/stocks"
        )
        assert stocks_response.status_code == 200
        available_stocks = stocks_response.json()

        # 2. Покупаем акции
        buy_response = requests.post(
            f"{BASE_URL}/api/investments/buy",
            headers=headers,
            json={"symbol": "AAPL", "quantity": 5}
        )
        assert buy_response.status_code == 200

        # 3. Проверяем портфель
        portfolio_response = requests.get(
            f"{BASE_URL}/api/investments/portfolio",
            headers=headers
        )
        assert portfolio_response.status_code == 200
        portfolio = portfolio_response.json()
        assert len(portfolio) > 0

        apple_position = next(
            (item for item in portfolio if item["symbol"] == "AAPL"),
            None
        )
        assert apple_position is not None
        assert apple_position["quantity"] == 5


if __name__ == "__main__":
    pytest.main([__file__])
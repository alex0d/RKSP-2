package ru.alex0d.rsocketserver.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ru.alex0d.rsocketserver.model.Stock

interface StockRepository : CoroutineCrudRepository<Stock, Long>
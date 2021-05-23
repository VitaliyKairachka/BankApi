package com.kairachka.bankapi.repository;

import com.kairachka.bankapi.entity.Card;

import java.util.List;
import java.util.Optional;

/**
 * Слой для запросов к базе данных карт
 */
public interface CardRepository {
    /**
     * Сеттер для смены ссылки на подключение к базе данных
     *
     * @param url ссылка для подключения к базе данных
     */
    void setUrl(String url);

    /**
     * Запрос к базе данных для создания новой карты
     *
     * @param card данные карты
     * @return возвращает true - при создании, false - при ошибке
     */
    boolean addCard(Card card);

    /**
     * Запрос к базе данных для получения списка карт по счёту
     *
     * @param billId данные счёта
     * @return возраващет список карт счёта
     */
    List<Card> getAllCardsByBill(long billId);

    /**
     * Запрос к базе данных для получения карты по параметру
     *
     * @param cardId данные карты
     * @return возвращает контейнер объекта, который может содержать null при его отсутсвии
     */
    Optional<Card> getCardById(long cardId);

    /**
     * Запрос к базе данных для получения списка всех карт
     *
     * @return возвращает список всех карт
     */
    List<Card> getAllCards();

    /**
     * Запрос к базе данных для получения списка всех карт с необходимым статусом
     *
     * @param status необходимый статус
     * @return возвращает список карт с необходимым статусом
     */
    List<Card> getAllCardsByStatus(String status);

    /**
     * Запрос к базе данных для смены статуса карты
     *
     * @param cardId данные карты
     * @param status необходимый статус
     * @return возвращает true - при смене, false - при ошибке
     */
    boolean changeCardStatus(long cardId, String status);
}

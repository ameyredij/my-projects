package com.ameycode.onlinebookstoreservice.configurations;


import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum BookTypePromoDiscount {
    FICTION(10),
    TECHNOLOGY(10),
    MYSTERY(0),
    ROMANCE(20),
    THRILLER(15),
    DRAMA(5),
    COMIC(0),
    POETRY(20),
    OTHERS(10);

    private int discount;

    private static Map<String, Integer> bookTypeDiscountMap = new HashMap<>();

    public static Integer getDiscountPercentage(String category) {
        return (Integer) bookTypeDiscountMap.get(category);
    }

    static {
        for (BookTypePromoDiscount bookTypePromoDiscount : BookTypePromoDiscount.values()) {
            bookTypeDiscountMap.put(bookTypePromoDiscount.name(), bookTypePromoDiscount.discount);
        }
    }
}



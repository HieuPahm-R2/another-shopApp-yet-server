package com.hustVN.otherShopYet.components;

import com.hustVN.otherShopYet.model.entity.Category;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@org.springframework.kafka.annotation.KafkaListener(id = "groupA", topics = { "get-all-categories", "insert-a-category" })
public class KafkaListener {
    @KafkaHandler
    public void listenCategory(Category category) {
        System.out.println("Received: " + category);
    }

    @KafkaHandler(isDefault = true)
    public void unknown(Object object) {
        System.out.println("Received unknown: " + object);
    }

    @KafkaHandler
    public void listenListOfCategories(List<Category> categories) {
        System.out.println("Received: " + categories);
    }
}

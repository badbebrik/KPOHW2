package org.example.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private int id;
    private String name;
    private int price;
    private String description;
    private long timeToCook;
    private int quantity;
    private double rating;
    private int ratingCount;

    @Override
    public String toString() {
        return "id = " + id + ", Название = " + name + ", Цена = " + price + ", Количество = " + quantity +
                ", Время приготовления = " + timeToCook + " секунд, " +
                "Рейтинг = " + (rating == 0 ? "нет оценок" : rating + "/5");
    }
}

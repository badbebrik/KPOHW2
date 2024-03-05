package org.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Menu implements Iterable<Dish> {
    private List<Dish> dishes;

    public Menu() {
        this.dishes = new ArrayList<>();
    }

    public Menu(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    @Override
    public Iterator<Dish> iterator() {
        return dishes.iterator();
    }

    @Override
    public void forEach(Consumer<? super Dish> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Dish> spliterator() {
        return Iterable.super.spliterator();
    }
}

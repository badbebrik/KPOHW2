package org.example.controller;

import org.example.model.Dish;

import java.util.Iterator;
import java.util.List;

public class OrderIterator implements Iterator<Dish> {
    private final Iterator<Dish> dishIterator;

    public OrderIterator(List<Dish> dishes) {
        this.dishIterator = dishes.iterator();
    }

    @Override
    public boolean hasNext() {
        return dishIterator.hasNext();
    }

    @Override
    public Dish next() {
        return dishIterator.next();
    }

    public boolean isEmpty() {
        return !dishIterator.hasNext();
    }
}
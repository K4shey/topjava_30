package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class InMemoryMealRepository implements MealRepository {

    private static AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    public InMemoryMealRepository() {
        for (Meal meal : MealsUtil.MEAL_LIST) {
            add(meal);
        }
    }

    @Override
    public void add(Meal meal) {
        meal.setId(counter.incrementAndGet());
        storage.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        if (!storage.containsKey(id)) {
            return;
        }
        storage.remove(id);
    }

    @Override
    public void update(Meal meal) {
        int id = meal.getId();
        if (!storage.containsKey(id)) {
            return;
        }
        storage.put(id, meal);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal getById(int id) {
        if (!storage.containsKey(id)) {
            return null;
        }
        return storage.get(id);
    }
}

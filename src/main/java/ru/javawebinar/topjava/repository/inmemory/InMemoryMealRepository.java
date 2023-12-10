package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {

        for (Meal meal : MealsUtil.meals) {
            save(SecurityUtil.getAuthUserId(), meal);
        }

        Meal adminMeal1 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак админа", 400);
        Meal adminMeal2 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед админа", 600);
        Meal adminMeal3 = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 16, 0),
                "Полдник админа", 200);
        save(2, adminMeal1);
        save(2, adminMeal2);
        save(2, adminMeal3);
    }

    @Override
    public Meal save(int userId, Meal meal) {

        Map<Integer, Meal> userMeal = repository.computeIfAbsent(userId, uId -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeal.putIfAbsent(meal.getId(), meal);
            repository.put(userId, userMeal);
        } else {
            userMeal.computeIfPresent(meal.getId(), (id, existingMeal) -> meal);
        }
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null && userMeals.containsKey(id)) {
            return userMeals.remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null && userMeals.containsKey(id)) {
            return userMeals.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getFiltered(userId, null, null);
    }

    public Collection<Meal> getFiltered(int userId, LocalDate dateFrom, LocalDate dateTo) {
        Map<Integer, Meal> userMeal = repository.get(userId);
        return CollectionUtils.isEmpty(userMeal) ? Collections.emptyList() :
                userMeal.values().stream()
                        .filter(v -> DateTimeUtil.isBetween(v.getDate(), dateFrom, dateTo))
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }

}
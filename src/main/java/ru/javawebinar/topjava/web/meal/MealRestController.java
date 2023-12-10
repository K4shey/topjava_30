package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        int userId = getAuthUserId();
        log.info("save {}  for userId {}", meal, userId);
        return service.save(userId, meal);
    }

    public void delete(int id) {
        int userId = getAuthUserId();
        log.info("delete {}  for userId {}", id, userId);
        service.delete(userId, id);
    }

    public Meal get(int id) {
        int userId = getAuthUserId();
        log.info("get {} for userId {} ", id, userId);
        return service.get(userId, id);
    }

    public Collection<MealTo> getAll() {
        int userId = getAuthUserId();
        log.info("getAll for userId {}", userId);
        return MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getFiltered(LocalDate dateFrom, LocalTime timeFrom, LocalDate dateTo, LocalTime timeTo) {
        int userId = getAuthUserId();
        log.info("getFiltered  from {}-{} to {}-{} for userId {}", dateFrom, timeFrom, dateTo, timeTo, userId);
        Collection<MealTo> mealTos = MealsUtil.getTos(service.getFiltered(userId, dateFrom, dateTo), authUserCaloriesPerDay());
        return mealTos.stream()
                .filter(m -> DateTimeUtil.isBetweenHalfOpen(m.getTime(), timeFrom, timeTo))
                .collect(Collectors.toList());
    }
}
package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        int userId = authUserId();
        log.info("save {}  for userId {}", meal, userId);
        return service.save(userId, meal);
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete {}  for userId {}", id, userId);
        service.delete(userId, id);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get {} for userId {} ", id, userId);
        return service.get(userId, id);
    }

    public Collection<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for userId {}", userId);
        return MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public Collection<MealTo> getFiltered(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
        int userId = authUserId();
        log.info("getFiltered  from {} to {} for userId {}", dateTimeFrom, dateTimeTo, userId);
        return MealsUtil.getTos(service.getFiltered(userId, dateTimeFrom, dateTimeTo), authUserCaloriesPerDay());
    }
}
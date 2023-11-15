package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private final MealRepository repository = new InMemoryMealRepository();

    private static final String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static final String LIST_USER = "/mealList.jsp";

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")) {
            Integer id = getId(request);
            repository.delete(id);
            log.info(" delete meal with id={}", id);
            forward = LIST_USER;
            log.debug("forward to {}", forward);
            request.setAttribute("mealToList", MealsUtil.getListMealTo(repository.getAll()));
        } else if (action.equalsIgnoreCase("edit")) {
            Meal meal = repository.getById(getId(request));
            request.setAttribute("meal", meal);
            log.info("edit meal {}", meal);
            forward = INSERT_OR_EDIT;
            log.debug("forward to {}", forward);
        } else if (action.equalsIgnoreCase("mealList")) {
            forward = LIST_USER;
            log.debug("forward to {}", forward);
            log.info("get meal list");
            request.setAttribute("mealToList", MealsUtil.getListMealTo(repository.getAll()));
        } else if (action.equalsIgnoreCase("add")) {
            log.info("add new meal");
            request.setAttribute("meal", new Meal(LocalDateTime.now(), "", 1000));
            forward = INSERT_OR_EDIT;
            log.debug("forward to {}", forward);
        } else {
            forward = INSERT_OR_EDIT;
            log.debug("forward to {}", forward);
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        meal.setDateTime(dateTime);
        int id = getId(request);
        if (id == 0) {
            repository.add(meal);
        } else {
            meal.setId(id);
            repository.update(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("mealToList", MealsUtil.getListMealTo(repository.getAll()));
        view.forward(request, response);
    }

    private Integer getId(HttpServletRequest request) {
        String parameter = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(parameter);
    }
}
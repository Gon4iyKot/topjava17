package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class DataJpaMealRepository implements MealRepository {

    @Autowired
    private CrudMealRepository crudMealRepository;

    @Autowired
    private CrudUserRepository crudUserRepository;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(crudUserRepository.getOne(userId));
        if (meal.isNew() || get(meal.getId(), userId) != null) {
            return crudMealRepository.save(meal);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal foundMeal = crudMealRepository.findById(id).orElse(null);
        return (foundMeal != null && foundMeal.getUser().getId() == userId) ? foundMeal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAll(userId);
    }

    @Override
    public Meal getMealWithUser(int id, int userId) {
        return crudMealRepository.getMealWithUser(id, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudMealRepository.findAllBetween(userId, startDate, endDate);
    }
}
package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    
    
    public static int valueDay = 0;
    
    public static void main(String[] args) { 
        System.out.println("UserMealsUtil.main");
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,11,0), "Завтрак2", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,11,0), "Завтрак3", 600),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,11,0), "Завтрак4", 1500),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 30,11,0), "Завтрак4", 1500),
                new UserMeal(LocalDateTime.of(2016, Month.MAY, 30,11,0), "Завтрак4", 1500)
        );
        List<UserMealWithExceed> list = getFilteredMealsWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000); 
        
        list.stream().forEach((UserMealWithExceed o) -> { 
            System.out.println(o);
        }); 
    }

    public static List<UserMealWithExceed>  getFilteredMealsWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        valueDay = 0;
        List<UserMealWithExceed> list = new ArrayList();
        
         
        mealList.stream()
            .filter((UserMeal m) -> {
                    return TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime);
        })
            .sorted((UserMeal a, UserMeal b) -> {
                    return a.getDateTime().compareTo(b.getDateTime());
        })
            .forEach((UserMeal m) -> { 
                       
                    UserMealWithExceed e = new UserMealWithExceed(
                            m.getDateTime(),
                            m.getDescription(),
                            m.getCalories(),
                            false
                    );  
                    
                    if(list.size()  > 1 ) {
                        UserMealWithExceed last = list.get(list.size() - 1);
                        if(!e.getDateTime().toLocalDate().equals(last.getDateTime().toLocalDate())) {  
                           if(valueDay > caloriesPerDay) { 
                                list.stream()
                                    .filter((UserMealWithExceed o) -> { 
                                        return last.getDateTime().toLocalDate().equals(o.getDateTime().toLocalDate()); 
                                    })
                                    .forEach((UserMealWithExceed o) -> {
                                        o.setExceed(true);
                                });
                            }
                            valueDay = 0;
                        } 
                    }
                    valueDay += e.getCalories(); 
                    list.add(e);  
        });
        
        if(list.size()  > 1 ) {
            UserMealWithExceed last = list.get(list.size() - 1); 
            if(valueDay > caloriesPerDay ) { 
                list.stream()
                    .filter((UserMealWithExceed o) -> { 
                        return last.getDateTime().toLocalDate().equals(o.getDateTime().toLocalDate()); 
                })
                    .forEach((UserMealWithExceed o) -> {
                        o.setExceed(true);
                });
            }
                        
        }
        return list;
    }
}
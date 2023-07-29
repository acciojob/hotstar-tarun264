package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        userRepository.save(user);
        return user.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        User user= userRepository.findById(userId).get();
        Subscription subType=user.getSubscription();

        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        int count=0;

        if(subType.equals(SubscriptionType.BASIC)){
        for(WebSeries webSeries: webSeriesList){
            //can watch only basics
            if(subType.equals(SubscriptionType.BASIC)) count++;
        }
        }
        else if (subType.equals(SubscriptionType.ELITE))
        {
            //can watch basics+elite
            for(WebSeries webSeries: webSeriesList){
                if(subType.equals(SubscriptionType.BASIC) || subType.equals(SubscriptionType.ELITE)) count++;
            }
        }
        else{
            for(WebSeries webSeries: webSeriesList) count++;
        }


        return count;
    }


}

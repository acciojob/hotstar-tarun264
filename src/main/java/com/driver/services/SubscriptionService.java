package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
        //We need to buy subscription and save its relevant subscription to the db and return the finalAmount
        //Save The subscription Object into the Db and return the total Amount that user has to pay

        User user= userRepository.findById(subscriptionEntryDto.getUserId()).get();
// dto to entity
        Subscription subscription= new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());

        SubscriptionType subscriptionType=subscription.getSubscriptionType();
        int amount=0;
        if(subscriptionType.equals(SubscriptionType.BASIC)){
            amount= 500 + 200* subscription.getNoOfScreensSubscribed();
        }
        else if (subscriptionType.equals(SubscriptionType.PRO)) {
            amount= 800 + 250*subscription.getNoOfScreensSubscribed();
        }
        else if (subscriptionType.equals(SubscriptionType.ELITE)){
            {
                amount= 1000 + 350*subscription.getNoOfScreensSubscribed();
            }
        }
        subscription.setTotalAmountPaid(amount);
        subscription.setUser(user);
        user.setSubscription(subscription);

        User savedUser= userRepository.save(user);
        return savedUser.getId();


    }

    public Integer upgradeSubscription(Integer userId)throws Exception{
        //In this function you need to upgrade the subscription to  its next level
        //ie if You are A BASIC subscriber update to PRO and if You are a PRO upgrade to ELITE.
        //Incase you are already an ELITE member throw an Exception
        //and at the end return the difference in fare that you need to pay to get this subscription done.

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        if(user==null){
            return null;
        }

        Subscription subscription= user.getSubscription();
        SubscriptionType subscriptionType= subscription.getSubscriptionType();
        int difference=0;
        if(subscriptionType.equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        else if(subscriptionType.equals(SubscriptionType.BASIC)){
            difference= 800 + 250*subscription.getNoOfScreensSubscribed()- subscription.getTotalAmountPaid();
            subscription.setSubscriptionType(SubscriptionType.PRO);
        }
        else if(subscriptionType.equals(SubscriptionType.PRO)){
            difference= 1000 + 350*subscription.getNoOfScreensSubscribed()-subscription.getTotalAmountPaid();
            subscription.setSubscriptionType(SubscriptionType.ELITE);
        }

        subscriptionRepository.save(subscription);


        return difference;
    }

    public Integer calculateTotalRevenueOfHotstar(){
        //Calculate the total Revenue of hot-star from all the Users combined...

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> subscriptionList= subscriptionRepository.findAll();
        if(subscriptionList.size()==0){
            return null;
        }

        int totalReveneue=0;

        for(Subscription subscriptions: subscriptionList){
            totalReveneue+= subscriptions.getTotalAmountPaid();
        }
        return totalReveneue;

    }
}

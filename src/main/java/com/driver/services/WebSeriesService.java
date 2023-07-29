package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{
        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        WebSeries webSeries=webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());

        if(webSeries!=null){
            throw new Exception("Series is already present");
        }
        // dto to entity
        WebSeries webSeries1= new WebSeries();
        webSeries1.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries1.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries1.setRating(webSeriesEntryDto.getRating());
        webSeries1.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());

        Optional<ProductionHouse> productionHouseOpt = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        ProductionHouse productionHouse = productionHouseOpt.get();
        if(productionHouse == null){
            throw new Exception("Production house is not present");
        }
        // setting the webseries to production house
        webSeries1.setProductionHouse(productionHouse);

        productionHouse.getWebSeriesList().add(webSeries1);

        double oldRating = productionHouse.getRatings();
        double newRating = webSeries1.getRating();
        int size = productionHouse.getWebSeriesList().size();
        double updateRating = oldRating + (newRating - oldRating)/size;
        productionHouse.setRatings(updateRating);

        productionHouseRepository.save(productionHouse);
        WebSeries savedWebseries= webSeriesRepository.save(webSeries);

        return savedWebseries.getId();

    }

}

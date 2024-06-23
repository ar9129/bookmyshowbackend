package com.aditya.bookmyshow.controller;

import com.aditya.bookmyshow.dto.ScreenDTO;
import com.aditya.bookmyshow.dto.TheatreDTO;
import com.aditya.bookmyshow.model.*;
import com.aditya.bookmyshow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.chrono.ThaiBuddhistChronology;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class CreateController {

    @Autowired
   private CityRepository cityRepository ;

    @Autowired
    private TheatreRepository theatreRepository ;

    @Autowired
    private SeatRepository seatRepository ;

    @Autowired
    private CategoryRepository categoryRepository ;

    @Autowired
    private ScreenRepository screenRepository ;


//
//    public CreateController(CityRepository cityRepository) {
//        this.cityRepository = cityRepository;
//    }

    @PostMapping("/create-city")
    public City createCity(@RequestBody  City city){
        return cityRepository.save(city) ;
    }

    @PostMapping("/create-theatre")
    public Theatre createTheatre(@RequestBody TheatreDTO theatreDTO){
        Optional<City>city1 = cityRepository.findById(theatreDTO.getCityID()) ;
        Theatre theatre =  Theatre.builder().city(city1.get()).name(theatreDTO.getName()).build() ;
        return  theatreRepository.save(theatre) ;
    }

    @PostMapping("/create-screen")
    public Screen createScreen(@RequestBody ScreenDTO screenDTO) {
        Optional<Theatre> theatre1 = theatreRepository.findById(screenDTO.getTheatreID());


        Screen screen1 = Screen.builder().name(screenDTO.getName()).theatre(theatre1.get()).build();
        Screen s = screenRepository.save(screen1);
        List<Seat> seats1 = new ArrayList<Seat>();
        for (Map.Entry<String, List<String>> entry : screenDTO.getCategoryTOSeatNumber().entrySet()) {
            String category = entry.getKey();
            List<String> seatNumbers = entry.getValue();
           // seats1 = new ArrayList<Seat>();

            Category category1 = Category.builder().categoryName(category).build();
            categoryRepository.save(category1);
            for (String seatNumber : seatNumbers) {

                Seat seat = Seat.builder().category(category1).seatNumber(seatNumber).screen(s).build();
                seats1.add(seat);
                seatRepository.save(seat);


            }

        }
        s.setSeats(seats1);
        return s;

    }
}

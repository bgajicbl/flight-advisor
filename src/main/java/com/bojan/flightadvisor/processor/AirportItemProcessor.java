package com.bojan.flightadvisor.processor;

import com.bojan.flightadvisor.dto.model.AirportDto;
import com.bojan.flightadvisor.entity.Airport;
import com.bojan.flightadvisor.entity.City;
import com.bojan.flightadvisor.repository.CityRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AirportItemProcessor implements ItemProcessor<AirportDto, Airport> {

    @Value("${app.load.cities:false}")
    private boolean loadCities;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Airport process(final AirportDto airportDto) throws Exception {
        Optional<City> cityOpt = cityRepository.findByNameAndCountry(airportDto.getCity(), airportDto.getCountry());

        if (!cityOpt.isPresent()) {
            if (!loadCities) {
                return null;
            }
            City city = new City()
                    .setName(airportDto.getCity())
                    .setCountry(airportDto.getCountry())
                    .setDescription("-");
            cityOpt = Optional.ofNullable(cityRepository.save(city));
        }

        Airport airport = new Airport()
                .setId(airportDto.getId())
                .setName(airportDto.getName())
                .setCity(cityOpt.get())
                .setIata(airportDto.getIata())
                .setIcao(airportDto.getIcao())
                .setLatitude(airportDto.getLatitude())
                .setLongitude(airportDto.getLongitude())
                .setAltitude(airportDto.getAltitude())
                .setTimezone(airportDto.getTimezone())
                .setDst(airportDto.getDst())
                .setTzOlson(airportDto.getTzOlson())
                .setType(airportDto.getType())
                .setSource(airportDto.getSource());

        return airport;
    }
}

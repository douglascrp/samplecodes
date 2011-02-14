package com.samplecodes.service;

import com.samplecodes.dao.CargoDao;
import com.samplecodes.dao.CityDao;
import com.samplecodes.dao.LocationDao;
import com.samplecodes.dao.StationDao;
import com.samplecodes.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommonService {

    @Resource
    protected CargoDao cargoDao;
    @Resource
    private CityDao cityDao;
    @Resource
    private StationDao stationDao;


    public List<City> listCities() {
        return cityDao.list();
    }

    public City getCity(String name) {
        return cityDao.findById(name);
    }

    public List<Station> listStations() {
        return stationDao.list();
    }

    public Station getStation(String name) {
        return stationDao.findById(name);
    }

    public List<Cargo> listCargos() {
        return cargoDao.list();
    }

    public void saveOrUpdate(Cargo cargo) {
        cargoDao.merge(cargo);
    }

    public User login(String username, String password) {
        User returnValue = null;// (User)getAdmin(username);
        if (returnValue != null && returnValue.getPassword().matches(password)) {
            return returnValue;
        } else {
            return null;
        }
    }


}

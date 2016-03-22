package it.stasbranger.rotarylive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.stasbranger.rotarylive.dao.LocationRepository;
import it.stasbranger.rotarylive.domain.Location;

@Service("LocationService")
public class LocationServiceImpl implements LocationService {

	@Autowired private LocationRepository locationRepository;
	
	public void create(Location location){
		locationRepository.save(location);
	}
	
	public Location update(Location location){
		return locationRepository.save(location);
	}
	
	public void delete(Location location){
		locationRepository.delete(location);
	}
	
	public void delete(String id){
		locationRepository.delete(id);
	}
	
	public Location findOne(String id){
		return locationRepository.findOne(id);
	}
	
	public List<Location> findAll(){
		return locationRepository.findAll();
	}
}

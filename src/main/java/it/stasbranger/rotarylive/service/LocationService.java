package it.stasbranger.rotarylive.service;

import java.util.List;

import it.stasbranger.rotarylive.domain.Location;

public interface LocationService {

	public void create(Location location);
	public Location update(Location location);
	public Location findOne(String id);
	public List<Location> findAll();
	public void delete(String id);
	public void delete(Location location);
}

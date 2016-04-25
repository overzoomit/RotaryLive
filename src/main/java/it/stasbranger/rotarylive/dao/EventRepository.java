package it.stasbranger.rotarylive.dao;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.stasbranger.rotarylive.domain.Event;

public interface EventRepository extends MongoRepository<Event, ObjectId> {

	@Query("{\"name\" : {\"$regex\" : ?0, \"$options\": \"i\"}}")
	public Page<Event> findAll(String query, Pageable pageable);	

	@Query("{\"startDate\" : {\"$lte\" : ?0}, \"endDate\" : {\"$gte\" : ?0}}")
	public Page<Event> findAll(Date date, Pageable pageable);	
	
	@Query("{\"name\" : {\"$regex\" : ?0, \"$options\": \"i\"}, \"startDate\" : {\"$lte\" : ?1}, \"endDate\" : {\"$gte\" : ?1}}")
	public Page<Event> findAll(String query, Date date, Pageable pageable);	
	
	@Query("{\"startDate\" : {\"$lte\" : ?0}, \"endDate\" : {\"$gte\" : ?1}}")
	public Page<Event> findByDate(Date date1, Date date2, Pageable pageable);	
	
	@Query("{\"startDate\" : {\"$gte\" : ?0}}")
	public Page<Event> findByDate(Date date, Pageable pageable);	
	
	@Query("{\"name\" : {\"$regex\" : ?0, \"$options\": \"i\"}, \"startDate\" : {\"$gte\" : ?1}}")
	public Page<Event> findByDate(String query, Date date1, Pageable pageable);	
	
	@Query("{\"name\" : {\"$regex\" : ?0, \"$options\": \"i\"}, \"startDate\" : {\"$gte\" : ?1}, \"endDate\" : {\"$lte\" : ?2}}")
	public Page<Event> findByDate(String query, Date date1, Date date2, Pageable pageable);	
}

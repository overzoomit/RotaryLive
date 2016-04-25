package it.stasbranger.rotarylive.dao;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.stasbranger.rotarylive.domain.User;


public interface UserRepository extends MongoRepository<User, ObjectId> {

	public User findByUsername(String username);
	
	@Query("{\"$or\":"
				+ "["
					+ "{\"name\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"member.firstName\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"member.lastName\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"member.phone\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"member.email\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"job.headline\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"club.$name\" : {\"$regex\" : ?0, \"$options\": \"i\"}}, "
					+ "{\"member.mobile\" : {\"$regex\" : ?0, \"$options\": \"i\"}} "
				+ "]"
			+ "}")
	public Page<User> findAll(String query, Pageable pageable);
}
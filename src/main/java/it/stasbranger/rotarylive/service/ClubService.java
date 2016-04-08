package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.stasbranger.rotarylive.domain.Club;

public interface ClubService {

	public void create(Club club);
	public Club update(Club club);
	public Club findOne(ObjectId id);
	public List<Club> findAll();
	public Page<Club> findAll(Pageable pageable);
	public void delete(ObjectId id);
	public void delete(Club club);
	public List<Club> findByNameLike(String name);
	public Club addImage(Club club) throws IOException;
}

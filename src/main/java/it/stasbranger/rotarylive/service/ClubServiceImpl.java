package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.dao.ClubRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.domain.Club;

@Service("ClubService")
public class ClubServiceImpl implements ClubService {
	
	@Autowired private ClubRepository clubRepository;
	@Autowired private AttachService attachService;
	
	public void create(Club club){
		if(clubRepository.findByName(club.getName())!=null) throw new DuplicateKeyException("this club already exists");

		clubRepository.save(club);
	}

	public Club update(Club club){
		return clubRepository.save(club);
	}
	
	public void delete(Club club){
		clubRepository.delete(club);
	}
	
	public void delete(ObjectId id){
		clubRepository.delete(id);
	}
	
	public Club findOne(ObjectId id){
		return clubRepository.findOne(id);
	}
	
	public List<Club> findAll(){
		return clubRepository.findAll();
	}
	
	public Page<Club> findAll(Pageable pageable){
		return clubRepository.findAll(pageable);
	}
	
	public Page<Club> findByNameContainingIgnoreCase(String name, Pageable pageable){
		return clubRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
	public Club addImage(Club club) throws IOException {
		MultipartFile image = club.getFile();

		if(image != null){	
			if(club.getId() == null) club = update(club);
			String type = "LOGO_CLUB";
			Attach attach = attachService.createAttach(image, type);
			club.setLogoId(attach.getId());
			club = clubRepository.save(club);
		}
		return club;
	}
}

package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.domain.Attach;

public interface AttachService {
	public void create(Attach attach);
	public Attach update(Attach attach);
	public Attach findOne(ObjectId id);
	public List<Attach> findAll();
	public Page<Attach> findAll(Pageable pageable);
	public void delete(ObjectId id);
	public void delete(Attach attach);
	
	public Attach createAttach(MultipartFile file, String type) throws IOException;
	
	public void deleteAttach(Attach attach) throws IOException;

}

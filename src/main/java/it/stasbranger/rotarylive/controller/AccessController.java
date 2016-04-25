package it.stasbranger.rotarylive.controller;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import it.stasbranger.rotarylive.dao.AttachRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.domain.Club;
import it.stasbranger.rotarylive.domain.User;
import it.stasbranger.rotarylive.resource.ClubResource;
import it.stasbranger.rotarylive.resource.ClubResourceAssembler;
import it.stasbranger.rotarylive.service.ClubService;
import it.stasbranger.rotarylive.service.UserService;
import it.stasbranger.rotarylive.service.utility.UtilityService;

@RestController
@ExposesResourceFor(User.class)
public class AccessController {

	@Autowired private UserService userService;
	@Autowired private ClubService clubService;
	@Autowired private AttachRepository attachRepository;
	@Autowired private UtilityService utilityService;
	@Autowired private ClubResourceAssembler clubResourceAssembler;

	@InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

	@RequestMapping(value="/signup", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resources<User>> createUser(@RequestBody User user) {
		try{
			this.userService.create(user);
			return new ResponseEntity<Resources<User>>(HttpStatus.CREATED);
		}catch(DuplicateKeyException e){
			return new ResponseEntity<Resources<User>>(HttpStatus.CONFLICT);	
		}catch(Exception e){
			return new ResponseEntity<Resources<User>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/forgot_password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<User>> forgotPassword(@RequestParam String username) {
		User user = this.userService.findByUsername(username);
		if(user == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		if(user.getMember().getEmail() == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NO_CONTENT);
		}
		this.userService.forgotPassword(user);
		return new ResponseEntity<Resource<User>>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/reset_password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Resource<User>> resetPassword(@RequestParam("code") String code, @RequestParam("password") String password) {
		String id = utilityService.dencodeID(code);
		User user = this.userService.findOne(new ObjectId(id));
		if(user == null){
			return new ResponseEntity<Resource<User>>(HttpStatus.NOT_FOUND);
		}
		this.userService.resetPassword(user, password);
		return new ResponseEntity<Resource<User>>(HttpStatus.OK);
	}

	@RequestMapping(value="/club", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody 
	public HttpEntity<PagedResources<ClubResource>> showClubs(@RequestParam(value = "q", required = false) String query, @PageableDefault(size = 10, page = 0, direction = Sort.Direction.DESC, sort = "name") Pageable pageable, PagedResourcesAssembler<Club> assembler) {
		Page<Club> clubs = this.clubService.findByNameContainingIgnoreCase(query, pageable);
		PagedResources<ClubResource> resources = assembler.toResource(clubs, clubResourceAssembler);
		return new ResponseEntity<PagedResources<ClubResource>>(resources, HttpStatus.OK);
	}
	
	@RequestMapping(value = { "/attach/file/{code}", "/attach/image/{code}", "/attach/thumbnail/{code}" }, method = RequestMethod.GET)
    public HttpEntity<Resources<Attach>> showFile(@PathVariable("code") ObjectId code, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String uri = request.getRequestURI();
        try {
            Attach attach = attachRepository.findOne(code);
            String pathFile = attach.getPathFile();
            if(uri.contains("/thumbnail/")) {
            	pathFile = attach.getPathFile();
            	String ext = FilenameUtils.getExtension(pathFile);
            	pathFile = pathFile.replace("."+ext, "_thumbnail") + "." + ext;
            }
            Path path = Paths.get(pathFile);
            byte[] image = null;
            try {
                image = Files.readAllBytes(path);
            } catch (NoSuchFileException nsfe) {
                nsfe.printStackTrace();
            }
            if (image != null) {
                try {
                    String originalFilename = attach.getOriginalFilename() != null ? attach.getOriginalFilename() : path.getFileName().toString();
                    response.setContentType(attach.getContentType());
                    response.setHeader("Content-Disposition", "filename=\"" + originalFilename + "\"");
                    OutputStream out = response.getOutputStream();
                    IOUtils.copy(new ByteArrayInputStream(image), out);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ResponseEntity<Resources<Attach>>(HttpStatus.OK);
        } catch (Exception e) {
        	e.printStackTrace();
            return new ResponseEntity<Resources<Attach>>(HttpStatus.NOT_FOUND);
        }
    }
}

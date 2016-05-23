package it.stasbranger.rotarylive.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.stasbranger.rotarylive.dao.EventRepository;
import it.stasbranger.rotarylive.domain.Attach;
import it.stasbranger.rotarylive.domain.Booking;
import it.stasbranger.rotarylive.domain.Event;
import it.stasbranger.rotarylive.domain.User;

@Service("EventService")
public class EventServiceImpl implements EventService {

	@Autowired private EventRepository eventRepository;
	@Autowired private AttachService attachService;
	@Autowired private UserService userService;

	public void create(Event event){
		eventRepository.save(event);
	}

	public Event update(Event event){
		event.setDateModified(new Date());
		return eventRepository.save(event);
	}

	public void delete(Event event){
		eventRepository.delete(event);
	}

	public void delete(ObjectId id){
		eventRepository.delete(id);
	}

	public Event findOne(ObjectId id){
		return eventRepository.findOne(id);
	}

	public List<Event> findAll(){
		return eventRepository.findAll();
	}

	public Page<Event> findAll(Pageable pageable){
		return eventRepository.findAll(pageable);
	}

	public Page<Event> findAll(String query, Date date, String username, Pageable pageable){
		Page<Event> page = null;
		
		if(date == null && query != null) page = eventRepository.findAll(query, pageable);
		else if(date != null && (query == null || query.trim().equals(""))) page = eventRepository.findAll(date, pageable);
		else if (date != null && query != null && !query.trim().equals("")) page = eventRepository.findAll(query, date, pageable);
		else page = eventRepository.findAll(pageable);
		
		return isBooked(page, username);
	}

	public Page<Event> findByDate(String query, Date date1, Date date2, String username, Pageable pageable){

		if(date1 == null && date2 == null) date1 = new Date();

		Calendar start = Calendar.getInstance();
		start.setTime(date1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);

		date1 = start.getTime();

		if(date2 != null){
			Calendar end = Calendar.getInstance();
			end.setTime(date2);
			end.set(Calendar.HOUR_OF_DAY, 23);
			end.set(Calendar.MINUTE, 59);
			end.set(Calendar.SECOND, 59);
			end.set(Calendar.MILLISECOND, 999);

			date2 = end.getTime();
		}

		Page<Event> page = null;

		if(date2 == null && (query == null || query.trim().equals(""))) page = eventRepository.findByDate(date1, pageable);
		else if(date2 != null && (query == null || query.trim().equals(""))) page = eventRepository.findByDate(date1, date2, pageable);
		else if (date2 != null && query != null && !query.trim().equals("")) page = eventRepository.findByDate(query, date1, date2, pageable);
		else page = eventRepository.findByDate(query, date1, pageable);

		return isBooked(page, username);
	}

	private Page<Event> isBooked(Page<Event> page, String username){
		User user = this.userService.findByUsername(username);

		Iterator<Event> iter = page.iterator();
		while (iter.hasNext()) {
			Event event = (Event) iter.next();
			List<Booking> list = event.getBooking();
			for (Booking booking : list) {
				if(booking.getUser()!= null && booking.getUser().getId().equals(user.getId())){
					event.setBooked(true);
					break;
				}
			}
		}
		
		return page;
	}

	public List<Event> clientNotification(){
		List<Event> events = findAll();
		Iterator<Event> iterator = events.iterator();
		while (iterator.hasNext()) {
			Event event = (Event) iterator.next();
			System.out.println(event.getId());
		}

		return events;
	}

	public Event addImage(Event event) throws IOException {
		MultipartFile image = event.getFile();

		if(image != null){	
			if(event.getId() == null) event = update(event);
			String type = "IMAGE_CLUB";
			Attach attach = attachService.createAttach(image, type);
			event.setImageId(attach.getId());
			event = eventRepository.save(event);
		}
		return event;
	}

	public Event removeBooking(Event event, User user) throws Exception {
		boolean find = false;
		List<Booking> books = event.getBooking();
		Iterator<Booking> iter = books.iterator();
		while (iter.hasNext()) {
			Booking booking = (Booking) iter.next();
			if(booking.getUser().getUsername().equals(user.getUsername())){
				iter.remove();
				find = true;
				break;
			}
		}

		if(!find) throw new NotFoundException("this account not exists");

		event.setBooking(books);
		return eventRepository.save(event);
	}

	public Event createBooking(Event event, User user, String comment) throws Exception {
		Booking booking = new Booking();
		booking.setComment(comment);
		booking.setUser(user);
		boolean find = false;
		List<Booking> books = event.getBooking();

		for (Booking book : books) {
			if(book.getUser().getUsername().equals(user.getUsername())){
				find = true;
				break;
			}
		}

		if(find)throw new DuplicateKeyException("this account already exists");

		books.add(booking);
		event.setBooking(books);
		return eventRepository.save(event);
	}
}

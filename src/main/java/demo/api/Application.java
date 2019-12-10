package demo.api;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

@RestController
@RequestMapping(("hello"))
class HelloWorldController {

	@Autowired
	private UserReposiory repo;

	@PutMapping(value = "/{username}")
	public ResponseEntity<?> createUser(@PathVariable String username, @RequestBody User user) {
		User userdb = repo.findByUsername(username);
		if (userdb != null)
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		user.setUsername(username);
		repo.save(user);
		return ResponseEntity.accepted().build();
	}

	@GetMapping(value = "/{username}")
	public ResponseEntity<String> getUserBirthday(@PathVariable String username) {
		User user = repo.findByUsername(username);
		StringBuilder sb = new StringBuilder();
		sb.append("Hello, " + username + "!");
		if (user != null && user.getDateOfBirth() != null) {
			Calendar userDateOfBirth = Calendar.getInstance();
			userDateOfBirth.setTime(user.getDateOfBirth());
			Calendar now = Calendar.getInstance();
			if (userDateOfBirth.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)
					&& userDateOfBirth.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
				sb.append(" Happy birthday");
			} else {
				userDateOfBirth.set(Calendar.YEAR, now.get(Calendar.YEAR));
				long daysBetween = Math.abs(
						ChronoUnit.DAYS.between(userDateOfBirth.getTime().toInstant(), now.getTime().toInstant())) + 1;
				sb.append(" Your birthday is in " + daysBetween + " days");
			}
		}
		return ResponseEntity.ok().body(sb.toString());
	}

}

@Entity
@Table(name = "USER_TABLE")
class User {

	@Id
	@GeneratedValue
	private long id;
	private String username;
	private Date dateOfBirth;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}

@Repository
interface UserReposiory extends JpaRepository<User, Long> {

	User findByUsername(String username);

}

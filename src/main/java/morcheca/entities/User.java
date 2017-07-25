package morcheca.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;

//import org.hibernate.validator.constraints.Email;


@Entity
@Table(name="users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicUpdate
public class User
		implements UserDetails
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;

	@NotNull(message = "{user.name.isEmpty}")
	@Pattern(regexp = "^[a-zA-Z0-9\\\\._-]*$", message = "{user.name.invalid}")
	@Size(min = 3, max = 20, message = "{user.name.length}")
	@Column(name="name", unique = true)
	private String name;

	@NotNull(message = "{user.email.isEmpty}")
	@Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
			message = "{user.email.invalid}")
	@Size(min = 5, max = 50, message = "{user.email.length}")
	@Column(name="email", unique = true)
	private String email;

	@NotNull(message = "{user.password.isEmpty}")
	@Pattern(regexp = "^[a-zA-Z0-9!\"#$%&'()*+,-./:;<=>?@\\]\\[^_`{|}~\\\\]*$", message = "{user.password.invalid}")
	@Size(min = 8, max = 20, message = "{user.password.length}")
	@Column(name="password")
	private String password;

	@NotNull(message = "{user.passwordConfirm.isEmpty}")
	@Transient
	private String passwordConfirm;


	public User() {}

	public User(String name, String email, String password, String passwordConfirm) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_USER");
	}

	@Override
	public String getUsername() {
		return name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
package com.example.demo.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user" , uniqueConstraints = @UniqueConstraint(columnNames = "email"))
	public class User{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String LastName;
	private String userName;
	
	private String email;
	private String password;
	private Date createdDate;
	private boolean enabled = true;
	private String profile;
	

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
	@JsonIgnore
	private Set<UserRole> userRoles = new HashSet<>();
	
	
	public User() {
		super();
	}
	
//	public User(String firstName, String lastName, String email, String password, Date createdDate
//			) {
//		super();
//		this.firstName = firstName;
//		LastName = lastName;
//		this.email = email;
//		this.password = password;
//		this.createdDate = createdDate;
//		this.roles = roles;
//	}


	public User(Long id, String firstName, String lastName, String userName, String email, String password,
			Date createdDate, boolean enabled, String profile, Set<UserRole> userRoles) {
		super();
		this.id = id;
		this.firstName = firstName;
		LastName = lastName;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.createdDate = createdDate;
		this.enabled = enabled;
		this.profile = profile;
		this.userRoles = userRoles;
	}

	
	
	public Set<UserRole> getUserRoles() {  return userRoles;  }

	

	public void setUserRoles(Set<UserRole> userRoles) { this.userRoles = userRoles;}

	public String getProfile() { return profile;}

	public void setProfile(String profile) { this.profile = profile;}

	public Long getId() {return id;}

	public void setId(Long id) { this.id = id;}

	public String getFirstName() {return firstName;}

	public void setFirstName(String firstName) {this.firstName = firstName;}

	public String getLastName() {return LastName;}

	public void setLastName(String lastName) {LastName = lastName;}

	public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

	public void setPassword(String password) {this.password = password;}

    public Date getCreatedDate() {return createdDate;}

    public void setCreatedDate() {this.createdDate = new Date();}

//	public Collection<Role> getRoles() {return roles;}

//	public void setRoles(Collection<Role> roles) {this.roles = roles;}

	public String getUserName() {return userName;}


	public void setUserName(String userName) {this.userName = userName;}

	public boolean isEnabled() {return enabled;}

	public void setEnabled(boolean enabled) {this.enabled = enabled;}

	
	
	
	
	
	
	
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		
//		
//		Set<Authority> set = new HashSet<>();
//		this.userRoles.forEach(userRole -> {
//			set.add(new Authority(userRole.getRole().getRoleName()));
//		});
//		return null;
//	}
//
//	@Override
//	public String getUsername() {
//		return null;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}


	
	

}

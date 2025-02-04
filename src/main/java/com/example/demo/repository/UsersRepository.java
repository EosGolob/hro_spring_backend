package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OurUsers;

@Repository
public interface UsersRepository extends JpaRepository<OurUsers, Integer> {

	Optional<OurUsers> findByEmail(String email);

	@Query("SELECT DISTINCT u.process FROM OurUsers u WHERE u.process IS NOT NULL AND u.process <> 'Admin'")
	List<String> findDistinctProcesses();
	
}

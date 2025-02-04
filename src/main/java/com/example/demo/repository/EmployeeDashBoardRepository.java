package com.example.demo.repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import javax.management.ConstructorParameters;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Employee;

public interface EmployeeDashBoardRepository extends JpaRepository<Employee, Long> {

//	  @Query("SELECT COUNT(e) FROM Employee e WHERE e.creationDate = :date")
//	  long countByCreationDate(@Param("date") Date date);
	@Query("SELECT COUNT(e) FROM Employee e WHERE e.creationDate >= :startOfDay AND e.creationDate < :endOfDay")
	long countByCreationDate(@Param("startOfDay") Timestamp startOfDay, @Param("endOfDay") Timestamp endOfDay);

	@Query("SELECT e.managerStatus, COUNT(e) FROM Employee e GROUP BY e.managerStatus")
	List<Object[]> countByManagerStatus();

	@Query("SELECT e.jobProfile, e.gender, COUNT(e) FROM Employee e GROUP BY e.jobProfile, e.gender")
	List<Object[]> countByJobProfileAndGender();

	@Query("SELECT e.source, COUNT(e) FROM Employee e GROUP BY e.source")
	List<Object[]> getSourceCount();

	@Query("SELECT e.jobProfile, COUNT(e) FROM Employee e GROUP By e.jobProfile")
	List<Object[]> getCountAppliedJobProfile();

	 @Query("SELECT DATE(e.creationDate), COUNT(e) FROM Employee e " +
	           "GROUP BY DATE(e.creationDate) " +
	           "ORDER BY DATE(e.creationDate) ASC")
	List<Object[]> getDayWiseCount();

	@Query("SELECT YEAR(e.creationDate) AS year, MONTH(e.creationDate) AS month, COUNT(e) " +
	           "FROM Employee e " +
	           "GROUP BY YEAR(e.creationDate), MONTH(e.creationDate) " +
	           "ORDER BY YEAR(e.creationDate) ASC, MONTH(e.creationDate) ASC")
	List<Object[]> getMonthWiseCount();
	
	@Query("SELECT YEAR(e.creationDate) AS year, COUNT(e) " +
	           "FROM Employee e " +
	           "GROUP BY YEAR(e.creationDate)" +
	           "ORDER BY YEAR(e.creationDate) ASC")
	List<Object[]> getYearWiseCount();

	@Query("SELECT e.jobProfile AS jobProfile, " +
	           "COUNT(e) AS totalCount, " +
	           "COUNT(CASE WHEN e.managerStatus = 'Select' THEN 1 END) AS selectCount, " +
	           "COUNT(CASE WHEN e.managerStatus = 'Reject' THEN 1 END) AS rejectCount, " +
	           "COUNT(CASE WHEN e.managerStatus IS NULL THEN 1 END) AS inProgressCount " +
	           "FROM Employee e " +
	           "GROUP BY e.jobProfile " +
	           "ORDER BY totalCount DESC")
	List<Object[]> getSelectRejectedCount();


	@Query("SELECT YEAR(e.creationDate) AS year, MONTH(e.creationDate) AS month, COUNT(e) " +
		       "FROM Employee e " +
		       "WHERE (:year IS NULL OR YEAR(e.creationDate) = :year) " +
		       "AND (:month IS NULL OR MONTH(e.creationDate) = :month) " +
		       "GROUP BY YEAR(e.creationDate), MONTH(e.creationDate) " +
		       "ORDER BY YEAR(e.creationDate) ASC, MONTH(e.creationDate) ASC")
	List<Object[]> getMonthWiseCountForYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

	
	@Query(value = "SELECT * FROM employees Where EXTRACT(MONTH FROM creation_Date) = :month AND EXTRACT(YEAR FROM creation_Date) = :year",nativeQuery = true)
	List<Employee> totalCountOnDateWise(@Param("month") Integer month , @Param("year") Integer year);
	
//	@Query(value = "SELECT e.manager_status, COUNT(e.id) FROM employees e WHERE DATE(e.creation_date) = :creationDate GROUP BY e.manager_status", nativeQuery = true)
	@Query(value = "SELECT e.manager_status, COUNT(e.id) FROM employees e WHERE EXTRACT(MONTH FROM e.creation_date) = :month AND EXTRACT(YEAR FROM e.creation_date) = :year GROUP BY e.manager_status", nativeQuery = true)
	List<Object[]> countByManagerStatus(@Param("month") Integer month , @Param("year") Integer year);

}

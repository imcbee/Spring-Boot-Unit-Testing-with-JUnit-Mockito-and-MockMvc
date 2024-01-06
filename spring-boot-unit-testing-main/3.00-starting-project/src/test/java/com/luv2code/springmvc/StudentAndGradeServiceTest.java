package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
  @Autowired
  private StudentAndGradeService studentAndGradeService;

  @Autowired
  private StudentDao studentDao;

  @Autowired
  private JdbcTemplate jdbc;

  @BeforeEach
  public void setupDataBase() {
    jdbc.execute("insert into student(id, firstname, lastname, email_address) " +
      "values (1, 'Ian', 'McBee', 'ian@mcbee')");
  }

  @Test
  public void createStudentService() {
    //studentAndGradeService.createStudent("Ian", "McBee", "ian@mcbee");

    CollegeStudent student = studentDao.findByEmailAddress("ian@mcbee");

    assertEquals("ian@mcbee", student.getEmailAddress(),"find by email");
  }

  @Test
  public void isStudentNullCheck() {
    assertTrue(studentAndGradeService.checkIfStudentIsNull(1));
    assertFalse(studentAndGradeService.checkIfStudentIsNull(0));
  }

  @Test
  public void deleteStudentSErvice() {
    Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);

    assertTrue(deletedCollegeStudent.isPresent(), "Return true");

    studentAndGradeService.deleteStudent(1);

    deletedCollegeStudent = studentDao.findById(1);

    assertFalse(deletedCollegeStudent.isPresent(), "Return false");
  }

  @AfterEach
  public void setUpAfterTransaction() {
    jdbc.execute("delete from student");
  }
}

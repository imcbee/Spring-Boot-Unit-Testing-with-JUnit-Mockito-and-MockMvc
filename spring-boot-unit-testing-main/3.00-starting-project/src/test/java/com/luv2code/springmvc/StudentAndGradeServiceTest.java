package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
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

  @Autowired
  MathGradesDao mathGradesDao;

  @Autowired
  ScienceGradesDao scienceGradesDao;

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

  @Test
  @Sql("/insertData.sql")
  public void getGradebookService() {
    Iterable<CollegeStudent> interableCollegeStudents = studentAndGradeService.getGradebook();

    List<CollegeStudent> collegeStudents = new ArrayList<>();

    for (CollegeStudent collegeStudent: interableCollegeStudents) {
      collegeStudents.add(collegeStudent);
    }

    assertEquals(6, collegeStudents.size());
  }

  @Test
  public void createGradeService() {

    // create the grade
    assertTrue(studentAndGradeService.createGrade(80.50, 1, "math"));
    assertTrue(studentAndGradeService.createGrade(80.50, 1, "science"));
    // get all grades with studentId

    Iterable<MathGrade> mathGrades = mathGradesDao.findGradeByStudentId(1);
    Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findGradeByStudentId(1);

    // verify there is grades
    assertTrue(mathGrades.iterator().hasNext(), "Student has math grades");
    assertTrue(scienceGrades.iterator().hasNext(), "Student has math grades");

  }

  @AfterEach
  public void setUpAfterTransaction() {
    jdbc.execute("delete from student");
  }
}

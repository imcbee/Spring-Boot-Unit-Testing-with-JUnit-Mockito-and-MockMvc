package com.luv2code.component;

import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
// @SpringBootTest(classes=MvcTestingExampleApplication.class) <- this is if the testing file names are different
public class ApplicationExampleTest {

  private static int count = 0;

  @Value("${info.app.name}")
  private String appInfo;
  @Value("${info.app.description}")
  private String appDescription;
  @Value("${info.app.version}")
  private String appVersion;
  @Value("${info.school.name}")
  private String schoolName;

  @Autowired
  CollegeStudent student;

  @Autowired
  StudentGrades studentGrades;

  @BeforeEach
  public void beforeEach() {
    count++;
    System.out.println(
      "Testing: " + appInfo + " which is " + appDescription + " Version: " + appVersion + ". Execution of test method " + count
    );

    student.setFirstname("Eric");
    student.setLastname("Roby");
    student.setEmailAddress("erc_roby@luv2code.com");
    studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.50, 91.75)));
    student.setStudentGrades(studentGrades);
  }

  @Test
  @DisplayName("Add grade results for student grades")
  public void addGradeResultsForStudentGrades() {
    assertEquals(353.25,  studentGrades.addGradeResultsForSingleClass(
      student.getStudentGrades().getMathGradeResults()
    ));
  }

  @Test
  @DisplayName("Add grade results for student grades not equal")
  public void addGradeResultsForStudentGradesAssertNotEquals() {
    assertNotEquals(0, studentGrades.addGradeResultsForSingleClass(
      student.getStudentGrades().getMathGradeResults()
    ));
  }
}

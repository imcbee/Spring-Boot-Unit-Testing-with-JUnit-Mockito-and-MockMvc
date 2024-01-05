package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = MvcTestingExampleApplication.class)
public class MockAnnotationTest {

  @Autowired
  ApplicationContext context;

  @Autowired
  CollegeStudent studentOne;

  @Autowired
  StudentGrades studentGrades;

//  @Mock
//  private ApplicationDao applicationDao;

  @MockBean //! this is preferred
  private ApplicationDao applicationDao;

//  @InjectMocks
//  private ApplicationService applicationService;
  @Autowired //! this is preferred
  private ApplicationService applicationService;

  @BeforeEach
  public void beforeEach() {
    studentOne.setFirstname("Ian");
    studentOne.setLastname("McBee");
    studentOne.setEmailAddress("ian@mcbee.com");
    studentOne.setStudentGrades(studentGrades);
  }

  @Test
  @DisplayName("When & Verify ")
  public void assertEqualsTestAddGrades() {
    when(applicationDao.addGradeResultsForSingleClass(
      studentGrades.getMathGradeResults())).thenReturn(100.00);

    assertEquals(100, applicationService.addGradeResultsForSingleClass(
      studentOne.getStudentGrades().getMathGradeResults()));

    verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    verify(applicationDao, times(1)).addGradeResultsForSingleClass(
      studentGrades.getMathGradeResults());
  }

  @Test
  @DisplayName("Find GPA")
  public void assertEqualsTestFindGpa() {
    when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
      .thenReturn(88.31);
    assertEquals(88.31, applicationService.findGradePointAverage(studentOne
      .getStudentGrades().getMathGradeResults()));
  }

  @Test
  @DisplayName("Not Null")
  public void testAssertNotNull() {
    when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
      .thenReturn(true);
    assertNotEquals(applicationService.checkNull(studentOne.getStudentGrades()
      .getMathGradeResults()), "Object should not be null");
  }

  @Test
  @DisplayName("Throw runtime error")
  public void throwRuntimeError() {
    CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

    doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);

    assertThrows(RuntimeException.class, () -> {
      applicationService.checkNull(nullStudent);
    });

    verify(applicationDao, times(1)).checkNull(nullStudent);
  }

  @Test
  @DisplayName("Multiple Stubbing")
  public void stubbingConsecutiveCalls() {
    CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

    when(applicationDao.checkNull(nullStudent))
      .thenThrow(new RuntimeException())
      .thenReturn("Do not throw exception second time");

    assertThrows(RuntimeException.class, () -> {
      applicationService.checkNull(nullStudent);
    });

    assertEquals("Do not throw exception second time",
      applicationService.checkNull(nullStudent));

    verify(applicationDao, times(2)).checkNull(nullStudent);
  }
}

package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {
  // this is static because all BeforeAll methods must be static
  private static MockHttpServletRequest request;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private StudentDao studentDao;

  @Mock
  private StudentAndGradeService studentCreateServiceMock;

  @Value("${sql.script.create.student}")
  private String sqlAddStudent;

  @Value("${sql.script.create.math.grade}")
  private String sqlAddMathGrade;

  @Value("${sql.script.create.history.grade}")
  private String sqlAddHistoryGrade;

  @Value("${sql.script.create.science.grade}")
  private String sqlAddScienceGrade;

  @Value("${sql.script.delete.student}")
  private String sqlDeleteStudent;

  @Value("${sql.script.delete.math.grade}")
  private String sqlDeleteMathGrade;

  @Value("${sql.script.delete.history.grade}")
  private String sqlDeleteHistoryGrade;

  @Value("${sql.script.delete.science.grade}")
  private String sqlDeleteScienceGrade;

  @Autowired
  private StudentAndGradeService studentService;

  @Autowired
  private MathGradesDao mathGradesDao;

  // this is static because all BeforeAll methods must be static
  @BeforeAll
  public static void setup() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Ian2");
    request.setParameter("lastname", "McBee2");
    request.setParameter("emailAddress", "ian@mcbee2");
  }

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute(sqlAddStudent);
    jdbcTemplate.execute(sqlAddMathGrade);
    jdbcTemplate.execute(sqlAddHistoryGrade);
    jdbcTemplate.execute(sqlAddScienceGrade);
  }

  @Test
  public void getStudentsHttpRequest() throws Exception {
    CollegeStudent studentOne = new GradebookCollegeStudent("Ian", "McBee", "ian@mcbee");
    CollegeStudent studentTwo = new GradebookCollegeStudent("Billy", "Frank", "billy@frank");

    List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

    when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

    assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    assert modelAndView != null;
    ModelAndViewAssert.assertViewName(modelAndView, "index");
  }

  @Test
  public void createStudentHttpRequest() throws Exception{
    CollegeStudent studentOne = new CollegeStudent("Eric",
      "Roby", "eric_roby@luv2code.com");

    List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));

    when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

    assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

    MvcResult mvcResult = this.mockMvc.perform(post("/")
      .contentType(MediaType.APPLICATION_JSON)
      .param("firstname", request.getParameterValues("firstname"))
      .param("lastname", request.getParameterValues("lastname"))
      .param("emailAddress", request.getParameterValues("emailAddress")))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    assert modelAndView != null;
    ModelAndViewAssert.assertViewName(modelAndView, "index");

    CollegeStudent verifyStudent = studentDao.findByEmailAddress("ian@mcbee2");
    
    assertNotNull(verifyStudent, "Student should be found");
  }

  @Test
  public void deleteStudentHttpRequest() throws Exception {

    assertTrue(studentDao.findById(1).isPresent());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/delete/student/{id}", 1))
      .andExpect(status().isOk()).andReturn();

    ModelAndView mav = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(mav, "index");

    assertFalse(studentDao.findById(1).isPresent());
  }

  @Test
  public void deleteStudentHttpRequestErrorPage() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/delete/student/{id}", 0))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    assert modelAndView != null;
    ModelAndViewAssert.assertViewName(modelAndView, "error");
  }

  @Test
  public void studentInformationHttpRequest() throws Exception {

    assertTrue(studentDao.findById(1).isPresent());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 1))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");

  }

  @Test
  public void studentInformationHttpStudentDoesNotExistRequest() throws Exception {
    assertFalse(studentDao.findById(0).isPresent());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}", 0))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "error");
  }

  @Test
  public void createValidGradeHttpRequest() throws Exception {
    assertTrue(studentDao.findById(1). isPresent());

    GradebookCollegeStudent student = studentService.studentInformation(1);

    assertEquals(1, student.getStudentGrades().getMathGradeResults().size());

    MvcResult mvcResult = this.mockMvc.perform(post("/grades")
      .contentType(MediaType.APPLICATION_JSON)
      .param("grade", "85.00")
      .param("gradeType", "math")
      .param("studentId", "1")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");

    student = studentService.studentInformation(1);

    assertEquals(2, student.getStudentGrades().getMathGradeResults().size());
  }

  @Test
  public void createAValidGradeHttpRequestDoesNotExistEmptyResponse() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(post("/grades")
      .contentType(MediaType.APPLICATION_JSON)
      .param("grade", "85.00")
      .param("gradeType", "history")
      .param("studentId", "0")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "error");
  }

  @Test
  public void createANonValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(post("/grades")
      .contentType(MediaType.APPLICATION_JSON)
      .param("grade", "85.00")
      .param("gradeType", "literature")
      .param("studentId", "1")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "error");
  }

  @Test
  public void deleteAValidGradeHttpRequest() throws Exception {

    Optional<MathGrade> mathGrade = mathGradesDao.findById(1);

    assertTrue(mathGrade.isPresent());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/grades/{id}/{gradeType}", 1, "math")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "studentInformation");

    mathGrade = mathGradesDao.findById(1);

    assertFalse(mathGrade.isPresent());
  }

  @Test
  public void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse() throws Exception {
    Optional<MathGrade> mathGrade = mathGradesDao.findById(2);

    assertFalse(mathGrade.isPresent());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/grades/{id}/{gradeType}", 2, "math")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView,"error");
  }

  @Test
  public void deleteANonValidGradeHttpRequest() throws Exception {
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
      .get("/grades/{id}/{gradeType}", 1, "literature")
    ).andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "error");
  }

  @AfterEach
  public void setUpAfterTransaction() {
    jdbcTemplate.execute(sqlDeleteStudent);
    jdbcTemplate.execute(sqlDeleteMathGrade);
    jdbcTemplate.execute(sqlDeleteHistoryGrade);
    jdbcTemplate.execute(sqlDeleteScienceGrade);
  }
}

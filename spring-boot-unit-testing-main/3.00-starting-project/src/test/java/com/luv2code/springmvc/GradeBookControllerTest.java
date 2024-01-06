package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
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
  private StudentAndGradeService studentAndGradeServiceMock;

  // this is static because all BeforeAll methods must be static
  @BeforeAll
  public static void setup() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Ian");
    request.setParameter("lastname", "McBee");
    request.setParameter("emailAddress", "ian@mcbee");
  }

  @BeforeEach
  public void beforeEach() {
    jdbcTemplate.execute("insert into student(id, firstname, lastname, email_address) " +
      "values (1, 'Ian', 'McBee', 'ian@mcbee')");
  }

  @Test
  public void getStudentsHttpRequest() throws Exception {
    CollegeStudent studentOne = new GradebookCollegeStudent("Ian", "McBee", "ian@mcbee");
    CollegeStudent studentTwo = new GradebookCollegeStudent("Billy", "Frank", "billy@frank");

    List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

    when(studentAndGradeServiceMock.getGradebook()).thenReturn(collegeStudentList);

    assertIterableEquals(collegeStudentList, studentAndGradeServiceMock.getGradebook());

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    assert modelAndView != null;
    ModelAndViewAssert.assertViewName(modelAndView, "index");
  }

  @Test
  public void createStudentHttpRequest() throws Exception{
    MvcResult mvcResult = this.mockMvc.perform(post("/")
      .contentType(MediaType.APPLICATION_JSON)
      .param("firstname", request.getParameterValues("firstname"))
      .param("lastname", request.getParameterValues("lastname"))
      .param("emailAddress", request.getParameterValues("emailAddress")))
      .andExpect(status().isOk()).andReturn();

    ModelAndView modelAndView = mvcResult.getModelAndView();

    ModelAndViewAssert.assertViewName(modelAndView, "index");

    CollegeStudent verifyStudent = studentDao.findByEmailAddress("ian@mcbee");
    
    assertNotNull(verifyStudent, "Student should be found");
  }



  @AfterEach
  public void setUpAfterTransaction() {
    jdbcTemplate.execute("delete from student");
  }
}

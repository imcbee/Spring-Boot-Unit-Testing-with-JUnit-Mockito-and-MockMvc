package com.luv2code.springmvc;

import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GradebookControllerTest {
  private static MockHttpServletRequest request;

  @PersistenceContext
  private EntityManager entityManager;

  @Mock
  StudentAndGradeService studentCreateServiceMock;

  @Autowired
  private JdbcTemplate jdbc;

  @Autowired
  private StudentDao studentDao;

  @Autowired
  private MathGradesDao mathGradeDao;

  @Autowired
  private ScienceGradesDao scienceGradeDao;

  @Autowired
  private HistoryGradesDao historyGradeDao;

  @Autowired
  private StudentAndGradeService studentService;

  @Value("${sql.script.create.student}")
  private String sqlAddStudent;

  @Value("${sql.script.create.math.grade}")
  private String sqlAddMathGrade;

  @Value("${sql.script.create.science.grade}")
  private String sqlAddScienceGrade;

  @Value("${sql.script.create.history.grade}")
  private String sqlAddHistoryGrade;

  @Value("${sql.script.delete.student}")
  private String sqlDeleteStudent;

  @Value("${sql.script.delete.math.grade}")
  private String sqlDeleteMathGrade;

  @Value("${sql.script.delete.science.grade}")
  private String sqlDeleteScienceGrade;

  @Value("${sql.script.delete.history.grade}")
  private String sqlDeleteHistoryGrade;

  @BeforeAll
  public static  void setup() {
    request = new MockHttpServletRequest();
    request.setParameter("firstname", "Ian");
    request.setParameter("lastname", "McBee");
    request.setParameter("emailAddress", "ian@mcbee");
  }

  @BeforeEach
  public void setupDatabase() {
    jdbc.execute(sqlAddStudent);
    jdbc.execute(sqlAddMathGrade);
    jdbc.execute(sqlAddScienceGrade);
    jdbc.execute(sqlAddHistoryGrade);
  }

  @Test
  public void placeHolder() {

  }

  @AfterEach
  public void setupAfterTransaction() {
    jdbc.execute(sqlDeleteStudent);
    jdbc.execute(sqlDeleteMathGrade);
    jdbc.execute(sqlDeleteScienceGrade);
    jdbc.execute(sqlDeleteHistoryGrade);
  }
}

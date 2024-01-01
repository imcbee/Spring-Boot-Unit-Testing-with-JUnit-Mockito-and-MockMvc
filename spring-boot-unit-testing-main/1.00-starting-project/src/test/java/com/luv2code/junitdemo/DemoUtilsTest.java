package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class DemoUtilsTest { // test files should not be p ublic

    DemoUtils demoUtils;

  @BeforeEach
  void setUpBeforeEach() {
    demoUtils = new DemoUtils();
    System.out.println("@BeforeEach executes before the execution of each test method");
  }

//  @BeforeAll
//  static void setUpBeforeEachClass() {
//    System.out.println("@BeforeAll executes only once before all test methods execution in the class");
//  }
//
//  @AfterAll
//  static void tearDownAfterAll() {
//    System.out.println("@AfterAll executes only once after all test methods execution in the class");
//  }
//
//
//  @AfterEach
//  void tearDownAfterEach() {
//    System.out.println("Running @AfterEach");
//    System.out.println();
//  }

  @Test
  @DisplayName("Equals and Not Equals")
  void testEqualsAndNotEquals() {
    //System.out.println("Running test: testEqualsAndNotEquals");

    assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
    assertNotEquals(6, demoUtils.add(1, 9), "1+9 must be 6");
  }

  @Test
  @DisplayName("Null and Not Null")
  void testNullAndNotNull() {
    //System.out.println("Running test: testNullAndNotNull");
    DemoUtils demoUtils = new DemoUtils();

    String str1 = null;
    String str2 = "luv2code";

    assertNull(demoUtils.checkNull(str1), "Object should be null");
    assertNotNull(demoUtils.checkNull(str2), "Object should not be null");
  }

  @Test
  @DisplayName("Same and Not Same")
  void testSameAndNotSame() {
    String str = "luv2code";

    assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Objects should refer to the same object.");
    assertNotSame(str, demoUtils.getAcademy(),"Objects should not refer to the same object.");
  }

  @Test
  @DisplayName("True and False")
  void testTrueFalse() {
    int gradeOne = 10;
    int gradeTwo = 5;

    assertTrue(demoUtils.isGreater(gradeOne, gradeTwo), "This should return true");
    assertTrue(demoUtils.isGreater(gradeTwo, gradeOne), "This should return true");
  }
}

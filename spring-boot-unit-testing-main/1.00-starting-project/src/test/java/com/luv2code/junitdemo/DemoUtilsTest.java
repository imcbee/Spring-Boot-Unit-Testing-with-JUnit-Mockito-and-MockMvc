package com.luv2code.junitdemo;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DemoUtilsTest {

    DemoUtils demoUtils;

  @BeforeEach
  void setUpBeforeEach() {
    demoUtils = new DemoUtils();
    System.out.println("@BeforeEach executes before the execution of each test method");
  }

  @BeforeAll
  static void setUpBeforeEachClass() {
    System.out.println("@BeforeAll executes only once before all test methods execution in the class");
  }

  @AfterAll
  static void tearDownAfterAll() {
    System.out.println("@AfterAll executes only once after all test methods execution in the class");
  }


  @AfterEach
  void tearDownAfterEach() {
    System.out.println("Running @AfterEach");
    System.out.println();
  }

  @Test
  void testEqualsAndNotEquals() {
    System.out.println("Running test: testEqualsAndNotEquals");

    assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
    assertNotEquals(6, demoUtils.add(1, 9), "1+9 must be 6");
  }

  @Test
  void testNullAndNotNull() {
    System.out.println("Running test: testNullAndNotNull");
    DemoUtils demoUtils = new DemoUtils();

    String str1 = null;
    String str2 = "luv2code";

    assertNull(demoUtils.checkNull(str1), "Object should be null");
    assertNotNull(demoUtils.checkNull(str2), "Object should not be null");
  }
}

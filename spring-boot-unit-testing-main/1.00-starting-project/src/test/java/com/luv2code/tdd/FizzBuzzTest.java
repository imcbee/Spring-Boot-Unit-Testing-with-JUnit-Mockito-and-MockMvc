package com.luv2code.tdd;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

  // FizzBuzz rules

  @Test
  @DisplayName("Divisible by Three")
  @Order(1)
  void testForDivisibleByThree() {
    String expected = "Fizz";

    assertEquals(expected, FizzBuzz.compute(3), "Should return Fizz");

  }

  @Test
  @DisplayName("Divisible by Five")
  @Order(2)
  void testForDivisibleByFive() {
    String expected = "Buzz";

    assertEquals(expected, FizzBuzz.compute(5), "Should return Buzz");

  }

  @Test
  @DisplayName("Divisible by Three and Five")
  @Order(3)
  void testForDivisibleByThreeAndFive() {
    String expected = "FizzBuzz";

    assertEquals(expected, FizzBuzz.compute(15), "Should return FizzBuzz");

  }

  @Test
  @DisplayName("Not Divisible by Three or Five")
  @Order(4)
  void testForNotDivisibleByThreeOrFive() {
    String expected = "1";

    assertEquals(expected, FizzBuzz.compute(1), "Should return 1");

  }
}

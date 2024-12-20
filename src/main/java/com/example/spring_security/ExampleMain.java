package com.example.spring_security;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExampleMain {
  public static void main(String args[]) {
    System.out.println("Ingresa un texto para realizar el reverse: ");
    Scanner scanner = new Scanner(System.in);
    String word = scanner.nextLine();
    StringBuilder reverse = new StringBuilder();
    StringBuilder reverse2 = new StringBuilder(word);
    String reverse3 = "";

    System.out.println(word.length());

    for(int i = word.length() -1 ; i >= 0; i--) {
      reverse.append(word.charAt(i));
    }

    reverse3 = Stream.iterate(word.length() -1, i -> i >= 0, n -> n -1)
      .map(word::charAt)
      .map(String::valueOf)
      .collect(Collectors.joining());

    System.out.println("Reverse manual: " + reverse);
    System.out.println("Reverse StringBuilder: " + reverse2.reverse().toString());
    System.out.println("Reverse with Stream: " + reverse3);
  }
}
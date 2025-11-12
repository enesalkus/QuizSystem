package pl.merito.quizsystem;

import pl.merito.quizsystem.interfaces.IAnswer;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App
{
    public static void main(String[] args) {
        List<IAnswer> answers = new ArrayList<IAnswer>();

        answers.add(new Answer("Tarantino", false));
        answers.add(new Answer("Nolan", true));
        answers.add(new Answer("Cameron", false));
        answers.add(new Answer("Caglar", false));

        Question question = new Question("Who is the director of the film Interstellar?", answers);

        System.out.println(question);
        System.out.print("Enter your answer : ");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        System.out.println("Your answer is " + (question.checkAnswer(input) ? "correct!" : "incorrect!"));

        sc.close();
    }
}

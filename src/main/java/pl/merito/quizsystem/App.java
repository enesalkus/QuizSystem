package pl.merito.quizsystem;

import pl.merito.quizsystem.interfaces.IAnswer;
import pl.merito.quizsystem.interfaces.IQuestion;
import pl.merito.quizsystem.loader.QuizLoader;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.model.Quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App
{
    public static void main(String[] args) {

        try {
            Quiz quiz = QuizLoader.loadFromJson("questions.json");
            quiz.startQuiz();
        } catch (Exception e) {
            System.out.println("JSON cannot be loaded: " + e.getMessage());
        }
    }
}

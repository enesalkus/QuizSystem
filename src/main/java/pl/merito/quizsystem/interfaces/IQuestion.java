package pl.merito.quizsystem.interfaces;

import java.util.List;

public interface IQuestion {
    String getQuestion();
    List<IAnswer> getAnswers();
    boolean checkAnswer(String userAnswer);
}
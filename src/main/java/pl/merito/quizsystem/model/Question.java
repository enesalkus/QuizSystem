package pl.merito.quizsystem.model;

import pl.merito.quizsystem.interfaces.IQuestion;
import pl.merito.quizsystem.interfaces.IAnswer;

import java.util.List;

public class Question implements IQuestion {
    private String question;
    private List<IAnswer> answers;

    public Question(String question, List<IAnswer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<IAnswer> getAnswers() {
        return answers;
    }

    public boolean checkAnswer(String userAnswer) {
        for (IAnswer answer : answers) {
            if (answer.getText().equalsIgnoreCase(userAnswer) && answer.isCorrect())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(question + "\n");
        for (IAnswer answer : answers) {
            sb.append("- ").append(answer.getText()).append("\n");
        }
        return sb.toString();
    }
}
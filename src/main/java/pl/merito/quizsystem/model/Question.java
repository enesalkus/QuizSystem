package pl.merito.quizsystem.model;

import java.util.List;

public class Question {
    private String text;
    private List<Answer> answers;

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
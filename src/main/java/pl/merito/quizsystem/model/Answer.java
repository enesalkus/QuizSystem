package pl.merito.quizsystem.model;


import pl.merito.quizsystem.interfaces.IAnswer;

public class Answer implements IAnswer {
    private String text;
    private boolean isCorrect;

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    @Override
    public String toString() {
        return text;
    }
}
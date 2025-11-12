package pl.merito.quizsystem;

import java.util.List;

public class Question {
    private String question;
    private List<Answer> answers;

    public Question(String question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean checkAnswer(String userAnswer) {
        for (Answer answer : answers) {
            if (answer.getText().equalsIgnoreCase(userAnswer) && answer.isCorrect())
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(question + "\n");
        for (Answer answer : answers) {
            sb.append("- ").append(answer.getText()).append("\n");
        }
        return sb.toString();
    }
}
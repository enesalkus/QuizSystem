package pl.merito.quizsystem.model;


import pl.merito.quizsystem.interfaces.IQuestion;
import pl.merito.quizsystem.interfaces.IQuiz;
import java.util.ArrayList;
import java.util.List;

public class Quiz<T extends IQuestion> implements IQuiz {
    private List<T> questions;

    public Quiz() {
        this.questions = new ArrayList<>();
    }

    @Override
    public void addQuestion(IQuestion question) {
        questions.add((T) question);
    }

    public List<T> getQuestions() {
        return questions;
    }

    @Override
    public void startQuiz() {
        for (T q : questions) {
            System.out.println(q.getQuestion());
        }
    }

    @Override
    public int getScore() {
        return 0;
    }
}
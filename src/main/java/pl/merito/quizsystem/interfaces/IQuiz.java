package pl.merito.quizsystem.interfaces;

public interface IQuiz {
    void addQuestion(IQuestion question);
    void startQuiz();
    int getScore();
}
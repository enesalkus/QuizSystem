package pl.merito.quizsystem.interfaces;

import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;

import java.util.List;
import java.util.Map;

public interface IQuizService {
    List<Question> loadQuestions();

    int calculateScore(List<Question> questions, Map<Question, List<Answer>> userAnswers);
}

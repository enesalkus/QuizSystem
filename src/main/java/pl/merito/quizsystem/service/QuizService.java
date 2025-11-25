package pl.merito.quizsystem.service;

import pl.merito.quizsystem.interfaces.IDataLoader;
import pl.merito.quizsystem.interfaces.IQuizService;
import pl.merito.quizsystem.loader.JsonDataLoader;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.model.QuizData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuizService implements IQuizService {

    private final IDataLoader<QuizData> dataLoader;
    private static final String QUESTIONS_FILE = "/questions.json";

    public QuizService() {
        this.dataLoader = new JsonDataLoader<>();
    }

    @Override
    public List<Question> loadQuestions() {
        QuizData data = dataLoader.load(QUESTIONS_FILE, QuizData.class);
        return data != null ? data.getQuestions() : new ArrayList<>();
    }

    @Override
    public int calculateScore(List<Question> questions, Map<Question, List<Answer>> userAnswers) {
        int score = 0;
        for (Question q : questions) {
            List<Answer> userSelection = userAnswers.getOrDefault(q, new ArrayList<>());
            List<Answer> correctAnswers = q.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .collect(Collectors.toList());

            // Strict scoring: User must select ALL correct answers and NO incorrect answers
            if (userSelection.size() == correctAnswers.size() && userSelection.containsAll(correctAnswers)) {
                score++;
            }
        }
        return score;
    }
}

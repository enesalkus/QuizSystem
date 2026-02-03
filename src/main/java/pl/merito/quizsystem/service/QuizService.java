package pl.merito.quizsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.merito.quizsystem.interfaces.IDataLoader;
import pl.merito.quizsystem.interfaces.IQuizService;
import pl.merito.quizsystem.loader.JsonDataLoader;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.model.QuizData;
import pl.merito.quizsystem.repository.QuestionRepository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizService implements IQuizService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuizService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @PostConstruct
    public void init() {
        if (questionRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        IDataLoader<QuizData> loader = new JsonDataLoader<>();
        QuizData data = loader.load("/questions.json", QuizData.class);
        if (data != null && data.getQuestions() != null) {
            for (Question q : data.getQuestions()) {
                if (q.getAnswers() != null) {
                    for (Answer a : q.getAnswers()) {
                        a.setQuestion(q);
                    }
                }
                questionRepository.save(q);
            }
        }
    }

    @Override
    public List<Question> loadQuestions() {
        return questionRepository.findAll();
    }

    public java.util.concurrent.CompletableFuture<Void> importQuestionsAsync() {
        return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            ExternalQuestionProvider provider = new ExternalQuestionProvider();
            return provider.fetchExternalQuestions();
        }).thenAccept(questions -> {
            if (questions != null) {
                for (Question q : questions) {
                    saveQuestion(q);
                }
            }
        });
    }

    public void saveQuestion(Question question) {
        if (question.getAnswers() != null) {
            for (Answer a : question.getAnswers()) {
                a.setQuestion(question);
            }
        }
        questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public Question getQuestion(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public int calculateScore(List<Question> questions, Map<Question, List<Answer>> userAnswers) {
        int score = 0;
        for (Question q : questions) {
            List<Answer> userSelection = userAnswers.getOrDefault(q, new ArrayList<>());
            List<Answer> correctAnswers = q.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .collect(Collectors.toList());

            if (userSelection.size() == correctAnswers.size() && userSelection.containsAll(correctAnswers)) {
                score++;
            }
        }
        return score;
    }
}

package pl.merito.quizsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.repository.QuestionRepository;
import pl.merito.quizsystem.service.QuizService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class QuizController {

    private final QuizService quizService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuizController(QuizService quizService, QuestionRepository questionRepository) {
        this.quizService = quizService;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/quiz")
    public String startQuiz(Model model) {
        List<Question> questions = quizService.loadQuestions();
        model.addAttribute("questions", questions);
        return "quiz";
    }

    @PostMapping("/submit")
    public String submitQuiz(@RequestParam org.springframework.util.MultiValueMap<String, String> allParams,
            Model model) {
        List<Question> questions = quizService.loadQuestions();
        Map<Question, List<Answer>> userAnswers = new HashMap<>();

        for (Question q : questions) {
            String paramName = "question_" + q.getId();
            if (allParams.containsKey(paramName)) {
                List<String> answerIdStrs = allParams.get(paramName);
                List<Answer> currentSelection = new ArrayList<>();

                for (String answerIdStr : answerIdStrs) {
                    try {
                        Long answerId = Long.parseLong(answerIdStr);
                        q.getAnswers().stream()
                                .filter(a -> a.getId().equals(answerId))
                                .findFirst()
                                .ifPresent(currentSelection::add);
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }
                userAnswers.put(q, currentSelection);
            }
        }

        int score = quizService.calculateScore(questions, userAnswers);
        int total = questions.size();

        model.addAttribute("score", score);
        model.addAttribute("total", total);
        return "result";
    }

    @PostMapping("/import")
    public String importQuestions(Model model) {
        quizService.importQuestionsAsync();
        // Redirect immediately, import happens in background
        return "redirect:/?message=Import+started+in+background";
    }
}

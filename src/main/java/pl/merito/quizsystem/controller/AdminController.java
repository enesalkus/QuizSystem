package pl.merito.quizsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.service.QuizService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final QuizService quizService;

    @Autowired
    public AdminController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public String listQuestions(Model model) {
        model.addAttribute("questions", quizService.loadQuestions());
        return "admin";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Question q = new Question();
        // Initialize 4 empty answers
        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            answers.add(new Answer());
        }
        q.setAnswers(answers);
        model.addAttribute("question", q);
        return "question_form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Question q = quizService.getQuestion(id);
        if (q == null)
            return "redirect:/admin";

        // Ensure at least 4 answers structure for the form if it has less
        if (q.getAnswers() == null)
            q.setAnswers(new ArrayList<>());
        while (q.getAnswers().size() < 4) {
            q.getAnswers().add(new Answer());
        }

        model.addAttribute("question", q);
        return "question_form";
    }

    @PostMapping("/save")
    public String saveQuestion(@ModelAttribute Question question) {
        if (question.getAnswers() != null) {
            question.getAnswers().removeIf(a -> a.getText() == null || a.getText().trim().isEmpty());
        }
        quizService.saveQuestion(question);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        quizService.deleteQuestion(id);
        return "redirect:/admin";
    }

    @PostMapping("/import")
    public String importQuestions() {
        quizService.importQuestionsAsync();
        return "redirect:/admin?message=Import+started+in+background";
    }
}

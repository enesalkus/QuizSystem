package pl.merito.quizsystem.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.repository.QuestionRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuizService quizService;

    private Question sampleQuestion;
    private List<Answer> sampleAnswers;

    @BeforeEach
    void setUp() {
        sampleQuestion = new Question();
        sampleQuestion.setId(1L);
        sampleQuestion.setText("test question");

        sampleAnswers = new ArrayList<>();
        Answer test1 = new Answer();
        test1.setText("test1");
        test1.setCorrect(true);
        Answer test2 = new Answer();
        test2.setText("test2");
        test2.setCorrect(false);
        sampleAnswers.add(test1);
        sampleAnswers.add(test2);

        sampleQuestion.setAnswers(sampleAnswers);
    }

    @Test
    void testSaveQuestion() {
        quizService.saveQuestion(sampleQuestion);
        verify(questionRepository, times(1)).save(sampleQuestion);
    }

    @Test
    void testLoadQuestions() {
        when(questionRepository.findAll()).thenReturn(Collections.singletonList(sampleQuestion));

        List<Question> result = quizService.loadQuestions();

        assertEquals(1, result.size());
        verify(questionRepository, times(1)).findAll();
    }

    @Test
    void testDeleteQuestion() {
        Long id = 1L;
        quizService.deleteQuestion(id);
        verify(questionRepository, times(1)).deleteById(id);
    }

}

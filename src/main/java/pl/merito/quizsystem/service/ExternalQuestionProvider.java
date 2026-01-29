package pl.merito.quizsystem.service;

import org.springframework.stereotype.Service;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ExternalQuestionProvider {

    public List<Question> fetchExternalQuestions() {
        try {
            // Simulate slow network call
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }

        List<Question> newQuestions = new ArrayList<>();

        Question q1 = new Question();
        q1.setText("Which method is used to start a thread execution?");
        Answer a1 = new Answer();
        a1.setText("init()");
        a1.setCorrect(false);
        a1.setQuestion(q1);
        Answer a2 = new Answer();
        a2.setText("start()");
        a2.setCorrect(true);
        a2.setQuestion(q1);
        Answer a3 = new Answer();
        a3.setText("run()");
        a3.setCorrect(false);
        a3.setQuestion(q1);
        q1.setAnswers(Arrays.asList(a1, a2, a3));

        Question q2 = new Question();
        q2.setText("What does synchronized keyword do?");
        Answer b1 = new Answer();
        b1.setText("Allows multiple threads to access a block");
        b1.setCorrect(false);
        b1.setQuestion(q2);
        Answer b2 = new Answer();
        b2.setText("Locks the object for a single thread");
        b2.setCorrect(true);
        b2.setQuestion(q2);
        q2.setAnswers(Arrays.asList(b1, b2));

        newQuestions.add(q1);
        newQuestions.add(q2);

        return newQuestions;
    }
}

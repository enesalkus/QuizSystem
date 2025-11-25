package pl.merito.quizsystem.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.model.Quiz;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

public class QuizLoader {

    public static Quiz loadFromJson(String filePath) throws Exception {
        Gson gson = new Gson();

        ClassLoader loader = QuizLoader.class.getClassLoader();
        File file = new File(loader.getResource(filePath).getFile());

        Map<String, Object> data = gson.fromJson(
                new FileReader(file),
                new TypeToken<Map<String, Object>>(){}.getType()
        );

        Quiz quiz = new Quiz();

        List<Map<String, Object>> questions = (List<Map<String, Object>>) data.get("questions");

        for (Map<String, Object> q : questions) {

            String text = (String) q.get("text");

            Question question = new Question(text, new java.util.ArrayList<>());

            List<Map<String, Object>> answers = (List<Map<String, Object>>) q.get("answers");

            for (Map<String, Object> a : answers) {
                String answerText = (String) a.get("text");
                boolean correct = (Boolean) a.get("correct");

                question.addAnswer(new Answer(answerText, correct));
            }

            quiz.addQuestion(question);
        }

        return quiz;
    }
}

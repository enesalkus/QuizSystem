package pl.merito.quizsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import pl.merito.quizsystem.interfaces.IQuizService;
import pl.merito.quizsystem.model.Answer;
import pl.merito.quizsystem.model.Question;
import pl.merito.quizsystem.service.QuizService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {

    private final IQuizService quizService;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private Stage primaryStage;
    private BorderPane quizRoot;
    private Label questionLabel;
    private VBox answersBox;
    private Button nextButton;
    private Button backButton;
    private ProgressBar progressBar;
    private Label progressLabel;

    // Track user answers: Question -> List of selected Answers
    private Map<Question, List<Answer>> userAnswers = new HashMap<>();

    public App() {
        this.quizService = new QuizService();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Quiz System");
        showWelcomeScreen();
        stage.show();
    }

    private void showWelcomeScreen() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Center content
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Quiz System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Label messageLabel = new Label("Welcome to the Quiz! Test your knowledge.");
        messageLabel.setFont(Font.font("Arial", 16));

        Button startButton = new Button("Start Quiz");
        startButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        startButton.setOnAction(e -> startQuiz());

        centerBox.getChildren().addAll(titleLabel, messageLabel, startButton);
        root.setCenter(centerBox);

        // Footer
        Label footerLabel = new Label("Created by Ayet Enes Alkus");
        footerLabel.setFont(Font.font("Arial", 10));
        BorderPane.setAlignment(footerLabel, Pos.CENTER);
        root.setBottom(footerLabel);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
    }

    private void startQuiz() {
        questions = quizService.loadQuestions();
        currentQuestionIndex = 0;
        userAnswers.clear(); // Reset answers

        quizRoot = new BorderPane();
        // Remove padding from root to allow MenuBar to span full width
        // We will add padding to the center content instead

        // Top: MenuBar
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> showWelcomeScreen()); // Abort quiz and return to main menu
        menu.getItems().add(exitItem);
        menuBar.getMenus().add(menu);
        quizRoot.setTop(menuBar);

        // Center: Question + Answers + Buttons
        VBox centerContent = new VBox(20); // Spacing between elements
        centerContent.setPadding(new Insets(20)); // Padding for the content
        centerContent.setAlignment(Pos.TOP_LEFT);

        questionLabel = new Label();
        questionLabel.setWrapText(true);
        questionLabel.setFont(Font.font("Arial", 18)); // Increased font size

        answersBox = new VBox(10);

        // Buttons
        HBox buttonBox = new HBox(20); // Spacing between buttons
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        backButton = new Button("Back");
        backButton.setOnAction(e -> showPreviousQuestion());

        nextButton = new Button("Next");
        nextButton.setOnAction(e -> showNextQuestion());

        buttonBox.getChildren().addAll(backButton, nextButton);

        centerContent.getChildren().addAll(questionLabel, answersBox, buttonBox);
        quizRoot.setCenter(centerContent);

        // Bottom: Progress Bar with Label
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(560); // Slightly less than scene width

        progressLabel = new Label("0%");
        progressLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        StackPane progressPane = new StackPane();
        progressPane.getChildren().addAll(progressBar, progressLabel);

        VBox bottomBox = new VBox(progressPane);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(0, 0, 10, 0)); // 10px from bottom
        quizRoot.setBottom(bottomBox);

        if (!questions.isEmpty()) {
            showQuestion(currentQuestionIndex);
        } else {
            questionLabel.setText("No questions found.");
            nextButton.setDisable(true);
            backButton.setDisable(true);
        }

        Scene scene = new Scene(quizRoot, 600, 400);
        primaryStage.setScene(scene);
    }

    private void showQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            Question q = questions.get(index);
            questionLabel.setText((index + 1) + ". " + q.getText());

            answersBox.getChildren().clear();

            long correctCount = q.getAnswers().stream().filter(Answer::isCorrect).count();
            List<Answer> selectedAnswers = userAnswers.getOrDefault(q, new ArrayList<>());

            if (correctCount > 1) {
                // Multiple choice - use CheckBox
                for (Answer answer : q.getAnswers()) {
                    javafx.scene.control.CheckBox cb = new javafx.scene.control.CheckBox(answer.getText());
                    cb.setFont(Font.font("Arial", 14));

                    // Restore selection
                    if (selectedAnswers.contains(answer)) {
                        cb.setSelected(true);
                    }

                    // Listener to update userAnswers
                    cb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        List<Answer> currentSelection = userAnswers.getOrDefault(q, new ArrayList<>());
                        if (isSelected) {
                            if (!currentSelection.contains(answer)) {
                                currentSelection.add(answer);
                            }
                        } else {
                            currentSelection.remove(answer);
                        }
                        userAnswers.put(q, currentSelection);
                    });

                    answersBox.getChildren().add(cb);
                }
            } else {
                // Single choice - use RadioButton
                ToggleGroup group = new ToggleGroup();
                for (Answer answer : q.getAnswers()) {
                    RadioButton rb = new RadioButton(answer.getText());
                    rb.setFont(Font.font("Arial", 14));
                    rb.setToggleGroup(group);

                    // Restore selection
                    if (selectedAnswers.contains(answer)) {
                        rb.setSelected(true);
                    }

                    // Listener to update userAnswers
                    rb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        if (isSelected) {
                            List<Answer> currentSelection = new ArrayList<>();
                            currentSelection.add(answer);
                            userAnswers.put(q, currentSelection);
                        }
                    });

                    answersBox.getChildren().add(rb);
                }
            }

            // Update progress bar: current index / total questions
            double progress = (double) index / questions.size();
            progressBar.setProgress(progress);
            progressLabel.setText(String.format("%.0f%%", progress * 100));

            // Update buttons
            backButton.setDisable(index == 0);

            if (index == questions.size() - 1) {
                nextButton.setText("Finish");
            } else {
                nextButton.setText("Next");
            }
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        } else {
            // End of quiz
            finishQuiz();
        }
    }

    private void showPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            showQuestion(currentQuestionIndex);
        }
    }

    private void finishQuiz() {
        int score = quizService.calculateScore(questions, userAnswers);
        int total = questions.size();

        questionLabel.setText("Quiz Finished!");
        answersBox.getChildren().clear();

        Label scoreLabel = new Label("Your Score: " + score + " / " + total);
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label detailsLabel = new Label("Correct: " + score + "\nIncorrect: " + (total - score));
        detailsLabel.setFont(Font.font("Arial", 16));

        answersBox.getChildren().addAll(scoreLabel, detailsLabel);

        nextButton.setDisable(true);
        backButton.setDisable(true);
        progressBar.setProgress(1.0);
        progressLabel.setText("100%");

        // Add Return to Main Menu button
        Button returnButton = new Button("Return to Main Menu");
        returnButton.setStyle("-fx-font-size: 14px; -fx-padding: 10px 20px;");
        returnButton.setOnAction(e -> showWelcomeScreen());
        answersBox.getChildren().add(returnButton);
    }

    public static void main(String[] args) {
        launch();
    }
}

package pl.merito.quizsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.merito.quizsystem.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}

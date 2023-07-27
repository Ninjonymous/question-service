package me.alokdev.questionservice.dao;


import me.alokdev.questionservice.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {

    List<Question> findAllByDifficultyLevel(String difficultyLevel);

    List<Question> findAllByCategory(String category);

    @Query(value = "select q.id from question q where q.category=:category order by rand() limit :numQ",nativeQuery = true)
    List<Integer> findRandomQuestionByCategory(String category, int numQ);
}

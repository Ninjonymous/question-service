package me.alokdev.questionservice.service;

import me.alokdev.questionservice.dao.QuestionDao;
import me.alokdev.questionservice.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions(){
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
    }

    public List<Question> getQuestionByDifficultyLevel(String difficultyLevel) {
        return questionDao.findAllByDifficultyLevel(difficultyLevel);
    }

    public ResponseEntity<List<Question>> getQuestionByCategory(String category) {
        return new ResponseEntity<>(questionDao.findAllByCategory(category),HttpStatus.OK);
    }

    public ResponseEntity<String> addQuestion(Question question) {

        questionDao.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }


    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numOfQuestion) {
        return new ResponseEntity<>(questionDao.findRandomQuestionByCategory(categoryName,numOfQuestion),HttpStatus.OK);
    }
}

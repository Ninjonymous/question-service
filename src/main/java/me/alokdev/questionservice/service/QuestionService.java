package me.alokdev.questionservice.service;

import me.alokdev.questionservice.dao.QuestionDao;
import me.alokdev.questionservice.model.Question;
import me.alokdev.questionservice.model.QuestionWrapper;
import me.alokdev.questionservice.model.Response;
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

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionsId) {
        List<QuestionWrapper> wrapper = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for (Integer id : questionsId){
            questions.add(questionDao.findById(id).get());
        }

        for(Question q : questions){
            QuestionWrapper wrap = new QuestionWrapper();
            wrap.setId(q.getId());
            wrap.setQuestionTitle(q.getQuestionTitle());
            wrap.setOption1(q.getOption1());
            wrap.setOption2(q.getOption2());
            wrap.setOption3(q.getOption3());
            wrap.setOption4(q.getOption4());
            wrapper.add(wrap);
        }

        return new ResponseEntity<>(wrapper, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right = 0;
        int i = 0;
        for (Response response : responses){
            Question question = questionDao.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer())){
                right++;
            }
            i++;
        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }
}

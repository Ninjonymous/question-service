package me.alokdev.questionservice.controller;


import me.alokdev.questionservice.dao.QuestionDao;
import me.alokdev.questionservice.model.Question;
import me.alokdev.questionservice.model.QuestionWrapper;
import me.alokdev.questionservice.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import me.alokdev.questionservice.model.Response;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionDao questionDao;

    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("level/{difficultyLevel}")
    public List<Question> getQuestionByDifficultyLevel(@PathVariable String difficultyLevel) {
        return questionService.getQuestionByDifficultyLevel(difficultyLevel);
    }

    @GetMapping("{category}")
    public ResponseEntity<List<Question>> getQuestionByCategory(@PathVariable String category) {
        return questionService.getQuestionByCategory(category);
    }

    @PostMapping("add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){

        return questionService.addQuestion(question);
    }

    //generate
    @GetMapping("generate")
    public ResponseEntity<List <Integer>> generateQuestionsForQuiz(@RequestParam String categoryName,@RequestParam Integer numOfQuestion){
        return questionService.getQuestionForQuiz(categoryName,numOfQuestion);
    }

    //get question
    @GetMapping("getQuestion")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionsId){
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

    //get score
    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses){
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

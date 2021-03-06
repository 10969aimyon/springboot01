package com.Hello.service;


import com.Hello.dto.PageDTO;
import com.Hello.dto.QuestionDto;
import com.Hello.dto.QuestionQueryDTO;
import com.Hello.enums.CommentTypeEnum;
import com.Hello.exception.CustomizeErrorCode;
import com.Hello.exception.CustomizeException;
import com.Hello.mapper.CommentMapper;
import com.Hello.mapper.QuestionExtMapper;
import com.Hello.mapper.QuestionMapper;
import com.Hello.mapper.UserMapper;
import com.Hello.model.CommentExample;
import com.Hello.model.Question;
import com.Hello.model.QuestionExample;
import com.Hello.model.User;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    // 可以同时使用QuestionMapper和UserMapper来组装

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;


    public PageDTO<QuestionDto> list(String search, int page, int size) {

        if (StringUtils.isNotBlank(search)){
            String[] searches = StringUtils.split(search," ");
            search = Arrays.stream(searches).collect(Collectors.joining("|"));
        }


        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        int totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        PageDTO<QuestionDto> pageDTO = new PageDTO<>();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = page < 1 ? 0:size * (page - 1);

        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setData(questionDtoList);
        return pageDTO;
    }

    public PageDTO<QuestionDto> list(Integer id, Integer page, Integer size){
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(id);
        int totalCount = (int) questionMapper.countByExample(questionExample);

        PageDTO<QuestionDto> pageDTO = new PageDTO<>();
        pageDTO.setPage(page, size, totalCount);

        if (page < 1){
            page = 1;
        }
        if (page > pageDTO.getTotalPage()) {
            page = pageDTO.getTotalPage();
        }

        int offset = page<1 ? 0 : size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDto> questionDtoList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question, questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        pageDTO.setData(questionDtoList);

        return pageDTO;

    }

    public QuestionDto getByQuestionID(Integer questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = new User();
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question, questionDto);
        user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);

        // 查找评论数
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(questionId)
                .andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        int commentCount = (int)commentMapper.countByExample(commentExample);
        questionDto.setCommentCount(commentCount);

        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() != null){
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());

            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (i != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }else {
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }
    }

    public void incView(Integer questionId) {
        Question question = new Question();
        question.setId(questionId);
        questionExtMapper.incView(question);
    }

    public List<Question> selectRelated(QuestionDto questionDto) {
        if (questionDto.getTag().equals("") || questionDto.getTag() == null){
            return new ArrayList<>();
        }
        String tags = questionDto.getTag().replace(",","|");

        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTag(tags);

        List<Question> questions = questionExtMapper.selectRelated(question);
        if (questions.size()==0){
            return new ArrayList<>();
        }
        return questions;
    }

    public List<Question> selectMostPop() {
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("view_count desc");
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(questionExample, new RowBounds(0, 5));
        return questions;
    }

    public int getPublishById(HttpServletRequest request){

        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(user.getId());
        int count = (int) questionMapper.countByExample(questionExample);
        return count;
    }
}

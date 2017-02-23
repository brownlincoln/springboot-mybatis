package com.chris.service;

import com.chris.dao.CommentDAO;
import com.chris.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by YaoQi on 2017/2/22.
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId, int entityType, int offset, int limit) {
        return commentDAO.selectCommentByEntity(entityId, entityType, offset, limit);
    }

    public Comment getCommentById(int commentId) {
        return commentDAO.getCommentById(commentId);
    }
    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        //Can comment.getId() work?
        return commentDAO.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
    // status = 1 means delete this comment
    public boolean deleteComment(int id, int status) {
        return commentDAO.updateStatus(id, 1) > 0;
    }
}

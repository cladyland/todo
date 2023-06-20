package kovalenko.vika.service;

import kovalenko.vika.command.CommentCommand;

public interface CommentService {
    void createComment(CommentCommand commentCommand);
}

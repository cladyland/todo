package kovalenko.vika.service;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dto.CommentDTO;

public interface CommentService {
    CommentDTO createComment(CommentCommand commentCommand);
}

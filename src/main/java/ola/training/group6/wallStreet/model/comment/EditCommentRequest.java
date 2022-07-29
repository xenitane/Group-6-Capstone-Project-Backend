package ola.training.group6.wallStreet.model.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCommentRequest {
    private String currentUserId;
    private String commentContent;
}
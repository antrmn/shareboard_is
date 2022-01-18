package service.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


public class CommentTreeDTO {
    private Map<Integer, List<CommentDTO>> comments = new HashMap<>();

    public List<CommentDTO> getRootComments(){
        return comments.get(0);
    }

    public List<CommentDTO> getReplies(int id){
        return comments.get(id);
    }

    public void add(CommentDTO comment){
        comments.computeIfAbsent(comment.getParentCommentId(), ArrayList::new).add(comment);
    }

    public static CommentTreeDTO create(Stream<CommentDTO> comments){
        CommentTreeDTO tree = new CommentTreeDTO();
        tree.comments = comments.collect(groupingBy(CommentDTO::getParentCommentId, toList()));
        return tree;
    }
}

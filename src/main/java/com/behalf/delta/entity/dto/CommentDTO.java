package com.behalf.delta.entity.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String username;
    private String text;
    private LocalDateTime creationDate;
    private List<CommentDTO> replies; // Avoid full recursion
}

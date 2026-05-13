package com.examme.examme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherExamResultRowDto {
    private String studentName;
    private String email;
    private int correct;
    private int wrong;
    private int skipped;
    private double score;
    private LocalDateTime finishedAt;
}

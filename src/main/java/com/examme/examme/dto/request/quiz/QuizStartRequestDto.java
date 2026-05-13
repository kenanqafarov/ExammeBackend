package com.examme.examme.dto.request.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizStartRequestDto {
    private Long examPackageId;
    private int fromQuestion;
    private int toQuestion;
    private int selectedCount;
}

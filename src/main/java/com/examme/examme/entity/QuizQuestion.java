package com.examme.examme.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz_questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_package_id", nullable = false)
    private ExamPackage examPackage;

    @Column(name = "question_number", nullable = false)
    private int questionId;

    @Column(name = "question_text", nullable = false, length = 4000)
    private String questionText;

    @Column(name = "option_a", nullable = false, length = 2000)
    private String optionA;

    @Column(name = "option_b", nullable = false, length = 2000)
    private String optionB;

    @Column(name = "option_c", nullable = false, length = 2000)
    private String optionC;

    @Column(name = "option_d", nullable = false, length = 2000)
    private String optionD;

    @Column(name = "correct_answer", nullable = false, length = 1)
    private String correctAnswer;
}

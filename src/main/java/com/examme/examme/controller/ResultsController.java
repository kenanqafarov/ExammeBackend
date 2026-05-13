package com.examme.examme.controller;

import com.examme.examme.dto.LeaderboardEntryDto;
import com.examme.examme.dto.MyResultHistoryDto;
import com.examme.examme.dto.TeacherExamResultRowDto;
import com.examme.examme.service.ResultsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Results")
@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ResultsController {

    private final ResultsService resultsService;

    @Operation(summary = "Teacher: full results for an exam in a group")
    @GetMapping("/group/{groupId}/exam/{examPackageId}")
    public ResponseEntity<List<TeacherExamResultRowDto>> teacherResults(
            @PathVariable Long groupId,
            @PathVariable Long examPackageId
    ) {
        return ResponseEntity.ok(resultsService.teacherGroupExamResults(groupId, examPackageId));
    }

    @Operation(summary = "Student/Teacher: leaderboard for an exam")
    @GetMapping("/group/{groupId}/exam/{examPackageId}/leaderboard")
    public ResponseEntity<List<LeaderboardEntryDto>> leaderboard(
            @PathVariable Long groupId,
            @PathVariable Long examPackageId
    ) {
        return ResponseEntity.ok(resultsService.studentLeaderboard(groupId, examPackageId));
    }

    @Operation(summary = "Student: my result history")
    @GetMapping("/my")
    public ResponseEntity<List<MyResultHistoryDto>> myResults() {
        return ResponseEntity.ok(resultsService.myHistory());
    }
}

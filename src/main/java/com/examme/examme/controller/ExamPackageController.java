package com.examme.examme.controller;

import com.examme.examme.dto.ApiResponse;
import com.examme.examme.dto.ExamPackageDetailDto;
import com.examme.examme.dto.ExamPackageSummaryDto;
import com.examme.examme.entity.enums.Difficulty;
import com.examme.examme.service.ExamPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Exam packages")
@RestController
@RequestMapping("/api/exam-packages")
@CrossOrigin(origins = "*", maxAge = 3600)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ExamPackageController {

    private final ExamPackageService examPackageService;

    @Operation(summary = "Create exam package from file (Gemini)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ExamPackageDetailDto> create(
            @RequestParam Long groupId,
            @RequestParam MultipartFile file,
            @RequestParam int questionCount,
            @RequestParam Difficulty difficulty,
            @RequestParam(required = false) String description
    ) throws IOException {
        return ResponseEntity.ok(examPackageService.create(groupId, file, questionCount, difficulty, description));
    }

    @Operation(summary = "List exam packages in a group")
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExamPackageSummaryDto>> listByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(examPackageService.listForGroup(groupId));
    }

    @Operation(summary = "Get exam package with questions")
    @GetMapping("/{id}")
    public ResponseEntity<ExamPackageDetailDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(examPackageService.getDetail(id));
    }

    @Operation(summary = "Delete my exam package")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        examPackageService.delete(id);
        return ResponseEntity.ok(new ApiResponse("Exam package deleted", true));
    }
}

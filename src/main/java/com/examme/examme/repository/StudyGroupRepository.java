package com.examme.examme.repository;

import com.examme.examme.entity.StudyGroup;
import com.examme.examme.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    List<StudyGroup> findByTeacherOrderByCreatedAtDesc(User teacher);

    @Query("select distinct g from StudyGroup g left join fetch g.students where g.id = :id")
    Optional<StudyGroup> findByIdWithStudents(@Param("id") Long id);
}

package com.examme.examme.service;

import com.examme.examme.dto.common.NotificationDto;
import com.examme.examme.entity.User;
import com.examme.examme.exception.NotFoundException;
import com.examme.examme.repository.NotificationRepository;
import com.examme.examme.repository.UserRepository;
import com.examme.examme.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<NotificationDto> listMine() {
        User user = userRepository.findByEmail(SecurityUtils.requireCurrentUserEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        return notificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(n -> NotificationDto.builder()
                        .id(n.getId())
                        .message(n.getMessage())
                        .read(n.isReadFlag())
                        .invitationId(n.getInvitationId())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }
}

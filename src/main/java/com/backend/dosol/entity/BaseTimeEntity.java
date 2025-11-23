package com.backend.dosol.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners; // 1. 이거 확인
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate; // 2. ★중요★ 패키지가 jakarta가 아님!
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // 3. ★중요★ 리스너 임포트

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 4. ★★★ 이 줄이 없으면 작동 안 함!!! ★★★
public abstract class BaseTimeEntity {

	@CreatedDate // 5. 생성 시간 자동 감지
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate // 6. 수정 시간 자동 감지
	private LocalDateTime modifiedAt;
}
package com.backend.dosol.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Exchanges")
public class Exchange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exchange_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id", nullable = false)
	private User receiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "playlist_id", nullable = false)
	private Playlist playlist;

	@Column(name = "exchanged_at")
	private LocalDateTime exchangedAt;

	@Builder
	public Exchange(User sender, User receiver, Playlist playlist) {
		this.sender = sender;
		this.receiver = receiver;
		this.playlist = playlist;
		this.exchangedAt = LocalDateTime.now(); // 생성 시점 저장
	}
}
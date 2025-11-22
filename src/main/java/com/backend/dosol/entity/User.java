package com.backend.dosol.entity;


import com.backend.dosol.entity.type.Genre;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 50)
	private String nickname;

	@Column(name = "auth_code", length = 100)
	private String authCode;

	@Column(length = 20)
	private String generation;

	@Enumerated(EnumType.STRING) // Enum 이름을 DB에 문자열로 저장
	@Column(name = "favorite_genre")
	private Genre favoriteGenre;

	@Builder
	public User(String nickname, String authCode, String generation, Genre favoriteGenre) {
		this.nickname = nickname;
		this.authCode = authCode;
		this.generation = generation;
		this.favoriteGenre = favoriteGenre;
	}

	public void updateAuthCode(String authCode) {
		this.authCode = authCode;
	}
}
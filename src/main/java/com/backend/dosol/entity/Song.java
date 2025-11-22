package com.backend.dosol.entity;


import com.backend.dosol.entity.type.Genre;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Songs")
public class Song { // 노래는 생성일자가 굳이 필요 없다면 BaseTimeEntity 상속 제외 가능

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "song_id")
	private Long id;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false, length = 100)
	private String artist;

	@Column(name = "youtube_video_id", length = 50)
	private String youtubeVideoId;

	@Column(name = "youtube_url", length = 500)
	private String youtubeUrl;

	@Builder
	public Song(String title, String artist, String youtubeVideoId, String youtubeUrl) {
		this.title = title;
		this.artist = artist;
		this.youtubeVideoId = youtubeVideoId;
		this.youtubeUrl = youtubeUrl;
	}

	public void updateYoutubeInfo(String youtubeVideoId, String youtubeUrl) {
		this.youtubeVideoId = youtubeVideoId;
		this.youtubeUrl = youtubeUrl;
	}
}
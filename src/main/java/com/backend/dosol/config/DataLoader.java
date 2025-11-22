package com.backend.dosol.config;

import com.backend.dosol.entity.Song;
import com.backend.dosol.entity.type.Genre;
import com.backend.dosol.repository.SongRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final SongRepository songRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (songRepository.count() > 0) {
			log.info("이미 데이터가 존재하여 더미 데이터를 생성하지 않습니다.");
			return;
		}
		log.info("더미 노래 데이터 생성 시작...");

		String jsonContent = """
    {
      "gen1": {
        "emo": [
          "S.E.S – Just A Feeling", "젝스키스 – 커플", "핑클 – 영원한 사랑", "god – 촛불하나", "신화 – 너의 결혼식",
          "S.E.S – Dreams Come True(원곡)", "H.O.T – 행복", "Turbo – 회상", "핑클 – Now(발라드 ver.)", "유피 – 바다",
          "김현정 – 그녀와의 이별", "박지윤 – 성인식(슬로우 ver.)", "god – 사랑해 그리고 기억해", "H.O.T – 아이야", "신화 – First Love",
          "김범수 – 약속", "신승훈 – I Believe", "박효신 – 좋은 사람", "임창정 – 결혼해줘", "조성모 – 아시나요",
          "박정현 – 꿈에", "박정현 – 편지할게요", "브라운아이즈 – 벌써 일년", "성시경 – 내게 오는 길", "휘성 – 안되나요",
          "김범수 – 슬픔 활용법", "SG워너비 – 내 사람", "에메랄드 캐슬 – 발걸음", "라이너스 – 연(緣)", "박효신 – 해줄 수 없는 일",
          "왁스 – 부탁해요", "이승환 – 덩크슛", "윤도현 – 사랑했나봐", "조성모 – 불멸의 사랑", "박정현 – 미아",
          "이정현 – 너(슬로우 ver.)", "포지션 – I Love You", "박효신 – 그곳에 서서", "김동률 – 감사", "린 – 사랑했잖아"
        ],
        "dance": [
          "쿨 – 해변의 여인", "H.O.T – Candy", "핑클 – Now", "S.E.S – I'm Your Girl", "터보 – Twist King",
          "엄정화 – Poison", "샵 – Sweety", "베이비복스 – Killer", "H.O.T – We Are The Future", "NRG – Hit Song",
          "코요태 – 순정", "코요태 – 패션", "박진영 – 허니", "DJ DOC – Run To You", "박지윤 – 성인식",
          "이정현 – 와", "이정현 – 끈", "터보 – Black Cat", "룰라 – 3!4!", "김현정 – 멍",
          "쥬얼리 – One More Time (초기)", "신화 – Wild Eyes", "샵 – 내 입술 따뜻한 커피처럼", "박미경 – 이브의 경고", "엄정화 – 몰라",
          "유피 – 뿌요뿌요", "핑클 – 영원", "쥬비 – 눈부신 날", "R.ef – 이별공식", "울랄라세션 – 나쁜 여자야",
          "코요태 – 비몽", "듀스 – 런 투 유(초기 ver.)", "철이와 미애 – 너는 왜", "젝스키스 – 아프지 마요", "NRG – 티파니에서 아침을",
          "티티마 – 로미오", "박지윤 – 난 사랑에 빠졌죠", "핑클 – To My Prince", "박진영 – 날 떠나지마", "채연 – 둘이서"
        ],
        "hip": [
          "DJ DOC – DOC와 춤을", "1TYM – One Love", "지누션 – How Deep Is Your Love", "Drunken Tiger – 난 너에게", "업타운 – 다시 만나줘",
          "DJ DOC – 머피의 법칙", "1TYM – Hot", "지누션 – 말해줘", "DEUX – 여우야", "DEUX – 난 알아요",
          "DEUX – Turn Around And Away", "CB Mass – 진짜", "버벌진트 – 다음번엔", "DJ DOC – 스트리트 라이프", "1TYM – Without You",
          "업타운 – 올라", "리쌍 – 내가 웃는게 아니야(초기)", "김진표 – 로맨틱 겨울", "Soul Company – Breakdown", "T – 시간을 달려",
          "CB MASS – Beautiful Life", "DJ DOC – Dance With DOC", "드렁큰타이거 – Good Life", "1TYM – 더 원", "지누션 – A-Yo!",
          "부가킹즈 – 성냥팔이 소녀", "가리온 – 무투", "타이거JK – Reset", "디기리 – Street Life", "업타운 – 나를 돌아봐",
          "거북이 – 왜이래", "이정현 – 반(랩 ver.)", "테크노보이즈 – Break The Wall", "듀스 – 나를 돌아봐(Remix)", "Double K – Clap",
          "가리온 – 영순위", "DJ DOC – 모두가 웃었다", "드렁큰타이거 – 소외된 사람들", "버벌진트 – 좋겠다", "프리스타일 – Y"
        ],
        "vocal": [
          "박정현 – 꿈에", "김범수 – 보고싶다", "조성모 – 다짐", "임재범 – 너를 위해", "박효신 – 좋은 사람",
          "김경호 – 금지된 사랑", "박정현 – 편지할게요", "신승훈 – 미소 속에 비친 그대", "성시경 – 내게 오는 길", "김조한 – 사랑에 빠지고 싶다",
          "휘성 – With Me", "김범수 – 나타나", "임창정 – 소주 한잔", "박효신 – 야생화(초기 ver.)", "부활 – Never Ending Story",
          "이승철 – 그런 사람 또 없습니다", "박정현 – 미아", "김동률 – 다시 사랑한다 말할까", "성시경 – 거리에서", "윤하 – 비밀번호 486",
          "브라운아이즈 – 점점", "박효신 – 추억은 사랑을 닮아", "김범수 – 보고싶다", "KCM – 흑백사진", "아이비 – 유혹의 소나타(발라드 ver.)",
          "이기찬 – 감기", "휘성 – 가슴 시린 이야기", "임창정 – 결혼해줘", "린 – 섬머타임", "바이브 – 미워도 다시 한번",
          "박정현 – PS I Love You", "김범수 – 하루", "박효신 – 빛", "이수영 – 휠릴리", "김경호 – 비정",
          "이승기 – 삭제", "김종국 – 사랑스러워(발라드 ver.)", "최재훈 – 비의 랩소디", "포맨 – Baby Baby", "성시경 – 난 좋을 텐데"
        ]
      }
    }
    """;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonContent);

		Iterator<Map.Entry<String, JsonNode>> generations = rootNode.fields();
		while (generations.hasNext()) {
			Map.Entry<String, JsonNode> generationEntry = generations.next();
			String generation = generationEntry.getKey();
			JsonNode genresNode = generationEntry.getValue();

			Iterator<Map.Entry<String, JsonNode>> genres = genresNode.fields();
			while (genres.hasNext()) {
				Map.Entry<String, JsonNode> genreEntry = genres.next();
				Genre genre = mapGenre(genreEntry.getKey());
				JsonNode songsNode = genreEntry.getValue();

				if (songsNode.isArray()) {
					for (JsonNode songNode : songsNode) {
						String songString = songNode.asText();
						String[] parts = songString.split("–", 2);
						if (parts.length == 2) {
							String artist = parts[0].trim();
							String title = parts[1].trim();

							Song song = Song.builder()
								.artist(artist)
								.title(title)
								.generation(generation)
								.genre(genre)
								.build();
							songRepository.save(song);
						}
					}
				}
			}
		}

		log.info("더미 노래 데이터 생성 완료! 총 {}개의 노래가 저장되었습니다.", songRepository.count());
	}

	private Genre mapGenre(String genreKey) {
		return switch (genreKey) {
			case "emo" -> Genre.EMOTIONAL;
			case "dance" -> Genre.DANCE;
			case "hip" -> Genre.HIPHOP;
			case "vocal" -> Genre.VOCAL;
			default -> throw new IllegalArgumentException("Unknown genre key: " + genreKey);
		};
	}
}
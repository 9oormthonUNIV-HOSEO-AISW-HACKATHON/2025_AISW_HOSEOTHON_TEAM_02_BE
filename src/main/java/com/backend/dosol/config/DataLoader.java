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
			  },
			  "gen2": {
			    "emo": [
			      "빅뱅 – Haru Haru", "2NE1 – Lonely", "원더걸스 – 2DT", "애프터스쿨 – Shampoo", "카라 – Honey",
			      "소녀시대 – Complete", "티아라 – 너 때문에 미쳐", "2AM – 죽어도 못 보내", "2PM – Again & Again", "포미닛 – 거울아 거울아",
			      "샤이니 – 누난 너무 예뻐", "브라운아이즈걸스 – 아브라카다브라(슬로우 ver.)", "f(x) – Goodbye Summer", "틴탑 – 미치겠어", "하이라이트 – Fiction",
			      "인피니트 – 내꺼하자", "씨엔블루 – 외톨이야", "다비치 – 사랑과 전쟁", "2NE1 – I Love You", "레인보우 – To Me",
			      "원더걸스 – Be My Baby", "브라운아이드소울 – Love Ballad", "슈퍼주니어 – It’s You", "샤이니 – Hello", "포미닛 – 이름이 뭐예요",
			      "씨스타 – Alone", "인피니트 – Be Mine", "카라 – Pandora", "티아라 – Cry Cry", "걸스데이 – 반짝반짝",
			      "애프터스쿨 – Because of You", "포맨 – Baby Baby", "브아걸 – 오아시스", "씨엔블루 – Love", "2NE1 – 아파",
			      "f(x) – Rum Pum Pum Pum", "케이윌 – Love Blossom", "허각 – Hello", "아이유 – 좋은 날", "아이유 – 너랑 나"
			    ],
			    "dance": [
			      "소녀시대 – Gee", "카라 – Mister", "티아라 – Roly Poly", "2NE1 – Fire", "슈퍼주니어 – Sorry Sorry",
			      "샤이니 – Ring Ding Dong", "포미닛 – Hot Issue", "f(x) – Hot Summer", "브라운아이드걸스 – Sixth Sense", "애프터스쿨 – Diva",
			      "원더걸스 – So Hot", "포미닛 – Volume Up", "티아라 – Lovey Dovey", "씨스타 – Push Push", "2PM – Heartbeat",
			      "4minute – Crazy", "인피니트 – The Chaser", "카라 – Step", "샤이니 – Lucifer", "초신성 – Super Star",
			      "U-KISS – Neverland", "애프터스쿨 – Bang!", "씨스타 – So Cool", "틴탑 – 향수뿌리지마", "티아라 – Sexy Love",
			      "걸스데이 – 기대해", "나인뮤지스 – 드라마", "f(x) – Electric Shock", "2NE1 – I Am The Best", "에이핑크 – NoNoNo",
			      "보이프렌드 – Janus", "비스트 – Shock", "인피니트 – Paradise", "비스트 – Beautiful Night", "카라 – Jumping",
			      "시크릿 – Madonna", "레인보우 – A", "오렌지캬라멜 – Magic Girl", "샤이니 – Sherlock", "슈퍼주니어 – Mr. Simple"
			    ],
			    "hip": [
			      "빅뱅 – Fantastic Baby", "GD&TOP – High High", "블락비 – Very Good", "2NE1 – I Am The Best", "EPIK HIGH – Fly",
			      "다이나믹 듀오 – 죽일 놈", "리쌍 – 헤어지지 못하는 여자", "MC몽 – 서커스", "버벌진트 – 좋아보여", "도끼 – It's Dok2",
			      "MC Sniper – Better Than Yesterday", "Double K – Clap", "Bobby – 가드 올리고 Bounce", "지드래곤 – 삐딱하게", "도끼 – On My Way",
			      "마이티마우스 – Energy", "산이 – 아는 사람 얘기", "블락비 – 난리나", "이센스 – The Anecdote(초기 스타일 기준)", "에픽하이 – 우산",
			      "BIGBANG – Tonight", "비스트 – Bad Girl(Remix ver.)", "MBLAQ – Oh Yeah", "제국의 아이들 – Mazeltov(랩 중심)", "GD – Heartbreaker",
			      "태양 – Where U At", "샤이니 – Juliette(Remix Hip ver.)", "Block B – Nalina", "GD&TOP – Oh Yeah", "2PM – Without U(힙합 편곡 ver.)",
			      "비스트 – 비가오는 날엔(Remix hiphop ver.)", "다이나믹듀오 – 출첵", "무브먼트 – Movementology Vol.1", "도끼 – Still On My Way", "리쌍 – 발레리노",
			      "에픽하이 – One", "윤하 – 비밀번호 486(Remix)", "버벌진트 – Go Easy", "정기고 – Blind", "Double K – Favorite"
			    ],
			    "vocal": [
			      "2AM – 이 노래", "다비치 – 파리파리", "브라운아이드걸스 – 밤새", "케이윌 – Love Blossom", "휘성 – 인생은 아름다워",
			      "거미 – 기억해줘요", "에이트 – 심장이 없어", "포맨 – Baby Baby", "티아라 – Time To Love", "FTIsland – 사랑앓이",
			      "하이라이트 – 비가 오는 날엔", "인피니트 – Julia", "2NE1 – 아파", "씨엔블루 – Love Light", "SISTAR – 너였어야 해",
			      "브아걸 – Oasis", "아이유 – 좋은 날", "아이유 – 나만 몰랐던 이야기", "성시경 – 너의 모든 순간(초기 버전)", "케이윌 – Marry Me",
			      "다비치 – 안녕이라고 말하지마", "포맨 – 울고 싶다", "Huh Gak – Hello", "나인뮤지스 – Dolls(보컬 ver.)", "씨스타 – 나 혼자(슬로우 ver.)",
			      "샤이니 – Replay(보컬 ver.)", "브아걸 – LOVE", "비스트 – Fiction(피아노 ver.)", "애프터스쿨 – 너 때문에", "인피니트 – Still I Miss You",
			      "2AM – 친구의 고백", "거미 – 통증", "아이유 – 부", "박효신 – 널 사랑한다", "케이윌 – 이러지마 제발",
			      "브라운아이드소울 – My Story", "바이브 – 가을타나봐", "정승환 – 보고싶었어요", "성시경 – 제주도의 푸른 밤", "윤하 – 오늘 헤어졌어요"
			    ]
			  },
			  "gen3": {
			    "emo": [
			      "BTS – 봄날", "EXO – Sing For You", "세븐틴 – 예쁘다", "트와이스 – What Is Love (슬로우 감성 ver.)", "악동뮤지션 – 200%",
			      "볼빨간사춘기 – 우주를 줄게", "여자친구 – 시간을 달려서", "세븐틴 – Don't Wanna Cry", "NCT – Universe", "레드벨벳 – Automatic",
			      "아이유 – 밤편지", "아이유 – 금요일에 만나요", "수란 – 오늘 취하면", "위너 – 공허해", "블랙핑크 – Stay",
			      "첸 – 최고의 행운", "정승환 – 이 바보야", "멜로망스 – 선물", "폴킴 – 모든 날 모든 순간", "아이콘 – 사랑을 했다(감성 ver.)",
			      "하이라이트 – 얼굴 찌푸리지 말아요", "세븐틴 – 웃음 꽃", "정은지 – 하늘바라기", "NCT DREAM – Candle Light", "레드벨벳 – 하늘을 달린다",
			      "GOT7 – Miracles In December(커버 ver.)", "BTS – Butterfly", "BTS – Epiphany", "첸 – 안녕", "DAY6 – You Were Beautiful",
			      "DAY6 – Congratulations", "B1A4 – 산들, 거짓말이야", "여자친구 – 밤", "EXO – Miracles in December", "위너 – Sentimental",
			      "세븐틴 – Hug", "NCT – Angel", "정세운 – It's You", "JBJ – Call Your Name"
			    ],
			    "dance": [
			      "EXO – Love Shot", "트와이스 – Fancy", "블랙핑크 – Kill This Love", "세븐틴 – 아주 NICE", "BTS – Dynamite",
			      "ITZY – Dalla Dalla", "레드벨벳 – Dumb Dumb", "NCT 127 – Cherry Bomb", "GOT7 – Hard Carry", "몬스타엑스 – Shoot Out",
			      "위너 – Really Really", "EXO – Growl", "트와이스 – Cheer Up", "프리스틴 – Wee Woo", "아이콘 – 리듬 타",
			      "BTS – Fire", "EXID – 위아래", "현아 – Bubble Pop", "AOA – 단발머리", "여자친구 – 유리구슬",
			      "빅스 – Chained Up", "빅스 – 도원경", "세븐틴 – HIT", "세븐틴 – Very Nice", "레드벨벳 – Red Flavor",
			      "레드벨벳 – Peek-A-Boo", "트와이스 – TT", "BTS – Boy In Luv", "몬스타엑스 – Hero", "EXO – Tempo",
			      "NCT DREAM – Boom", "NCT U – Boss", "태민 – Move", "선미 – Gashina", "오마이걸 – 비밀정원(댄스 ver.)",
			      "CLC – No", "카더가든 – 명동콜링(EDM ver.)", "우주소녀 – 부기업", "EXO – Call Me Baby"
			    ],
			    "hip": [
			      "BTS – Mic Drop", "지코 – Artist", "송민호 – Fiancé", "지코 – 아무노래", "몬스타엑스 – DramaRamA",
			      "GOT7 – If You Do", "NCT – Boss", "방탄소년단 – 뱁새", "RM – Forever Rain", "비와이 – Day Day",
			      "창모 – Meteor", "세븐틴 – HIT(Edgy ver.)", "iKON – Rhythm Ta", "Stray Kids(초기) – Hellevator", "Stray Kids – District 9",
			      "블락비 – Toy", "GD – 무제", "GD&TOP – Zutter(무대 ver.)", "BTS – Dope", "NCT 127 – Simon Says",
			      "NCT 127 – Cherry Bomb", "한해 – 한해한테", "쇼미 – 거북선", "비와이 – GOTT", "저스디스 – One of Them",
			      "식케이 – Ring Ring", "Ash Island – Melody", "기리보이 – 교통정리", "크러쉬 – Rush Hour(초기 부클럽 ver.)", "몬스타엑스 – Trespass",
			      "ATEEZ 초창기 – Pirate King", "GOT7 – Look(힙합 믹스)", "SEVENTEEN – Check-in", "방탄 – Cypher Pt.3", "블랙핑크 – Kick It",
			      "NCT – Fire Truck", "지코 – Bermuda Triangle", "BTS – ON(Edgy ver.)", "Stray Kids – Side Effects"
			    ],
			    "vocal": [
			      "아이유 – 무릎", "정승환 – 너였다면", "백예린 – Bye Bye My Blue", "볼빨간사춘기 – 나만 안되는 연애", "태연 – 사계",
			      "태연 – Fine", "정준일 – 안아줘", "아이콘 – 사랑을 했다", "악동뮤지션 – 오랜 날 오랜 밤", "NCT – From Home",
			      "세븐틴 – 포옹", "첸 – 안녕", "BTS – The Truth Untold", "정승환 – 눈사람", "폴킴 – 비",
			      "멜로망스 – 인사", "크러쉬 – 가끔", "폴킴 – 모든 날 모든 순간", "헤이즈 – 비도 오고 그래서", "세븐틴 – Don't Listen In Secret",
			      "NCT DREAM – 사랑한 적 있었다면", "EXO – Miracles in December", "첸 – 아름다워", "태연 – Rain", "아이유 – 푸르던",
			      "수란 – 연애시대", "정승환 – 사랑에 빠지고 싶다", "찬열&펀치 – Stay With Me", "정은지 – 너란 봄", "폴킴 – 너를 만나",
			      "Ben – 180도", "하성운 – Bird", "NELL – 시간이 흐른 뒤", "임한별 – 이별하러 가는 길", "멜로망스 – 초대",
			      "첸 – 사랑의 말", "BTS – Serendipity", "BTS – Epiphany", "태연 – Blue", "아이유 – 이름에게"
			    ]
			  },
			  "gen4": {
			    "emo": [
			      "NewJeans – Ditto", "NewJeans – OMG", "STAYC – Teddy Bear", "IVE – Either Way", "LE SSERAFIM – Eve, Psyche & The Bluebeard’s Wife(감성 ver.)",
			      "NewJeans – Hurt", "IVE – Mine", "NMIXX – Young, Dumb, Stupid(soft ver.)", "TEMPEST – Taste The Feeling", "ZEROBASEONE – Always",
			      "BOYNEXTDOOR – But I Like You", "LE SSERAFIM – Perfect Night", "IVE – Eleven (lofi ver.)", "뉴진스 – ASMR ver. Super Shy", "트리플에스 – Rising",
			      "Kep1er – MVSK(슬로우 ver.)", "TNX – Love or Die", "Xdinary Heroes – Strawberry Cake", "BABYMONSTER – Stuck In The Middle", "TXT – Happy Fools",
			      "RIIZE – Memories", "ENHYPEN – Polaroid Love", "IVE – Off The Record(감성 ver.)", "NewJeans – Hype Boy(Soft ver.)", "STAYC – I Like U",
			      "NMIXX – Roller Coaster", "LE SSERAFIM – Impurities", "IVE – Kitsch(감성 ver.)", "TNX – Love Again", "BOYNEXTDOOR – Serenade(soft ver.)",
			      "TREASURE – Hold It In", "TXT – Opening Sequence", "Aespa – Welcome To My World", "RIIZE – Talk Saxy(느린 ver.)", "ZEROBASEONE – In Bloom(soft ver.)",
			      "ATBO – Next To Me", "NCT DREAM – Dreaming", "Stray Kids – My Pace(soft ver.)", "BOYNEXTDOOR – One And Only(감성 ver.)", "LE SSERAFIM – Blue Flame"
			    ],
			    "dance": [
			      "NewJeans – Super Shy", "IVE – After Like", "LE SSERAFIM – Antifragile", "Aespa – Next Level", "Stray Kids – God's Menu",
			      "Aespa – Spicy", "NewJeans – Hype Boy", "IVE – I AM", "Kep1er – Wadada", "TXT – Sugar Rush Ride",
			      "RIIZE – Get A Guitar", "RIIZE – Love 119", "ENHYPEN – Drunk-Dazed", "ZEROBASEONE – In Bloom", "BOYNEXTDOOR – One & Only",
			      "LE SSERAFIM – Fearless", "AESPA – Savage", "NMIXX – O.O", "ITZY – Wannabe", "ITZY – LOCO",
			      "STAYC – ASAP", "ENHYPEN – Given-Taken", "TREASURE – Jikjin", "ATEEZ – Halazia", "Stray Kids – S-Class",
			      "TXT – Blue Hour(dance ver.)", "IVE – Baddie", "NewJeans – Cookie", "Aespa – Black Mamba", "LE SSERAFIM – No Celestial",
			      "XG – Mascara", "NMIXX – Dice", "NCT DREAM – Hot Sauce", "RIIZE – Siren", "Kep1er – Giddy",
			      "ENHYPEN – Bite Me", "Billlie – Ring X Ring", "CSR – Pop? Pop!", "Xdinary Heroes – Happy Death Day", "woo!ah! – Danger"
			    ],
			    "hip": [
			      "Stray Kids – Back Door", "Stray Kids – Topline", "ATEEZ – Guerrilla", "TXT – LO$ER LOVER", "ENHYPEN – Blessed-Cursed",
			      "TREASURE – JIKJIN", "BSS – Fighting", "Xdinary Heroes – Test Me", "TNX – Move", "NCT DREAM – Glitch Mode",
			      "NCT 127 – 2 Baddies", "SEVENTEEN – Super", "ATEEZ – Say My Name(Remaster)", "ZEROBASEONE – New Kidz On The Block", "THE BOYZ – Maverick",
			      "CIX – Jungle", "P1Harmony – Scared", "JUST B – Damage", "Stray Kids – Thunderous", "ATEEZ – Deja Vu",
			      "NMIXX – TANK", "Xdinary Heroes – Hair Cut", "RIIZE – Siren(힙합 ver.)", "BOYNEXTDOOR – But Sometimes", "ENHYPEN – Paradoxxx Invasion",
			      "NCT DREAM – Countdown", "TXT – Good Boy Gone Bad", "TREASURE – DARARI(Remix Hip ver.)", "XG – Tippy Toes"
			    ],
			    "vocal": [
			      "Stray Kids – Back Door", "Stray Kids – Topline", "ATEEZ – Guerrilla", "TXT – LO$ER LOVER", "ENHYPEN – Blessed-Cursed",
			      "TREASURE – JIKJIN", "BSS – Fighting", "Xdinary Heroes – Test Me", "TNX – Move", "NCT DREAM – Glitch Mode",
			      "NCT 127 – 2 Baddies", "SEVENTEEN – Super", "ATEEZ – Say My Name(Remaster)", "ZEROBASEONE – New Kidz On The Block", "THE BOYZ – Maverick",
			      "CIX – Jungle", "P1Harmony – Scared", "JUST B – Damage", "Stray Kids – Thunderous", "ATEEZ – Deja Vu",
			      "NMIXX – TANK", "Xdinary Heroes – Hair Cut", "RIIZE – Siren(힙합 ver.)", "BOYNEXTDOOR – But Sometimes", "ENHYPEN – Paradoxxx Invasion",
			      "NCT DREAM – Countdown", "TXT – Good Boy Gone Bad", "TREASURE – DARARI(Remix Hip ver.)", "XG – Tippy Toes"
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
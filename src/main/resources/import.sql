
/* big_category : 대분류 */

INSERT INTO big_category (name) VALUES ('숙박'), ('놀이'), ('문화'), ('카페'), ('애견 카페'), ('식당');

/* small_category : 소분류 */

SELECT big_category_id INTO @accommodation FROM big_category WHERE name = '숙박';
INSERT INTO small_category (name, big_category_id) VALUES ('애견 펜션', @accommodation), ('캠핑장', @accommodation), ('펜션', @accommodation), ('호텔', @accommodation);

SELECT big_category_id INTO @play FROM big_category WHERE name = '놀이';
INSERT INTO small_category (name, big_category_id) VALUES ('무료 반려견 놀이터', @play), ('유료 반려견 놀이터', @play), ('수영장', @play), ('테마파크', @play);

SELECT big_category_id INTO @culture FROM big_category WHERE name = '문화';
INSERT INTO small_category (name, big_category_id) VALUES ('복합시설', @culture), ('영화관', @culture), ('명소', @culture);

SELECT big_category_id INTO @cafe FROM big_category WHERE name = '카페';
INSERT INTO small_category (name, big_category_id) VALUES ('베이커리', @cafe), ('브런치', @cafe), ('아이스크림', @cafe), ('카페', @cafe);

SELECT big_category_id INTO @dogCafe FROM big_category WHERE name = '애견 카페';
INSERT INTO small_category (name, big_category_id) VALUES ('실내', @dogCafe), ('실외', @dogCafe);

SELECT big_category_id INTO @restaurant FROM big_category WHERE name = '식당';
INSERT INTO small_category (name, big_category_id) VALUES ('남미 음식', @restaurant), ('동남아 음식', @restaurant), ('샐러드/샌드위치', @restaurant), ('양식', @restaurant), ('인도/중동 음식', @restaurant), ('일식/돈가스', @restaurant), ('주점/바/펍', @restaurant), ('중식', @restaurant), ('피자/햄버거/치킨', @restaurant), ('한식', @restaurant);

/* state : 시/도 */

INSERT INTO state (name) VALUES ('서울특별시'), ('부산광역시'), ('대구광역시'), ('인천광역시'), ('광주광역시'), ('대전광역시'), ('울산광역시'), ('세종특별자치시'), ('경기도'), ('강원특별자치도'), ('충청북도'), ('충청남도'), ('전라북도'), ('전라남도'), ('경상북도'), ('경상남도'), ('제주특별자치도');

/* city : 시/군/구 */

SELECT state_id INTO @seoul FROM state WHERE name = '서울특별시';
INSERT INTO city (name, state_id) VALUES ('종로구', @seoul), ('중구', @seoul), ('용산구', @seoul), ('성동구', @seoul), ('광진구', @seoul), ('동대문구', @seoul), ('중랑구', @seoul), ('성북구', @seoul), ('강북구', @seoul), ('도봉구', @seoul), ('노원구', @seoul), ('은평구', @seoul), ('서대문구', @seoul), ('마포구', @seoul), ('양천구', @seoul), ('강서구', @seoul), ('구로구', @seoul), ('금천구', @seoul), ('영등포구', @seoul), ('동작구', @seoul), ('관악구', @seoul), ('서초구', @seoul), ('강남구', @seoul), ('송파구', @seoul), ('강동구', @seoul);

SELECT state_id INTO @busan FROM state WHERE name = '부산광역시';
INSERT INTO city (name, state_id) VALUES ('중구', @busan), ('서구', @busan), ('동구', @busan), ('영도구', @busan), ('부산진구', @busan), ('동래구', @busan), ('남구', @busan), ('북구', @busan), ('강서구', @busan), ('해운대구', @busan), ('사하구', @busan), ('금정구', @busan), ('연제구', @busan), ('수영구', @busan), ('사상구', @busan),('기장군', @busan);

SELECT state_id INTO @deagu FROM state WHERE name = '대구광역시';
INSERT INTO city (name, state_id) VALUES ('중구', @deagu), ('동구', @deagu), ('서구', @deagu), ('남구', @deagu), ('북구', @deagu), ('수성구', @deagu), ('달서구', @deagu), ('달성군', @deagu), ('군위군', @deagu);

SELECT state_id INTO @incheon FROM state WHERE name = '인천광역시';
INSERT INTO city (name, state_id) VALUES ('중구', @incheon), ('동구', @incheon), ('미추홀구', @incheon), ('연수구', @incheon), ('남동구', @incheon), ('부평구', @incheon), ('계양구', @incheon), ('서구', @incheon), ('강화군', @incheon), ('옹진군', @incheon);

SELECT state_id INTO @gwangju FROM state WHERE name = '광주광역시';
INSERT INTO city (name, state_id) VALUES ('동구', @gwangju), ('서구', @gwangju), ('남구', @gwangju), ('북구', @gwangju), ('광산구', @gwangju);

SELECT state_id INTO @deajeon FROM state WHERE name = '대전광역시';
INSERT INTO city (name, state_id) VALUES ('동구', @deajeon), ('중구', @deajeon), ('서구', @deajeon), ('유성구', @deajeon), ('대덕구', @deajeon);

SELECT state_id INTO @ulsan FROM state WHERE name = '울산광역시';
INSERT INTO city (name, state_id) VALUES ('중구', @ulsan), ('남구', @ulsan), ('동구', @ulsan), ('북구', @ulsan), ('울주군', @ulsan);

SELECT state_id INTO @sejong FROM state WHERE name = '세종특별자치시';
INSERT INTO city (name, state_id) VALUES ('세종특별자치시', @sejong);

SELECT state_id INTO @gyoenggi FROM state WHERE name = '경기도';
INSERT INTO city (name, state_id) VALUES ('수원시', @gyoenggi), ('성남시', @gyoenggi), ('의정부시', @gyoenggi), ('안양시', @gyoenggi), ('부천시', @gyoenggi), ('광명시', @gyoenggi), ('동두천시', @gyoenggi), ('평택시', @gyoenggi), ('안산시', @gyoenggi), ('고양시', @gyoenggi), ('과천시', @gyoenggi), ('구리시', @gyoenggi), ('남양주시', @gyoenggi), ('오산시', @gyoenggi), ('시흥시', @gyoenggi), ('군포시', @gyoenggi), ('의왕시', @gyoenggi), ('하남시', @gyoenggi), ('용인시', @gyoenggi), ('파주시', @gyoenggi), ('이천시', @gyoenggi), ('안성시', @gyoenggi), ('김포시', @gyoenggi), ('화성시', @gyoenggi), ('광주시', @gyoenggi), ('양주시', @gyoenggi), ('포천시', @gyoenggi), ('여주시', @gyoenggi), ('연천군', @gyoenggi), ('가평군', @gyoenggi), ('양평군', @gyoenggi);

SELECT state_id INTO @gangwon FROM state WHERE name = '강원특별자치도';
INSERT INTO city (name, state_id) VALUES ('춘천시', @gangwon), ('원주시', @gangwon), ('강릉시', @gangwon), ('동해시', @gangwon), ('태백시', @gangwon), ('속초시', @gangwon), ('삼척시', @gangwon), ('홍천군', @gangwon), ('횡성군', @gangwon), ('영월군', @gangwon), ('평창군', @gangwon), ('정선군', @gangwon), ('철원군', @gangwon), ('화천군', @gangwon), ('양구군', @gangwon), ('인제군', @gangwon), ('고성군', @gangwon), ('양양군', @gangwon);

SELECT state_id INTO @choongnbook FROM state WHERE name = '충청북도';
INSERT INTO city (name, state_id) VALUES ('청주시', @choongnbook), ('충주시', @choongnbook), ('제천시', @choongnbook), ('보은군', @choongnbook), ('옥천군', @choongnbook), ('영동군', @choongnbook), ('증평군', @choongnbook), ('진천군', @choongnbook), ('괴산군', @choongnbook), ('음성군', @choongnbook), ('단양군', @choongnbook);

SELECT state_id INTO @choongnnam FROM state WHERE name = '충청남도';
INSERT INTO city (name, state_id) VALUES ('천안시', @choongnnam), ('공주시', @choongnnam), ('보령시', @choongnnam), ('아산시', @choongnnam), ('서산시', @choongnnam), ('논산시', @choongnnam), ('계룡시', @choongnnam), ('당진시', @choongnnam), ('금산군', @choongnnam), ('부여군', @choongnnam), ('서천군', @choongnnam), ('청양군', @choongnnam), ('홍성군', @choongnnam), ('예산군', @choongnnam), ('태안군', @choongnnam);

SELECT state_id INTO @jeonbook FROM state WHERE name = '전라북도';
INSERT INTO city (name, state_id) VALUES ('전주시', @jeonbook), ('군산시', @jeonbook), ('익산시', @jeonbook), ('정읍시', @jeonbook), ('남원시', @jeonbook), ('김제시', @jeonbook), ('완주군', @jeonbook), ('진안군', @jeonbook), ('무주군', @jeonbook), ('장수군', @jeonbook), ('임실군', @jeonbook), ('순창군', @jeonbook), ('고창군', @jeonbook), ('부안군', @jeonbook);

SELECT state_id INTO @jeonnam FROM state WHERE name = '전라남도';
INSERT INTO city (name, state_id) VALUES ('목포시', @jeonnam), ('여수시', @jeonnam), ('순천시', @jeonnam), ('나주시', @jeonnam), ('광양시', @jeonnam), ('담양군', @jeonnam), ('곡성군', @jeonnam), ('구례군', @jeonnam), ('고흥군', @jeonnam), ('보성군', @jeonnam), ('화순군', @jeonnam), ('장흥군', @jeonnam), ('강진군', @jeonnam), ('해남군', @jeonnam), ('영암군', @jeonnam), ('무안군', @jeonnam), ('함평군', @jeonnam), ('영광군', @jeonnam), ('장성군', @jeonnam), ('완도군', @jeonnam), ('진도군', @jeonnam), ('신안군', @jeonnam);

SELECT state_id INTO @gyungbook FROM state WHERE name = '경상북도';
INSERT INTO city (name, state_id) VALUES ('포항시', @gyungbook), ('경주시', @gyungbook), ('김천시', @gyungbook), ('안동시', @gyungbook), ('구미시', @gyungbook), ('영주시', @gyungbook), ('영천시', @gyungbook), ('상주시', @gyungbook), ('문경시', @gyungbook), ('경산시', @gyungbook), ('의성군', @gyungbook), ('청송군', @gyungbook), ('영양군', @gyungbook), ('영덕군', @gyungbook), ('청도군', @gyungbook), ('고령군', @gyungbook), ('성주군', @gyungbook), ('칠곡군', @gyungbook), ('예천군', @gyungbook), ('봉화군', @gyungbook), ('울진군', @gyungbook), ('울릉군', @gyungbook);

SELECT state_id INTO @gyungnam FROM state WHERE name = '경상남도';
INSERT INTO city (name, state_id) VALUES ('창원시', @gyungnam), ('진주시', @gyungnam), ('통영시', @gyungnam), ('사천시', @gyungnam), ('김해시', @gyungnam), ('밀양시', @gyungnam), ('거제시', @gyungnam), ('양산시', @gyungnam), ('의령군', @gyungnam), ('함안군', @gyungnam), ('창녕군', @gyungnam), ('고성군', @gyungnam), ('남해군', @gyungnam), ('하동군', @gyungnam), ('산청군', @gyungnam), ('함양군', @gyungnam), ('거창군', @gyungnam), ('합천군', @gyungnam);

SELECT state_id INTO @jeju FROM state WHERE name = '제주특별자치도';
INSERT INTO city (name, state_id) VALUES ('제주시', @jeju), ('서귀포시', @jeju);

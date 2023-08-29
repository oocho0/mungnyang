
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
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('종로구', @seoul, 03000, 03299), ('중구', @seoul, 04500, 04699), ('용산구', @seoul, 04300, 04499), ('성동구', @seoul, 04700, 04899), ('광진구', @seoul, 04900, 05199), ('동대문구', @seoul, 02400, 02699), ('중랑구', @seoul, 02000, 02399), ('성북구', @seoul, 02700, 02999), ('강북구', @seoul, 01000, 01299), ('도봉구', @seoul, 01300, 01599), ('노원구', @seoul, 01600, 01999), ('은평구', @seoul, 03300, 03599), ('서대문구', @seoul, 03600, 03899), ('마포구', @seoul, 03900, 04299), ('양천구', @seoul, 07900, 08199), ('강서구', @seoul, 07500, 07899), ('구로구', @seoul, 08200, 08499), ('금천구', @seoul, 08500, 08699), ('영등포구', @seoul, 07200, 07499), ('동작구', @seoul, 06900, 07199), ('관악구', @seoul, 08700, 08999), ('서초구', @seoul, 06500, 06899), ('강남구', @seoul, 06000, 06499), ('송파구', @seoul, 05500, 05999), ('강동구', @seoul, 05200, 05499);

SELECT state_id INTO @busan FROM state WHERE name = '부산광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES('중구', @busan, 48900, 48999), ('서구', @busan, 49200, 49299), ('동구', @busan, 48700, 48899), ('영도구', @busan, 49000, 49199), ('부산진구', @busan, 47100, 47499), ('동래구', @busan, 47700, 47999), ('남구', @busan, 48400, 48699), ('북구', @busan, 46500, 46699), ('강서구', @busan, 46700, 46899), ('해운대구', @busan, 48000, 48199), ('사하구', @busan, 49300, 49599), ('금정구', @busan, 46200, 46499), ('연제구', @busan, 47500, 47699), ('수영구', @busan, 48200, 48399), ('사상구', @busan, 46900, 47099), ('기장군', @busan, 46000, 46199);

SELECT state_id INTO @deagu FROM state WHERE name = '대구광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('중구', @deagu, 41900, 41999), ('동구', @deagu, 41000, 41399), ('서구', @deagu, 41700, 41899), ('남구', @deagu, 42400, 42599), ('북구', @deagu, 41400, 41699), ('수성구', @deagu, 42000, 42399), ('달서구', @deagu, 42600, 42899), ('달성군', @deagu, 42900, 43099), ('군위군', @deagu, 43100, 43199);

SELECT state_id INTO @incheon FROM state WHERE name = '인천광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('중구', @incheon, 22300, 22499), ('동구', @incheon, 22500, 22599), ('미추홀구', @incheon, 22100, 22299), ('연수구', @incheon, 21900, 22099), ('남동구', @incheon, 21500, 21899), ('부평구', @incheon, 21300, 21499), ('계양구', @incheon, 21000, 21299), ('서구', @incheon, 22600, 22999), ('강화군', @incheon, 23000, 23099), ('옹진군', @incheon, 23100, 23199);

SELECT state_id INTO @gwangju FROM state WHERE name = '광주광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('동구', @gwangju, 61400, 61599), ('서구', @gwangju, 61900, 62199), ('남구', @gwangju, 61600, 61899), ('북구', @gwangju, 61000, 61399), ('광산구', @gwangju, 62200, 62599);

SELECT state_id INTO @deajeon FROM state WHERE name = '대전광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('동구', @deajeon, 34500, 34799), ('중구', @deajeon, 34800, 35199), ('서구', @deajeon, 35200, 35499), ('유성구', @deajeon, 34000, 34299), ('대덕구', @deajeon, 34300, 34499);

SELECT state_id INTO @ulsan FROM state WHERE name = '울산광역시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('중구', @ulsan, 44400, 44599), ('남구', @ulsan, 44600, 44899), ('동구', @ulsan, 44000, 44199), ('북구', @ulsan, 44200, 44399), ('울주군', @ulsan, 44900, 45199);

SELECT state_id INTO @sejong FROM state WHERE name = '세종특별자치시';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('세종특별자치시', @sejong, 30000, 30199);

SELECT state_id INTO @gyoenggi FROM state WHERE name = '경기도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('수원시', @gyeonggi, 16200, 16799), ('성남시', @gyeonggi, 13100, 13799), ('의정부시', @gyeonggi, 11600, 11899), ('안양시', @gyeonggi, 13900, 14199), ('부천시', @gyeonggi, 14400, 14899), ('광명시', @gyeonggi, 14200, 14399), ('동두천시', @gyeonggi, 11300, 11399), ('평택시', @gyeonggi, 17700, 18099), ('안산시', @gyeonggi, 15200, 15799), ('고양시', @gyeonggi, 10200, 10799), ('과천시', @gyeonggi, 13800, 13899), ('구리시', @gyeonggi, 11900, 11999), ('남양주시', @gyeonggi, 12000, 12399), ('오산시', @gyeonggi, 18100, 18199), ('시흥시', @gyeonggi, 14900, 15199), ('군포시', @gyeonggi, 15800, 15999), ('의왕시', @gyeonggi, 16000, 16199), ('하남시', @gyeonggi, 12900, 13099), ('용인시', @gyeonggi, 16800, 17299), ('파주시', @gyeonggi, 10800, 10999), ('이천시', @gyeonggi, 17300, 17499), ('안성시', @gyeonggi, 17500, 17699), ('김포시', @gyeonggi, 10000, 10199), ('화성시', @gyeonggi, 18200, 18799), ('광주시', @gyeonggi, 12700, 12899), ('양주시', @gyeonggi, 11400, 11599), ('포천시', @gyeonggi, 11100, 11299), ('여주시', @gyeonggi, 12600, 12699), ('연천군', @gyeonggi, 11000, 11099), ('가평군', @gyeonggi, 12400, 12499), ('양평군', @gyeonggi, 12500, 12599);

SELECT state_id INTO @gangwon FROM state WHERE name = '강원특별자치도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('춘천시', @gangwon, 24200, 24499), ('원주시', @gangwon, 26300, 26599), ('강릉시', @gangwon, 25400, 25699), ('동해시', @gangwon, 25700, 25899), ('태백시', @gangwon, 26000, 26099), ('속초시', @gangwon, 24800, 24999), ('삼척시', @gangwon, 25900, 25999), ('홍천군', @gangwon, 25100, 25199), ('횡성군', @gangwon, 25200, 25299), ('영월군', @gangwon, 26200, 26299), ('평창군', @gangwon, 25300, 25399), ('정선군', @gangwon, 26100, 26199), ('철원군', @gangwon, 24000, 24099), ('화천군', @gangwon, 24100, 24199), ('양구군', @gangwon, 24500, 24599), ('인제군', @gangwon, 24600, 24699), ('고성군', @gangwon, 24700, 24799), ('양양군', @gangwon, 25000, 25099);

SELECT state_id INTO @choongnbook FROM state WHERE name = '충청북도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('청주시', @choongnbook, 28100, 28899), ('충주시', @choongnbook, 27300, 27599), ('제천시', @choongnbook, 27100, 27299), ('보은군', @choongnbook, 28900, 28999), ('옥천군', @choongnbook, 29000, 29099), ('영동군', @choongnbook, 29100, 29199), ('증평군', @choongnbook, 27900, 27999), ('진천군', @choongnbook, 27800, 27899), ('괴산군', @choongnbook, 28000, 28099), ('음성군', @choongnbook, 27600, 27799), ('단양군', @choongnbook, 27000, 27099);

SELECT state_id INTO @choongnnam FROM state WHERE name = '충청남도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('천안시', @choongnnam, 31000, 31399), ('공주시', @choongnnam, 32500, 32699), ('보령시', @choongnnam, 33400, 33599), ('아산시', @choongnnam, 31400, 31699), ('서산시', @choongnnam, 31900, 32099), ('논산시', @choongnnam, 32900, 33099), ('계룡시', @choongnnam, 32800, 32899), ('당진시', @choongnnam, 31700, 31899), ('금산군', @choongnnam, 32700, 32799), ('부여군', @choongnnam, 33100, 33299), ('서천군', @choongnnam, 33600, 33699), ('청양군', @choongnnam, 33300, 33399), ('홍성군', @choongnnam, 32200, 32399), ('예산군', @choongnnam, 32400, 32499), ('태안군', @choongnnam, 32100, 32199);

SELECT state_id INTO @jeonbook FROM state WHERE name = '전라북도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('전주시', @jeonbook, 54800, 55299), ('군산시', @jeonbook, 54000, 54299), ('익산시', @jeonbook, 54500, 54799), ('정읍시', @jeonbook, 56100, 56299), ('남원시', @jeonbook, 55700, 55899), ('김제시', @jeonbook, 54300, 54499), ('완주군', @jeonbook, 55300, 55399), ('진안군', @jeonbook, 55400, 55499), ('무주군', @jeonbook, 55500, 55599), ('장수군', @jeonbook, 55600, 55699), ('임실군', @jeonbook, 55900, 55999), ('순창군', @jeonbook, 56000, 56099), ('고창군', @jeonbook, 56400, 56499), ('부안군', @jeonbook, 56300, 56399);

SELECT state_id INTO @jeonnam FROM state WHERE name = '전라남도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('목포시', @jeonnam, 58600, 58799), ('여수시', @jeonnam, 59600, 59899), ('순천시', @jeonnam, 57900, 58099), ('나주시', @jeonnam, 58200, 58399), ('광양시', @jeonnam, 57700, 57899), ('담양군', @jeonnam, 57300, 57499), ('곡성군', @jeonnam, 57500, 57599), ('구례군', @jeonnam, 57600, 57699), ('고흥군', @jeonnam, 59500, 59599), ('보성군', @jeonnam, 59400, 59499), ('화순군', @jeonnam, 58100, 58199), ('장흥군', @jeonnam, 59300, 59399), ('강진군', @jeonnam, 59200, 59299), ('해남군', @jeonnam, 59000, 59099), ('영암군', @jeonnam, 58400, 58499), ('무안군', @jeonnam, 58500, 58599), ('함평군', @jeonnam, 57100, 57199), ('영광군', @jeonnam, 57000, 57099), ('장성군', @jeonnam, 57200, 57299), ('완도군', @jeonnam, 59100, 59199), ('진도군', @jeonnam, 58900, 58999), ('신안군', @jeonnam, 58800, 58899);

SELECT state_id INTO @gyungbook FROM state WHERE name = '경상북도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('포항시', @gyungbook, 37500, 37999), ('경주시', @gyungbook, 38000, 38299), ('김천시', @gyungbook, 39500, 39799), ('안동시', @gyungbook, 36600, 36799), ('구미시', @gyungbook, 39100, 39499), ('영주시', @gyungbook, 36000, 36199), ('영천시', @gyungbook, 38800, 38999), ('상주시', @gyungbook, 37100, 37299), ('문경시', @gyungbook, 36900, 37099), ('경산시', @gyungbook, 38400, 38799), ('의성군', @gyungbook, 37300, 37399), ('청송군', @gyungbook, 37400, 37499), ('영양군', @gyungbook, 36500, 36599), ('영덕군', @gyungbook, 36400, 36499), ('청도군', @gyungbook, 38300, 38399), ('고령군', @gyungbook, 40100, 40199), ('성주군', @gyungbook, 40000, 40099), ('칠곡군', @gyungbook, 39800, 39999), ('예천군', @gyungbook, 36800, 36899), ('봉화군', @gyungbook, 36200, 36299), ('울진군', @gyungbook, 36300, 36399), ('울릉군', @gyungbook, 40200, 40299), ('군위군', @gyungbook, 39000, 39099);

SELECT state_id INTO @gyungnam FROM state WHERE name = '경상남도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('창원시', @gyungnam, 51100, 51999), ('진주시', @gyungnam, 52600, 52899), ('통영시', @gyungnam, 53000, 53199), ('사천시', @gyungnam, 52500, 52599), ('김해시', @gyungnam, 50800, 51099), ('밀양시', @gyungnam, 50400, 50499), ('거제시', @gyungnam, 53200, 53399), ('양산시', @gyungnam, 50500, 50799), ('의령군', @gyungnam, 52100, 52199), ('함안군', @gyungnam, 52000, 52099), ('창녕군', @gyungnam, 50300, 50399), ('고성군', @gyungnam, 52900, 52999), ('남해군', @gyungnam, 52400, 52499), ('하동군', @gyungnam, 52300, 52399), ('산청군', @gyungnam, 52200, 52299), ('함양군', @gyungnam, 50000, 50099), ('거창군', @gyungnam, 50100, 50199), ('합천군', @gyungnam, 50200, 50299);

SELECT state_id INTO @jeju FROM state WHERE name = '제주특별자치도';
INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('제주시', @jeju, 63000, 63499), ('서귀포시', @jeju, 63500, 63699);
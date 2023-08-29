package com.mungnyang;

import org.junit.jupiter.api.Test;

public class MakeSQL {
    String[] cities = {"@seoul", "@gyunggi", "@incheon", "@gangwon", "@choongnbook", "@sejong", "@choongnnam",
            "@deajeon", "@gyungbook", "@deagu", "@ulsan", "@busan", "@gyungnam", "@jeonbook", "@gwangju", "@jeonnam", "@jeju"};

    String[][][][] list = {
            {
                    {{"01"}, {"강북구", "도봉구", "노원구"}},
                    {{"02"}, {"중랑구", "동대문구", "성북구"}},
                    {{"03"}, {"종로구", "은평구", "서대문구", "마포구"}},
                    {{"04"}, {"마포구", "용산구", "중구", "성동구", "광진구"}},
                    {{"05"}, {"광진구", "강동구", "송파구"}},
                    {{"06"}, {"강남구", "서초구", "동작구"}},
                    {{"07"}, {"동작구", "영등포구", "강서구", "양천구"}},
                    {{"08"}, {"양천구", "구로구", "금천구", "관악구"}}
            },
            {
                    {{"10"}, {"김포시", "고양시", "파주시"}},
                    {{"11"}, {"연천군", "포천시", "동두천시", "양주시", "의정부시", "구리시"}},
                    {{"12"}, {"남양주시", "가평군", "양평군", "여주시", "광주시", "하남시"}},
                    {{"13"}, {"하남시", "성남시", "과천시", "안양시"}},
                    {{"14"}, {"안양시", "광명시", "부천시", "시흥시"}},
                    {{"15"}, {"시흥시", "안산시", "군포시"}},
                    {{"16"}, {"의왕시", "수원시", "용인시"}},
                    {{"17"}, {"용인시", "이천시", "안성시", "평택시"}},
                    {{"18"}, {"평택시", "오산시", "화성시"}}
            },
            {
                    {{"21"}, {"계양구", "부평구", "남동구", "연수구"}},
                    {{"22"}, {"연수구", "미추홀구", "중구", "동구", "서구"}},
                    {{"23"}, {"강화군", "옹진군"}}
            },
            {
                    {{"24"}, {"철원군", "화천군", "춘천시", "양구군", "인제군", "고성군", "속초시"}},
                    {{"25"}, {"양양군", "홍천군", "횡성군", "평창군", "강릉시", "동해시", "삼척시"}},
                    {{"26"}, {"태백시", "정선군", "영월군", "원주시"}}
            },
            {
                    {{"27"}, {"단양군", "제천시", "충주시", "음성군", "진천군", "증평군"}},
                    {{"28"}, {"괴산군", "청주시", "보은군"}},
                    {{"29"}, {"옥천군", "영동군"}}
            },
            {
                    {{"30"}, {"세종특별자치시"}}
            },
            {
                    {{"31"}, {"천안시", "아산시", "당진시", "서산시"}},
                    {{"32"}, {"서산시", "태안군", "홍성군", "예산군", "공주시", "금산군", "계룡시", "논산시"}},
                    {{"33"}, {"논산시", "부여군", "청양군", "보령시", "서천군"}}
            },
            {
                    {{"34"}, {"유성구", "대덕구", "동구", "중구"}},
                    {{"35"}, {"중구", "서구"}}
            },
            {
                    {{"36"}, {"영주시", "봉화군", "울진군", "영덕군", "영양군", "안동시", "예천군", "문경시"}},
                    {{"37"}, {"문경시", "상주시", "의성군", "청송군", "포항시"}},
                    {{"38"}, {"경주시", "청도군", "경산시", "영천시"}},
                    {{"39"}, {"군위군", "구미시", "김천시", "칠곡군"}},
                    {{"40"}, {"성주군", "고령군", "울릉군"}}
            },
            {
                    {{"41"}, {"동구", "북구", "서구", "중구"}},
                    {{"42"}, {"수성구", "남구", "달서구", "달성군"}},
                    {{"43"}, {"달성군", "군위군"}}
            },
            {
                    {{"44"}, {"동구", "북구", "중구", "남구", "울주군"}},
                    {{"45"}, {"울주군"}}
            },
            {
                    {{"46"}, {"기장군", "금정구", "북구", "강서구", "사상구"}},
                    {{"47"}, {"사상구", "부산진구", "연제구", "동래구"}},
                    {{"48"}, {"해운대구", "수영구", "남구", "동구", "중구"}},
                    {{"49"}, {"영도구", "서구", "사하구"}}
            },
            {
                    {{"50"}, {"함양군", "거창군", "합천군", "창녕군", "밀양시", "양산시", "김해시"}},
                    {{"51"}, {"김해시", "창원시"}},
                    {{"52"}, {"함안군", "의령군", "산청군", "하동군", "남해군", "사천시", "진주시", "고성군"}},
                    {{"53"}, {"통영시", "거제시"}}
            },
            {
                    {{"54"}, {"군산시", "김제시", "익산시", "전주시"}},
                    {{"55"}, {"전주시", "완주군", "진안군", "무주군", "장수군", "남원시", "임실군"}},
                    {{"56"}, {"순창군", "정읍시", "부안군", "고창군"}}
            },
            {
                    {{"57"}, {"영광군", "함평군", "장성군", "담양군", "곡성군", "구례군", "광양시", "순천시"}},
                    {{"58"}, {"순천시", "화순군", "나주시", "영암군", "무안군", "목포시", "신안군", "진도군"}},
                    {{"59"}, {"해남군", "완도군", "강진군", "장흥군", "보성군", "고흥군", "여수시"}}
            },
            {
                    {{"61"}, {"북구", "동구", "남구", "서구"}},
                    {{"62"}, {"서구", "광산구"}}
            },
            {
                    {{"63"}, {"제주시", "서귀포시"}}
            }
    };

    @Test
    void makeSQL() {


        System.out.println(list[0][0][0][0]);
        String result = "";
        for (int k = 0; k < list.length; k++) {
            result = "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ";
            for (int i = 0; i < list[k].length; i++) {
                for (int j = 0; j < list[k][i][1].length; j++) {
                    result += "('" + list[k][i][1][j] + "', " + cities[k] + ", " + list[k][i][0][0] + "000," + list[k][i][0][0] + "099$, ";
                }
            }
            int index = result.lastIndexOf(",");
            result = result.substring(0, index);
            result = result.concat(";");
            System.out.println(result);
        }
    }

    @Test
    void right() {

        String all =
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('강북구', @seoul, 01000, 01299), ('도봉구', @seoul, 01300, 01599), ('노원구', @seoul, 01600, 01999), ('중랑구', @seoul, 02000, 02399), ('동대문구', @seoul, 02400, 02699), ('성북구', @seoul, 02700, 02999), ('종로구', @seoul, 03000, 03299), ('은평구', @seoul, 03300, 03599), ('서대문구', @seoul, 03600, 03899), ('마포구', @seoul, 03900, 04299), ('용산구', @seoul, 04300, 04499), ('중구', @seoul, 04500, 04699), ('성동구', @seoul, 04700, 04899), ('광진구', @seoul, 04900, 05199), ('강동구', @seoul, 05200, 05499), ('송파구', @seoul, 05500, 05999), ('강남구', @seoul, 06000, 06499), ('서초구', @seoul, 06500, 06899), ('동작구', @seoul, 06900, 07199), ('영등포구', @seoul, 07200, 07499), ('강서구', @seoul, 07500, 07899), ('양천구', @seoul, 07900, 08199), ('구로구', @seoul, 08200, 08499), ('금천구', @seoul, 08500, 08699), ('관악구', @seoul, 08700, 08999);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('기장군', @busan, 46000, 46199), ('금정구', @busan, 46200, 46499), ('북구', @busan, 46500, 46699), ('강서구', @busan, 46700, 46899), ('사상구', @busan, 46900, 47099), ('부산진구', @busan, 47100, 47499), ('연제구', @busan, 47500, 47699), ('동래구', @busan, 47700, 47999), ('해운대구', @busan, 48000, 48199), ('수영구', @busan, 48200, 48399), ('남구', @busan, 48400, 48699), ('동구', @busan, 48700, 48899), ('중구', @busan, 48900, 48999), ('영도구', @busan, 49000, 49199), ('서구', @busan, 49200, 49299), ('사하구', @busan, 49300, 49599);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('동구', @deagu, 41000, 41399), ('북구', @deagu, 41400, 41699), ('서구', @deagu, 41700, 41899), ('중구', @deagu, 41900, 41999), ('수성구', @deagu, 42000, 42399), ('남구', @deagu, 42400, 42599), ('달서구', @deagu, 42600, 42899), ('달성군', @deagu, 42900, 43099), ('군위군', @deagu, 43100, 43199);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('계양구', @incheon, 21000, 21299), ('부평구', @incheon, 21300, 21499), ('남동구', @incheon, 21500, 21899), ('연수구', @incheon, 21900, 22099), ('미추홀구', @incheon, 22100, 22299), ('중구', @incheon, 22300, 22499), ('동구', @incheon, 22500, 22599), ('서구', @incheon, 22600, 22999), ('강화군', @incheon, 23000, 23099), ('옹진군', @incheon, 23100, 23199);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('북구', @gwangju, 61000, 61399), ('동구', @gwangju, 61400, 61599), ('남구', @gwangju, 61600, 61899), ('서구', @gwangju, 61900, 62199), ('광산구', @gwangju, 62200, 62599);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('유성구', @deajeon, 34000, 34299), ('대덕구', @deajeon, 34300, 34499), ('동구', @deajeon, 34500, 34799), ('중구', @deajeon, 34800, 35199), ('서구', @deajeon, 35200, 35499);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('동구', @ulsan, 44000, 44199), ('북구', @ulsan, 44200, 44399), ('중구', @ulsan, 44400, 44599), ('남구', @ulsan, 44600, 44899), ('울주군', @ulsan, 44900, 45199);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('세종특별자치시', @sejong, 30000, 30199);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('김포시', @gyeonggi, 10000, 10199), ('고양시', @gyeonggi, 10200, 10799), ('파주시', @gyeonggi, 10800, 10999), ('연천군', @gyeonggi, 11000, 11099), ('포천시', @gyeonggi, 11100, 11299), ('동두천시', @gyeonggi, 11300, 11399), ('양주시', @gyeonggi, 11400, 11599), ('의정부시', @gyeonggi, 11600, 11899), ('구리시', @gyeonggi, 11900, 11999), ('남양주시', @gyeonggi, 12000, 12399), ('가평군', @gyeonggi, 12400, 12499), ('양평군', @gyeonggi, 12500, 12599), ('여주시', @gyeonggi, 12600, 12699), ('광주시', @gyeonggi, 12700, 12899), ('하남시', @gyeonggi, 12900, 13099), ('성남시', @gyeonggi, 13100, 13799), ('과천시', @gyeonggi, 13800, 13899), ('안양시', @gyeonggi, 13900, 14199), ('광명시', @gyeonggi, 14200, 14399), ('부천시', @gyeonggi, 14400, 14899), ('시흥시', @gyeonggi, 14900, 15199), ('안산시', @gyeonggi, 15200, 15799), ('군포시', @gyeonggi, 15800, 15999), ('의왕시', @gyeonggi, 16000, 16199), ('수원시', @gyeonggi, 16200, 16799), ('용인시', @gyeonggi, 16800, 17299), ('이천시', @gyeonggi, 17300, 17499), ('안성시', @gyeonggi, 17500, 17699), ('평택시', @gyeonggi, 17700, 18099), ('오산시', @gyeonggi, 18100, 18199), ('화성시', @gyeonggi, 18200, 18799);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('철원군', @gangwon, 24000, 24099), ('화천군', @gangwon, 24100, 24199), ('춘천시', @gangwon, 24200, 24499), ('양구군', @gangwon, 24500, 24599), ('인제군', @gangwon, 24600, 24699), ('고성군', @gangwon, 24700, 24799), ('속초시', @gangwon, 24800, 24999), ('양양군', @gangwon, 25000, 25099), ('홍천군', @gangwon, 25100, 25199), ('횡성군', @gangwon, 25200, 25299), ('평창군', @gangwon, 25300, 25399), ('강릉시', @gangwon, 25400, 25699), ('동해시', @gangwon, 25700, 25899), ('삼척시', @gangwon, 25900, 25999), ('태백시', @gangwon, 26000, 26099), ('정선군', @gangwon, 26100, 26199), ('영월군', @gangwon, 26200, 26299), ('원주시', @gangwon, 26300, 26599);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('단양군', @choongnbook, 27000, 27099), ('제천시', @choongnbook, 27100, 27299), ('충주시', @choongnbook, 27300, 27599), ('음성군', @choongnbook, 27600, 27799), ('진천군', @choongnbook, 27800, 27899), ('증평군', @choongnbook, 27900, 27999), ('괴산군', @choongnbook, 28000, 28099), ('청주시', @choongnbook, 28100, 28899), ('보은군', @choongnbook, 28900, 28999), ('옥천군', @choongnbook, 29000, 29099), ('영동군', @choongnbook, 29100, 29199);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('천안시', @choongnnam, 31000, 31399), ('아산시', @choongnnam, 31400, 31699), ('당진시', @choongnnam, 31700, 31899), ('서산시', @choongnnam, 31900, 32099), ('태안군', @choongnnam, 32100, 32199), ('홍성군', @choongnnam, 32200, 32399), ('예산군', @choongnnam, 32400, 32499), ('공주시', @choongnnam, 32500, 32699), ('금산군', @choongnnam, 32700, 32799), ('계룡시', @choongnnam, 32800, 32899), ('논산시', @choongnnam, 32900, 33099), ('부여군', @choongnnam, 33100, 33299), ('청양군', @choongnnam, 33300, 33399), ('보령시', @choongnnam, 33400, 33599), ('서천군', @choongnnam, 33600, 33699);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('군산시', @jeonbook, 54000, 54299), ('김제시', @jeonbook, 54300, 54499), ('익산시', @jeonbook, 54500, 54799), ('전주시', @jeonbook, 54800, 55299), ('완주군', @jeonbook, 55300, 55399), ('진안군', @jeonbook, 55400, 55499), ('무주군', @jeonbook, 55500, 55599), ('장수군', @jeonbook, 55600, 55699), ('남원시', @jeonbook, 55700, 55899), ('임실군', @jeonbook, 55900, 55999), ('순창군', @jeonbook, 56000, 56099), ('정읍시', @jeonbook, 56100, 56299), ('부안군', @jeonbook, 56300, 56399), ('고창군', @jeonbook, 56400, 56499);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('영광군', @jeonnam, 57000, 57099), ('함평군', @jeonnam, 57100, 57199), ('장성군', @jeonnam, 57200, 57299), ('담양군', @jeonnam, 57300, 57499), ('곡성군', @jeonnam, 57500, 57599), ('구례군', @jeonnam, 57600, 57699), ('광양시', @jeonnam, 57700, 57899), ('순천시', @jeonnam, 57900, 58099), ('화순군', @jeonnam, 58100, 58199), ('나주시', @jeonnam, 58200, 58399), ('영암군', @jeonnam, 58400, 58499), ('무안군', @jeonnam, 58500, 58599), ('목포시', @jeonnam, 58600, 58799), ('신안군', @jeonnam, 58800, 58899), ('진도군', @jeonnam, 58900, 58999), ('해남군', @jeonnam, 59000, 59099), ('완도군', @jeonnam, 59100, 59199), ('강진군', @jeonnam, 59200, 59299), ('장흥군', @jeonnam, 59300, 59399), ('보성군', @jeonnam, 59400, 59499), ('고흥군', @jeonnam, 59500, 59599), ('여수시', @jeonnam, 59600, 59899);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('영주시', @gyungbook, 36000, 36199), ('봉화군', @gyungbook, 36200, 36299), ('울진군', @gyungbook, 36300, 36399), ('영덕군', @gyungbook, 36400, 36499), ('영양군', @gyungbook, 36500, 36599), ('안동시', @gyungbook, 36600, 36799), ('예천군', @gyungbook, 36800, 36899), ('문경시', @gyungbook, 36900, 37099), ('상주시', @gyungbook, 37100, 37299), ('의성군', @gyungbook, 37300, 37399), ('청송군', @gyungbook, 37400, 37499), ('포항시', @gyungbook, 37500, 37999), ('경주시', @gyungbook, 38000, 38299), ('청도군', @gyungbook, 38300, 38399), ('경산시', @gyungbook, 38400, 38799), ('영천시', @gyungbook, 38800, 38999), ('군위군', @gyungbook, 39000, 39099), ('구미시', @gyungbook, 39100, 39499), ('김천시', @gyungbook, 39500, 39799), ('칠곡군', @gyungbook, 39800, 39999), ('성주군', @gyungbook, 40000, 40099), ('고령군', @gyungbook, 40100, 40199), ('울릉군', @gyungbook, 40200, 40299);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('함양군', @gyungnam, 50000, 50099), ('거창군', @gyungnam, 50100, 50199), ('합천군', @gyungnam, 50200, 50299), ('창녕군', @gyungnam, 50300, 50399), ('밀양시', @gyungnam, 50400, 50499), ('양산시', @gyungnam, 50500, 50799), ('김해시', @gyungnam, 50800, 51099), ('창원시', @gyungnam, 51100, 51999), ('함안군', @gyungnam, 52000, 52099), ('의령군', @gyungnam, 52100, 52199), ('산청군', @gyungnam, 52200, 52299), ('하동군', @gyungnam, 52300, 52399), ('남해군', @gyungnam, 52400, 52499), ('사천시', @gyungnam, 52500, 52599), ('진주시', @gyungnam, 52600, 52899), ('고성군', @gyungnam, 52900, 52999), ('통영시', @gyungnam, 53000, 53199), ('거제시', @gyungnam, 53200, 53399);\n" +
                "INSERT INTO city (name, state_id, zipcode_start, zipcode_end) VALUES ('제주시', @jeju, 63000, 63499), ('서귀포시', @jeju, 63500, 63699);";
        String[] split_state = all.split(";");
        int ok = 0;
        int wrong = 0;
        for (int i = 0; i < split_state.length; i++) {
            String[] split_city = split_state[i].split("@");
            int city_count = split_city.length - 1;
            var count = 0;
            for (int j = 0; j < list[i].length; j++) {
                for (int k = 0; k < list[i][j][1].length; k++) {
                        count++;
                }
            }
            if (count == city_count) {
                System.out.println("ok");
                System.out.println(++ok);
            } else {
                System.out.println("not");
                System.out.println(++wrong);
            }

        }
    }

}

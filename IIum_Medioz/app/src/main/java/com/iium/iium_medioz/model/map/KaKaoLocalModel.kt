package com.iium.iium_medioz.model.map

data class KaKaoLocalModel(
    val documents : List<KaKaoDocuments>,
    val meta : KaKaoMeta
)

data class KaKaoDocuments(
    val address_name: String? = null,   // 전체 지번 주소 또는 전체 도로명 주소, 입력에 따라 결정됨
    val address_type: String? = null,   // address_name의 값의 타입(Type)
    val address : KaKaoAddress, // 지번 주소 상세 정보, 아래 KaKaoAddress 참고
    val road_address : KaKaoRoadAddress,    // 도로명 주소 상세 정보, 아래 KaKaoRoadAddress 참고
    val x : String? = null, // X 좌표값, 경위도인 경우 경도(longitude)
    val y : String? = null, // Y 좌표값, 경위도인 경우 위도(latitude)
)

data class KaKaoMeta(
    val is_end: Boolean,        // 현재 페이지가 마지막 페이지인지 여부
    val pageable_count: Int? = null,    // total_count중 노출 가능 문서 수(최대: 45)
    val total_count: Int? = null    // 검색어에 검색된 문서 수
)

data class KaKaoAddress(
    val address_name: String? = null,   // 전체 지번 주소
    val region_1depth_name: String? = null, //  시도 단위
    val region_2depth_name: String? = null, // 구 단위
    val region_3depth_name: String? = null, // 동 단위
    val region_3depth_h_name: String? = null,   // 행정동 명칭
    val h_code : String? = null,    // 행정코드
    val b_code: String? = null,     // 법정 코드
    val mountain_yn: String? = null,    // 산 여부, Y 또는 N
    val main_address_no: String? = null,    // 지번 주번지
    val sub_address_no: String? = null, // 지번 부번지, 없을 경우 빈 문자열("") 반환
    val x: String? = null,  // X 좌표값, 경위도인 경우 경도(longitude)
    val y: String? = null   // Y 좌표값, 경위도인 경우 위도(latitude)
)

data class KaKaoRoadAddress(
    val address_name: String? = null,   // 전체 도로명 주소
    val region_1depth_name: String? = null, // 지역명1
    val region_2depth_name: String? = null, // 지역명 2
    val region_3depth_name: String? = null, // 지역명 3
    val road_name: String? = null,  // 도로명
    val underground_yn: String? = null, //지하여부, Y또는 N
    val main_building_no : String? = null,  //건물 본번
    val sub_building_no: String? = null,    // 건물 부번, 없을 경우 빈 문자열("") 반환
    val building_name: String? = null,  // 건물 이름
    val zone_no : String? = null,   // 우편번호(5자리)
    val x: String? = null,  // X 좌표값, 경위도인 경우 경도(longitude)
    val y: String? = null   // Y 좌표값, 경위도인 경우 위도(latitude)
)


소스 설명
AddressBookController : 주소록에 관련된 요청을 처리하는 컨트롤러 클래스.

AddressBookControllerTest : 통합테스트

AddressBookControllerUnitTest : 단위 테스트 ( Mock)

AddressBookDto :  주소록에 있는 연락처 항목을 나타내는 데이터 전송 객체(DTO) 전화번호, 이메일 주소, 주소, 이름의 정보를 캡슐화.

BaseDto: 페이지네이션 및 정렬에 사용되는 기본 DTO 인터페이스.

RequestDto :  클래스는 페이지네이션 및 정렬 정보를 포함하는 데이터 전송 객체(DTO)

ResponseDto: API 응답을 나타내는 클래스로, 메시지와 성공 여부, 데이터 등을 포함.

AddressBookRepository 인터페이스는 주소록 데이터의 영속성을 관리하기 위한 메서드들을 정의합니다. 이 인터페이스는 다음과 같은 기능을 제공

이름에 따라 항목 삭제

전화번호 또는 이메일로 항목 찾기

주소를 포함하는 항목 검색 및 주소에 따른 오름차순 또는 내림차순 정렬

이름을 포함하는 항목 검색 및 이름에 따른 오름차순 또는 내림차순 정렬

전체 주소록 항목 조회

새로운 주소록 항목 저장

MemoryAddressBookRepository: 메모리 기반의 주소록 저장소를 구현한 클래스로, 실제 데이터베이스가 아닌 메모리에 데이터를 저장하고 관리. 주소록을 추가, 조회, 삭제하는 여러 메서드를 구현.
AddressBookBackup : 주소록 데이터를 백업하는 기능을 담당

AddressBookLoader : CSV 파일에서 주소록 데이터를 로드하고 유효성을 검사하여 주소록에 추가하는 기능을 제공

AddressBookLoaderTest : 스프링 부트 테스트

AddressBookLoaderUnitTest : 단위 테스트

AddressBookInitializer: 주소록 초기화를 담당하는 클래스로, 어플리케이션이 실행될 때 메모리에 데이터를 로드합니다. 이를 위해 AddressBookLoader를 사용하고, 파일 경로를 주입받아 파일에서 데이터를 읽어와 초기화.

AddressBookService :  클래스는 주소록 데이터를 관리하기 위한 비즈니스 로직을 제공

AddressBookServiceTest : 단위 테스트

deleteByNameLike(RequestDto requestDto, String name): 이름을 기반으로 주소록 항목을 삭제.

searchByNameLike(RequestDto requestDto, String name): 이름을 기반으로 주소록 항목을 검색.

searchByTelephone(String telephone): 전화번호를 기반으로 주소록 항목을 검색

searchByAddressLike(RequestDto requestDto, String address): 주소를 기반으로 주소록 항목을 검색.

deleteByTelephone(String telephone): 주어진 전화번호에 해당하는 주소록 항목을 삭제.

updateByTelephone(String telephone, AddressBookDto updatedDto): 주어진 전화번호에 해당하는 주소록 항목을 업데이트.

searchByEmail(String email): 이메일을 기반으로 주소록 항목을 검색.

isDuplicateEmail(String email): 주어진 이메일이 이미 존재하는지 확인.

saveAddressBookEntry(AddressBookDto addressBookDto): 새로운 주소록 항목을 저장. 중복된 전화번호 및 이메일인 경우 오류가 기록.

findAll(): 모든 주소록 항목을 반환.
AddressBookBackupUtil: 주소록 백업과 관련된 유틸리티 클래스로, 백업 파일 생성, 주소록 데이터를 CSV 파일로 저장, 파일 삭제 등의 기능을 제공
AddressBookBackupUtilTest : 스프링 부트 테스트
ValidationUtil: 전화번호와 이메일의 유효성을 검사하는 유틸리티 클래스

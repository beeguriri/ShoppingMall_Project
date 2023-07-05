# ShoppingMall_Project
 '스프링부트 쇼핑몰프로젝트 with JPA' 책을 참조하여 공부하고 있습니다.

## 프로젝트 생성 및 세팅
- 'https://start.spring.io/' 에서 프로젝트 생성
    - SpringBoot `2.7.13`
    - Gradle Groovy `7.6.1`
    - java `17`
    - Dependencies
        - WEB : `Spring Web`
        - SQL : `Spring Data JPA`  `H2 Database`
        - DEVELOPER TOOLS : `Lombok`
        - SECURITY : `Spring Security`
        - External Library : `com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.7`
- IDE : IntelliJ
- DB : H2 `jdbc:h2:tcp://localhost/~/shop`

## 프로젝트 일정
- 7/5 전체적인 엔티티 설계, Item Entity 개발

## 엔티티 설계
- 회원 정보를 중심으로 연관관계 매핑
  - `member` 한명은 여러개의 `orders`를 가질 수 있음
  - `orders` 하나는 여러개의 `orderItem`을 가질 수 있음
  - `member` 한명은 한개의 `cart`를 가질 수 있음
  - `cart` 하나는 여러개의 `cartItem`을 가질 수 있음
  - `Item`하나는 여러개의 `orderItem`과 여러개의 `cartItem`이 될 수 있음
  - `Item`하나에 여러개의 `ItemImage`를 가질 수 있음
- 회원 인증, 인가는 시큐리티로 처리
  - 인증이 필요없는 경우 : 상품상세 페이지
  - 인증이 필요한 경우 : 상품 주문
  - 관리자 권한이 필요한 경우 : 상품등록

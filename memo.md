## 객체 지향 쿼리 언어1 - 기본 문법

### 1. 소개

#### JPA 는 다양한 쿼리 방법을 지원 
- **JPQL**  
- JPA criteria 
- **QueryDSL** 
- native SQL 
- JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함계 사용 

#### JPQL 소개 
- 가장 단순한 조회 방법 
  - EntityManager.find()
  - 객체 그래프 탐색(a.getB().getC())
  - 만약 나이가 18살 이상인 회원을 모두 검색하고 싶다면 ? 
- JPA를 사용하면 엔티티 객체를 중심으로 개발 
- 문제는 검색쿼리 
- 검색을 할 때도 **테이블이 아닌 엔티티 객체를 대상으로 검색** 
- 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능 
- application이 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요 
- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공 
- SQL 과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원 
- **JPQL은 엔티티 객체를 대상으로 쿼리** // 엔티티 대상으로 하니 표현 방식이 다름 -> 실행시 SQL 문법으로 매핑됨
- SQL은 DB 테이블을 대상으로 쿼리 
- SQL을 추상화해서 특정 데이터베이스 SQL에 의존 x  // 추상화 했으니 여러 DBMS 바꿔서 사용가능, DBMS 문법 매핑 지원함

> JPQL을 한마디로 정의하면 객체 지향 SQL 

//검색 JPQL 예시 생략(7분, ppt 참고)

- 단점. JPQL 은 동적 쿼리 만들기 굉장히 어려움 // 조건문에 짜르기 더하기로 string query 생성 ㅋㅋ  

#### Criteria 소개 
//예시 (12분, ppt 참고)
- 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- 표준 문법 스펙 지원함(자바 1.6) 
- JPQL 빌더 역할 
- JPA 공식 기능 
- 장점 
  - 오타 날 경우 컴파일 시점에 에러 확인가능 
  - 동적 쿼리 짜기 쉬움 
- 단점 
  - SQL 스럽지 않음  // 실무에서 안씀. 운영/유지보수가 안되서.. 망한 스펙 같다고 함.
  - 디버깅 어려움 // 결국 너무 복잡하고 실용성 없음..
- Criteria 대신 **QueryDSL(오픈소스 라이브러리) 사용 권장**

#### QueryDSL 소개  -- 실무 사용 권장👍
//예시 (19분)
- 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- JPQL 빌더 역할 
- 컴파일 시점에 문법 오류를 찾을 수 있음 
- 동적 쿼리 작성 편리함
- **단순하고 쉬움, 직관적**   // Criteria 보다 좀 더 보기 쉽네 . jpql 알면 querydsl도 습득가능하다 함

> www.querydsl.com  > Reference doc 가면 setting 이랑 문법 어떻게 사용하는지 잘 나와 있음 !!

#### native SQL 소개 
//예시 (25분)
- JPA가 제공하는 **SQL을 직접 사용하는 기능**   // = 진짜 SQL문 사용가능 
- JPQL로 해결할 수 없는 특정 DB에 의존적인 기능 
- 예) 오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트(<-> hibernate에서 셋팅해서 기능 제공한다함)

#### JDBC 직접 사용, SpringJdbcTemplate 등 
- JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, Mybatis 등을 함께 사용 가능 
- 단, 영속성 컨텍스트를 적절한 시점에 강제로 flush 필요 
  - flush 되야(persist -> commit 할 때) DB에 데이터가 들어가는데..
  - em.createNativeQuery() 실행시 바로 flush 되고 commit(이 시점에도 flush) 됨 // 전략 
  - 문제가 되는 예시에 대해 설명함 (30분)
- 예) JPA를 우회해서 SQL을 실행하기 직전에 영속성 컨텍스트 수동 flush

### 2. 기본 문법과 쿼리 API 

#### JPQL(Java Persistence Query Language) 
- 객체지향 쿼리 언어다. 따라서 테이블 대상으로 쿼리하는 것이 아니라, **엔티티 객체 대상으로 쿼리**한다
- JPQL은 SQL을 추상화해서 특정 DB SQL에 의존하지 않는다. 
- JPQL은 결국 SQL롤 변환된다.

#### 문법 
- select m from **Member as m** where **m.age** > 18 
- Entity 와 속성은 대소문자 구분 O (Member, age)
- JPQL 키워드는 대소문자 구분 x (SELECT, FROM, where)
- Entity 이름 사용, 테이블 이름 아님(Member)
- **별칭은 필수(m)** (이때 as 표기는 생략 가능)

#### 집합과 정렬 
- count(), sum(), avg(), max(), min() // ANSI SQL , 표준 function 지원함 
- group by, having
- order by 

#### TypeQuery, Query 
- TypeQuery<Class> : 반환 타입이 명확할 때 사용 
- Query : 반환 타입이 명확하지 않을 때 사용 

#### 결과 조회 API 
- query.getResultList() : 결과가 하나 이상일때, 리스트 반환 
  - 결과가 없으면 빈 리스트 반환 
- query.getSingleResult() : **결과가 정확히 하나 나와야 함,** 단일 객체 반환 --- 값이 보장일때 사용, 조심👨‍💻
  - 결과가 없으면 : javax.persistence.NoResultException
  - 둘 이상이면 : javax.persistence.NonUniqueResultException

#### 파라미터 바인딩 - 이름기준,위치기반 
``` 
# 이름 기준 - 사용 권장 o 
SELECT m FROM Member m where m.username =:username
query.setParameter("username", usernameParam);

# 위치 기반 - 사용 권장 x, 순서(position) 밀리면 버그 발생가능 
 SELECT m FROM Member m where m.username =?1
query.setParameter(1, usernameParam);
```

### 3. 프로젝션(SELECT)
- SELECT 절에 조회할 대상을 지정하는 것 
- 프로젝션 대상 : 엔티티, 임베디드 타입, 스칼라 타입(숫자, 문자 등 기본 데이터 타입)
- SELECT m FROM Member m -> 엔티티 프로젝션 
- SELECT m.team FROM Member m -> 엔티티 프로젝션 (묵시적 조인)
  - 이렇게 입력해도 자체에서 join query 나가는데 
  - select t from Member m join m.team t  이렇게 하는게 인식하기 좋음 (명시적으로 하는게 좋다)
- SELECT m.address FROM Member m -> 임베디드 타입 프로젝션 // Address = 임베디드 , 자체 호출하는게 아니라 소속 엔티티 통해 호출 
- SELECT m.username, m.age FROM Member m -> 스칼라 타입 프로젝션 
- DISTINCT 로 중복제거

#### 프로젝션 - 여러값 조회 
> SELECt m.username, m.age FROM Member m

``` 
// 1. Query 타입으로 조회 
      List resultList = em.createQuery("select m.username, m.age from Member m").getResultList();

      Object o = resultList.get(0);
      Object[] resultObjects = (Object[])o;
      System.out.println("username =" + resultObjects[0]);
      System.out.println("age =" + resultObjects[1]);
      
// 2. Object[] 타입으로 조회 
   
      List<Object[]> resultList2 = em.createQuery("select m.username, m.age from Member m").getResultList();

      Object[] resultObjects2 = resultList2.get(0);
      System.out.println("username =" + resultObjects2[0]);
      System.out.println("age =" + resultObjects2[1]);

// 3. new 명령어로 조회 
   - 단순 값을 DTO로 바로 조회 
     SELECT new jpabook.jpql.UserDTO(m.username, m.age) FROM Member m
   - 패키지 명을 포함한 전체 클래스명 입력 -> 텍스트가 길어지니 단점이기도 함  
   - 순서와 타입이 일치하는 생성자 필요 
    
      List<MemberDTO> memberDTOs = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m").getResultList();
      MemberDTO memberDTO = memberDTOs.get(0);
      System.out.println("username =" + memberDTO.getUsername());
      System.out.println("age =" + memberDTO.getAge());
```

### 4. 페이징 
- JPA는 페이징을 다음 두 API로 추상화 
  - hibernate.dialect 방언 설정에 따라 알아서 페이징 처리 query 만들어서 실행해줌  
- **setFirstResult(int startPosition)** : 조회 시작 위치 (0부터 시작)
- **setMaxResults(int maxResult)** : 조회할 데이터 수 

``` 
// 방언만 수정하면 추상화된 JPA가 알아서 query 만들어줌 -> 우린 표준 스펙만 맞춰주면 됨 
 List<Member> result = em.createQuery("select m from Member m Order by m.age desc", Member.class)
                            .setFirstResult(1)
                            .setMaxResults(10)
                            .getResultList();
```

### 5. 조인 
-내부 조인 
> SELECT m FROM Member m [INNER] JOIN m.team t 

-외부 조인 
> SELECT m FROM Member m LEFT [OUTER] JOIN m.team t 

- 세타 조인 : 연관관계 없는 데 억지로 조인하는 용도, Cross join 
> SELECT count(m) FROM Member m, Team t WHERE m.username = t.name 

#### ON 절 
- ON절을 활용한 조인(JPA 2.1부터 지원)
  1. 조인 대상 필터링 
     예) 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인 
     > JQPL : SELECT m, t FROM Member m LEFT JOIN m.team on t.name = 'A'
     > SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.TEAM_ID = t.id and t.name = 'A'
     
  2. 연관관계 없는 엔티티 외부 조인 (hibernate 5.1부터) // 세타 조인 처럼
     예) 회원의 이름과 팀의 이름이 같은 대상 외부 조인 
     > JQPL : SELECT m, t FROM Member m LEFT JOIN Team t on m.username = t.name 
     > SQL : SELECT m.*, t.* FROM Member m LEFT JOIN Team t ON m.username = t.name

### 6. 서브쿼리
• 나이가 평균보다 많은 회원 
> select m from Member m where m.age > (select avg(m2.age) from Member m2)

• 한 건이라도 주문한 고객 
> select m from Member m where (select count(o) from Order o where m = o.member) > 0

#### 서브 쿼리 지원 함수 
- [NOT] EXISTS (subquery) : 서브 쿼리에 결과가 존재하면 참 
  - {ALL | ANY | SOME} (subquery)
  - ALL : 모두 만족하면 참 
  - ANY, SOME : 같은 의미, 조건을 하나라도 만족하면 참(=OR)
- [NOT] IN (subquery) : 서브쿼리의 결과 중 하나라도 같은 것이 있으면 참 

#### 서브쿼리 - 예제 (2:50)
• 팀 A 소속인 회원 
> select m from Member m where exists (select t from m.team t where t.name = '팀A')

• 전체 상품 각각의 재고보다 주문량이 많은 주문들 
> select o from Order o where o.orderAmount > ALL (select p.stockAmount from Product p)

• 어떤 팀이든 팀에 소속된 회원 
> select m from Member m where m.team = ANY (select t from Team t)

#### JPA 서브 쿼리 한계 
• JPA 표준 스펙에서는 WHERE, HAVING 절에서만 서브 쿼리 사용 가능
• SELECT 절도 가능 (hibernate 에서 지원)
• **FROM 절의 서브 쿼리는 현재 JPQL 에서 불가능 → 조인으로 풀 수 있으면 풀어서 해결**  // from 절 sub query 안되서 불편했다함
  - 방안) join 사용 || query 2번 날리거나 || native query 쓰거나 
  
### 7. JPQL 타입 표현과 기타식 
#### JPQL 타입 표현
• 문자 : 'HELLO', 'She"s'  // 홑따옴표 안에 
• 숫자 : 10L(Long), 10D(Double), 10F(Float)
• Boolean : TRUE, FALSE
• ENUM : jpabook.MemberType.ADMIN (패키지명 포함해서 다 넣어야 함)   // queryDSL 쓰거나, 다른 방법으로 개선 가능  
• 엔티티 타입 : TYPE(m) = Member (상속 관계에서 사용) // 캐스팅 같은거라네 , 상속 관계 편 다시 보기 

#### JPQL 기타 
• SQL과 문법이 같은식
• EXISTS, IN
• AND, OR, NOT
• =, >, >=, <, <=, <>
• BETWEEN, LIKE, IS NULL 등등

### 8. 조건식(CASE 등등)
#### 조건식 - CASE 식
``` 
# 기본 CASE 식 
select 
    case when m.age <= 10 then '학생요금'
         when m.age >= 60 then '경로요금'
         else '일반요금'
    end 
from Member m 

# 단순 CASE 식 (정확히 매칭되면 수행)
select 
    case t.name 
        when '팀A' then '인센티브110%'
        when '팀B' then '인센티브120%'
        else '인센티브105%'
    end 
from Team t         
```
• COALESCE : 하나씩 조회해서 null이 아니면 반환
> select coalesce(m.username, '이름 없는 회원') from Member m; //사용자 이름이 없으면 이름 없는 회원 반환 
 
• NULLIF : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
> select NULLIF(m.username, '관리자') from Member m; // 사용자 이름이 '관리자'면 null을 반환하고 나머지는 본인의 이름 반환

### 9. JPQL 함수 
#### JPQL 기본(표준) 함수 
• CONCAT
• SUBSTRING
• TRIM
• LOWER, UPPER
• LENGTH
• LOCATE
• ABS, SORT, MOD
• SIZE, INDEX(JPA 용도)  // SIZE() : 양방향 일떄 collection size, INDEX : 거의 안쓰는게 좋음 

#### 사용자 정의 함수 호출 
• hibernate 는 사용 전 방언에 추가해야함
• 사용하는 DB 방언을 상속받고, 사용자 정의 함수를 등록한다 
> select function('group_concat', i.name) from Item i 
> select group_concat(m.username) from Member m      // hibernate 표현 방식 지원 , 인텔리제이 에러 뜨면 꺼버리기 

// MySQL57Dialect 클래스(hibernate 패키지) 보면 this.registerFunction(..) 함수 등록되어 있는게 있음
``` 
package dialect;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class MyH2Dialect extends H2Dialect {

    public MyH2Dialect() {
        registerFunction("group_concat", new StandardSQLFunction("group_concat", StandardBasicTypes.STRING));
    }
}

//persistence.xml 에서 수정하면 custom 방언 사용가능
<property name="hibernate.dialect" value="dialect.MyH2Dialect"/>
```


## 객체 지향 쿼리 언어2 - 중급 문법 

### 1. 경로 표현식

#### 경로 표현식 
- .(점)을 찍어 객체 그래프를 탐색하는 것 
``` 
select m.username    → 상태 필드
from Member m 
    join m.team t    → 단일 값 연관 필드 
    join m.orders o  → 컬렉션 값 연관 필드
where t.name = '팀A'
``` 

#### 용어 정리 
• **상태 필드(state field)** : 단순히 값을 저장하기 위한 필드 (ex. m.username)
• **연관 필드(association field)** : 연관 관계를 위한 필드
    • 단일 값 연관 필드 : @ManyToOne, @OneToOne, 대상이 엔티티 (ex. m.team)
    • 컬렉션 값 연관 필드 : @OneToMany, @ManyToMany, 대상이 컬렉션 (ex. m.orders)

#### 경로 표현식 특징 
• **상태 필드(state field)** : 경로 탐색의 끝, 탐색 X
    - select m.username from Member m;
• **단일 값 연관 경로** : 묵시적 내부 조인(inner join) 발생, 탐색 O // 좀 위험하다함 , 내부 조인 계속 발생
    - (jql) select m.team.블라블라 from Member m;
    - (실제 쿼리) select t.id , t.name from Member m inner join team t on m.team_id = t.id
• **컬렉션 값 연관 경로** : 묵시적 내부 조인 발생, 탐색 X
    • FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색 가능
    - (jql) select t.members from Team t;  // Collection.class 리턴함, 이렇게 사용하진 않음  
    - (jql) select t.members.size from Team t; // Integer.class, 이렇게 사용하진 않음
    - (jql) select m.username from Team t join t.members m; // 명시적 조인으로 탐색한 예시

#### 명시적/묵시적 조인 
• 명시적 조인 : join 키워드 직접 사용
    • select m from Member m **join m.team t;**
• 묵시적 조인 : 경로 표현식에 의해 묵시적으로 SQL 조인 발생(내부 조인만 가능)
    • select m.team from Member m;

#### 경로표현식 - 예제 (19분, 생략)

#### 경로 탐색을 사용한 묵시적 조인 시 주의사항 
• 항상 내부 조인
• 컬렉션은 경로 탐색의 끝, 명시적 조인을 통해 별칭을 얻어야 함
• 경로 탐색은 주로 SELECT, WHERE 절에서 사용하지만 묵시적 조인으로 인해 SQL의 FROM(Join)절에 영향을 줌

#### 실무 조언 
• **가급적 묵시적 조인 대신에 명시적 조인 사용**
• 조인은 SQL 튜닝에 중요 포인트
• 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움 

### 2. 패치조인 1 - 기본 ( 중요 )

#### 패치 조인(fetch join)
• SQL 조인 종류 x
• JPQL에서 **성능 최적화**를 위해 제공하는 기능
• 연관된 엔티티나 컬렉션을 **SQL 한 번에 함께 조회**하는 기능
• join fetch 명령어 사용
• [LEFT [OUTER] | INNER] JOIN FETCH 조인 경로 

#### 엔티티 페치 조인 
• 회원을 조회하면서 연관된 팀도 함께 조회(SQL 한번에!!) 가능
• SQL을 보면 회원 뿐만 아니라 **팀(T.*)** 도 함께 **SELECT**
• (JPQL) select m from Member m **join fetch** m.team
• (SQL) select m.*, t.* from Member m **inner join Team t** on m.team_id = t.id
// 예시 이미지. 4:30초

```java 
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setAge(10);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setAge(10);
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setAge(10);
            member3.setTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("member4");
            member4.setAge(10);
            em.persist(member4); // member4는 team에 값이 없어 join 된게 없으니 select x
            
            // fetch를 안하고 따로 m.getTeam().getName() 하면 lazy 호출함  -> N + 1 문제 발생 
            String jpql = "select m from Member m join fetch m.team";
            List<Member> members = em.createQuery(jpql, Member.class)
                            .getResultList();

            for(Member m : members) {
                System.out.println(m.toString());
            }

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    /*
        Member{id=35, username='member1', age=10, team=jpql.Team@5ed4bc}
        Member{id=36, username='member2', age=10, team=jpql.Team@12fe1f28}
        Member{id=37, username='member3', age=10, team=jpql.Team@12fe1f28}
    */
```

#### 컬렉션 패치 조인 
• 일대다 관계 -> 데이터 뻥튀기 될 수 있음(row가 여러개 만들어져서 리턴, 문제..)
• (jpql) select t from Team t **join fetch t.members** where t.name = '팀A'
• (SQL) select T.*, **M.*** from team t inner join member m on t.id = m.team_id where t.name = '팀A'
// 예시 이미지. 16분, 18분 
```java 
    String jpql2 = "select t from Team t join fetch t.members where t.name = 'teamA' ";
    List<Team> teams = em.createQuery(jpql2, Team.class).getResultList();

    for(Team team : teams) {
        System.out.println("teamname = " + team.getName() + ", team = " + team);
        for(Member member : team.getMembers()) {
            System.out.println("->username =" + member.getUsername() + ", member =" + member);
        }
    }
```
// 실행결과 Team A 에 소속된 member가 2명이면 2 row가 반환되는데, jpa 입장에서는 collection 리턴 해야 하므로 그대로 가져옴(처리는 사용자가 알아서)
// 일대다 조인할때 DB row 뻥튀기 될 수 있다.

#### 패치 조인과 DISTINCT 
• SQL의 DISTINCT는 중복된 결과를 제거하는 명령 
• JPQL의 DISTINCT 2가지 기능 제공
    • 1. SQL 에 DISTINCT 를 추가
    • 2. 애플리케이션에서 엔티티 중복 제거 // 결과가 dbms 에서 날라오면 처리해줌 
``` 
# query 에서 distinct 할 경우 
    select distinct t 
    from Team t join fetch t.members 
    where t.name = 'teamA'; 
    -> SQL에 distinct를 추가하지만 데이터가 다르므로 SQL에서 중복 제거 실패 (join 된 member 테이블 필드까지 동일해야 중복제거 됨) 

# jqpl (JPA) 에서는 같은 식별자를 가진 Team 엔티티 제거 ! 
    String jpql2 = "select distinct t from Team t join fetch t.members where t.name = 'teamA' ";
    List<Team> teams = em.createQuery(jpql2, Team.class).getResultList();

    for(Team team : teams) {
        System.out.println("teamname = " + team.getName() + ", team = " + team);
        for(Member member : team.getMembers()) {
            System.out.println("->username =" + member.getUsername() + ", member =" + member);
        }
    }
```

#### 패치 조인과 일반 조인의 차이
• 일반 조인 실행시 연관된 엔티티를 함께 조회하지 않음
• (JPQL) select t from TEAM t join t.members m where t.name = 'teamA';
• (SQL) select T.* from TEAM T INNER JOIN MEMBER M ON T.ID = M.TEAM_ID WHERE T.NAME = 'teamA';
• JPQL 은 결과를 반환할 때 연관관계 고려 X 
• 단지 SELECT 절에 지정한 엔티티만 조회할 뿐 
• 여기서는 TEAM 엔티티만 조회하고, MEMBER 엔티티는 조회 x
• 패치 조인을 사용할 때만 연관된 엔티티도 함께 조회 **(즉시 로딩)**
• **패치 조인은 객체 그래프를 SQL 한번에 조회하는 개념**

### 3. 패치조인 2 - 한계 
#### 패치조인의 특징과 한계 -- 예시가 이해 안됨... 컬렉션 패치조인을 할 경우가 없으니  
• **패치 조인 대상에는 별칭을 줄 수 없다.(관례)**
    • hibernate 는 가능, 가급적 사용 x // 정합성 이슈 존재 
• **둘 이상의 컬렉션은 패치 조인 할 수 없다.** // 일대다 * 일대다 뻥튀기 문제 ! 
• **컬렉션을 패치 조인하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다.** // team 기준 데이터 뻥튀기 되서 teamA row가 2개인데 , 1개만 가져오라면 .. teamA 콜렉션은 1개만 가져옴(실제2개인데)
    • 일대일, 다대일 같은 단일 값 연관 필드들은 패치 조인해도 페이징 가능 
    • hibernate 는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
// 예시가 제대로 이해 x, Team을 fetch join 조회하는데 특정 조건 걸어서 조회하면 문제 발생가능하다는데..
> WARN: HHH000104: firstResult/maxResults specified with 'collection fetch'; applying in memory!

``` 
# 1. Collection 패치 조인일대 @BatchSize 사용해서 뻥튀기(N+1) 해결하기도 한다함
@BatchSize(size = 100)     
@OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
private List<Member> members = new ArrayList<>();

# 2. 글로벌 배치 size 설정 사용하는 경우 
<property name="hibernate.default_batch_fetch_size" value="100"/>
```

#### 패치 조인의 특징과 한계 
• 연관된 엔티티들을 SQL 한번으로 조회 가능 - 성능 최적화
• 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
    • @OneToMany(fetch = FetchType.LAZY) //글로벌 로딩 전략
• 실무에서 글로벌 로딩 전략은 모두 지연 로딩
• 최적화가 필요한 곳은 패치 조인 적용

#### 정리 
• 모든 것을 패치 조인으로 해결할 수 x
• 패치 조인은 객체 그래프를 유지할 떄 사용하면 효과적
• (point) 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 하면,
  패치 조인 보다 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적

### 4. 다형성 쿼리 




### 5. 엔티티 직접 사용 




### 6. Named 쿼리 




### 7. 벌크 연산 
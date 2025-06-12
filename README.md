# KBHC

## 업데이트
- 2025.06.13: 과제 구현

## 모듈 구성
- data module은 Entity, Repository, Service로 구성되어 있으며 기본적인 조회, 저장 기능을 제공합니다.
- boot module은 Controller, Service로 구성되어 있으며, data module의 기능을 조합하여 API를 제공합니다.
 

## API 구현
- POST /users/walks: Input Data 저장 
- GET /users/walks/daily/{recordKey}: 일별 집계 데이터 조회
- GET /users/walks/monthly/{recordKey}: 월별 집계 데이터 조회

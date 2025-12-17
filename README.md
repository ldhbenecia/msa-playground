# MSA Playground 

Spring Cloud 기반의 마이크로서비스 아키텍처(MSA)를 구축하고, 이를 **Cloud Native(Kubernetes)** 환경으로 전환하는 과정을 실험하는 프로젝트입니다. e-commerce 도메인을 기반으로 서비스 분리, 비동기 통신(Kafka), 인프라 최적화를 다룹니다.

## 🏗 Project Structure (Monorepo)

이 레포지토리는 여러 마이크로서비스를 하나의 모노레포로 관리합니다.

```text
msa-playground/
├── api-gateway/        # Spring Cloud Gateway
├── config-server/      # Centralized Config (Local Only)
├── eureka-server/      # Service Discovery (Local Only)
├── user-service/       # 회원 도메인
├── order-service/      # 주문 도메인
├── docker-compose.yml  # 로컬 개발용 인프라 (MySQL, Redis, Kafka)
└── README.md
```
Note: 로컬 개발 효율성을 위해 Eureka(Service Discovery)와 Config Server는 사용하지 않고, docker-compose와 Static Routing을 통해 경량화된 환경에서 개발합니다.

## 🔄 Architecture Transition (Spring Cloud vs Kubernetes)

이 프로젝트는 로컬 개발 환경(Docker Compose)과 배포 환경(Kubernetes)의 차이를 이해하고, 단계적으로 Spring Cloud 컴포넌트를 K8s Native 기능으로 대체하는 전략을 취합니다.

| Component | Local (Spring Cloud) | Prod (Kubernetes Native) | Status |
|-----------|---------------------|--------------------------|--------|
| **Service Discovery** | Eureka Server | K8s Service (CoreDNS) | K8s 배포 시 Eureka 제거 예정 |
| **Configuration** | Config Server | ConfigMap & Secret | K8s 배포 시 Config Server 제거 예정 |
| **API Gateway** | Spring Cloud Gateway | Ingress Controller (+ SCG) | Ingress 도입 후 역할 축소 및 병행 |
| **Load Balancing** | Spring Cloud LoadBalancer | K8s Service (ClusterIP) | K8s 자체 로드밸런싱 활용 |

> **Note:** 현재 `main` 브랜치는 로컬 개발 편의성을 위해 Eureka와 Config Server를 포함하고 있습니다. K8s 배포 설정(Manifest)은 별도의 Helm/Infra 레포지토리에서 관리합니다.

## 🛠 Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.5, Spring Cloud
- **Database:** MySQL 8.0, Redis (Cache/Session)
- **Message Broker:** Apache Kafka (KRaft Mode)
- **Infra:** Docker, Docker Compose (Local) / Kubernetes (Prod)
- **Tools:** GitHub Actions, k6 (Load Testing)

## 🔌 Port Mapping (Local)

로컬 docker-compose 환경에서의 포트 구성입니다.

| Service | Port | Description |
|---------|------|-------------|
| **Infra: MySQL** | 3306 | Main Database |
| **Infra: Redis** | 6379 | Cache & Session |
| **Infra: Kafka** | 9092 | Message Broker |
| **Config Server** | 8888 | (Local Only) 설정 중앙 관리 |
| **Eureka Server** | 8761 | (Local Only) 서비스 디스커버리 |
| **API Gateway** | 8080 | 외부 API 진입점 |
| **User Service** | 8081 | 회원 서비스 |
| **Order Service** | 8082 | 주문 서비스 |

## 🚀 How to Run (Local)

### 1. Infrastructure 실행

프로젝트 루트에서 Docker Compose를 실행합니다. (DB, Kafka 구동)

```bash
docker-compose up -d
```

실행 후 MySQL(`localhost:3306`, `root/root`)에 접속하여 `user_db`, `order_db` 스키마를 생성해주세요.

### 2. Application 실행

Local 환경에서는 Eureka/Config 의존성으로 인해 아래 순서로 실행을 권장합니다.

1. **config-server** (가장 먼저 실행)
2. **eureka-server**
3. **user-service** / **order-service**
4. **api-gateway**

## 📝 Performance & Optimization

- **Kafka Event-Driven:** 서비스 간 강결합을 제거하기 위해 Kafka 기반 비동기 이벤트 처리
- **DB Optimization:** JPA N+1 문제 해결 및 인덱싱 튜닝
- **Load Testing:** k6를 활용한 시나리오별 부하 테스트 및 병목 구간 개선
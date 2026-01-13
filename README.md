# Halo Cloud ☁️🤖

> **The First Cloud-Native AI Agent Mesh for Java & Spring Developers.**
>
> 专为 Java 开发者打造的云原生 AI Agent 编排平台。

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Ready-blue.svg)](https://kubernetes.io/)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)]()

## 📖 Introduction

**Halo Cloud** is an open-source infrastructure designed to build, deploy, and manage AI Agents using **Java** and **Kubernetes**.

While the Python ecosystem (LangChain/Dify) dominates the AI landscape, Java developers lack a cloud-native, high-concurrency solution. Halo Cloud bridges this gap by leveraging **Spring Cloud Gateway** and the **Kubernetes Sidecar Pattern**, injecting AI capabilities into microservices without rewriting business logic.

Halo Cloud is the infrastructure. The 'Financial Sentinel' is the proof-of-concept application built on top of Halo Cloud to demonstrate its power in high-concurrency markets.

## 🌟 Key Features

* **☕ Java Native**: Built on **Spring AI** and Spring Boot 3. Seamless integration for enterprise Java teams.
* **🚀 Reactive Gateway**: High-performance AI traffic router based on Spring WebFlux.
* **🛡️ Sidecar Architecture**: Decouples AI logic (Perception/Action) from the main service. Agents run as independent sidecars.
* **🔒 Distributed Safety**: Built-in **Redis Distributed Lock (Redisson)** prevents "Split-Brain" issues and saves API tokens in multi-replica K8s deployments.
* **🧠 Model Agnostic**: Supports OpenAI, DeepSeek, Claude, and Local LLMs via standard interfaces.

## 🛣️ Roadmap & Status

We are building in public! Here is our current progress:

### ✅ Phase 1: The Foundation (Current Status)
- [x] **Core Engine**: Reactive Gateway integrated with **DeepSeek V3 API**.
- [x] **Agent Sidecar**: Standalone process for autonomous tasks.
- [x] **Real-time Perception**: Integration with Binance API to fetch real-time BTC price & 24h change.
- [x] **Distributed Lock**: Implemented `Redisson` lock to ensure task uniqueness across multiple pods (Cost-saving & Anti-spam).
- [x] **Infrastructure**: Docker Compose setup for Redis and PostgreSQL.

### 🚧 Phase 2: Intelligence & Flexibility (Next Steps)
- [ ] **Dynamic Configuration**: Move API Keys from `application.yml` to PostgreSQL for hot-reloading.
- [ ] **Notification System**: Integration with Email/Telegram for agent alerts.
- [ ] **Function Calling (Skills)**: Empower agents to actively call tools (e.g., `audit_contract`, `check_whale`).

### 🔮 Phase 3: Enterprise & Ecosystem (Future)
- [ ] **RAG Integration**: Vector Database (pgvector) support for long-term memory.
- [ ] **Halo Operator**: Kubernetes Operator for auto-managing agent lifecycles.
- [ ] **Dashboard**: A visual UI for prompt engineering and monitoring (SaaS/Self-hosted).

## 🚀 Quick Start

### Prerequisites
* JDK 17+
* Maven 3.8+
* Docker & Docker Compose

### 1. Start Infrastructure
```bash
# Starts Redis (for locks) and PostgreSQL (for config)
docker-compose up -d
```

### 2. Configure Environment
```bash
# Set your API Key (Support DeepSeek/OpenAI)
export SPRING_AI_OPENAI_API_KEY=sk-your-key
export SPRING_AI_OPENAI_BASE_URL=https://api.deepseek.com
```

### 3. Run the System
```bash
# Terminal A: Start the Gateway (The Brain)
mvn -pl halo-gateway-engine spring-boot:run

# Terminal B: Start the Sidecar (The Agent)
mvn -pl halo-ghost-sidecar spring-boot:run
```

You will see the Sidecar automatically acquiring the Redis lock and analyzing market data!

🤝 Contributing
Contributions are welcome! Please submit Pull Requests or open Issues.

📄 License
Halo Cloud is Open Source software released under the Apache 2.0 license.


---

### 2. 🛠️ Git 操作建议 (合并到主分支)

如果你现在是在开发分支（比如 `dev` 或 `feature/xxx`），建议按照以下流程合并，保持记录干净：

```bash
# 1. 确保在项目根目录，并把刚才的 README.md 提交
git add README.md
git commit -m "docs: add project roadmap and architecture overview"

# 2. 切换到主分支 (main 或 master)
git checkout main

# 3. 合并你的开发分支
git merge dev  # 假设你之前的分支叫 dev

# 4. 推送到 GitHub
git push origin main
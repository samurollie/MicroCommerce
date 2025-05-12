# MicroCommerce

## Requisitos do sistema:

- Docker
- Node.js versão 22
- Yarn

## Como executar o sistema

1. Inicie os microserviços, executando o docker:

```bash
docker compose up --build -d
```
Isso fará com que os microserviços rodem em http://localhost:8080 (API Gateway)

2. Entre na pasta do front end:

```bash
cd front
```

3. Instale as dependências

```bash
yarn
```

4. Execute o front-end

```bash
yarn dev
```

O sistema estará rodando em [http://localhost:3000](http://localhost:3000).

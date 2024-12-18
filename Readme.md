# Readme da Avaliação de ppd (programação paralela e distribuida) para o Professor Lucas Venezian 


## Funcionalidade

- **Cliente (PhilosopherClient)**: Simula um filósofo que alterna entre o estado de "pensando" e "comendo", solicitando garfos do servidor quando necessário e liberando-os após terminar a refeição.
- **Servidor (Server)**: Gerencia a alocação de garfos entre os filósofos e mantém um registro em tempo real sobre o número de pensamentos e refeições de cada filósofo. O servidor também gerencia os pedidos de garfos, concedendo ou negando conforme a disponibilidade.

## Estrutura

### 1. **Filósofo (Client)**
O cliente simula um filósofo, com um nome atribuído pelo usuário. Ele executa um ciclo onde pensa por um tempo aleatório, solicita os garfos ao servidor, come por um tempo fixo e libera os garfos após comer.

- **Comandos Enviados para o Servidor**:
  - **REGISTER**: Registra um novo filósofo no servidor.
  - **THINK**: Simula o tempo de pensamento do filósofo.
  - **REQUEST_FORK**: Solicita os garfos (esquerdo e direito).
  - **EAT**: Simula o tempo de alimentação do filósofo.
  - **RELEASE_FORK**: Libera os garfos após o uso.

### 2. **Servidor (Server)**
O servidor gerencia os filósofos, controla a disponibilidade dos garfos e registra suas atividades, incluindo o número de vezes que pensaram e comeram.

- **Comandos Recebidos do Cliente**:
  - **REGISTER**: Registra o filósofo com um nome.
  - **REQUEST_FORK**: Tenta alocar os garfos solicitados.
  - **RELEASE_FORK**: Libera os garfos após o uso.
  - **THINK**: Registra o evento de pensamento do filósofo.
  - **EAT**: Registra o evento de alimentação do filósofo.

## Requisitos

- Java ou Java extension pack para Vs code

## Como Executar

### 1. Iniciar o Servidor

Rode o Arquivo Server.java

O servidor irá iniciar na porta `12345` e ficará aguardando conexões de clientes.

### 2. Iniciar o Cliente

No terminal, abra outro terminal e navegue até o diretório onde o código do cliente está localizado. Em seguida rode o arquivo PhilosopherClient.java

O cliente irá solicitar o nome do filósofo e, em seguida, se conectará ao servidor. Cada cliente pode ser executado múltiplas vezes, representando múltiplos filósofos.

### 3. Interações

- O servidor registra todas as ações dos filósofos em tempo real no console, incluindo o número de vezes que cada filósofo pensou e comeu.
- O cliente simula um filósofo que alterna entre pensar e comer. O tempo de pensamento é aleatório, enquanto o tempo de alimentação é fixo.

## Exemplo de Saída

**Servidor**:
```
Servidor iniciado na porta 12345
filosofo 1 (name = Gabriel) Registrado.
filosofo 1 (name = Gabriel) Pensando. Vezes que pensou: 1
filosofo 1 (name = Gabriel) Comendo. Vezes que comeu: 1
...
```

**Cliente**:
```
Digite o nome do filósofo:
Gabriel
Filósofo Gabriel registrado com ID 1.
Filósofo Gabriel pensando por 4673ms
Filósofo Gabriel solicitando garfos...
Filósofo Gabriel pegou os garfos.
Filósofo Gabriel comendo por 2000ms
Filósofo Gabriel liberando os garfos...
Filósofo Gabriel liberou os garfos.
```

## Arquitetura

1. **PhilosopherClient**: Classe que representa o cliente filósofo. Ela faz a comunicação com o servidor, enviando comandos para registrar o filósofo, solicitar e liberar garfos, e registrar ações como "pensar" e "comer".
2. **Server**: Classe que representa o servidor, responsável por gerenciar os filósofos e a disponibilidade dos garfos. O servidor mantém um log com as ações dos filósofos e garante que apenas um filósofo por vez possa utilizar os garfos.
3. **Fork**: Classe que representa os garfos, com métodos para verificar disponibilidade e marcar um garfo como "em uso".
4. **Philosopher**: Classe que representa o filósofo, mantendo seu ID, nome e contadores para o número de pensamentos e refeições.


# Protocolo de Comunicação para o Problema do Jantar dos Filósofos Distribuído

Este documento descreve o protocolo de comunicação entre o servidor centralizado e os clientes filósofos.

## Fluxo de Comunicação
### 1. Registro do Filósofo
- **Cliente envia:** `REGISTER <nome_do_filosofo>`
  - O cliente solicita ao servidor o registro de um filósofo com o nome especificado.
- **Servidor responde:** `OK <id_filosofo>` ou `ERROR <motivo>`
  - O servidor retorna um ID único para o filósofo ou uma mensagem de erro.

### 2. Pensar
- **Cliente envia:** `THINK <id_filosofo>`
  - O cliente informa ao servidor que o filósofo começou a pensar.
- **Servidor responde:** `THINK_LOGGED`
  - O servidor confirma que registrou a ação de pensar.

### 3. Solicitação de Garfos
- **Cliente envia:** `REQUEST_FORK <garfo_esquerdo>,<garfo_direito>`
  - O cliente solicita ao servidor os dois garfos necessários para comer.
- **Servidor responde:** `GRANTED_FORK` ou `DENIED_FORK`
  - O servidor concede os garfos se eles estiverem disponíveis ou nega se não estiverem.

### 4. Comer
- **Cliente envia:** `EAT <id_filosofo>`
  - O cliente informa ao servidor que o filósofo começou a comer.
- **Servidor responde:** `EAT_LOGGED`
  - O servidor confirma que registrou a ação de comer.

### 5. Liberar Garfos
- **Cliente envia:** `RELEASE_FORK <garfo_esquerdo>,<garfo_direito>`
  - O cliente informa ao servidor que o filósofo terminou de comer e liberou os garfos.
- **Servidor responde:** `FORK_RELEASED`
  - O servidor confirma que os garfos foram liberados.

### 6. Erros
- O servidor pode enviar mensagens de erro no formato: `ERROR <motivo>`

## Exemplos de Comunicação
#### Registro
- Cliente: `REGISTER <nome>`
- Servidor: `OK`

#### Pensar
- Cliente: `THINK`
- Servidor: `THINK_LOGGED`

#### Solicitação de Garfos
- Cliente: `REQUEST_FORK`
- Servidor: `GRANTED_FORK`

#### Comer
- Cliente: `EAT`
- Servidor: `EAT_LOGGED`

#### Liberar Garfos
- Cliente: `RELEASE_FORK`
- Servidor: `FORK_RELEASED`

## Notas
1. O ID do filósofo é atribuído pelo servidor no momento do registro.
2. O cliente deve sempre usar o ID retornado para todas as mensagens subsequentes.
3. O protocolo serve para garantir a sincronização e evitar condições de corrida ou deadlock no uso dos garfos.



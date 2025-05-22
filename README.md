# 🔲 Sistema QMove



## 👥 Integrantes

- 👩‍💻 Hellen Marinho Cordeiro - RM: 558841
- 👩‍💻 Heloisa Alves de Mesquita - RM: 559145 
## 🌐 Visão Geral

O projeto QMove foi desenvolvido para a empresa Mottu, com o objetivo de otimizar a gestão do pátio, auxiliando na organização, identificação e localização das motos de forma tecnológica, ágil e eficiente.

A solução automatiza o controle de motos no pátio, utilizando uma arquitetura que integra backend em Java (Spring Boot), um aplicativo móvel e tecnologia de IoT via QR Code.

🧩 Componentes da Solução:

🔗 API Backend (Spring Boot) - Gerencia o cadastro e movimentação de motos e setores, implementando:

- Cadastro de motos com um QR Code único para cada uma.
  
- Registro de movimentações e localização das motos.

📱 Aplicativo Móvel - Ferramenta usada pelos funcionários para:

- Escanear QR Codes das motos com a câmera do celular.

- Consultar informações da moto, como localização atual (setor).

- Atualizar o setor da moto em caso de movimentação no pátio.
  
📸 QR Codes únicos por moto

- Gerados ao cadastrar a moto.

- Fixados fisicamente nas motos para escaneamento rápido.

- Facilitam o rastreamento, movimentação e atualização no sistema
  
🔌 Na integração com IoT, a leitura dos QR Codes representa essa  camada do projeto, conectando dispositivos físicos (smartphones e etiquetas) com o ambiente digital da aplicação.

## 🚀 Instruções para Execução

1º Rodar a aplicação :

    - No terminal, dentro da pasta do projeto rode: mvn spring-boot:run 
    - Ou execute diretamente pela interface utilizada

2º Acesse a API pelo postman, insomnia, entre outros.

3º Utilize a URL: http://localhost:8080/

## 📬 Exemplos de Requisições para a API

**Criação de Setor (POST):**

- POST: http://localhost:8080/setor
- Content-Type: application/json

    {
    "nome": "Manutenção",
    "codigo": "Cor Laranja"
    }

**Criação de QRCode (POST):**

- POST: http://localhost:8080/qrcode
- Content-Type: application/json
```json
    {
    "valor": "MOTO 01",
    "tipo": "Ativo"
    }
```
**Criação de Moto (POST):**

- POST: http://localhost:8080/motos
- Content-Type: application/json
```json
{
  "placa": "XYZ1234",
  "modelo": "Mottu Sport",
  "status": "Disponível",
  "qrcode": {
    "id": 1
  },
  "setor": {
    "id": 1
  }
}
```
**Modificar informações da Moto (PUT):**

- PUT: http://localhost:8080/motos
- Content-Type: application/json
```json
{
  "placa": "XYZ1234",
  "modelo": "Mottu Sport",
  "status": "Pendências",
  "qrcode": {
    "id": 1
  },
  "setor": {
    "id": 2
  }
}
```

**Verificar os alertas (GET):**

- GET: http://localhost:8080/alertas
- Content-Type: application/json
- Ele retornará as mudanças do status da moto


**Cadastro de Funcionário (POST):**

- POST: http://localhost:8080/funcionarios
- Content-Type: application/json
```json
{
  "nome": "Marcelo Cruz",
  "email": "marcel@gmail.com",
  "senha": "marcelo102"
}
```
**Para deletar um cadastro de Funcionarios (DELETE):**

- DELETE: http://localhost:8080/funcionarios/
- Content-Type: application/json

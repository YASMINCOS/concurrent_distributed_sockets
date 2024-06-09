### Descrição do Projeto
Este projeto implementa um servidor de biblioteca utilizando sockets em Java 17. O servidor gerencia um registro/cadastro de livros, permitindo as seguintes funcionalidades:

- Listagem dos livros disponíveis.
- Aluguel e devolução de livros.
- Cadastro de novos livros.
Os dados dos livros são armazenados em um arquivo JSON, que serve como a "base de dados" da biblioteca. As operações realizadas pelo usuário (como cadastro e aluguel) são refletidas diretamente nesse arquivo.

### Estrutura do Projeto
O projeto está dividido nas seguintes partes:

1. LivroCRUD
Esta classe é responsável por realizar as operações de CRUD (Create, Read, Update, Delete) sobre os livros. As operações são realizadas diretamente no arquivo JSON.

2. Cliente e Servidor
Implementação do cliente e do servidor utilizando sockets. O servidor gerencia as solicitações recebidas dos clientes, utilizando os métodos fornecidos pela classe LivroCRUD.

3. Model
Contém a classe Livro, que representa os livros da biblioteca. Os atributos mínimos da classe Livro são:

- Autor
- Nome
- Gênero
- Número de exemplares
4. JSON
Armazena o arquivo JSON livros.json, que contém a lista de livros da biblioteca.

5. Classe Conexao
Gerencia a conexão entre o cliente e o servidor, facilitando a comunicação entre eles.

### Uso do ObjectMapper
Nesse projeto, fizemos o uso do ObjectMapper da biblioteca Jackson que é utilizado para serializar e desserializar objetos Java para JSON e vice-versa. Este é um passo fundamental, pois permite a leitura e escrita de dados no arquivo JSON de maneira simples e eficiente. Abaixo estão algumas razões para o uso do ObjectMapper:

- Facilidade de Uso: Converte facilmente objetos Java para JSON e JSON para objetos Java.
- Flexibilidade: Suporta uma ampla gama de tipos de dados, incluindo coleções e mapas.
- Configuração: Permite configuração personalizada para adequar a serialização/deserialização às necessidades específicas do projeto.


## Como Executar o Projeto
- Clone o repositório.
- Compile o código fonte utilizando uma IDE ou via linha de comando.
- Execute a classe servidor para iniciar o servidor.
- Utilize um cliente para se conectar ao servidor e realizar operações de listagem, aluguel, devolução e cadastro de livros.

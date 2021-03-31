
# TODO LIST API

repositório para projetos pessoais desenvolvidos em Java

## FUNCIONALIDADES  DESENVOLVIDAS NESTE PROJETO:

Projeto: TODO-LIST API (API para armazenamento/leitura de tarefas) 

### - API RESTFUL simples ( em linguagem JAVA ) que possui funcionalidades como: 

  => cria tarefas com nome e situação da tarefa: { name: "nome da tarefa", status: "pending ou completed" } (POST /todo )
  => lista todas as tarefas e e seus status: (GET /todo )
  => lista tarefas filtrando por nome ou situação: (GET /todo?name="nome da tarefa" ) (GET /todo?status="pending ou completed" )
  => lista uma tarefa especifica pelo seu ID - ex: (GET /todo/1 )
  => deleta uma tarefa especifica através do ID se esta existir no banco de dados  - ex: (DELETE /todo/1 )
  => atualiza uma tarefa existente no banco de dados através do ID - ex:{ name: "nome editado", status: "pending ou completed" } (PUT /todo/1 )
 

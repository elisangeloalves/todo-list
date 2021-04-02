
# TODO LIST API RESTFUL - em linguagem JAVA
  - (CRUD - API para armazenamento / leitura de tarefas)

repositório para projetos pessoais desenvolvidos em Java

---
## FUNCIONALIDADES  DESENVOLVIDAS NESTE PROJETO:


  - cria tarefas com nome e situação (opcional) da tarefa: => (POST /todo )
    - ex: { name: "nome da tarefa", status: "completed" }
		
  - lista todas as tarefas e e seus status: => (GET /todo )
	
  - lista tarefas filtrando por nome ou situação: 
    - ex: (GET /todo?name=nome da tarefa ) || (GET /todo?status=pending)
		
  - lista uma tarefa especifica pelo seu ID
    - ex: (GET /todo/1 )
	
  - deleta uma tarefa especifica através do ID se esta existir no banco de dados
    - ex: (DELETE /todo/2 )
	
  - atualiza uma tarefa existente no banco de dados através do ID => (PUT /todo/1) 
    - ex: { name: "nome editado", status: "completed" } 
 
---
## - EXTRA - Rotas de monitoramento da API RESTFUL: 

  - Rota para validar o funcionamento dos componentes da API Spring Actuator
    - (GET /healthcheck)
		
  - Rota para gerenciamento de metricas com suporte a outros sistemas de monitoramento via Spring Actuator / MicroMeter
    - (GET /metrics)
	
  - Rota para indicadores de performance da API via Prometheus
    - (GET /prometheus)
    - ex: (GET /todo?name=nome da tarefa ) || (GET /todo?status=pending)

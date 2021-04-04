
# 						TODO-LIST API RESTFUL - em linguagem JAVA
##				 		(Creat Read Update Delete - CRUD API RESTFULL)

Este projeto pode ser visualizado e executado em um computador local. Para isso o computador precisa ter instalados previamente os seguintes programas: 
		
		- Docker, Docker Compose e um gerenciador de requisições HTTP ex: Postman, Insomia, ou algo do tipo.

Com estes requisitos basicos atendidos, siga as orientações a abaixo:

- crie um diretório em um local de sua preferencia no seu computador;
- na pagina principal deste projeto no site do gitHub você deve conseguir localizar um botão verde escrito "Code";
- clique nesse botão verde e selecione na janela pop-up que abrir a opção que melhor te servir e copie o link que estiver dentro da janela.

####	- ex: SSH ->  git@github.com:preto115/Projetos-Java.git

- volte á janela do seu diretorio onde deseja clonar  o projeto e digite 
- o seguinte "git clone " e cole logo após o link que vc copiou do projeto, e tecle ENTER:
			
####		localhost:$  git  clone  git@github.com:preto115/todo-list.git
			
- após o git concluir a clonagem, navegue para dentro da pasta clonada:
	
####		localhost:$ cd todo-List   		

-  e em seguida digite o seguinte comando:
		
####		localhost:$  docker-compose  up  -d
			
- pronto agora é so aguardar os containers serem criados. Apos o terminal 
- liberar o cursor, e verifcar se os container estão rodando com o seguinte comando:
		
####		localhost:$  docker container  ps  -a
			
- deverá ter 3 container parecidos como abaixo, sendo 2 com stasus "UP"  (todo-backend e mysql)
- e um outro com status "Restarting" (prometheus) como no print;

![image](https://user-images.githubusercontent.com/64562701/113464929-c02fba00-9406-11eb-8405-abf091c9eaba.png)

		 
Agora basta abrir um programa de monitoramento de requisições HTTP -(Postman ou Insomia) e acessar os endPoint relacionados abaixo.

NOTE: é possivel que mesmo estando como status UP, após tentar acessar os endpoints, deve ter algum delay de acesso devido ao processamento do seu computador das novas informaçoes da API.



## FUNCIONALIDADES  DESENVOLVIDAS NESTE PROJETO:


  - cria tarefas com nome e situação (opcional) da tarefa: -- (POST /todo )
    	-- ex: { name: "nome da tarefa", status: "completed" }
		
  - lista todas as tarefas e e seus status: -- (GET /todo )
	
  - lista tarefas filtrando por nome ou situação: 
    	-- ex: (GET /todo?name="nome da tarefa" ) 
	-- ex: (GET /todo?status=pending)
  
  - lista uma tarefa especifica pelo seu ID
    	-- ex: (GET /todo/1 )
	
  - deleta uma tarefa especifica através do ID se esta existir no banco de dados
    	-- ex: (DELETE /todo/2 )
	
  - atualiza uma tarefa existente no banco de dados através do ID 
  	-- ex: { name: "nome editado", status: "completed" } (PUT /todo/1) 

---
## - EXTRA - Rotas de monitoramento da API RESTFUL: 

  - Rota para validar o funcionamento dos componentes da API via Spring Actuator
    	-- (GET /healthcheck)
		
  - Rota para gerenciamento de métricas com suporte a outros sistemas de monitoramento via Spring Actuator / MicroMeter
    - (GET /metrics)
	
  - Rota para indicadores de performance da API via Prometheus
    - (GET /prometheus)

 - Rota para status da API via Spring Actuator
    - (GET /info)

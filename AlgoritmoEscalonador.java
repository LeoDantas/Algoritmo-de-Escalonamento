import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.io.FileReader;


public class AlgoritmosEscalonador{

	public static void main(String args[]){
		
		
	try{
			String arquivo = "/home/maheus/Área de Trabalho/Leo/t1.txt";
	        BufferedReader ler = new BufferedReader(new FileReader(arquivo));
	        Queue<Job> filaPronto = new LinkedList<Job>();	         
	        String line;
	        String[] arr = new String[2];
	        while ((line = ler.readLine()) != null) {
	        	Job tmp = new Job();
	        	arr = line.split(" ");
	        	int aux;
	        	aux = Integer.parseInt(arr[0]);
	        	tmp.setTempoChegada(aux);
				tmp.setTempoEntrada(aux);        	
	        	aux = Integer.parseInt(arr[1]);
				tmp.setDuracaoProcesso(aux);
				filaPronto.add(tmp);
	        	
	        }

	        ler.close();
		FCFS(new LinkedList<Job>(filaPronto));
		SJF(new LinkedList<Job>(filaPronto));
		RR(new LinkedList<Job>(filaPronto),2);
		

	  	}catch (IOException ex) {
	        System.err.print(ex.getMessage());
	    }catch (Exception e) {
	        System.err.print(e.getMessage());
	    }
	}    



	public static void FCFS(Queue<Job> filaPronto){
		int tempoSis = 0; //Variavel que armazena o tempo do sistema
		float tRetornoMedio = 0; 
		float tRespostaMedio = 0;
		float tEsperaMedio = 0;
		int tam = filaPronto.size();
		Job a = new Job();

		//O escalonador vai executar até não existir ninguem na fila de pronto
		while(filaPronto.peek() != null){

			/*
			Teste feito para checar se o tempo do sistema é maior ou igual do tempo de chegada
			do processo. Caso negativo, atualizamos o tempo do sistema
			*/
			if(tempoSis >= filaPronto.peek().getTempoChegada()){
				a = filaPronto.poll();

				tRespostaMedio += tempoSis - a.getTempoChegada();
				tEsperaMedio += tempoSis - a.getTempoChegada();

				tempoSis += a.getDuracaoProcesso();
				tRetornoMedio += tempoSis - a.getTempoChegada();
			
			}else{
				/*
				caso nenhum processo chegue para ser escalonado é feita a passagem do tempo
				do sistema
				*/
				tempoSis = filaPronto.peek().getTempoChegada();
			}


		}
		tRetornoMedio = (tRetornoMedio/tam);
		tRespostaMedio = (tRespostaMedio/tam);
		tEsperaMedio = (tEsperaMedio/tam); 

		System.out.printf("FCFS %.1f %.1f %.1f\n",tRetornoMedio,tRespostaMedio,tEsperaMedio);


	}



	public static void SJF(Queue<Job> filaPronto){
		int tempoSis = 0;
		float tRetornoMedio = 0;
		float tRespostaMedio = 0;
		float tEsperaMedio = 0;
		int tam = filaPronto.size();
		Job a = new Job();
		PriorityQueue<Job> filapri = new PriorityQueue<Job>();//fila de prioridade

		//O escalonador vai executar até não existir ninguem na fila de pronto ou na fila de prioridade
		while(filaPronto.peek() != null || filapri.peek() != null){
			/*
			Teste feito para checar se o tempo do sistema é maior ou igual do tempo de chegada
			do processo, retiramos esses processos da fila de pronto de colocamos na fila de
			 prioridades . 
			*/
			while(filaPronto.peek() != null && tempoSis >= filaPronto.peek().getTempoChegada()){
					filapri.add(filaPronto.poll());
			}

			//Verifica se existe algum processo na fila SJF
			if(filapri != null){
				a = filapri.poll();//simula o processo sair da fila

				tRespostaMedio += tempoSis - a.getTempoChegada();
				tEsperaMedio += tempoSis - a.getTempoChegada();

				tempoSis += a.getDuracaoProcesso();
				tRetornoMedio += tempoSis - a.getTempoChegada();
				
			}else{
				/*
				caso nenhum processo chegue para ser escalonado é feita a passagem do tempo
				do sistema
				*/
				tempoSis = filaPronto.peek().getTempoChegada();
			}
		}
		tRetornoMedio = (tRetornoMedio/tam);
		tRespostaMedio = (tRespostaMedio/tam);
		tEsperaMedio = (tEsperaMedio/tam); 

		
		System.out.printf("SJF %.1f %.1f %.1f\n",tRetornoMedio,tRespostaMedio,tEsperaMedio);


	}
 	
	public static void RR(Queue<Job> filaPronto, int Round){
		int tempoSis = 0;
		float tRetornoMedio = 0;
		float tRespostaMedio = 0;
		float tEsperaMedio = 0;
		int tam = filaPronto.size();
		Job a = new Job();

		Queue<Job> filaAux = new LinkedList<Job>();//fila auxiliar
		
			//O escalonador vai executar até não existir ninguem na fila de pronto ou na fila auxiliar
			while(filaPronto.peek() != null || filaAux.peek() != null){
				/*
				Teste feito para checar se o tempo do sistema é maior ou igual do tempo de chegada
				do processo, retiramos esses processos da fila de pronto de colocamos na fila auxliar
				*/
				while(filaPronto.peek() != null && tempoSis >= filaPronto.peek().getTempoChegada()){
					filaAux.add(filaPronto.poll());
				}
				
				//Verifica se existe algum processo na fila aux
				if(filaAux.peek() != null){
					
					a = filaAux.poll(); 
					tEsperaMedio += tempoSis - a.getTempoEntrada();
					
					//verifica se é a primeira entrada do processo
					if(a.getPrimeiraEntrada()){
						tRespostaMedio += tempoSis - a.getTempoChegada();
						a.setPrimeiraEntrada(false);	
					}
					
					//Verifica se o tempo restante do processo é menor do que o Round(2)
					if(a.getDuracaoProcesso() < Round){
						
						a.setDuracaoProcesso(0);
					}
					else{
						
						tempoSis += Round;				
						a.setDuracaoProcesso(a.getDuracaoProcesso() - Round);
					}

					while(filaPronto.peek() != null && tempoSis >= filaPronto.peek().getTempoChegada()){
					filaAux.add(filaPronto.poll());
					}					
					
					//Verifica se o processo ja terminou ou se deve ser inserido denovo na fila aux
					if(a.getDuracaoProcesso() > 0){
						a.setTempoEntrada(tempoSis);
						filaAux.add(a);
					}
					else{
						
						tRetornoMedio += tempoSis - a.getTempoChegada();
					}
				}
				else{
					
					tempoSis = filaPronto.peek().getTempoChegada();
				}

			}
			tRetornoMedio = (tRetornoMedio / tam);
			tRespostaMedio = (tRespostaMedio / tam);
			tEsperaMedio = (tEsperaMedio / tam);

			System.out.printf("RR %.1f %.1f %.1f\n",tRetornoMedio,tRespostaMedio ,tEsperaMedio );
		}

	}

	




		







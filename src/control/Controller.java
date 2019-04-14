/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import minimax.NodeTree;
import minimax.TreeMiniMax;
import view.ViewVelha;

/**
 *
 * @author Rodrigo Maia
 */
public class Controller {
    
    private ViewVelha vv;;
    private TreeMiniMax tMiniMax;
    private NodeTree lastAcess;
    private int firstPlayerMatch;
    
    /**Construtor*/
    public Controller() {
         vv = new ViewVelha(this); 
         lastAcess = null;
         startPlay();
    }  
    
    /**Método que inicia a primeira partida definindo quem será o jogador que começa
     * se for a maquina instancia uma árvore de jogo chama o IAplay()*/
    private void startPlay(){        
        firstPlayerMatch = (int)(Math.random() * 2);//Definindo quem começa 
        if(firstPlayerMatch==0){//Se for a maquina cria a árvore de jogo a partir do estado vazio
            tMiniMax = new TreeMiniMax(vv.getStatusTab(), 'O', 'X', true, 10);
            IAplay();//Chamando IA para jogar
        }
    }
    
    /*Inicia uma nova partida limpado o lastAcess, definindo o jogador a iniciar, 
     *se necessário destrói a árvore e cria uma nova(opcional)*/    
    public void restartPlay() {
        int newFPM = (int)(Math.random() * 2);//Defindo quem começa
        lastAcess = null;//Limpa ultimo acesso a árvore
        if(firstPlayerMatch != newFPM || newFPM != 0){//Se maquina não for quem começa
            //ou começa mas a partida anterior não foi ela que começou
            tMiniMax.destroy();//Destroi a arvore 
            tMiniMax = null;
            
            if(newFPM == 0)//Caso ela começe cria uma nova árvore*/
               tMiniMax = new TreeMiniMax(vv.getStatusTab(), 'O', 'X', true, 10);
        }          

        firstPlayerMatch = newFPM;
        if(firstPlayerMatch == 0) IAplay();//Se for a maquina que começa chama a sua jogada
    }
    
    /**Método responsavel pela jogada da maquina*/
    public void IAplay(){
        vv.stopCaptMatch();//Interronpendo captação dos botões
        int proxJog = -1;
        char [] stTab = vv.getStatusTab();//Recuperando estado do tabuleiro
        if(tMiniMax == null){//Se oponente foi primeiro e essa é a primeira jogada da maquina 
            tMiniMax = new TreeMiniMax(vv.getStatusTab(), 'O', 'X', true, 9);//Constroi árvore
        }
        lastAcess = tMiniMax.searchInMinimax(lastAcess, stTab);//Definindo estado ideal para maquina e retornando nó que o contem
        vv.machineFills(lastAcess.getMovCoord());//Recuperando proximo movimento ideal
        isFinalStatus();
    }
    
    /*Método que retorna se o jogo acabou ou não para o controller*/
    public boolean isFinalStatus(){
       char [] stTab = vv.getStatusTab(); 
       return vv.reportStateMatch(TreeMiniMax.isFinal(stTab, 'X', 'O'));       
    }

    
}

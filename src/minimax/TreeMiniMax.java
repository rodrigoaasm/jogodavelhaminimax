/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Rodrigo Maia
 */
public class TreeMiniMax {
    private NodeTree noRaiz;
    private char afavor;
    private char contraa;
    
    //Contrutor da classe
    public TreeMiniMax(char [] inicio,char afavor, char contraa, boolean proxJogMax, int prof) {
        noRaiz = new NodeTree(inicio.clone(),-1,proxJogMax);//Instancia nó raiz da árvore
        this.contraa = contraa;//Definindo caracteres dos jogadores
        this.afavor = afavor;
        assitBuildTree(noRaiz ,!proxJogMax,prof-1);//Gerando árvore apartir do primeiro nó.
    } 
    
    /*Método que recursivamente gera os possíveis estados do jogo e instancia os 
     *nós a qual ele deve ser carregado e chama o metodo de avaliação que o pontua
     *e propagada na volta da recursão a pontuação*/
    private void assitBuildTree(NodeTree nt, boolean proxJogMax, int prof){                
        char [] newStatus;
        char [] temp;
        
        int m = nt.isMax()? -1 : 1;//Se for nó da camada max, m é iniciado com -1,
        //se for da camada min inicia com 1
        
        if(prof>0){ //Delimitando profudidade da árvore
            temp = nt.getTabu(); //Pegando estado de jogo do nó atual;
            for(int i=0;i<9;i++){// Laço para gera as possibilidades de jogo
                if(temp[i]=='\0'){//Se achar uma casa vazia 
                    newStatus = temp.clone();//Copia estado do nó pai
                   
                    if(proxJogMax) newStatus[i]= contraa; //Preechendo casa com X ou O
                    else newStatus[i]= afavor;
                    NodeTree tnt = new NodeTree(newStatus,i,proxJogMax);//Instanciando nó com a nova possibilidade 
                    nt.addNodeProx(tnt);//Adicionando nó como filho do atual               
                    
                    int stts = isFinal(newStatus);//Verificando se o estado é terminal
                    if(stts == 0)  assitBuildTree(tnt, !proxJogMax,prof-1);
                    else tnt.setPoint(stts);//Se não for terminal faz uma chamada recursiva para completar a 
                    //ramificação, se for terminal pontua ele com o valor adquirido atraves do metodo isFinal()
                    
                    //Na volta da recursão propaga a pontuação dos nós
                    //Na camada Max defini m com o maior valor de pontuação entre seus filhos
                    if(tnt.isMax() && m > tnt.getPoint())  m = tnt.getPoint();
                    //Na camada Min defini m com o menor valor de pontuação entre seus filhos
                    else if(!tnt.isMax() && m < tnt.getPoint()) m = tnt.getPoint();
                }
            }   
            nt.setPoint(m);//Definindo pontuação
        }        
        
    }
    
    /**Método que notifica se o estado argumentado é final e retorna sua pontuação */
    public int isFinal(char [] tabu){
        return isFinal(tabu, afavor, contraa);
    }
    
    /**Método que notifica se o estado argumentado é final e retorna sua pontuação 
     *conforme os caracteres argumetados*/
    public static int isFinal(char [] tabu, char afavor, char contraa){
        char chWin = '\0';
        
        if(tabu[0]==tabu[4] && tabu[4]==tabu[8]) chWin = tabu[0];//Verificando as diagonais 
        else if(tabu[6]==tabu[4] && tabu[4]==tabu[2]) chWin = tabu[6];
              
        for(int i = 0;tabu.length>i && chWin==0 ;i+=3){//Verificando as linhas
           if(tabu[i]==tabu[i+1] && tabu[i+1]==tabu[i+2]) chWin = tabu[i];
        }
        
        for(int i = 0;3>i && chWin==0;i++){//Verificando as colunas
           if(tabu[i]==tabu[i+3] && tabu[i+3]==tabu[i+6]) chWin = tabu[i];         
        }        
        
        if(chWin == afavor)    return 1;//Analisando de quem é a vitoria, se for do caractere afavor volta 1
        else if(chWin == contraa)   return -1;       
        return 0;//Caso de empate volta 0
    }
    
    
    /*Método que retorna à representação textual da árvore.*/
    @Override
    public String toString(){
        String text = super.toString()+ "\n< #"; //Iniciando String com os valores do nó raiz
        if(noRaiz.isMax()) text += "[MAX]";
        else text += "[MIN]";
        text += "M("+noRaiz.getMovCoord()+ ")" + " P["+ noRaiz.getPoint() + "] E:" + Arrays.toString(noRaiz.getTabu())+ " ";
        return this.assistToString(noRaiz,text); //Chamando metodo que realiza a contrução da String para o resto da árvore
    }
    
    /**Auxilia na contrução da String que representa textualmente a estrutura da árvore **/
    private String assistToString(NodeTree node, String text){
        ArrayList<NodeTree> listNode;
        int i =0;        
     
        while((listNode = node.getListProx()).size()>0){ //Busca por listas de nós filhos não vazias.            
            text += " < ";
            NodeTree ntw = listNode.remove(0); //Varrendo a lista de nós filhos
            if(ntw.isMax()) text += "[MAX]"; //Criando texto do nó analisado atualmente
            else text += "[MIN]";
            text += "M("+ntw.getMovCoord()+ ")" + " P["+ ntw.getPoint() + "] E:" + Arrays.toString(ntw.getTabu())+ " ";
            text+= " > ";
            i++;
        } 
        text+= " > ";
        return text;//Retorna texto
    }
    
    /*Metodo que realiza a busca do melhor caminho a ser feito*/
    public NodeTree searchInMinimax(NodeTree nt, char [] stTab){
        
        if(nt == null) nt = noRaiz;//Se o nó de entrada for nulo, carrega nt com o no Raiz
        
        if(nt.getListProx().isEmpty()){//Se for nó folha retorna-o
            return nt;
        }else if(nt.isMax()){//Se for nó da camada max, chama diretamente o metodo nextMov
            return nextMov(nt);
        }else if(!nt.isMax()){//Se for nó da camada min, busca o estado argumentado entre os nós filhos do nó argumentado
            for(NodeTree tnt : nt.getListProx()){//Buscado estado  
                //Comparando os estados, se forem iguais
                if(Arrays.toString(stTab).compareTo(Arrays.toString(tnt.getTabu()))==0){
                    return nextMov(tnt);//Chama o metodo nextMov
                } 
            }
        }
        return null;
    }
    
    /*Retorna o nó que possui a melhor jogada*/
    public NodeTree nextMov(NodeTree a){
        NodeTree tempNt = null;                   
            int m = -1;//Iniciando m com -1
            for(NodeTree tnt: a.getListProx()){ //Varrendo nós filhos do argumentado
                if(tnt.getPoint()== 1)//Se encontra um nó com pontuação 1, o retorna.
                    return tnt;
                else if(m < tnt.getPoint()){//procura o nó com maior pontuação
                    m = tnt.getPoint();
                    tempNt = tnt;//Guarda nó com maior pontuação
                }                
            }            
        return tempNt;
    }

    /**Metodos para destruição da árvore*/
    public void destroy() {
        assitDestroy(noRaiz);
        noRaiz = null;
    }

    private void assitDestroy(NodeTree nt){
        for(NodeTree tNt: nt.getListProx()){//Passa por todos os nós os excluido.
            assitDestroy(tNt);
            tNt = null;
        }
    }

}
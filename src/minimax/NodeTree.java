/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;

import java.util.ArrayList;
/**
 *
 * @author Rodrigo Maia
 */
public class NodeTree { 
    private int point;
    private ArrayList<NodeTree> listProx;
    private char tabu[]; 
    private int movCoord;
    private boolean max;

    /*Contrutor da classe*/
    public NodeTree(char [] tabu, int tMoviCoord,boolean mm) {
        this.tabu = tabu;//Armazena estado do tabuleiro
        listProx = new ArrayList<NodeTree>();//Instancia a lista de nós filhos
        point =0;//Inicia pontuação
        movCoord = tMoviCoord; //Guarda movimentação resultante desse estado
        max = mm; //Definindo camada a qual o nó pertece.
    }

    public char [] getTabu() {
        return tabu;
    }  
    
    public void addNodeProx(NodeTree ntw) {
        listProx.add(ntw);        
    }

    public ArrayList<NodeTree> getListProx() {
        return listProx;
    }     

    public void setPoint(int vlr) {
       point=vlr;
    }

    public int getPoint() {
        return point;
    }

    public int getMovCoord() {
        return movCoord;
    }

    boolean isMax() {
        return max;
    }

    
    
}

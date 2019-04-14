/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import control.Controller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Rodrigo Maia
 */
public class ViewVelha extends JFrame implements ActionListener{


    private JPanel prinPanel;
    private JButton [] casas;
    private Controller ctr;
    private boolean captMovi;
            
    public ViewVelha(Controller c){
        super();        
        ctr = c;//Guardando ligação com o controle do software
        //Definindo propriedades da janela
        this.setTitle("Jogo da velha");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       
        //Definindo conteudo da janela
        prinPanel = new JPanel();
        prinPanel.setPreferredSize(new Dimension(309, 309));
        prinPanel.setLayout(new GridLayout(3, 3, 3, 3));
        prinPanel.setBackground(new Color(0, 0, 0));
        
        //Instaciado os botões que serão as casas do jogo
        casas = new JButton[9];        
        for(int i=0;i<9;i++){
           casas[i] = new JButton();
           casas[i].addActionListener(this);//Adicionado gatilho
           casas[i].setActionCommand(String.valueOf(i));//Definindo uma forma de identificar o botão quando pressionado
           casas[i].setFont(new Font("Arial",3,72));//Sefinindo fonte do texto no botão
           prinPanel.add(casas[i]);           
        }
        
        //Mostrando interface na tela
        this.add(prinPanel);        
        this.pack();
        captMovi=true;
        this.setVisible(true); 
    }

    /**Chamado quando alguns dos botões do tabuleiro são pressionados 
     * preenchendo assim um botão que esteja vazio*/
    @Override
    public void actionPerformed(ActionEvent e) {
        if(captMovi){//Se estiver autorizada a captura de botões
            int id = Integer.parseInt(e.getActionCommand());//Recuperando a identificação do botão
            casas[id].setText("X");//Setando X para casa que o usuário escolher
            casas[id].setEnabled(false);//Desabilitando botão ja preechido          
            
            if(!ctr.isFinalStatus()) ctr.IAplay();//Se não acabou a partida chama jogada da IA
        }
    }
    
    /**Método que retorna o estado atual do tabuleiro */
    public char[] getStatusTab() {
        char [] status = new char[9];
        for(int i =0; i < 9; i++){//Varrendo todos os botões            
            if(!casas[i].isEnabled()) {//Passando para o vetor os valores de uma casa já preechida
                status[i] = casas[i].getText().charAt(0);
            } else status[i]='\0';
        }
        return status;
    }
    
    public void stopCaptMatch() {
        captMovi=false;
    }

    /**Metodo que efetua  a jogada da Maquina*/
    public void machineFills(int proxJog) {
        casas[proxJog].setText("O");//Preechendo casa escolhida pela IA
        casas[proxJog].setEnabled(false);
        captMovi=true;//Liberando a captação dos botões
    }

    /**Método que notifica o fim da partida através de mensagens exibidas ao usuário
     * e também retorna se o jogo acabou além de exibir a opção para o usuário iniciar 
     * uma nova partida.*/
    public boolean reportStateMatch(int finish) {
        String msgTxt = "";
        if(finish == 0){ //Se for 0, compara com os botões se todas as opção foram preechidas 
            for(int i = 0;i < 9;i++){
                if(casas[i].isEnabled()) return false;//Se algum botão não foi preechido retorna falso
            }
            msgTxt += "A Partida Empatou!"; //Se todas estão preechidos constroi frase final
        }
        else if(finish == -1){// definindo msgs de vitória ou derrota
           msgTxt += "Voce Perdeu!"; 
        }else if(finish == 1){
           msgTxt += "Voce Ganhou!"; 
        }
        
        JOptionPane.showMessageDialog(this, msgTxt);//Exibi mensagem de status final
        int opt;
        //Pergunta se usuário quer iniciar uma nova partida
        opt = JOptionPane.showConfirmDialog(this, "Deseja Iniciar outro jogo?","Jogo Da Velha",
                                                JOptionPane.YES_NO_OPTION);
        if(opt == JOptionPane.OK_OPTION){//Se sim inicia a nova partida
            clearField();
            ctr.restartPlay();
        }
        return true;
    }

    /**Limpa o tabuleiro*/
    private void clearField() {
        for(int i = 0;i<9;i++){//Varrendo o vetor de botões de forma que remova o preechimento efetuado anteriormente
            casas[i].setText("");
            casas[i].setEnabled(true);
        }
    }
 
}

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

/**
 *
 * @author Gustavo Guerra
 */
public class baconBakerMain{

    //Loja: botoes dos itens, preco e quantidades
    JButton item1, item2, item3, item4;
    JTextArea descricao_item;
    boolean item2Unlocked, item3Unlocked, item4Unlocked;
    int qtd1, qtd2, qtd3, qtd4;
    int preco1, preco2, preco3, preco4;

    //contadores
    JLabel contador_principal;
    JLabel contador_secundario;
    int pontuacao_atual, timerSpeed;
    double pps; //pontos por segundo
    boolean timerOn;

    //fontes utilizadas
    Font font1, font2, font3;

    //botao principal e imagens
    ImageIcon bacon1, bacon2;
    JButton baconButton;

    //handler dos botoes
    clickHandler acao_click = new clickHandler();
    mouseOver acao_hover = new mouseOver();

    //contagem de tempo
    Timer timer_principal;
    Timer timer_relogio;
    JLabel exibe_tempo;
    long game_time_start;
    long game_time_now;
    long elapsed;
    long total_points;
    int seg = 1;
    int min = 60;
    int hor = 3600;
    
    public static void main(String[] args){
        new baconBakerMain();
    }

    public baconBakerMain(){
        inicializa_jogo();//inicializa as variaveis de jogo
        inicializa_fonte();//inicializa as fontes utilizadas
        inicializa_loja();//inicializa as variaveis da loja
        inicializa_interface();//inicializa interface e chama a timeElapsed()
    }

    public void inicializa_jogo(){
        total_points = 0;
        game_time_start = System.currentTimeMillis();
        game_time_now = System.currentTimeMillis();
        elapsed = (long)0;
        timerOn = false;
        pps = 0.0;
        pontuacao_atual = 0;
    }
    
    //As variaveis relacionadas a loja que precisavam
    //ser globais sao inicializadas aqui
    public void inicializa_loja(){

        item2Unlocked = item3Unlocked = item4Unlocked = false;
        qtd1 = qtd2 = qtd3 = qtd4 = 0;
        preco1 = 10;
        preco2 = 100;
        preco3 = 1100;
        preco4 = 5000;

        item1 = new JButton("Frying pan");
        item1.setActionCommand("item1");
        item1.setFont(font3);
        item1.setFocusPainted(false);
        item1.addMouseListener(acao_hover);
        item1.addActionListener(acao_click);

        item2 = new JButton("?");
        item2.setActionCommand("item2");
        item2.setFont(font3);
        item2.setFocusPainted(false);
        item2.addMouseListener(acao_hover);
        item2.addActionListener(acao_click);

        item3 = new JButton("?");
        item3.setActionCommand("item3");
        item3.setFont(font3);
        item3.setFocusPainted(false);
        item3.addMouseListener(acao_hover);
        item3.addActionListener(acao_click);

        item4 = new JButton("?");
        item4.setActionCommand("item4");
        item4.setFont(font3);
        item4.setFocusPainted(false);
        item4.addMouseListener(acao_hover);
        item4.addActionListener(acao_click);
    }

    public void inicializa_fonte(){
        font1 = new Font("Sans Serif", Font.PLAIN, 30);
        font2 = new Font("Sans Serif", Font.PLAIN, 13);
        font3 = new Font("Sans Serif", Font.PLAIN, 18);
    }
    
    //Funcao responsavel por inicializar quase toda a interface do jogo
    //Chama a funcao timeElapsed() para iniciar a variavel timer_relogio
    public void inicializa_interface(){
        
        //estrutura do JFrame
        JFrame frame_principal = new JFrame();
        frame_principal.setSize(750,440);
        frame_principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame_principal.getContentPane().setBackground(Color.orange);
        frame_principal.setLayout(null);

        //painel da imagem principal
        JPanel baconPanel = new JPanel();
        baconPanel.setBounds(0,100,500,275);
        baconPanel.setBackground(Color.orange);
        
        //Imagem e botoes do bacon
        bacon2 = new ImageIcon(getClass().getClassLoader().getResource("bacon1.png"));
        bacon1 = new ImageIcon(getClass().getClassLoader().getResource("bacon2.png"));
        baconButton = new JButton();
        baconButton.setBackground(Color.orange);
        baconButton.setFocusPainted(false);
        baconButton.setBorder(null);
        baconButton.setContentAreaFilled(false);
        baconButton.setIcon(bacon1);
        baconButton.addMouseListener(acao_hover);
        baconButton.addActionListener(acao_click);
        baconButton.setActionCommand("click");
        baconPanel.add(baconButton);
        
        //Painel dos contadores
        JPanel counterPanel = new JPanel();
        counterPanel.setBounds(175, 10, 250, 90);
        counterPanel.setBackground(Color.orange);
        counterPanel.setLayout(new GridLayout(2,1));

        //inicializa o contador principal
        contador_principal = new JLabel(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
        contador_principal.setForeground(Color.black);
        contador_principal.setFont(font1);
        counterPanel.add(contador_principal);

        //Inicializa o contador secundario (pontos/tempo)
        contador_secundario = new JLabel();
        contador_secundario.setForeground(Color.black);
        contador_secundario.setFont(font2);
        String pps_string = String.format("%.1f", pps);
        contador_secundario.setText(pps_string + " points/second");
        counterPanel.add(contador_secundario);

        //Loja
        JPanel itemPanel = new JPanel();
        itemPanel.setBounds(500, 100, 250, 275);
        itemPanel.setBackground(Color.blue);
        itemPanel.setLayout(new GridLayout(4,1));
        itemPanel.add(item1);
        itemPanel.add(item2);
        itemPanel.add(item3);
        itemPanel.add(item4);

        //JPanel da descricao dos itens
        JPanel descricaoPanel = new JPanel();
        descricaoPanel.setBounds(500, 0, 250, 100);
        descricaoPanel.setBackground(Color.black);

        //Area de texto pras descricoes dos itens
        descricao_item = new JTextArea();
        descricao_item.setBounds(505, 0, 240, 100);
        descricao_item.setForeground(Color.white);
        descricao_item.setBackground(Color.black);
        descricao_item.setFont(font2);
        descricao_item.setLineWrap(true);
        descricao_item.setWrapStyleWord(true);
        descricao_item.setEditable(false);

        descricaoPanel.add(descricao_item);

        //Contador de tempo decorrido
        JPanel timePanel = new JPanel();
        timePanel.setBounds(10, 375, 220, 100);
        timePanel.setBackground(Color.orange);
        timeElapsed();
        timer_relogio.start();
        exibe_tempo = new JLabel("Time elapsed: " + (elapsed/hor) + ":" + (elapsed/min) + ":" + (elapsed/seg));
        exibe_tempo.setForeground(Color.black);
        exibe_tempo.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        timePanel.add(exibe_tempo);

        frame_principal.add(timePanel);
        frame_principal.add(descricaoPanel);
        frame_principal.add(itemPanel);
        frame_principal.add(counterPanel);
        frame_principal.add(baconPanel);
        frame_principal.setVisible(true);
    }
    
    //Controla a variavel timer_relogio, usada pro relogio que
    //marca o tempo passado desde o inicio do jogo
    public void timeElapsed(){

        timer_relogio = new Timer(1000, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                game_time_now = System.currentTimeMillis();
                elapsed = (game_time_now - game_time_start)/1000;
                exibe_tempo.setText("Time elapsed: " + (elapsed/hor) + ":" + ((elapsed/min)%60) + ":" + ((elapsed/seg)%60));
            }
        });
    }
    
    //Controla a variavel timer_principal, que
    //marca o ganho de pontos por segundo
    public void inicializaTimer(){

        timer_principal = new Timer(timerSpeed, new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                pontuacao_atual++;
                total_points++;
                contador_principal.setText(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
                
                if(item2Unlocked == false && pontuacao_atual >= 20){
                    item2Unlocked = true;
                    item2.setText("Pig farm");
                }
                if(item3Unlocked == false && pontuacao_atual >= 350){
                    item3Unlocked = true;
                    item3.setText("American BBQ");
                }
                if(item4Unlocked == false && pontuacao_atual >= 1000){
                    item4Unlocked = true;
                    item4.setText("Heart Attack");
                }
            }
        });
    }
    
    //Sempre que ocorre uma atualizacao
    //na variavel "pps" (pontos por segundo)
    //a funcao e chamada para atualizar o timer e o texto
    public void timerUpdate(){

        if(timerOn == true){
           timer_principal.stop();
        }

        double speed = (1.0/pps)*1000;
        timerSpeed = (int)Math.round(speed);

        String pps_string = String.format("%.1f", pps);
        contador_secundario.setText(pps_string + " points/second");
        inicializaTimer();
        timer_principal.start();

        timerOn = true;
    }

    //Todos os botoes do jogo sao tratados aqui
    // - Botao principal
    //   - Adiciona pontos
    //   - Libera os itens da loja
    //   - Atualiza o contador
    // - Item[i]
    //   - Atualiza o dinheiro atual
    //   - Aumenta a variavel pps (pontos por segundo)
    //   - Atualiza o timer de acordo com a pontuacao
    //   - Aumenta o preco do item
    //   - O *ultimo item* chama a funcao que completa o jogo
    public class clickHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent event){
            String nome_acao = event.getActionCommand();

            if(nome_acao == "click"){ //click no bacon (pontuando)

                pontuacao_atual++;
                total_points++;
                if(item2Unlocked == false && pontuacao_atual >= 20){
                    item2Unlocked = true;
                    item2.setText("Pig farm");
                }
                if(item3Unlocked == false && pontuacao_atual >= 350){
                    item3Unlocked = true;
                    item3.setText("American BBQ");
                }
                if(item4Unlocked == false && pontuacao_atual >= 1000){
                    item4Unlocked = true;
                    item4.setText("Heart Attack");
                }
                contador_principal.setText(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
            }
            else if(nome_acao == "item1"){ //comprando item 1
                if(pontuacao_atual >= preco1){
                    pontuacao_atual -= preco1; //preco
                    qtd1++; //contador de itens
                    pps += 0.1; //pontos por seg
                    double novo_preco = preco1*(1.16);
                    preco1 = (int)Math.round(novo_preco);//aumento no preco
                    timerUpdate();
                    item1.setText("Frying pan (" + qtd1 + ")"); 
                    contador_principal.setText(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
                    descricao_item.setText("Frying Pan\nPrice: " 
                    + preco1 + "\nGrants the player 0.1 points/second\n"
                    + "Get ready to cook some bacon!");
                } else {
                    descricao_item.setText("Not enough points!");
                }
            }
            else if(nome_acao == "item2"){ //comprando item 2
                if(pontuacao_atual >= preco2){
                    pontuacao_atual -= preco2;
                    qtd2++;
                    pps += 1.0;
                    double novo_preco = preco2*(1.16);
                    preco2 = (int)Math.round(novo_preco);
                    timerUpdate();
                    item2.setText("Pig farm (" + qtd2 + ")");
                    contador_principal.setText(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
                    descricao_item.setText("Pig farm\nPrice: " 
                    + preco2 + "\nGrants the player 1.0 points/second\n"
                    + "The american dream");
                } else if(item2Unlocked == true){
                    descricao_item.setText("Not enough points!");
                }
            }
            else if(nome_acao == "item3"){ //comprando item 3
                if(pontuacao_atual >= preco3){
                    pontuacao_atual -= preco3;
                    qtd3++;
                    pps += 2.2;
                    double novo_preco = preco3*(1.16);
                    preco3 = (int)Math.round(novo_preco);
                    timerUpdate();
                    item3.setText("American BBQ (" + qtd3 + ")");
                    contador_principal.setText(pontuacao_atual + " point" + ((pontuacao_atual != 1) ? "s" : ""));
                    descricao_item.setText("American BBQ\nPrice: " 
                    + preco3 + "\nGrants the player 2.2 points/second\n"
                    + "Greasy and tasty barbecue");
                } else if(item3Unlocked == true){
                    descricao_item.setText("Not enough points!");
                }
            }
            else if(nome_acao == "item4"){ //comprando item 4
                if(pontuacao_atual >= preco4){
                    pontuacao_atual -= preco4;
                    qtd4++;
                    endGame();
                } else if (item4Unlocked == true){
                    descricao_item.setText("Not enough points!");
                }
            }
        }
    }
    
    //Mouse Listener pra lidar com os eventos de:
    // - Clique no botao principal (mudar imagem)
    // - Mouse passa sobre os itens da lojinha (mouse over tip)
    public class mouseOver implements MouseListener{
        
        @Override
        public void mouseClicked(MouseEvent e){

        }
        
        @Override
        public void mousePressed(MouseEvent e){
            //quando o botao principal e apertado, a imagem muda
            JButton botao_evento = (JButton)e.getSource();

            if(botao_evento == baconButton){
                baconButton.setIcon(bacon2);
            }

        }

        @Override
        public void mouseReleased(MouseEvent e){
            //retornar pra imagem original quando o botao for solto
            JButton botao_evento = (JButton)e.getSource();

            if(botao_evento == baconButton){
                baconButton.setIcon(bacon1);
            }

        }

        @Override
        public void mouseEntered(MouseEvent e){
            // "Mouse over tip" dos itens da loja
            // exibe o texto na caixa preta quando o mouse passa sobre o item
            JButton botao_loja = (JButton)e.getSource();

            if(botao_loja == item1){
                descricao_item.setText("Frying Pan\nPrice: " 
                + preco1 + "\nGrants the player 0.1 points/second\n"
                + "Get ready to cook some bacon!");
            }
            else if(botao_loja == item2){
                if(item2Unlocked == true){
                    descricao_item.setText("Pig farm\nPrice: " 
                    + preco2 + "\nGrants the player 1.0 points/second\n"
                    + "The american dream");
                }
                else{
                    descricao_item.setText("Get 20 points to unlock this item");
                }
            }
            else if(botao_loja == item3){
                if(item3Unlocked == true){
                    descricao_item.setText("American BBQ\nPrice: " 
                    + preco3 + "\nGrants the player 2.2 points/second\n"
                    + "Greasy and tasty barbecue");
                }
                else{
                    descricao_item.setText("Get 350 points to unlock this item");
                }
            }
            else if(botao_loja == item4){
                
                if(item4Unlocked == true){
                    descricao_item.setText("Heart Attack\nPrice: " 
                    + preco4 + "\nAre you *paying* to die?");
                }
                else{
                    descricao_item.setText("Get 1000 points to unlock this item");
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e){
            descricao_item.setText(null);
        }
    }
    
    //Funcao simples que completa o jogo assim que o ultimo item for comprado
    public void endGame(){
        timer_relogio.stop();
        timer_principal.stop();
        long horas = (elapsed/3600) % 60;
        long minutos = (elapsed/60) % 60;
        long segundos = elapsed % 60;
        
        String[] options = {"I am ready to go."};
        JOptionPane.showOptionDialog(null, "You played for " + horas + " hours, "
        + minutos + " minutes and " + segundos + " seconds. You collected " + total_points + " points."
        + "Moments before the end, you were gracefully eating bacon\nThanks for playing the game,\nGustavo Guerra", "You died",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        System.exit(0);
    }

}

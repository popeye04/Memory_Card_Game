import java.awt.*;  //GUI
import java.awt.event.*; //GUI
import java.util.ArrayList; //for cards
import javax.swing.*; //GUI

public class DuckCards {

    class Card{
        String cardname;
        ImageIcon cardImageIcon;

        Card(String cardname, ImageIcon cardImageIcon){   //a class for representing the cards
            this.cardname = cardname;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString(){  //to print out the class = string representation
            return cardname;
        }
    }
    
    String[] cardList = {

        "angry",
        "depressed",
        "eating",
        "excited",
        "happy",
        "sad",
        "scared",
        "stunned"
    };

    int rows = 4;
    int columns = 4;
    int cardheight = 120;
    int cardwidth = 120;

    ArrayList<Card> duckset; // to create an arraylist for the cards
    ImageIcon cardbackImageIcon;  // for the background image of the card

    int boardWidth = columns * cardwidth; // 4 * 120 = 480 px
    int boardHeight = rows * cardheight;  // 4 * 120 = 480 px

    JFrame frame = new JFrame("Duck Matching Card Game");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();

    int errorCount = 0; // to count total error of one round
    ArrayList<JButton> board; // tracking all the card buttons
    Timer hideDuckTimer; // set timer to hide the card later
    boolean duckReady = false; //allowing to click the card
    JButton duckSelected_1;
    JButton duckSelected_2;


    DuckCards(){

        setupCards();
        shuffleCards();

        // frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.setSize(boardHeight, boardWidth);
        frame.setLocationRelativeTo(null); //window at the centre of screen
        frame.setResizable(false); //window is not resizable by user
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit by clicking close button

        //error count text 
        textLabel.setFont(new Font("Serif", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: "+ Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        //game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for(int i = 0; i < duckset.size(); i++){  //creating button for each card

            JButton block = new JButton();
            block.setPreferredSize(new Dimension(cardwidth, cardheight));
            block.setOpaque(true);
            block.setIcon(duckset.get(i).cardImageIcon); //to make sure the buttons and image have same size
            block.setFocusable(false);
            block.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                if(!duckReady){
                    return;
                }
                JButton block = (JButton) e.getSource();
                if(block.getIcon() == cardbackImageIcon){
                    if(duckSelected_1 == null){
                        duckSelected_1 = block;
                        int index = board.indexOf(duckSelected_1);
                        duckSelected_1.setIcon(duckset.get(index).cardImageIcon);
                    }
                    else if(duckSelected_2 == null){
                        duckSelected_2 = block;
                        int index =board. indexOf(duckSelected_2);
                        duckSelected_2.setIcon(duckset.get(index).cardImageIcon);

                        //if two buttons have same image
                        if(duckSelected_1.getIcon() != duckSelected_2.getIcon()){
                            //error count ++
                            errorCount +=1;
                            textLabel.setText("Errors: " +Integer.toString(errorCount));
                            hideDuckTimer.start();
                        }
                        else{

                            // when card matches we do not flip it
                            duckSelected_1 = null;
                            duckSelected_2 = null;
                        }
                    }
                }
            }
        });

        board.add(block);
        boardPanel.add(block);

        }

        frame.add(boardPanel);

        // for adding restart game button
        restartButton.setFont(new Font("Serif", Font.BOLD, 18));
        restartButton.setText(("Restart The Game"));
        restartButton.setPreferredSize(new Dimension(boardWidth, 35));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false); // to stop unnecessary click on restart utton (disabling the button)
        restartGamePanel.add(restartButton);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){ //restart the game
                if(!duckReady){
                    return;
                }

                duckReady = false;
                restartButton.setEnabled(false);
                duckSelected_1 = null; //unselecting the card for restart
                duckSelected_2 = null;
                shuffleCards();

                //reassigning buttons with new card image through shuffling
                for(int i = 0; i < board.size(); i++){
                    board.get(i).setIcon(duckset.get(i).cardImageIcon);
                }

                errorCount = 0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                hideDuckTimer.start();

            }
        });
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack(); //recalculating width and height of the window
        frame.setVisible(true);

        //start the game
        hideDuckTimer = new Timer(2000, new ActionListener() { //set timer
            @Override
            public void actionPerformed(ActionEvent e){
                hideDucks();
            }
        });
        
        hideDuckTimer.setRepeats(false);
        hideDuckTimer.start();


    }


        void setupCards(){
            duckset = new ArrayList<Card>();

            for(String cardname : cardList){

                Image cardImg = new ImageIcon(getClass().getResource("./img/" + cardname + ".jpg")).getImage();
                // to access the source of the image
                ImageIcon cardImageIcon = new ImageIcon(cardImg.getScaledInstance(cardwidth, cardheight, java.awt.Image.SCALE_SMOOTH));

                // creating card image for adding into cardset

                Card card = new Card(cardname, cardImageIcon);
                duckset.add(card);
            }

            duckset.addAll(duckset);

            // load the back image

            Image cardbackImg = new ImageIcon(getClass().getResource("./img/back.jpg")).getImage();
            cardbackImageIcon = new ImageIcon(cardbackImg.getScaledInstance(cardwidth, cardheight, java.awt.Image.SCALE_SMOOTH));
        }

        void shuffleCards(){

            System.out.println(duckset); //printing the card set

            //shuffle 
            for(int i = 0; i < duckset.size() ; i++){
                int j = (int) (Math.random()* duckset.size()); //random shuffling
                //swapping
                Card temp = duckset.get(i);
                duckset.set(i,duckset.get(j));
                duckset.set(j,temp);
            }
            System.out.println(duckset);

        }

        void hideDucks(){ 

            //if the cards do not match

            if(duckReady && duckSelected_1 != null && duckSelected_2 != null){
                duckSelected_1.setIcon(cardbackImageIcon);
                duckSelected_1 = null;
                duckSelected_2.setIcon(cardbackImageIcon);
                duckSelected_2 = null;
            }

            //hide all cards after timer

            else{
                for(int i = 0; i < duckset.size(); i++){
                    board.get(i).setIcon(cardbackImageIcon);
                }
                duckReady = true;
                restartButton.setEnabled(true);
    
            }
        }

    
}

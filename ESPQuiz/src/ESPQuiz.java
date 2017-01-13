import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


/**
 * Created by tiger on 2016-12-19.
 */
public class ESPQuiz extends JFrame implements ActionListener {

    private static List<QuizQuestion> quizQuestionsList;
    private static QuizQuestions quizQuestions;
    private final Font font1 = new Font("Arial", Font.BOLD, 20);
    private final Font font2 = new Font("Arial", Font.BOLD, 28);
    private final String[] op = {"A", "B", "C", "D"};
    private JPanel p1, p2, p3;
    private JPanel pi;
    private JLabel lb1;
    private JLabel[] lbs;
    private JLabel reason;
    private int questionNum = 0;
    private JButton[] buttons;
    private int randCorrectNum;
    private int score = 0;
    private int t = 30;
    private int totalTime = 0;
    private Timer tim;
    private boolean finished = false;

    public static void main(String args[]) {
        ESPQuiz q = new ESPQuiz();  //make object ESPQuiz
        q.prepGUI();  //prep for GUI
        q.prepQuestions();  //prep for Question panel
        q.makeGUI();  //make the GUI
        q.makeQuestions();  //make questions
        q.tim.start();  //start timer
    }

    private static String[] shuffling(String[] answers, int correctNum, int randCorrectNum) {
        Random random = new Random();
        String a = answers[randCorrectNum];
        answers[randCorrectNum] = answers[correctNum];
        answers[correctNum] = a;
        //swap correct answer to desired position
        for (int i = answers.length - 1; i > 0; i--) {
            int index = random.nextInt(answers.length);  //pick two items
            // Simple swap
            if (!(i == randCorrectNum || index == randCorrectNum)) {  //if the swap does not consist correct answer
                // simple swap
                String b = answers[index];
                answers[index] = answers[i];
                answers[i] = b;
            }
        }
        return answers;
    }

    public static void refresh() {
        quizQuestions = new QuizQuestions();
        quizQuestionsList = new ArrayList<>();
        quizQuestionsList = quizQuestions.getQuizQuestions();  //get questions
    }

    public void actionPerformed(ActionEvent e) {
        /* ActionListener get input from the buttons
        *  and check if the correct button was clicked;
        *  if correct button was clicked,
        *  JFrame shows a popup to show the user their score
        *  if not another popup will show up and the user can access "help" session from the popup.
        *  If all the questions in the quiz were answered,
        *  there will be a popup to show the final score
        *  and attempt the user to start over again or exit.
        *  */
        if (e.getSource() == buttons[randCorrectNum]) {
            score += 10;  // add 10 points to their total score
            tim.stop();  // stop the timer
            JOptionPane.showMessageDialog(null, "You got it! Your score: " + score, "Your Score", JOptionPane.INFORMATION_MESSAGE);
            // show popup with their current score
        } else if (e.getSource() != buttons[randCorrectNum]) {
            // if wrong button is clicked
            Object[] options = {"OK", "Help!"};
            tim.stop();  // stop the timer
            score -= 5;  // deduct 5 points
            int help = JOptionPane.showOptionDialog(null, "Sorry :( Wrong answer. Your score: " + score, "Your Score", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            // show popup with an option to access help
            if (help == 1) {  // if the user needs help
                pi = new JPanel();  //create dialog panel
                reason = new JLabel();  //create reason label
                reason.setPreferredSize(new Dimension(250, 400));  //set size
                reason.setFont(font1);  //set font
                reason.setText("<html>" + quizQuestionsList.get(questionNum).getReason() + "The correct answer is " + op[randCorrectNum] + ".</html>");  //set reason
                pi.add(reason);  //add reason
                JOptionPane.showMessageDialog(null, pi);
                // show the user the help documentation
            }
        }

        if (questionNum == quizQuestionsList.size() - 1) {  // check if all questions were answered
            finished = true;  // if all answered turn finished to true
            lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
            JOptionPane.showMessageDialog(null, "Your final score: " + score, "Quiz Finished!", JOptionPane.INFORMATION_MESSAGE);
            // tell user their final score
            int result = JOptionPane.showConfirmDialog(null, "Want to start over again?", "", JOptionPane.YES_NO_OPTION);
            //ask user if they want to start over again
            if (result == 0) {  // if they do
                t = 30;  // reset time count
                tim.start();  //restart timer
                questionNum = 0;  // restart quiz
                score = 0;  // reset their score
                p2.revalidate();  // reset question panel
                lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
                refresh();  //load questions
                prepQuestions();  // reload questions
                makeQuestions();  // make questions
                p2.repaint();  //repaint question panel
                finished = false;  // turn finished to false
            } else {  // if the user wants to quit
                System.exit(0);  //exit
            }
        } else {  // if there are still questions left in the quiz
            questionNum += 1;  //add 1 to question number
            t = 30;  //reset time count
            tim.start();  //restart timer
            p2.revalidate();  //reset panel
            lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
            prepQuestions();  //reload questions
            makeQuestions();  //remake question panel
            p2.repaint();  //repaint question panel
        }
    }

    private void prepGUI() {
        this.setTitle("ESP Quiz");  //set frame title
        setSize(1024, 768);  //set frame size
        setLocationRelativeTo(null);  //set location

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);  //set close operation
        addWindowListener(new WindowAdapter() {  //override windowClosing method
            @Override
            public void windowClosing(WindowEvent e) {  //on closing the window
                JFrame frame = (JFrame)e.getSource();  //get current frame
                tim.stop();  //stop time
                Object[] options = {"Stay!", "Don't Leave!"};  //make options
                int quit = JOptionPane.showOptionDialog(frame,"Are you sure you want to quit? :(","ESP needs you!",JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,null,options,options[0]);
                //make dialog
                if(quit == 1){
                    System.exit(0);  //exit
                }
                else{
                    tim.start();  //restart timer
                }
            }
        });

        GridBagLayout gridB = new GridBagLayout();
        setLayout(gridB);  //set layout for the frame

        p1 = new JPanel();
        p1.setLayout(gridB);  //set p1 layout
//        p1.setBorder(BorderFactory.createLineBorder(Color.black));

        score = 0;
        lb1 = new JLabel();
        lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
        lb1.setFont(font1);
        //define timer
        tim = new Timer(1000, e -> {
            if (t > 1 && !finished) {  //if 1 or more seconds is left and quiz not finished
                t--;  //timer count down 1
                lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
            } else if (t == 1 && !finished) {  //if only 1 second left and quiz not finished
                t--;  //timer count down 1
                tim.stop();  //stop timer
                lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
                Object[] options = {"OK", "Help!"};
                int help = JOptionPane.showOptionDialog(null, "Sorry :( Time's out. Your score: " + score, "Your Score", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                //popup show time's out and attempt user to use help
                if (help == 1) {
                    pi = new JPanel();  //make dialog panel
                    reason = new JLabel();  //make label
                    reason.setPreferredSize(new Dimension(250, 400));  //set size
                    reason.setFont(font1);  //set font
                    reason.setText("<html>" + quizQuestionsList.get(questionNum).getReason() + "The correct answer is " + op[randCorrectNum] + ".</html>");  //set reason
                    pi.add(reason);  //add reason
                    JOptionPane.showMessageDialog(null, pi);
                    //show help popup
                }
                if (questionNum == quizQuestionsList.size() - 1) {  //if no question is left
                    finished = true;  //turn finished to true
                    lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
                    JOptionPane.showMessageDialog(null, "Your final score: " + score, "Quiz Finished!", JOptionPane.INFORMATION_MESSAGE);
                    //tell user final score
                    tim.stop();  //stop timer
                    int result = JOptionPane.showConfirmDialog(null, "Want to start over again?", "", JOptionPane.YES_NO_OPTION);
                    //ask user to start over again
                    if (result == 0) {  //if yes
                        questionNum = 0;  //reset question number
                        t = 30;  //reset timer count
                        tim.start();  //restart timer
                        p2.revalidate();  //reset question panel
                        lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
                        refresh();  //reload questions
                        prepQuestions();  //reload questions
                        makeQuestions();  //remake questions
                        p2.repaint();  //repaint
                        finished = false;  //turn finished to false
                    } else {  //if user wants to quit
                        System.exit(0);
                    }
                } else {  //if still questions left
                    questionNum += 1;  //add 1 to question number
                    t = 30;  //reset time count
                    tim.start();  //restart timer
                    p2.revalidate();  //reset question panel
                    lb1.setText("Score: " + score + "                   ESP Quiz                    Time Left: " + t);  //reload time count panel
                    prepQuestions();  //reload questions
                    makeQuestions();  //remake questions
                    p2.repaint();  //repaint questions
                }
            }

            if(totalTime < 49){   //time penalty
                totalTime++;
            }else if(totalTime == 49){
                totalTime++;
                score--;
                totalTime = 0;  //take away 1 mark every 50 seconds
            }
        });

        p2 = new JPanel();  //make question panel
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS)); //set layout
        refresh(); //load questions
//        p2.setBorder(BorderFactory.createLineBorder(Color.black));


        p3 = new JPanel();  //make buttons panel
        p3.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));  //set layout
//        p3.setBorder(BorderFactory.createLineBorder(Color.black));


        buttons = new JButton[4];  //make buttons
        for (int i = 0; i < 4; i++) {
            buttons[i] = new JButton();  //define buttons
            buttons[i].setFont(font2);  //set font
            buttons[i].setPreferredSize(new Dimension(200, 40));  //set size
            buttons[i].addActionListener(this);  //add ActionListener
            switch (i) {  //set button text
                case 0:
                    buttons[i].setText("A");
                    break;
                case 1:
                    buttons[i].setText("B");
                    break;
                case 2:
                    buttons[i].setText("C");
                    break;
                case 3:
                    buttons[i].setText("D");
            }
        }
    }

    private void prepQuestions() {  //prepare for questions panel
        Random random = new Random();
        randCorrectNum = random.nextInt(4);  //generate random number for correct option

        /*
        *This part of the program shuffles the choices of the quiz questions.
        * All 4 choices will be put in a different order every time based on
        * the shuffling algorithm.
        *
        * The index of the correct answer will be generated by a random number generator.
        * First the old index of the correct answer will be found by using getCorrectAnsInd() method,
        * and the correct answer will be put into the new list with an index of the new random correct index.
        * Then all the wrong answers will be shuffled and put in other positions in the list by shuffling().
        *
        * For future references:
        * randCorrectNum = 0 means correct answer is A,
        * 1 is B,
        * 2 is C,
        * and 3 is D.
        * */

        String[] answerList;//make answerList string array
        answerList = quizQuestionsList.get(questionNum).getAnswers();  //get answer strings
        int correctNum = quizQuestionsList.get(questionNum).getCorrectAnsInd();  //get correct answer index
        String[] cAnswerList;  //make new answer list array
        cAnswerList = shuffling(answerList, correctNum, randCorrectNum);  //shuffle the old list

        lbs = new JLabel[5];  //make question labels
        for (int i = 0; i < 5; i++) {
            lbs[i] = new JLabel();
            lbs[i].setPreferredSize(new Dimension(600, 200));
            lbs[i].setFont(font1);
            switch (i) {  //input question
                case 0:
                    lbs[i].setText("<html>" + (questionNum+1) + ". " + quizQuestionsList.get(questionNum).getQuestion() + "</html>");  //question body
                    break;
                case 1:
                    lbs[i].setText("<html>A. " + cAnswerList[i - 1] + "</html>");  //choice A
                    break;
                case 2:
                    lbs[i].setText("<html>B. " + cAnswerList[i - 1] + "</html>");  //choice B
                    break;
                case 3:
                    lbs[i].setText("<html>C. " + cAnswerList[i - 1] + "</html>");  //choice C
                    break;
                case 4:
                    lbs[i].setText("<html>D. " + cAnswerList[i - 1] + "</html>");  //choice D
            }
        }
    }

    private void makeGUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints g = new GridBagConstraints();

        p1.add(lb1, gbc);  //add timer label
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 3;
        g.weightx = 1;
        g.ipady = 0;
        g.insets = new Insets(50, 0, 0, 0);
        add(p1, g);  //add timer panel

        g.ipady = 0;
        g.gridy = 2;
        g.weighty = 0;
        g.insets = new Insets(0, 0, 25, 0);
        add(p3, g);  //add button panel
        for (JButton b : buttons) {  //add buttons
            p3.add(b, gbc);
        }
        setVisible(true);  //set visible
    }

    private void makeQuestions() {
        GridBagConstraints gbc = new GridBagConstraints();
        GridBagConstraints g = new GridBagConstraints();

        p2.removeAll();
        for (JLabel l : lbs) {  //add question labels
            p2.add(l, gbc);
        }
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridy = 1;
        g.gridwidth = 3;
        g.weightx = 1.0;
        g.weighty = 1.0;
        g.ipady = 300;
        g.insets = new Insets(0, 50, 0, 50);
        add(p2, g);  //add question panel
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by tiger on 2016-12-19.
 */
public class ESPQuiz extends JFrame implements ActionListener {

    private final Font font1 = new Font("Arial", Font.BOLD, 20);
    private final Font font2 = new Font("Arial", Font.BOLD, 28);
    private JPanel p1, p2, p3;
    private JLabel lb1;
    private JLabel[] lbs;
    private int questionNum = 0;
    private JButton[] buttons;
    private static List<QuizQuestion> quizQuestionsList;
    private int randCorrectNum;
    private int score = 0;
    private int t = 20;
    private Timer tim;
    private boolean finished = false;
    private static QuizQuestions quizQuestions;

    public static void main(String args[]) {
        ESPQuiz q = new ESPQuiz();  //make object ESPQuiz
        q.prepGUI();  //prep for GUI
        q.prepQuestions();  //prep for Question panel
        q.makeGUI();  //make the GUI
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

    public static void refresh(){
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
                JOptionPane.showMessageDialog(null, quizQuestionsList.get(questionNum).getReason());
                // show the user the help documentation
            }
        }

        if (questionNum == quizQuestionsList.size() - 1) {  // check if all questions were answered
            finished = true;  // if all answered turn finished to true
            JOptionPane.showMessageDialog(null, "Your final score: " + score, "Quiz Finished!", JOptionPane.INFORMATION_MESSAGE);
            // tell user their final score
            int result = JOptionPane.showConfirmDialog(null, "Want to start over again?", "", JOptionPane.YES_NO_OPTION);
            //ask user if they want to start over again
            if (result == 0) {  // if they do
                t = 20;  // reset time count
                tim.start();  //restart timer
                questionNum = 0;  // restart quiz
                score = 0;  // reset their score
                p2.removeAll();  // reset question panel
                lb1.setText("ESP Quiz                                  Time Left: " + t);  //reload time count panel
                refresh();  //load questions
                prepQuestions();  // reload questions
                makeGUI();  // remake GUI
                finished = false;  // turn finished to false
            } else {  // if the user wants to quit
                System.exit(0);  //exit
            }
        } else {  // if there are still questions left in the quiz
            questionNum += 1;  //add 1 to question number
            t = 20;  //reset time count
            tim.start();  //restart timer
            p2.removeAll();  //reset question panel
            lbs[0].removeAll();
            lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset time count label
            prepQuestions();  //reload questions
            makeGUI();  //remake GUI
        }
    }

    private void prepGUI() {
        this.setTitle("ESP Quiz");  //set frame title
        setSize(1024, 768);  //set frame size
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //set close operation
        setLocationRelativeTo(null);  //set location

        GridBagLayout gridB = new GridBagLayout();
        setLayout(gridB);  //set layout for the frame

        p1 = new JPanel();
        p1.setLayout(gridB);  //set p1 layout
        p1.setPreferredSize(new Dimension(600, 75));  //set p1 size

        lb1 = new JLabel("ESP Quiz                                  Time Left: " + t);  //set timer label
        lb1.setFont(font1);
        //define timer
        tim = new Timer(1000, e -> {
            if (t > 15 && !finished) {  //if 15 or more seconds is left and quiz not finished
                t--;  //timer count down 1
                lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset timer label
                //but do not take away marks
            } else if (t <= 15 && t > 1 && !finished) {  //if less than 15 seconds is left and quiz not finished
                t--;  //timer count down 1
                score--;  //take away 1 mark per second
                lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset timer label
            } else if (t == 1 && !finished) {  //if only 1 second left and quiz not finished
                t--;  //timer count down 1
                score--;  //take away 1 mark
                tim.stop();  //stop timer
                lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset timer label
                Object[] options = {"OK", "Help!"};
                int help = JOptionPane.showOptionDialog(null, "Sorry :( Time's out. Your score: " + score, "Your Score", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                //popup show time's out and attempt user to use help
                if (help == 1) {
                    JOptionPane.showMessageDialog(null, quizQuestionsList.get(questionNum).getReason());
                    //show help popup
                }
                if (questionNum == quizQuestionsList.size() - 1) {  //if no question is left
                    finished = true;  //turn finished to true
                    JOptionPane.showMessageDialog(null, "Your final score: " + score, "Quiz Finished!", JOptionPane.INFORMATION_MESSAGE);
                    //tell user final score
                    tim.stop();  //stop timer
                    int result = JOptionPane.showConfirmDialog(null, "Want to start over again?", "", JOptionPane.YES_NO_OPTION);
                    //ask user to start over again
                    if (result == 0) {  //if yes
                        questionNum = 0;  //reset question number
                        t = 20;  //reset timer count
                        tim.start();  //restart timer
                        p2.removeAll();  //reset question panel
                        lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset timer label
                        refresh();
                        prepQuestions();  //reload questions
                        makeGUI();  //remake GUI
                        finished = false;  //turn finished to false
                    } else {  //if user wants to quit
                        System.exit(0);
                    }
                } else {  //if still questions left
                    questionNum += 1;  //add 1 to question number
                    t = 20;  //reset time count
                    tim.start();  //restart timer
                    lbs[0].removeAll();
                    p2.removeAll();  //reset question panel
                    lb1.setText("ESP Quiz                                  Time Left: " + t);  //reset timer label
                    prepQuestions();  //reload questions
                    makeGUI();  //remake GUI
                }
            }
        });

        p2 = new JPanel();  //make question panel
        p2.setPreferredSize(new Dimension(300, 425));  //set size
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS)); //set layout
        refresh(); //load questions

        p3 = new JPanel();  //make buttons panel
        p3.setPreferredSize(new Dimension(600, 100));  //set size
        p3.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 30));  //set layout

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
            switch (i) {  //input question
                case 0:
                    lbs[i].setText(quizQuestionsList.get(questionNum).getQuestion());  //question body
                    break;
                case 1:
                    lbs[i].setText("A. " + cAnswerList[i - 1]);  //choice A
                    break;
                case 2:
                    lbs[i].setText("B. " + cAnswerList[i - 1]);  //choice B
                    break;
                case 3:
                    lbs[i].setText("C. " + cAnswerList[i - 1]);  //choice C
                    break;
                case 4:
                    lbs[i].setText("D. " + cAnswerList[i - 1]);  //choice D
            }
            lbs[i].setPreferredSize(new Dimension(600, 100));
            lbs[i].setFont(font1);
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
        g.ipady = 50;
        add(p1, g);  //add timer panel

        for (JLabel l : lbs) {  //add question labels
            p2.add(l, gbc);
        }
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 1;
        g.weighty = 1.0;
        g.ipady = 300;
        g.insets = new Insets(0, 50, 0, 50);
        add(p2, g);  //add question panel

        g.fill = GridBagConstraints.HORIZONTAL;
        g.ipady = 0;
        g.gridx = 0;
        g.gridy = 2;
        g.insets = new Insets(0, 0, 0, 0);
        add(p3, g);  //add button panel
        for (JButton b : buttons) {  //add buttons
            p3.add(b, gbc);
        }
        setVisible(true);  //set visible
    }
}

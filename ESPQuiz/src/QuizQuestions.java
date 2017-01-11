import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by tiger on 2016-12-19.
 */
public class QuizQuestions {

    private List<QuizQuestion> quizQuestions;

    public QuizQuestions(){
        this.quizQuestions = addQuizQuestions();
        //add questions into this object
    }

    private List<QuizQuestion> addQuizQuestions(){
        List<QuizQuestion> quizQuestions = new ArrayList<>();
        List<String> Lines = new ArrayList<>();
        String thisLine = null;
        try{
            InputStream in = QuizQuestions.class.getResourceAsStream("questions.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((thisLine = reader.readLine()) != null) {
                Lines.add(thisLine);
            }
        }catch(UnsupportedEncodingException u){
            u.printStackTrace();
        }catch (IOException i){
            i.printStackTrace();
        }

        for(int i = 0; i < (Lines.size()/7); i++){
            QuizQuestion q = new QuizQuestion(Lines.get(i*7),Integer.parseInt(Lines.get(i*7+1))-1,Lines.get(i*7+2),Lines.get(i*7+3),Lines.get(i*7+4),
                    Lines.get(i*7+5), Lines.get(i*7+6));
            quizQuestions.add(q);
        }

        quizQuestions = shuffling(quizQuestions);
        //shuffle the list

        return quizQuestions;
    }

    public List<QuizQuestion> getQuizQuestions(){
        quizQuestions = shuffling(quizQuestions);
        //shuffle questions list
        return quizQuestions;
    }

    private static List<QuizQuestion> shuffling(List<QuizQuestion> quizQuestions) {
        Random random = new Random();
        QuizQuestion[] q = new QuizQuestion[quizQuestions.size()];
        q = quizQuestions.toArray(q);  //turn this list to an array

        for (int i = quizQuestions.size() - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Simple swap
                QuizQuestion b = q[i];
                q[i] = q[index];
                q[index] = b;
        }

        quizQuestions = Arrays.asList(q);  //turn the array back to a list
        return quizQuestions;
    }

}

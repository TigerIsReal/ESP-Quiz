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

        QuizQuestion q1 = new QuizQuestion("How much is an apple?",0,
                "An apple should cost $3.",
                "$3","$5","$7","$9");
        quizQuestions.add(q1);

        QuizQuestion q2 = new QuizQuestion("How much is a banana?",2,
                "A banana should cost $7",
                "$3","$5","$7","$9");
        quizQuestions.add(q2);
        //add questions

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

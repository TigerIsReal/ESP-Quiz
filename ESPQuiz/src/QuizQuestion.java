/**
 * Created by tiger on 2016-12-19.
 */
public class QuizQuestion {

    private final String question;
    private final String[] answers;
    private final int ansIndex;
    private final String reason;

    public QuizQuestion(String question, int ansIndex, String reason, String... answers){
        this.question = question;
        this.answers = answers;
        this.ansIndex = ansIndex;
        this.reason = reason;
        //construct questions
    }

    public String[] getAnswers(){
        return answers;
        //get an answer list
    }

    public String getQuestion(){
        return question;
        //get the question
    }

    public int getCorrectAnsInd(){
        return ansIndex;
        //get the correct answer index
    }

    public String getReason(){
        return reason;
    }
}

package carbonbutterfly.generateqti;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Represent a block of one or more questions. Questions should be grouped into a block if they rely on common preliminaries
 * (e.g. code) or 'follow on' from previous questions. Note that this concept isn't part of text2qti and this class is mainly
 * just to ensure that related questions always appear together. Additionally, an integer 'difficulty' indicator (level)
 * is associated with each block (this could be per question but that introduces extra complexities - because all questions in
 * a block have to appear together, and there need to be a certian number of questions of each level - it's doable but more
 * complicated and not really necessary). This is used to help
 * ensure that generated quizzes are reasonably balanced in difficulty. At this point, levels range from 1 to 3.
 */
public class Block {

    @JacksonXmlProperty(isAttribute = false, localName = "level")
    private int level;
    private int startNum;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "question")
    private List<Question> questionList;

    /**
     * The list of questions in a block
     * @return the list of questions in a block
     */
    public List<Question> getQuestionList() {
        return questionList;
    }

    /**
     * Set the list of questions in a block
     * @param questionList the list of questions in a block
     */
    public void setQuestionList(final List<Question> questionList) {
        this.questionList = questionList;
    }

    /**
     * Set the starting number of this block when it appears in a quiz
     * @param startNum the number of the first question in the block as it appears in a quiz
     */
    public void setStartNum(final int startNum) {
        this.startNum = startNum;
    }

    /**
     * The number that should be used for the first question in a following block - this allows blocks of questions
     * to be 'chained' together
     * @return the number that should be used at the start of the follwing block
     */
    public int getNextNum() {
        return startNum + questionList.size();
    }

    /**
     * The difficulty level of the question block
     * @return the difficulty level of the question block
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the difficulty level of the block
     * @param level the difficulty level of the block
     */
    public void setLevel(final int level) {
        this.level = level;
    }

    /**
     * Return the number of questions in the block
     * @return the number of questions in the block
     */
    public int getNumberOfQuestions() {
        return questionList.size();
    }

    /**
     * Format a block of questions as text2qti compliant text. The formatting is mainly delegated to
     * the Question class (and in turn to Answer and Alternate classes) - but the level appears only as a comment
     * (this will not appear in the QTI format)
     * @return the block as a text2qti compliant string
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("% Level: " + level + "\n");
        int qCount = startNum;
        for(Question question : questionList) {
            question.setNumber(qCount++);
            builder.append(question);
        }
        return builder.toString();
    }
}

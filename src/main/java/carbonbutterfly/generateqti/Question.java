package carbonbutterfly.generateqti;

import carbonbutterfly.generateqti.utils.Utils;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

import java.util.List;

/**
 * A question - one or more questions (which would be related) occur in each block. A question has a text and one or more
 * (usually 5) possible answers, one of which is correct
 * Needs to be compliant with Jackson XML parser so not all methods may be directly called by user code
 */
public class Question {

    @JacksonXmlText
    private String questionText;
    @JacksonXmlProperty(isAttribute = false)
    private Answers answers;

    private int number;

    /**
     * Get the question text
     * @return the question text
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Set the question text
     * @param questionText the question text
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Get the question text
     * @return the question text
     */
    public Answers getAnswers() {
        return answers;
    }

    /**
     * Set the answer list
     * @param answers a list of answers (one of which should be correct)
     */
    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    /**
     * Set the question number - only used internally
     * @param number the question number
     */
    public void setNumber(final int number) {
        this.number = number;
    }

    /**
     * Generate a text2qti compliant question. Defer representing the answers to the Answer class
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder scratchBuilder = new StringBuilder("Title: Question\nPoints: 1\n");
        final List<String> qText = Utils.cleanUpTextToList(questionText);

        scratchBuilder.append(Utils.outputTextBlock(5, number++ + ".", qText));
        scratchBuilder.append(answers);
        return scratchBuilder.toString();
    }
}

package carbonbutterfly.generateqti;

import carbonbutterfly.generateqti.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

/**
 * A possible question answer - consists of the answer text and an optional attribute (which defaults to 'no')
 * representing the correct answer (correct='yes').
 * Needs to be compliant with Jackson XML parser so not all methods may be directly called by user code
 */
public class Alternate {
    @JacksonXmlProperty(isAttribute = true)
    private String correct = "no";
    @JacksonXmlText
    private String answer;
    @JsonIgnore
    private char answerCount;

    /**
     * Set the answer count to a character - in practice usually a to e but there can be more/less answers
     * @param answerCount the character representing the 'number' of the answer
     */
    public void setAnswerCount(final char answerCount) {
        this.answerCount = answerCount;
    }

    /**
     * The value of the correct attribute - no for incorrect (default) and yes for correct.
     * @return the correct attribure
     */
    public String getCorrect() {
        return correct;
    }

    /**
     * Set the value of the correct attribute (will in practice be called by the XML parser)
     * @param correct attribute
     */
    public void setCorrect(final String correct) {
        this.correct = correct;
    }

    /**
     *  The answer text
     * @return the answer text
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Set the answer text
     * @param answer the answer text
     */
    public void setAnswer(final String answer) {
        this.answer = answer;
    }

    @Override
    /**
     * Generate an answer as a formatted string:
     * x) answer for incorrect answers (x == a character a,b...)
     * *x) answer for correct answers
     */
    public String toString() {
        final String ansLabel = (correct.equals("yes") ? "*" : "") + (char) answerCount + ")";
        return Utils.outputTextBlock(5, ansLabel, Utils.cleanUpTextToList(answer));

    }
}

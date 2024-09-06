package carbonbutterfly.generateqti;

import carbonbutterfly.generateqti.utils.Utils;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

/**
 * Represent a list of answers in text2qti compliant form.
 * Needs to be compliant with Jackson XML parser so not all methods may be directly called by user code
 */
public class Answers {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "alternate")
    private List<Alternate> altList;

    /**
     * Get the list of possible answers
     * @return list of answers
     */
    public List<Alternate> getAltList() {
        return altList;
    }

    /**
     * Set the list of possible answers
     * @param altList the list of possible answers
     */
    public void setAltList(final List<Alternate> altList) {
        this.altList = altList;
    }

    /**
     * Construct a list of answers in text2qti compliant format, starting from 'a'
     * @return the answer text
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        char ansCount = 'a';
        for(Alternate answer : altList) {
            answer.setAnswerCount(ansCount);
            builder.append(answer);
            ansCount++;
        }
        return builder.toString();
    }
}

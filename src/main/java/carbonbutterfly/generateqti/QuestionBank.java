package carbonbutterfly.generateqti;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Class to make XML representing an exam. An exam consists of one or more blocks of questions.
 * Needs to be compliant with Jackson XML parser so not all methods may be directly called by user code
 */
public class QuestionBank {
    @JacksonXmlProperty(localName = "block")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Block> blockList;

    /**
     * Get the list of Block objects that make up this exam
     * @return list of Block object
     */
    public List<Block> getBlockList() {
        return blockList;
    }

    /**
     * Set the list of Block objects that make up this exam
     * @param blockList a List of Block objects.
     */
    public void setBlockList(final List<Block> blockList) {
        this.blockList = blockList;
    }

    /**
     * Write all questions in text2qti compliant format to the file at the named path
     * Will exit if the file cannot be opened
     * @param outputPath the file path
     */
    public void outputAllQuestions(final String outputPath) {
        final File outFile = new File(outputPath);
        try (final PrintStream outStream = new PrintStream(outFile)){
            outStream.println(this);
        } catch(IOException io) {
            System.out.println("Failed to open file");
            io.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Returns a Block (of questions) in text2qti compliant format - devolves most of the work
     * to other classes' toString() methods (but does handle the question number)
     * @return text2qti compliant text representation of the block
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        int questionNum=1;
        for(Block block: blockList) {
            block.setStartNum(questionNum);
            builder.append(block);
            questionNum = block.getNextNum();
        }
        return builder.toString();
    }
}

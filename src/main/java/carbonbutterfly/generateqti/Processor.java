package carbonbutterfly.generateqti;

import carbonbutterfly.generateqti.utils.Utils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Handle reading questions in an XML format (for which there should be a schema but there isn't...) and then generate
 * quizzes in text2qti format (https://github.com/gpoore/text2qti)
 */
public class Processor {
    private final QuestionBank allBlocks;
    private final Map<Integer, List<Block>> questionBlockByLevel = new HashMap<>();

    public static void main(String[] args) {
        final Processor processor = Processor.deSerializeQuestions("/Users/neal/Library/CloudStorage/OneDrive-SwanseaUniversity/Exams/WorkingBank.xml");
        processor.outputAllQuestions("/Users/neal/Library/CloudStorage/OneDrive-SwanseaUniversity/Exams/AllQuestions.txt");
        int[] levelCount = {6,10,4};
        processor.generateQuiz("/Users/neal/Library/CloudStorage/OneDrive-SwanseaUniversity/Exams/SampleQuiz.txt", levelCount);
    }

    /*
    Private constructor (because it can throw an IOException and it's easier to deal with that in a factory method.
    Open a new file (using filePath) that should represent questions in the appropriate format, then parse it and
    generate an object tree rooted in an QuestionBank object. If the file is not present or the format is incorrect, throw
    IOException. Also generates a hashmap containing one list of questions per level - not really an appropriate use
    of hashmap so think of something better...
     */
    private Processor(final String filePath) throws IOException {
        final File dataFile = new File(filePath);
        final XmlMapper mapper = new XmlMapper();
        allBlocks = mapper.readValue(dataFile, QuestionBank.class);
        //To simplify things, generate a list of question blocks for each Level - this is an abuse of hashmap...
        allBlocks.getBlockList().stream().forEach(block -> {
            final int blockLevel = block.getLevel();
            if (!questionBlockByLevel.containsKey(blockLevel)) {
                questionBlockByLevel.put(blockLevel, new ArrayList<>());
            }
            List<Block> blockAtLevel = questionBlockByLevel.get(blockLevel);
            blockAtLevel.add(block);
        });
    }

    /**
     * Public factor method - opens and reads filePath generating an object tree rooted at an QuestionBank object. Will exit
     * with error code -1 if the file can't be read or the format is not correct
     * @param filePath the path to the input file
     * @return A Processor object containing all questions rooted at an QuestionBank object
     */
    public static Processor deSerializeQuestions(final String filePath) {
        try {
            return new Processor(filePath);
        } catch (IOException io) {
            System.err.println("Failed to read XML file");
            io.printStackTrace();
            System.exit(-1);
            return null;//do something better
        }
    }

    public void generateQuiz(final String outputPath, final int[] numQsPerLevel) {
        final StringBuilder builder = new StringBuilder(Utils.PRE_AMBLE);
        int qCount = 1;

        for(int i = 0; i < numQsPerLevel.length; i++) {
            final List<Block> blockClone = questionBlockByLevel.get(i + 1); //Need to remove added questions so work on copy
            int qsSoFar = 0;
            while (qsSoFar < numQsPerLevel[i]) {
                final int possBlockIndex = ThreadLocalRandom.current().nextInt(0, blockClone.size());
                final Block possQBlock = blockClone.get(possBlockIndex);
                if (qsSoFar + possQBlock.getNumberOfQuestions() <= numQsPerLevel[i]) {
                    possQBlock.setStartNum(qCount);
                    qCount = possQBlock.getNextNum();
                    builder.append(possQBlock);
                    qsSoFar += possQBlock.getNumberOfQuestions();
                    blockClone.remove(possBlockIndex);
                }
            }
        }
        writeFile(outputPath, builder.toString());
    }

    /**
     * Generate a 'quiz' containing all questions in the order they appear in the input file and write them in text2qti
     * format to a file. It isn't intended to be used as an actual quiz, just to output the text in a more readable format
     * that can then be imported into Canvas (where it is even more readable)
     * @param outputPath the output filename
     */
    public void outputAllQuestions(final String outputPath) {
        final StringBuilder builder = new StringBuilder("Quiz title: All Questions\n" +
                "Quiz description: All questions currently in the question bank.\n");
        builder.append(allBlocks);
        writeFile(outputPath, builder.toString());
    }

    private void writeFile(final String outFile, final String outputString) {
        try (final PrintStream outStream = new PrintStream(outFile)){
            outStream.println(outputString);
        } catch(IOException io) {
            System.out.println("Failed to open file");
            io.printStackTrace();
            System.exit(1);
        }
    }
}